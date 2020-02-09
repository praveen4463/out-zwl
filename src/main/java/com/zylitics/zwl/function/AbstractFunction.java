package com.zylitics.zwl.function;

import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.exception.InvalidRegexPatternException;
import com.zylitics.zwl.exception.InvalidTypeException;
import com.zylitics.zwl.interpret.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public abstract class AbstractFunction implements Function {
  
  private static final Logger LOG = LoggerFactory.getLogger(AbstractFunction.class);
  
  protected final ZwlValue _void = new VoidZwlValue();
  
  protected void assertArgs(List<ZwlValue> args) {
    Objects.requireNonNull(args, "arguments can't be null");
    int argsCount = args.size();
    
    if (argsCount < minParamsCount() || argsCount > maxParamsCount()) {
      throw new EvalException(String.format("function: %s with parameters count: %s isn't " +
          "defined.", getName(), argsCount));
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
  
  protected List<ZwlValue> tryCastList(int argIndex, ZwlValue val) {
    Optional<List<ZwlValue>> l = val.getListValue();
    if (!l.isPresent()) {
      throwWrongTypeException(val, "List", argIndex);
    }
    return l.get();
  }
  
  protected Double tryCastDouble(int argIndex, ZwlValue val) {
    Optional<Double> d = val.getDoubleValue();
    if (!d.isPresent()) {
      throwWrongTypeException(val, "Double", argIndex);
    }
    return d.get();
  }
  
  protected Boolean tryCastBoolean(int argIndex, ZwlValue val) {
    Optional<Boolean> b = val.getBooleanValue();
    if (!b.isPresent()) {
      throwWrongTypeException(val, "Boolean", argIndex);
    }
    return b.get();
  }
  
  protected String tryCastString(int argIndex, ZwlValue val) {
    Optional<String> s = val.getStringValue();
    if (!s.isPresent()) {
      throwWrongTypeException(val, "String", argIndex);
    }
    return s.get();
  }
  
  private void throwWrongTypeException(ZwlValue val, String type, int argIndex) {
    throw new InvalidTypeException(String.format("Given value: %s at argument: %s, isn't of type" +
            " '%s'.", val, argIndex, type));
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
      throw new InvalidRegexPatternException(pse.getMessage(), pse);
    }
    return pattern;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AbstractFunction that = (AbstractFunction) o;
    return getName().equals(that.getName()) && minParamsCount() == that.minParamsCount()
        && maxParamsCount() == that.maxParamsCount();
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(getName(), minParamsCount(), maxParamsCount());
  }
}
