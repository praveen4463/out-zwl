package com.zylitics.zwl.interpret;

import com.zylitics.zwl.api.ZwlInterpreter;
import com.zylitics.zwl.api.ZwlInterpreterVisitor;
import com.zylitics.zwl.antlr4.ZwlLexer;
import com.zylitics.zwl.antlr4.ZwlParser.*;
import com.zylitics.zwl.antlr4.ZwlParserBaseVisitor;
import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.exception.IllegalIdentifierException;
import com.zylitics.zwl.exception.IndexOutOfRangeException;
import com.zylitics.zwl.exception.InvalidTypeException;
import com.zylitics.zwl.internal.Variables;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import static com.zylitics.zwl.internal.ReservedKeywords.RESERVED_KEYWORDS;

public class DefaultZwlInterpreter extends ZwlParserBaseVisitor<ZwlValue>
    implements ZwlInterpreter {
  
  private static final Logger LOG = LoggerFactory.getLogger(DefaultZwlInterpreter.class);
  
  private final List<String> readOnlyVars = new ArrayList<>();
  private final ZwlValue _void = new VoidZwlValue();
  
  private final Variables vars;
  private final List<Function> functions;
  
  public DefaultZwlInterpreter() {
    long start = System.nanoTime();
    // added function list should be constructed new, each request should use it's own set of
    // function objects.
    this.functions = new ArrayList<>(BuiltInFunction.get());
    LOG.debug("time to create built-in function list: {}", TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - start));
    this.vars = new Variables();
  }
  
  public void accept(ZwlInterpreterVisitor visitor) {
    visitor.visit(this);
  }
  
  @Override
  public void setReadOnlyVariable(String identifier, ZwlValue value) {
    Objects.requireNonNull(identifier, "identifier can't be null");
    Objects.requireNonNull(value, "value can't be null");
    
    String lower = identifier.toLowerCase();
    if (readOnlyVars.contains(lower)) {
      return;
    }
    readOnlyVars.add(lower);
    vars.assign(identifier, value);
  }
  
  @Override
  public void setFunctions(List<Function> functions) {
    Objects.requireNonNull(functions, "function list can't be null");
    
    for (Function f : functions) {
      setFunction(f);
    }
  }
  
  @Override
  public void setFunction(Function function) {
    Objects.requireNonNull(function, "function can't be null");
    
    functions.removeIf(f -> f.equals(function));
    functions.add(function);
  }
  
  @Override
  public ZwlValue visitAssignment(AssignmentContext ctx) {
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
    String funcName = ctx.Identifier().getText();
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
      throw new EvalException(String.format("function: %s with parameters count: %s isn't " +
          "defined. " + lineNColumn(ctx.Identifier()), funcName, params.size()));
    }
    ZwlValue val;
    
    try {
      val = matched.get().invoke(params,
          () -> ctx.defaultVal() != null ? visit(ctx.defaultVal().expression())
              : new NothingZwlValue(),
          () -> lineNColumn(ctx.Identifier()));
      // we expect all returned values to have one of the specified ZwlValue type and not 'null',
      // thus skipping a null check on function returned values.
    } catch (EvalException eval) {
      throw eval;
    } catch (Throwable e) {
      throw new RuntimeException(e.getMessage() + " " + lineNColumn(ctx.Identifier()), e);
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
  
  @Override
  public ZwlValue visitIdentifierExpr(IdentifierExprContext ctx) {
    String id = ctx.Identifier().getText();
    Optional<ZwlValue> idValue = vars.resolve(id);
    LOG.debug("inside identifier exp, id: {}, val: {}", id, idValue);
    return processResolvedIdentifier(idValue.orElse(new NothingZwlValue()), ctx);
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
  
  private ZwlValue resolveMapKey(ZwlValue val, IdentifierExprContext ctx) {
    String id = ctx.Identifier().getText();
    Optional<Map<String, ZwlValue>> map = val.getMapValue();
    if (!map.isPresent()) {
      throw new EvalException("Resolving identifier " + id + " with DOT operator failed because" +
          " left operand is of type " + val.getType() + ". A 'Map' type is required. " +
          lineNColumn(ctx.Identifier()));
    }
    // users have two ways to check a key existence, using containsKey or exists
    // This handle null handle null keys as well as null values (although there shouldn't be null
    // value for a valid key but type Nothing)
    return handleNull(map.get().get(id));
  }
  
  // resolves indexes recursively for n dimensional array.
  private ZwlValue resolveIndexes(ZwlValue val, List<ExpressionContext> indexes) {
    if (indexes == null || indexes.size() == 0) {
      throw new RuntimeException("Illegally trying to resolve index when they are not given");
    }
    
    for (ExpressionContext ec : indexes) {
      Optional<List<ZwlValue>> list = val.getListValue();
      if (!list.isPresent()) {
        throw new InvalidTypeException("Can't resolve indexes on type " + val.getType() + ". A" +
            " 'List' type is required. " + lineNColumn(ec));
      }
      
      int i = processNumberExpr(ec).intValue();
      if (i < 0 || i >= list.get().size()) {
        throw new IndexOutOfRangeException(String.format("The specified index isn't within " +
            "the range of this List. Index given: %s, List size: %s %s", i, list.get().size(),
            lineNColumn(ec)));
      }
      // no need to handle nulls as we don't expect list elements to be of type 'null'
      val = list.get().get(i);
    }
    return val;
  }
  
  @Override
  public ZwlValue visitIfStatement(IfStatementContext ctx) {
    if (processBooleanExpr(ctx.ifBlock().expression())) {
      return visit(ctx.ifBlock().block());
    }
    
    if (ctx.elseIfBlock() != null) {
      for (ElseIfBlockContext elseif : ctx.elseIfBlock()) {
        if (processBooleanExpr(elseif.expression())) {
          return visit(elseif.block());
        }
      }
    }
    
    if (ctx.elseBlock() != null) {
      return visit(ctx.elseBlock().block());
    }
    
    return _void;
  }
  
  @Override
  public ZwlValue visitForInListStatement(ForInListStatementContext ctx) {
    ZwlValue z = visit(ctx.expression());
    Optional<List<ZwlValue>> list = z.getListValue();
    if (!list.isPresent()) {
      throw new InvalidTypeException("Expression of type " + z.getType() + " couldn't be converted" +
          " to a 'List'. " + lineNColumn(ctx.expression()));
    }
    LOG.debug("inside for-in, list is {}", list);
    String id = ctx.Identifier().getText();
    if (vars.exists(id)) {
      throw new EvalException(String.format("Variable in 'for' statement is already assigned in" +
              " outer scope.%nVariable: %s, %s",
          id, lineNColumn(ctx.Identifier())));
    }
    for (ZwlValue val : list.get()) {
      vars.assign(id, val);
      LOG.debug("in loop, id: {}, val: {}", id, val);
      visit(ctx.block());
    }
    // remove the identifier once the loop is finished
    vars.delete(id);
    
    return _void;
  }
  
  @Override
  public ZwlValue visitForInMapStatement(ForInMapStatementContext ctx) {
    ZwlValue z = visit(ctx.expression());
    Optional<Map<String, ZwlValue>> map = z.getMapValue();
    if (!map.isPresent()) {
      throw new InvalidTypeException("Expression of type " + z.getType() + " couldn't be converted" +
          " to a 'Map'. " + lineNColumn(ctx.expression()));
    }
    String key = ctx.Identifier(0).getText();
    String value = ctx.Identifier(1).getText();
    
    StringBuilder variablesExist = new StringBuilder();
    if (vars.exists(key)) {
      variablesExist.append(String.format("Variable: %s, %s%n", key,
          lineNColumn(ctx.Identifier(0))));
    }
    if (vars.exists(value)) {
      variablesExist.append(String.format("Variable: %s, %s", value,
          lineNColumn(ctx.Identifier(1))));
    }
    if (variablesExist.length() > 0) {
      throw new EvalException("Variable in 'for' statement is already assigned in outer scope.\n" +
          variablesExist.toString());
    }
    
    for (Map.Entry<String, ZwlValue> entry : map.get().entrySet()) {
      vars.assign(key, new StringZwlValue(entry.getKey()));
      vars.assign(value, entry.getValue());
      visit(ctx.block());
    }
    // remove the identifiers once the loop is finished
    vars.delete(key);
    vars.delete(value);
  
    return _void;
  }
  
  @Override
  public ZwlValue visitWhileStatement(WhileStatementContext ctx) {
    long iterations = 0;
    long forLoopMaxItr = getForLoopMaxIterations();
    while (processBooleanExpr(ctx.expression())) {
      visit(ctx.block());
      iterations++;
      if (forLoopMaxItr > -1 && iterations > forLoopMaxItr) {
        throw new EvalException("While loop was broken after max iterations: " + forLoopMaxItr +
            " reached. Condition needs to be fixed. " + lineNColumn(ctx.WHILE()));
      }
    }
    return _void;
  }
  
  @Override
  public ZwlValue visitForToStatement(ForToStatementContext ctx) {
    String id = ctx.assignment().Identifier().getText();
    if (vars.exists(id)) {
      throw new EvalException(String.format("Variable assigned in 'for' statement is already" +
              " assigned in outer scope.%nVariable: %s, %s",
          id, lineNColumn(ctx.assignment().Identifier())));
    }
    Double start = processNumberExpr(ctx.assignment().expression());
    Double stop = processNumberExpr(ctx.expression());
    
    for (int i = start.intValue(); i <= stop.intValue(); i++) {
      vars.assign(id, new DoubleZwlValue(i));
      visit(ctx.block());
    }
    // remove the identifier once the loop is finished
    vars.delete(id);
    
    return _void;
  }
  
  @Override
  public ZwlValue visitIncrement(IncrementContext ctx) {
    return incrementDecrement(ctx.Identifier(), v -> v + 1);
  }
  
  @Override
  public ZwlValue visitDecrement(DecrementContext ctx) {
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
        throw new InvalidTypeException("Identifier " + id + " couldn't be converted from type " +
            previous.get().getType() + " to type Number. " + lineNColumn(node));
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
    Map<String, ZwlValue> map = new HashMap<>();
    if (ctx.mapEntries() != null) {
      for (MapEntryContext entry : ctx.mapEntries().mapEntry()) {
        map.put(entry.Identifier().getText(), visit(entry.expression()));
      }
    }
    return new MapZwlValue(map);
  }
  
  @Override
  public ZwlValue visitUnaryMinusExpression(UnaryMinusExpressionContext ctx) {
    Double val = processNumberExpr(ctx.expression());
    return new DoubleZwlValue(-1 * val);
  }
  
  @Override
  public ZwlValue visitNotExpression(NotExpressionContext ctx) {
    return new BooleanZwlValue(!processBooleanExpr(ctx.expression()));
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
    ZwlValue left = visit(eLeft);
    ZwlValue right = visit(eRight);
    
    Optional<Double> dLeft = left.getDoubleValue();
    Optional<Double> dRight = right.getDoubleValue();
    LOG.debug("in addition, left: {}, right: {}", dLeft, dRight);
    if (dLeft.isPresent() && dRight.isPresent()) {
      return new DoubleZwlValue(dLeft.get() + dRight.get());
    }
    
    return new StringZwlValue(left + "" + right);
  }
  
  private ZwlValue binaryOpArithmetic(ExpressionContext left, ExpressionContext right,
                                      BinaryOperator<Double> operation) {
    return new DoubleZwlValue(operation.apply(processNumberExpr(left), processNumberExpr(right)));
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
    return new BooleanZwlValue(operation.apply(processNumberExpr(left), processNumberExpr(right)));
  }
  
  @Override
  public ZwlValue visitEqExpression(EqExpressionContext ctx) {
    ZwlValue left = visit(ctx.expression(0));
    ZwlValue right = visit(ctx.expression(1));
    return new BooleanZwlValue((ctx.op.getType() == ZwlLexer.EQUAL) == left.equals(right));
  }
  
  @Override
  public ZwlValue visitAndExpression(AndExpressionContext ctx) {
    LOG.debug("in and exp, left: {}_{}, right: {}_{}", ctx.expression(0).getText(),
        ctx.expression(0).getClass().getSimpleName(), ctx.expression(1).getText(),
        ctx.expression(1).getClass().getSimpleName());
    return new BooleanZwlValue(
        processBooleanExpr(ctx.expression(0)) && processBooleanExpr(ctx.expression(1)));
  }
  
  @Override
  public ZwlValue visitOrExpression(OrExpressionContext ctx) {
    LOG.debug("in or exp, left: {}_{}, right: {}_{}", ctx.expression(0).getText(),
        ctx.expression(0).getClass().getSimpleName(), ctx.expression(1).getText(),
        ctx.expression(1).getClass().getSimpleName());
    // if first call to processBooleanExpr yields a true, second call is not made.
    return new BooleanZwlValue(
        processBooleanExpr(ctx.expression(0)) || processBooleanExpr(ctx.expression(1)));
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
    return new StringZwlValue(StringEscapeUtils.unescapeJava(s.substring(1, s.length() - 1)));
  }
  
  @Override
  public ZwlValue visitRawStringExpression(RawStringExpressionContext ctx) {
    String s = ctx.getText();
    LOG.debug("Raw string: {}", s);
    // strip out back quotes in string.
    return new StringZwlValue(s.substring(1, s.length() - 1));
  }
  
  private Double processNumberExpr(ExpressionContext exp) {
    ZwlValue z = visit(exp);
    Optional<Double> val = z.getDoubleValue();
    if (!val.isPresent()) {
      throw new InvalidTypeException("Expression of type " + z.getType() + " couldn't be " +
          "converted to a 'Number'. " + lineNColumn(exp));
    }
    return val.get();
  }
  
  private Boolean processBooleanExpr(ExpressionContext exp) {
    LOG.debug("on enter processBooleanExpr, exp class: {}", exp.getClass().getSimpleName());
    ZwlValue z = visit(exp);
    LOG.debug("processBooleanExpr, class: {}, exp: {}, visit res: {}",
        exp.getClass().getSimpleName(), exp.getText(), z);
    Optional<Boolean> val = z.getBooleanValue();
    if (!val.isPresent()) {
      throw new InvalidTypeException("Expression of type " + z.getType() + " couldn't be " +
          "converted to a 'Boolean'. " + lineNColumn(exp));
    }
    return val.get();
  }
  
  private String lineNColumn(TerminalNode node) {
    return lineNColumn(node.getSymbol());
  }
  
  private String lineNColumn(Token token) {
    return String.format("at line %s:%s", token.getLine()
        , token.getCharPositionInLine() + 1); // add 1 because it's 0 based index
  }
  
  private String lineNColumn(ParserRuleContext ctx) {
    return String.format("at start_line %s:%s, end_line %s:%s",
        ctx.start.getLine(), ctx.start.getCharPositionInLine() + 1, ctx.stop.getLine(),
        ctx.stop.getCharPositionInLine() + ctx.stop.getText().length()); // added last token's
    // char length to index to get last char position in line
  }
  
  private long getForLoopMaxIterations() {
    String key = "for_loop_max_iterations";
    Optional<Map<String, ZwlValue>> p = getPreferences();
    if (!(p.isPresent() && p.get().containsKey(key)
        && p.get().get(key).getDoubleValue().isPresent())) {
      LOG.warn("Preferences doesn't contain infinite loop detection key: " + key);
      return -1;
    }
    return p.get().get(key).getDoubleValue().get().longValue();
  }
  
  private Optional<Map<String, ZwlValue>> getPreferences() {
    Optional<ZwlValue> p = vars.resolve("preferences");
    if (!p.isPresent()) {
      LOG.warn("Preferences were not supplied.");
      return Optional.empty();
    }
    Optional<Map<String, ZwlValue>> preferences = p.get().getMapValue();
    if (!preferences.isPresent()) {
      throw new RuntimeException("Preferences isn't a type of 'Map'.");
    }
    return preferences;
  }
  
  private ZwlValue handleNull(ZwlValue val) {
    return val != null ? val : new NothingZwlValue();
  }
  
  private void checkLegalIdentifierAssign(TerminalNode identifier) {
    String id = identifier.getText();
    String idLower = id.toLowerCase();
    if (readOnlyVars.contains(idLower)) {
      throw new IllegalIdentifierException("Read only identifier: " + id + " can't be assigned. " +
          lineNColumn(identifier));
    }
    if (RESERVED_KEYWORDS.contains((idLower))) {
      throw new IllegalIdentifierException("Restricted keyword: " + id + " can't be used as an" +
          " identifier. " + lineNColumn(identifier));
    }
  }
}
