package com.zylitics.zwl.interpret;

import com.zylitics.zwl.api.InterpreterLineChangeListener;
import com.zylitics.zwl.api.ZwlInterpreter;
import com.zylitics.zwl.api.ZwlInterpreterVisitor;
import com.zylitics.zwl.antlr4.ZwlLexer;
import com.zylitics.zwl.antlr4.ZwlParser.*;
import com.zylitics.zwl.antlr4.ZwlParserBaseVisitor;
import com.zylitics.zwl.constants.Exceptions;
import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.exception.*;
import com.zylitics.zwl.internal.Variables;
import com.zylitics.zwl.util.ParseUtil;
import com.zylitics.zwl.util.StringUtil;
import com.zylitics.zwl.webdriver.constants.Browsers;
import com.zylitics.zwl.webdriver.constants.By;
import com.zylitics.zwl.webdriver.constants.Colorz;
import com.zylitics.zwl.webdriver.constants.Keyz;
import com.zylitics.zwl.webdriver.constants.Platforms;
import com.zylitics.zwl.webdriver.constants.Timeouts;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static com.zylitics.zwl.internal.ReservedKeywords.RESERVED_KEYWORDS;

public class DefaultZwlInterpreter extends ZwlParserBaseVisitor<ZwlValue>
    implements ZwlInterpreter {
  
  private static final Logger LOG = LoggerFactory.getLogger(DefaultZwlInterpreter.class);
  
  private static final String FOR_LOOP_MAX_ITERATIONS_KEY = "forLoopMaxIterations";
  
  private static final long FOR_LOOP_MAX_ITERATIONS = 100000;
  
  public static final String PREFERENCES_KEY = "preferences";
  
  private final Set<String> readOnlyVars = new HashSet<>();
  private final ZwlValue _void = new VoidZwlValue();
  
  private final Variables vars = new Variables();
  private final Set<Function> functions;
  
  private final List<InterpreterLineChangeListener> lineChangeListeners = new ArrayList<>();
  
  private int currentLineInProgram = 0;
  
  public DefaultZwlInterpreter() {
    // add language specific functions (should always create new instances of available functions)
    functions = new HashSet<>(BuiltInFunction.get());
    
    // add internal readonly variables
    Map<String, ZwlValue> exceptions = new HashMap<>(Exceptions.asMap());
    exceptions.putAll(com.zylitics.zwl.webdriver.constants.Exceptions.asMap());
    addReadOnlyVariable("exceptions",
        new MapZwlValue(Collections.unmodifiableMap(exceptions)));
    addReadOnlyVariable("browsers", new MapZwlValue(
        Arrays.stream(Browsers.values()).collect(Collectors.toMap(Browsers::getAlias,
            browsers -> new StringZwlValue((browsers.getAlias()))))));
    addReadOnlyVariable("platforms", new MapZwlValue(
        Arrays.stream(Platforms.values()).collect(Collectors.toMap(Platforms::getName,
            platforms -> new StringZwlValue((platforms.getName()))))));
    addReadOnlyVariable("colors", new MapZwlValue(Colorz.asMap()));
    addReadOnlyVariable("keys", new MapZwlValue(Keyz.asMap()));
    addReadOnlyVariable("by", new MapZwlValue(By.asMap()));
    addReadOnlyVariable("timeouts", new MapZwlValue(Timeouts.asMap()));
  }
  
  public void accept(ZwlInterpreterVisitor visitor) {
    visitor.visit(this);
  }
  
  @Override
  public void addReadOnlyVariable(String identifier, ZwlValue value) {
    Objects.requireNonNull(identifier, "identifier can't be null");
    Objects.requireNonNull(value, "value can't be null");
  
    String lower = identifier.toLowerCase();
    if (!readOnlyVars.add(lower)) {
      return;
    }
    vars.assign(identifier, value);
  }
  
  @Override
  public void addFunction(Function function) {
    Objects.requireNonNull(function, "function can't be null");
  
    functions.add(function);
  }
  
  @Override
  public void addFunctions(Set<Function> functions) {
    Objects.requireNonNull(functions, "function list can't be null");
  
    for (Function f : functions) {
      addFunction(f);
    }
  }
  
  public void replaceFunction(Function function) {
    Objects.requireNonNull(function, "function can't be null");
  
    functions.remove(function);
    functions.add(function);
  }
  
  @Override
  public void setLineChangeListener(InterpreterLineChangeListener lineChangeListener) {
    Objects.requireNonNull(lineChangeListener, "lineChangeListener can't be null");
    
    lineChangeListeners.add(lineChangeListener);
  }
  
  @Override
  public void setLineChangeListeners(List<InterpreterLineChangeListener> lineChangeListeners) {
    Objects.requireNonNull(lineChangeListeners, "lineChangeListeners can't be null");
    
    this.lineChangeListeners.addAll(lineChangeListeners);
  }
  
  @Override
  public ZwlValue visitAssignment(AssignmentContext ctx) {
    compareAndSetCurrentLineInProgram(ctx.Identifier());
    String id = ctx.Identifier().getText();
    checkLegalIdentifierAssign(ctx.Identifier());
    
    vars.assign(id, visit(ctx.expression()));
    return _void;
  }
  
  // a class for every function, contains the name of function, no of arguments with a method called
  // invoke. This object is put in a list of available methods attached here. once a function needs
  // to be invoked, search a matching object is list by function-name and no-of-arguments. If
  // matched, call the invoke function by passing list of arguments. The invoke function then
  // fetches argument one by one from list, converts to relevant type and invoke the actual function
  @Override
  public ZwlValue visitFunctionInvocation(FunctionInvocationContext ctx) {
    TerminalNode idNode = ctx.Identifier();
    compareAndSetCurrentLineInProgram(idNode);
    String funcName = idNode.getText();
    List<ZwlValue> params = new ArrayList<>();
    if (ctx.expressionList() != null) {
      for (ExpressionContext exp : ctx.expressionList().expression()) {
        params.add(visit(exp));
      }
    }
    Optional<Function> matched = functions.stream().filter(func -> func.getName().equals(funcName)
            && params.size() >= func.minParamsCount() && params.size() <= func.maxParamsCount())
        .findFirst();
    if (!matched.isPresent()) {
      throw new EvalException(getFromPos(ctx), getToPos(ctx),
          String.format("function: %s with parameters count: %s isn't defined%s",
              funcName, params.size(), lineNColumn(idNode)));
    }
    ZwlValue val;
    
    try {
      Function f = matched.get();
      f.setFromPos(() -> getFromPos(ctx)).setToPos(() -> getToPos(ctx));
      val = f.invoke(params,
          () -> ctx.defaultVal() != null ? visit(ctx.defaultVal().expression())
              : new NothingZwlValue(),
          () -> lineNColumn(idNode));
      // we expect all returned values to have one of the specified ZwlValue type and not 'null',
      // thus skipping a null check on function returned values.
    } catch (ZwlLangException zlEx) {
      throw zlEx;
    } catch (Throwable e) {
      throw new RuntimeException(e.getMessage() + lineNColumn(idNode), e);
    }
    
    // if index is given, apply it and get result
    if (ctx.indexes() != null) {
      val = resolveIndexes(val, ctx.indexes().expression());
    }
    
    // if further identifiers are given as part of map's key invocation, process them now.
    if (ctx.identifierExpr() != null) {
      val = resolveMapKey(val, ctx.identifierExpr());
      val = processResolvedIdentifier(val, ctx.identifierExpr());
    }
    return val;
  }
  
  // If a non-existent variable is referenced, process fails fast.
  @Override
  public ZwlValue visitIdentifierExpr(IdentifierExprContext ctx) {
    TerminalNode idNode = ctx.Identifier();
    String id = idNode.getText();
    Optional<ZwlValue> idValue = vars.resolve(id);
    if (!idValue.isPresent()) {
      throw new NoSuchVariableException(getFromPos(idNode), getToPos(idNode),
          String.format("Variable '%s' doesn't exist%s", id, lineNColumn(idNode)));
    }
    LOG.debug("inside identifier exp, id: {}, val: {}", id, idValue);
    return processResolvedIdentifier(idValue.get(), ctx);
  }
  
  private ZwlValue processResolvedIdentifier(ZwlValue resolvedId,
                                             IdentifierExprContext ctx) {
    // if index is given, apply it and get result
    LOG.debug("identifier: {}", ctx.Identifier().getText());
    if (ctx.indexes() != null) {
      LOG.debug("index: {}", ctx.indexes().getText());
      resolvedId = resolveIndexes(resolvedId, ctx.indexes().expression());
      LOG.debug("resolvedId: {}", resolvedId);
    }
    
    // if further identifiers are given as part of map's key invocation, process them now.
    if (ctx.identifierExpr() != null) {
      resolvedId = resolveMapKey(resolvedId, ctx.identifierExpr());
      return processResolvedIdentifier(resolvedId, ctx.identifierExpr());
    }
    LOG.debug("resolvedId: {}", resolvedId);
    return resolvedId;
  }
  
  // If a non-existent key is referenced through dot operator, process fail fast to detect error
  // earlier.
  private ZwlValue resolveMapKey(ZwlValue val, IdentifierExprContext ctx) {
    TerminalNode idNode = ctx.Identifier();
    String id = idNode.getText();
    Optional<Map<String, ZwlValue>> map = val.getMapValue();
    if (!map.isPresent()) {
      throw new EvalException(getFromPos(idNode), getToPos(idNode),
          "Resolving identifier " + id + " with DOT operator failed because" +
          " left operand is of type " + val.getType() + ". A 'Map' type is required" +
          lineNColumn(idNode));
    }
    if (!map.get().containsKey(id)) {
      throw new NoSuchMapKeyException(getFromPos(idNode), getToPos(idNode),
          String.format("The given map key %s has no mapping in the map%s", id,
              lineNColumn(idNode)));
    }
    return map.get().get(id);
  }
  
  // If a non-existent key is referenced through index, process fail fast to detect error earlier.
  // Same happens to list index.
  // resolves indexes recursively for n dimensional array or n key map.
  private ZwlValue resolveIndexes(ZwlValue val, List<ExpressionContext> indexes) {
    if (indexes == null || indexes.size() == 0) {
      throw new RuntimeException("Illegally trying to resolve index when they are not given");
    }
    
    for (ExpressionContext ec : indexes) {
      if (!(val.getListValue().isPresent() || val.getMapValue().isPresent())) {
        throw new InvalidTypeException(getFromPos(ec), getToPos(ec),
            "Can't resolve indexes on type " + val.getType() + ". A" +
            " 'List' or 'Map' type is required" + lineNColumn(ec));
      }
      if (val.getListValue().isPresent()) {
        List<ZwlValue> list = val.getListValue().get();
        int i = parseNumberExpr(ec).intValue();
        if (i < 0 || i >= list.size()) {
          throw new IndexOutOfRangeException(getFromPos(ec), getToPos(ec),
              String.format("The specified index isn't within " +
                  "the range of this List. Index given: %s, List size: %s%s", i, list.size(),
              lineNColumn(ec)));
        }
        val = list.get(i);
      } else {
        Map<String, ZwlValue> map = val.getMapValue().get();
        String key = visit(ec).toString();
        if (!map.containsKey(key)) {
          throw new NoSuchMapKeyException(getFromPos(ec), getToPos(ec),
              String.format("The given map key %s has no mapping in " +
              " the map%s", key, lineNColumn(ec)));
        }
        val = map.get(key);
      }
    }
    return val;
  }
  
  @Override
  public ZwlValue visitIfStatement(IfStatementContext ctx) {
    // report new line change only where the condition is met.
    if (parseBooleanExpr(ctx.ifBlock().expression())) {
      compareAndSetCurrentLineInProgram(ctx.ifBlock().IF());
      return visit(ctx.ifBlock().block());
    }
  
    if (ctx.elseIfBlock() != null) {
      for (ElseIfBlockContext elseif : ctx.elseIfBlock()) {
        if (parseBooleanExpr(elseif.expression())) {
          compareAndSetCurrentLineInProgram(elseif.ELSE());
          return visit(elseif.block());
        }
      }
    }
  
    if (ctx.elseBlock() != null) {
      compareAndSetCurrentLineInProgram(ctx.elseBlock().ELSE());
      return visit(ctx.elseBlock().block());
    }
    
    return _void;
  }
  
  @Override
  public ZwlValue visitForInListStatement(ForInListStatementContext ctx) {
    compareAndSetCurrentLineInProgram(ctx.FOR());
    ExpressionContext ec = ctx.expression();
    ZwlValue z = visit(ec);
    Optional<List<ZwlValue>> list = z.getListValue();
    if (!list.isPresent()) {
      throw new InvalidTypeException(getFromPos(ec), getToPos(ec),
          "Expression of type " + z.getType() + " couldn't be converted to a 'List'" +
              lineNColumn(ec));
    }
    LOG.debug("inside for-in, list is {}", list);
    TerminalNode idNode = ctx.Identifier();
    String id = idNode.getText();
    if (vars.exists(id)) {
      throw new EvalException(getFromPos(idNode), getToPos(idNode),
          String.format("Variable %s in 'for' statement is already assigned in outer scope%s",
              id, lineNColumn(idNode)));
    }
    for (ZwlValue val : list.get()) {
      vars.assign(id, val);
      LOG.debug("in loop, id: {}, val: {}", id, val);
      visit(ctx.block());
    }
    // remove the identifier once the loop is finished
    if (vars.exists(id)) {
      vars.delete(id);
    }

    return _void;
  }
  
  @Override
  public ZwlValue visitForInMapStatement(ForInMapStatementContext ctx) {
    compareAndSetCurrentLineInProgram(ctx.FOR());
    ExpressionContext ec = ctx.expression();
    ZwlValue z = visit(ec);
    Optional<Map<String, ZwlValue>> map = z.getMapValue();
    if (!map.isPresent()) {
      throw new InvalidTypeException(getFromPos(ec), getToPos(ec), "Expression of type " +
          z.getType() + " couldn't be converted to a 'Map'" + lineNColumn(ec));
    }
    TerminalNode keyId = ctx.Identifier(0);
    TerminalNode valueId = ctx.Identifier(1);
    String key = keyId.getText();
    String value = valueId.getText();
    
    if (vars.exists(key)) {
      throw new EvalException(getFromPos(keyId), getToPos(keyId),
          String.format("Variable %s in 'for' statement is already assigned in outer scope%s",
              key, lineNColumn(keyId)));
    }
    if (vars.exists(value)) {
      throw new EvalException(getFromPos(valueId), getToPos(valueId),
          String.format("Variable %s in 'for' statement is already assigned in outer scope%s",
              value, lineNColumn(valueId)));
    }
    
    for (Map.Entry<String, ZwlValue> entry : map.get().entrySet()) {
      vars.assign(key, new StringZwlValue(entry.getKey()));
      vars.assign(value, entry.getValue());
      visit(ctx.block());
    }
    // remove the identifiers once the loop is finished
    if (vars.exists(key)) {
      vars.delete(key);
    }
    if (vars.exists(value)) {
      vars.delete(value);
    }
  
    return _void;
  }
  
  @Override
  public ZwlValue visitWhileStatement(WhileStatementContext ctx) {
    TerminalNode idNode = ctx.WHILE();
    compareAndSetCurrentLineInProgram(idNode);
    long iterations = 0;
    long forLoopMaxItr = getForLoopMaxIterations();
    if (forLoopMaxItr < 0) {
      // set a default when no preferences are set, this should be set as default in front end too.
      forLoopMaxItr = FOR_LOOP_MAX_ITERATIONS;
    }
    while (parseBooleanExpr(ctx.expression())) {
      visit(ctx.block());
      iterations++;
      if (iterations > forLoopMaxItr) {
        throw new EvalException(getFromPos(idNode), getToPos(idNode),
            "While loop was broken after max iterations: " + forLoopMaxItr +
            " reached. Condition needs to be fixed" + lineNColumn(idNode));
      }
    }
    return _void;
  }
  
  @Override
  public ZwlValue visitForToStatement(ForToStatementContext ctx) {
    compareAndSetCurrentLineInProgram(ctx.FOR());
    TerminalNode idNode = ctx.assignment().Identifier();
    String id = idNode.getText();
    if (vars.exists(id)) {
      throw new EvalException(getFromPos(idNode), getToPos(idNode),
          String.format("Variable %s in 'for' statement is already assigned in outer scope%s",
              id, lineNColumn(idNode)));
    }
    Double start = parseNumberExpr(ctx.assignment().expression());
    Double stop = parseNumberExpr(ctx.expression());
    
    for (int i = start.intValue(); i <= stop.intValue(); i++) {
      vars.assign(id, new DoubleZwlValue(i));
      visit(ctx.block());
    }
    // remove the identifier once the loop is finished
    if (vars.exists(id)) {
      vars.delete(id);
    }
    
    return _void;
  }
  
  @Override
  public ZwlValue visitIncrement(IncrementContext ctx) {
    compareAndSetCurrentLineInProgram(ctx.Identifier());
    return incrementDecrement(ctx.Identifier(), v -> v + 1);
  }
  
  @Override
  public ZwlValue visitDecrement(DecrementContext ctx) {
    compareAndSetCurrentLineInProgram(ctx.Identifier());
    return incrementDecrement(ctx.Identifier(), v -> v - 1);
  }
  
  private ZwlValue incrementDecrement(TerminalNode node,
                                      java.util.function.Function<Double, Double> operation) {
    double val = 0;
    String id = node.getText();
    checkLegalIdentifierAssign(node);
    
    Optional<ZwlValue> previous = vars.resolve(id);
    if (previous.isPresent()) {
      Optional<Double> d = previous.get().getDoubleValue();
      if (!d.isPresent()) {
        throw new InvalidTypeException(getFromPos(node), getToPos(node),
            "Identifier " + id + " couldn't be converted from type " +
            previous.get().getType() + " to type Number" + lineNColumn(node));
      }
      val = d.get();
    }
    val = operation.apply(val);
  
    ZwlValue updatedVal = new DoubleZwlValue(val);
    vars.assign(id, updatedVal);
    
    // An increment/decrement operation returns previous value of variable, if variable didn't have
    // a value, return it's updated value.
    return previous.orElse(updatedVal);
  }
  
  @Override
  public ZwlValue visitAssertThrowsFunc(AssertThrowsFuncContext ctx) {
    TerminalNode idNode = ctx.ASSERT_THROWS();
    compareAndSetCurrentLineInProgram(idNode);
    // assertThrows accepts two or three arguments, 1) simple name of exception class,
    // 2) expression or block to evaluate, 3) message when assertion fails
    String exception = visit(ctx.expression(0)).toString();
    String customMessage = null;
    if (ctx.expression(2) != null) {
      customMessage = visit(ctx.expression(2)).toString();
    }
    String thrownException = null;
    String cause = null;
    try {
      visit(ctx.block() != null ? ctx.block() : ctx.expression(1));
    } catch (Throwable t) {
      thrownException = t.getClass().getSimpleName();
      if (t.getCause() != null) {
        cause = t.getCause().getClass().getSimpleName();
      }
    }
    
    if (exception.equals(thrownException) || exception.equals(cause)) {
      return _void;
    }
  
    String message = String.format("Expected exception: %s, Actual exception: %s%s", exception,
        thrownException == null ? "NONE"
            : thrownException + (cause == null ? "" : ", cause: " + cause),
        lineNColumn(idNode));
    
    throw new AssertionFailedException(getFromPos(idNode), getToPos(idNode), customMessage == null
        ? message : customMessage + StringUtil.getPlatformLineSeparator() + message);
  }
  
  @Override
  public ZwlValue visitAssertDoesNotThrowFunc(AssertDoesNotThrowFuncContext ctx) {
    TerminalNode idNode = ctx.ASSERT_DOES_NOT_THROW();
    compareAndSetCurrentLineInProgram(idNode);
    // assertDoesNotThrow accepts one or two arguments,
    // 1) expression/block to evaluate, 2) message when assertion fails
  
    String customMessage = null;
    if (ctx.expression(1) != null) {
      customMessage = visit(ctx.expression(1)).toString();
    }
  
    String thrownException = null;
    try {
      visit(ctx.block() != null ? ctx.block() : ctx.expression(0));
    } catch (Throwable t) {
      thrownException = t.getClass().getSimpleName();
    }
    
    if (thrownException == null) {
      return _void;
    }
    String message = "Expected no exception but " + thrownException + " was thrown" +
        lineNColumn(idNode);
    throw new AssertionFailedException(getFromPos(idNode), getToPos(idNode), customMessage == null
        ? message : customMessage + StringUtil.getPlatformLineSeparator() + message);
  }
  
  /**
   * Exists takes an expression and expects it to be one of {@link ZwlValue} type. It returns a
   * {@code true} only if the type is not {@link NothingZwlValue}, otherwise a {@code false} is
   * returned because {@link NothingZwlValue} points to non-existent value. If a non existent variable
   * is used, it returns false.
   */
  @Override
  public ZwlValue visitExistsFunc(ExistsFuncContext ctx) {
    compareAndSetCurrentLineInProgram(ctx.EXISTS());
    try {
      ZwlValue value = visit(ctx.expression());
      return new BooleanZwlValue(!value.getNothingValue().isPresent());
    } catch (NoSuchVariableException e) {
      return new BooleanZwlValue(false);
    }
  }
  
  @Override
  public ZwlValue visitListLiteral(ListLiteralContext ctx) {
    List<ZwlValue> list = new ArrayList<>();
    if (ctx.expressionList() != null) {
      for (ExpressionContext exp : ctx.expressionList().expression()) {
        list.add(visit(exp));
      }
    }
    return new ListZwlValue(list);
  }
  
  @Override
  public ZwlValue visitMapLiteral(MapLiteralContext ctx) {
    Map<String, ZwlValue> map = new LinkedHashMap<>();
    // we need ordering as insertion order, that's why a linked list based map.
    if (ctx.mapEntries() != null) {
      for (MapEntryContext entry : ctx.mapEntries().mapEntry()) {
        String key = entry.Identifier() != null ? entry.Identifier().getText()
            : processStringLiteral(entry.StringLiteral().getText());
        map.put(key, visit(entry.expression()));
      }
    }
    return new MapZwlValue(map);
  }
  
  @Override
  public ZwlValue visitUnaryMinusExpression(UnaryMinusExpressionContext ctx) {
    Double val = parseNumberExpr(ctx.expression());
    return new DoubleZwlValue(-1 * val);
  }
  
  @Override
  public ZwlValue visitNotExpression(NotExpressionContext ctx) {
    return new BooleanZwlValue(!parseBooleanExpr(ctx.expression()));
  }
  
  @Override
  public ZwlValue visitMultExpression(MultExpressionContext ctx) {
    ExpressionContext left = ctx.expression(0);
    ExpressionContext right = ctx.expression(1);
    switch (ctx.op.getType()) {
      case ZwlLexer.MUL:
        return binaryOpArithmetic(left, right, (l, r) -> l * r);
      case ZwlLexer.DIV:
        return binaryOpArithmetic(left, right, (l, r) -> l / r);
      case ZwlLexer.MOD:
        return binaryOpArithmetic(left, right, (l, r) -> l % r);
      default:
        throw new RuntimeException("unknown operator: " + ctx.op.getText());
    }
  }
  
  @Override
  public ZwlValue visitAddExpression(AddExpressionContext ctx) {
    ExpressionContext left = ctx.expression(0);
    ExpressionContext right = ctx.expression(1);
    switch (ctx.op.getType()) {
      case ZwlLexer.ADD:
        return add(left, right);
      case ZwlLexer.SUB:
        return binaryOpArithmetic(left, right, (l, r) -> l - r);
      default:
        throw new RuntimeException("unknown operator: " + ctx.op.getText());
    }
  }
  
  private ZwlValue add(ExpressionContext eLeft, ExpressionContext eRight) {
    LOG.debug("in addition, eLeft: {}, eRight: {}", eLeft.getText(), eRight.getText());
    // add operation first tries converting both operands into number.
    ZwlValue left = tryConvertNumber(visit(eLeft));
    ZwlValue right = tryConvertNumber(visit(eRight));
    
    if (left.getDoubleValue().isPresent() && right.getDoubleValue().isPresent()) {
      return new DoubleZwlValue(left.getDoubleValue().get() + right.getDoubleValue().get());
    }
    // if arithmetic add wasn't possible, treat both of them as string and concat.
    return new StringZwlValue(left + "" + right);
  }
  
  private ZwlValue tryConvertNumber(ZwlValue val) {
    if (val.getStringValue().isPresent()) {
      try {
        return new DoubleZwlValue(Double.parseDouble(val.getStringValue().get()));
      } catch (NumberFormatException ignore) {
        // ignore
      }
    }
    return val;
  }
  
  private ZwlValue binaryOpArithmetic(ExpressionContext left, ExpressionContext right,
                                      BinaryOperator<Double> operation) {
    return new DoubleZwlValue(operation.apply(parseNumberExpr(left), parseNumberExpr(right)));
  }
  
  @Override
  public ZwlValue visitCompExpression(CompExpressionContext ctx) {
    ExpressionContext left = ctx.expression(0);
    ExpressionContext right = ctx.expression(1);
    switch (ctx.op.getType()) {
      case ZwlLexer.GE:
        return binaryOpBoolean(left, right, (l, r) -> l >= r);
      case ZwlLexer.LE:
        return binaryOpBoolean(left, right, (l, r) -> l <= r);
      case ZwlLexer.GT:
        return binaryOpBoolean(left, right, (l, r) -> l > r);
      case ZwlLexer.LT:
        return binaryOpBoolean(left, right, (l, r) -> l < r);
      default:
        throw new RuntimeException("unknown operator: " + ctx.op.getText());
    }
  }
  
  private ZwlValue binaryOpBoolean(ExpressionContext left, ExpressionContext right,
                                   BiFunction<Double, Double, Boolean> operation) {
    return new BooleanZwlValue(operation.apply(parseNumberExpr(left), parseNumberExpr(right)));
  }
  
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  @Override
  public ZwlValue visitEqExpression(EqExpressionContext ctx) {
    ZwlValue left = visit(ctx.expression(0));
    ZwlValue right = visit(ctx.expression(1));
    boolean eqOp = ctx.op.getType() == ZwlLexer.EQUAL;
    if (left.getClass() == right.getClass()) {
      return new BooleanZwlValue(eqOp == left.equals(right));
    }
    // if left and right needs to be equal, it doesn't matter which side should convert to other,
    // both the sides should be convertible to each other to be able to equalize. For example,
    // to prove, 5 == "5" we can convert either left to right or right to left, both should become
    // of same type.
    switch (left.getType()) {
      case Types.STRING:
        return new BooleanZwlValue(eqOp == left.toString().equals(right.toString()));
      case Types.BOOLEAN:
        return new BooleanZwlValue(
            eqOp == left.getBooleanValue().get().equals(parseBooleanExpr(right, ctx)));
      case Types.NUMBER:
        return new BooleanZwlValue(
            eqOp == left.getDoubleValue().get().equals(parseNumberExpr(right, ctx)));
      default:
        throw new InvalidTypeException(getFromPos(ctx), getToPos(ctx), "Can't convert type " +
            right.getType() + " to " + left.getType() + " and vice-versa" + lineNColumn(ctx));
    }
  }
  
  @Override
  public ZwlValue visitAndExpression(AndExpressionContext ctx) {
    LOG.debug("in and exp, left: {}_{}, right: {}_{}", ctx.expression(0).getText(),
        ctx.expression(0).getClass().getSimpleName(), ctx.expression(1).getText(),
        ctx.expression(1).getClass().getSimpleName());
    return new BooleanZwlValue(
        parseBooleanExpr(ctx.expression(0)) && parseBooleanExpr(ctx.expression(1)));
  }
  
  @Override
  public ZwlValue visitOrExpression(OrExpressionContext ctx) {
    LOG.debug("in or exp, left: {}_{}, right: {}_{}", ctx.expression(0).getText(),
        ctx.expression(0).getClass().getSimpleName(), ctx.expression(1).getText(),
        ctx.expression(1).getClass().getSimpleName());
    // if first call to processBooleanExpr yields a true, second call is not made.
    return new BooleanZwlValue(
        parseBooleanExpr(ctx.expression(0)) || parseBooleanExpr(ctx.expression(1)));
  }
  
  @Override
  public ZwlValue visitParenthesizedExpression(ParenthesizedExpressionContext ctx) {
    return visit(ctx.expression());
  }
  
  @Override
  public ZwlValue visitNumberExpression(NumberExpressionContext ctx) {
    return new DoubleZwlValue(Double.parseDouble(ctx.getText()));
  }
  
  @Override
  public ZwlValue visitBooleanExpression(BooleanExpressionContext ctx) {
    return new BooleanZwlValue(Boolean.parseBoolean(ctx.getText()));
  }
  
  /*
   TODO: Currently I am only interpreting BMP characters from string since java chars are UTF16
    encoded. For unicode chars in other planes with more than 4 hex digits, those will need to be
    converted into UTF16 before they could be interpreted by java, for example the 😀 \\u+1F600 will
    need to converted into UTF16's \ud83d\ude00 (surrogate pair) first.
    Let's ask users to use raw strings for such chars for now and learn how to interpret all unicode
    chars later.
    https://www.branah.com/unicode-converter
    https://stackoverflow.com/questions/3537706/how-to-unescape-a-java-string-literal-in-java/4298836#4298836
    https://stackoverflow.com/questions/3630609/reading-unicode-character-in-java
    https://stackoverflow.com/questions/24840667/what-is-the-regex-to-extract-all-the-emojis-from-a-string
    https://stackoverflow.com/questions/47731148/convert-utf-8-to-unicode-to-find-emoji-in-string-in-java
    https://en.wikipedia.org/wiki/Emoticons_(Unicode_block)
    https://en.wikipedia.org/wiki/Plane_%28Unicode%29
    https://en.wikipedia.org/wiki/List_of_Unicode_characters
   */
  @Override
  public ZwlValue visitStringExpression(StringExpressionContext ctx) {
    String s = ctx.getText();
    LOG.debug("Normal string: {}", s);
    // Interpret escape sequences, strip out quotes in string.
    return new StringZwlValue(processStringLiteral(s));
  }
  
  private String processStringLiteral(String textWithQuotes) {
    return StringEscapeUtils.unescapeJava(textWithQuotes.substring(1,
        textWithQuotes.length() - 1));
  }
  
  @Override
  public ZwlValue visitRawStringExpression(RawStringExpressionContext ctx) {
    // When user types something with new lines and os is windows, carriage return is added together with new line.
    // When matching something with driver, driver doesn't give carriage returns but only new line (nothing else should
    // add a carriage return with new line than a windows OS), thus we should strip them out and make it a unix like
    // string.
    String s = ctx.getText().replace("\r", "");
    LOG.debug("Raw string: {}", s);
    // strip out back quotes in string.
    return new StringZwlValue(s.substring(1, s.length() - 1));
  }
  
  private Double parseNumberExpr(ZwlValue z, ExpressionContext exp) {
    return ParseUtil.parseDouble(z,
        () -> new InvalidTypeException(getFromPos(exp), getToPos(exp),
            "Expression of type " + z.getType() + " couldn't be converted to a 'Number'" +
                lineNColumn(exp)));
  }
  
  private Double parseNumberExpr(ExpressionContext exp) {
    
    ZwlValue z = visit(exp);
    return parseNumberExpr(z, exp);
  }
  
  private Boolean parseBooleanExpr(ZwlValue z, ExpressionContext exp) {
    LOG.debug("processBooleanExpr, class: {}, exp: {}, visit res: {}",
        exp.getClass().getSimpleName(), exp.getText(), z);
  
    return ParseUtil.parseBoolean(z,
        () -> new InvalidTypeException(getFromPos(exp), getToPos(exp),
            "Expression of type " + z.getType() + " couldn't be converted to a 'Boolean'" +
                lineNColumn(exp)));
  }
  
  private Boolean parseBooleanExpr(ExpressionContext exp) {
    ZwlValue z = visit(exp);
    return parseBooleanExpr(z, exp);
  }
  
  private String lineNColumn(TerminalNode node) {
    return lineNColumn(node.getSymbol());
  }
  
  // all exception messages must append their line information using this function or if they
  // can't use it, they must implement the mentioned method.
  private String lineNColumn(Token token) {
    return String.format(" - %s:%s", token.getLine()
        , token.getCharPositionInLine() + 1); // add 1 because char pos is 0 based index but we need to
    // show 1..n based at client.
  }
  
  private String lineNColumn(ParserRuleContext ctx) {
    return String.format(" - %s:%s to %s:%s",
        ctx.start.getLine(), ctx.start.getCharPositionInLine() + 1, ctx.stop.getLine(),
        ctx.stop.getCharPositionInLine() + 1 + ctx.stop.getText().length()); // added last token's
    // char length to index to get last char position in line
  }
  
  private long getForLoopMaxIterations() {
    Optional<Map<String, ZwlValue>> p = getPreferences();
    if (!(p.isPresent() && p.get().containsKey(FOR_LOOP_MAX_ITERATIONS_KEY))) {
      // LOG.warn("Preferences doesn't contain infinite loop detection key: " +
         // FOR_LOOP_MAX_ITERATIONS_KEY);
      return -1;
    }
    ZwlValue num = tryConvertNumber(p.get().get(FOR_LOOP_MAX_ITERATIONS_KEY));
    if (!num.getDoubleValue().isPresent()) {
      return -1;
    }
    return num.getDoubleValue().get().longValue();
  }
  
  // Right now there is no preferences option in front end, thus warning logging is disabled.
  private Optional<Map<String, ZwlValue>> getPreferences() {
    Optional<ZwlValue> p = vars.resolve(PREFERENCES_KEY);
    if (!p.isPresent()) {
      // LOG.warn(PREFERENCES_KEY + " were not supplied.");
      return Optional.empty();
    }
    Optional<Map<String, ZwlValue>> preferences = p.get().getMapValue();
    if (!preferences.isPresent()) {
      throw new RuntimeException(PREFERENCES_KEY + " isn't a type of 'Map'.");
    }
    return preferences;
  }
  
  private void checkLegalIdentifierAssign(TerminalNode identifier) {
    String id = identifier.getText();
    String idLower = id.toLowerCase();
    LOG.debug("idLower is {}", idLower);
    LOG.debug("readOnlyVars is {}", readOnlyVars);
    LOG.debug("RESERVED_KEYWORDS is {}", RESERVED_KEYWORDS);
    if (readOnlyVars.contains(idLower)) {
      throw new IllegalIdentifierException(getFromPos(identifier), getToPos(identifier),
          "Read only identifier: " + id + " can't be assigned" + lineNColumn(identifier));
    }
    if (RESERVED_KEYWORDS.contains(idLower)) {
      throw new IllegalIdentifierException(getFromPos(identifier), getToPos(identifier),
          "Restricted keyword: " + id + " can't be used as an identifier" +
              lineNColumn(identifier));
    }
  }
  
  private String getFromPos(TerminalNode node) {
    return getFromPos(node.getSymbol());
  }
  
  private String getFromPos(ParserRuleContext ctx) {
    return getFromPos(ctx.start);
  }
  
  private String getFromPos(Token token) {
    return String.format("%s:%s", token.getLine(), token.getCharPositionInLine()); // char pos is 0 index based
  }
  
  private String getToPos(TerminalNode node) {
    return getToPos(node.getSymbol());
  }
  
  private String getToPos(ParserRuleContext ctx) {
    return getToPos(ctx.stop);
  }
  
  private String getToPos(Token token) {
    // to pos is after the token ends
    return String.format("%s:%s", token.getLine(),
        token.getCharPositionInLine() + token.getText().length());
  }
  
  // should be invoked on every statement visit
  private void compareAndSetCurrentLineInProgram(TerminalNode node) {
    int tokenLine = node.getSymbol().getLine();
    if (currentLineInProgram == tokenLine) {
      return;
    }
    currentLineInProgram = tokenLine;
    notifyLineChangeListeners();
  }
  
  private void notifyLineChangeListeners() {
    lineChangeListeners.forEach(l -> l.onLineChange(currentLineInProgram));
  }
}
