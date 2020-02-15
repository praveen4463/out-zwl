package com.zylitics.zwl.function;

import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.exception.InvalidRegexPatternException;
import com.zylitics.zwl.exception.InvalidTypeException;
import com.zylitics.zwl.interpret.Function;
import com.zylitics.zwl.util.ParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public abstract class AbstractFunction implements Function {
  
  private static final Logger LOG = LoggerFactory.getLogger(AbstractFunction.class);
  
  protected final ZwlValue _void = new VoidZwlValue();
  
  protected Supplier<String> lineNColumn;
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue
      , Supplier<String> lineNColumn) {
    assertArgs(args);
    
    this.lineNColumn = lineNColumn;
    return _void;
  }
  
  protected void assertArgs(List<ZwlValue> args) {
    Objects.requireNonNull(args, "arguments can't be null");
    int argsCount = args.size();
    
    if (argsCount < minParamsCount() || argsCount > maxParamsCount()) {
      throw new EvalException(String.format("function: %s with parameters count: %s isn't " +
          "defined. %s", getName(), argsCount, lineNColumn.get()));
    }
    
    // TODO: remove this after no such warning in logs.
    // check none of the elements in list is 'null' as we don't expect them, we just expect our own
    // type 'Nothing', this is not required though since we expect all null values to be
    // Nothing.
    if (args.stream().anyMatch(Objects::isNull)) {
      LOG.warn("some arguments in call to {} were 'null' and being converted to 'Nothing'",
          getName());
      args.replaceAll(z -> Objects.isNull(z) ? new NothingZwlValue() : z);
    }
  }
  
  protected RuntimeException unexpectedEndOfFunctionOverload(int argsCount) {
    return new RuntimeException(String.format("function: %s with parameters count: %s should've " +
        "been implemented but is not there, it could be possible that maxParamsCount field " +
        "contains an invalid number that exceeds the availability.", getName(), argsCount));
  }
  
  protected Map<String, ZwlValue> tryCastMap(int argIndex, ZwlValue val) {
    Optional<Map<String, ZwlValue>> m = val.getMapValue();
    if (!m.isPresent()) {
      throw getWrongTypeException(val, Types.MAP, argIndex);
    }
    return m.get();
  }
  
  protected List<ZwlValue> tryCastList(int argIndex, ZwlValue val) {
    Optional<List<ZwlValue>> l = val.getListValue();
    if (!l.isPresent()) {
      throw getWrongTypeException(val, Types.LIST, argIndex);
    }
    return l.get();
  }
  
  protected Double parseDouble(int argIndex, ZwlValue val) {
    return ParseUtil.parseDouble(val, getWrongTypeException(val, Types.NUMBER, argIndex));
  }
  
  @SuppressWarnings("SameParameterValue")
  protected Boolean parseBoolean(int argIndex, ZwlValue val) {
    return ParseUtil.parseBoolean(val, getWrongTypeException(val, Types.BOOLEAN, argIndex));
  }
  
  protected String tryCastString(int argIndex, ZwlValue val) {
    Optional<String> s = val.getStringValue();
    if (!s.isPresent()) {
      throw getWrongTypeException(val, Types.STRING, argIndex);
    }
    return s.get();
  }
  
  // Note: This exception is worded for function arguments specifically, sometimes when user sends
  // arguments in form of array (if function supports), this function becomes slightly wrong due to
  // using "at argument" whereas it should say "at list index". We can list this in documentation so
  // that it doesn't confuse them. For example we could say: When functions accepts a list in place
  // of arguments, that list is expanded to arguments internally and if some element was invalid,
  // we'll report it as if it was an argument.
  private InvalidTypeException getWrongTypeException(ZwlValue val, String type, int argIndex) {
    return new InvalidTypeException(String.format("Given value: %s at argument: %s, isn't of type" +
            " '%s'. %s", val, argIndex, type, lineNColumn.get()));
  }
  
  protected ListZwlValue getListZwlValue(List<String> stringsList) {
    return new ListZwlValue(stringsList.stream().map(StringZwlValue::new)
        .collect(Collectors.toList()));
  }
  
  protected Pattern getPattern(String regex) {
    Pattern pattern;
    try {
      // if we can't interpret the pattern, raise exception.
      // Note that this handles most errors in pattern, such as duplicate group names, invalid use
      // of flags etc.
      pattern = Pattern.compile(regex);
    } catch (PatternSyntaxException pse) {
      throw new InvalidRegexPatternException(withLineNCol(pse.getMessage()), pse);
    }
    return pattern;
  }
  
  protected String withLineNCol(String s) {
    return s + " " + lineNColumn.get();
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !getClass().getSimpleName().equals(o.getClass().getSimpleName())) return false;
    AbstractFunction that = (AbstractFunction) o;
    return getName().equals(that.getName()) && minParamsCount() == that.minParamsCount()
        && maxParamsCount() == that.maxParamsCount();
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(getName(), minParamsCount(), maxParamsCount());
  }
}
