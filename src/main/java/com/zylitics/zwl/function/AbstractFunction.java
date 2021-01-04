package com.zylitics.zwl.function;

import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.exception.IndexOutOfRangeException;
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

/*
Note:
1. Any implementing function may save state but that will be remain same for the lifetime of the
   build, if that function is used further in build, the same state will be used because functions
   are instantiated only once per build.
2. Pay attention to doNotExpandListToArguments
 */
public abstract class AbstractFunction implements Function {
  
  private static final Logger LOG = LoggerFactory.getLogger(AbstractFunction.class);
  
  protected final ZwlValue _void = new VoidZwlValue();
  
  protected Supplier<String> lineNColumn;
  
  protected Supplier<String> fromPos;
  
  protected Supplier<String> toPos;
  
  @Override
  public AbstractFunction setFromPos(Supplier<String> fromPos) {
    this.fromPos = fromPos;
    return this;
  }
  
  @Override
  public AbstractFunction setToPos(Supplier<String> toPos) {
    this.toPos = toPos;
    return this;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue
      , Supplier<String> lineNColumn) {
    assertArgs(args);
    expandListToArguments(args);
    this.lineNColumn = lineNColumn;
    return _void;
  }
  
  protected void assertArgs(List<ZwlValue> args) {
    Objects.requireNonNull(args, "arguments can't be null");
    int argsCount = args.size();
    
    if (argsCount < minParamsCount() || argsCount > maxParamsCount()) {
      throw new EvalException(fromPos.get(), toPos.get(), String.format("function: %s with" +
          " parameters count: %s isn't defined%s", getName(), argsCount, lineNColumn.get()));
    }
    
    // check none of the elements in list is 'null' as we don't expect them, we just expect our own
    // type 'Nothing', this is not required though since we expect all null values to be
    // Nothing.
    if (args.stream().anyMatch(Objects::isNull)) {
      LOG.warn("some arguments in call to {} were 'null' and being converted to 'Nothing'",
          getName());
      args.replaceAll(z -> Objects.isNull(z) ? new NothingZwlValue() : z);
    }
  }
  
  /**
   * <p>Many functions in ZWL accepts varargs argument (always max 1 and last argument), users can
   * either send them expanded or as a list.</p>
   * <p>When it is given as list, this method expand it to arguments before the function starts so
   * that each function can assume it always get expanded argument wherever a varargs is accepted.
   * </p>
   * <p>Function that wish to accept list as argument (that are not varargs), can override
   * doNotExpandListToArguments and return 'true'.</p>
   * <p>This method returns immediately when there are more than one list present in arguments
   * or the single list isn't given as last argument. If a function accepts more than one varargs
   * it may become impossible for it to differentiate between varargs arguments. If varargs not
   * the last argument, argument that appears after it will also become part of it. In both
   * scenarios, the correctness of arguments can't be validated.</p>
   * @param args The received arguments from Zwl
   */
  private void expandListToArguments(List<ZwlValue> args) {
    if (doNotExpandListToArguments()) {
      return;
    }
    long listsInArgs = args.stream().filter(z -> z.getListValue().isPresent()).count();
    if (listsInArgs == 0) {
      return;
    }
    if (listsInArgs > 1) {
      LOG.warn(getName() + " receives more than one list as argument and is not marked as" +
          "doNotExpandListToArguments=true, this should be fixed");
      return;
    }
    int lastIndex = args.size() - 1;
    Optional<List<ZwlValue>> listFromArgs = args.get(lastIndex).getListValue();
    if (!listFromArgs.isPresent()) {
      LOG.warn(getName() + " has a list in arguments but it's not the last argument and thus" +
          "shouldn't be expanded, did you mean to set doNotExpandListToArguments=true?");
      return;
    }
    // first add all items from varargs as list at the end of list
    args.addAll(listFromArgs.get());
    // now delete the list from args, this will shift all added items left, we shouldn't delete
    // first because then we won't be able to get list items unless copied to another list which is
    // not free.
    args.remove(lastIndex);
  }
  
  protected boolean doNotExpandListToArguments() {
    return false;
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
  
  protected List<ZwlValue> tryCastList(@SuppressWarnings("SameParameterValue") int argIndex, ZwlValue val) {
    Optional<List<ZwlValue>> l = val.getListValue();
    if (!l.isPresent()) {
      throw getWrongTypeException(val, Types.LIST, argIndex);
    }
    return l.get();
  }
  
  public Double parseDouble(int argIndex, ZwlValue val) {
    return ParseUtil.parseDouble(val, () -> getWrongTypeException(val, Types.NUMBER, argIndex));
  }
  
  @SuppressWarnings("SameParameterValue")
  protected Boolean parseBoolean(int argIndex, ZwlValue val) {
    return ParseUtil.parseBoolean(val, () -> getWrongTypeException(val, Types.BOOLEAN, argIndex));
  }
  
  protected <T extends Enum<T>> T parseEnum(int argIndex, ZwlValue val, Class<T> enumType) {
    try {
      return Enum.valueOf(enumType, tryCastString(argIndex, val));
    } catch (IllegalArgumentException ia) {
      throw getWrongTypeException(val, enumType.getSimpleName(), argIndex);
    }
  }
  
  public String tryCastString(int argIndex, ZwlValue val) {
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
  protected InvalidTypeException getWrongTypeException(ZwlValue val, String type, int argIndex) {
    return new InvalidTypeException(fromPos.get(), toPos.get(),
        String.format("Function %s, value: %s at argument: %s, isn't of type '%s'%s",
            getName(), val, argIndex, type, lineNColumn.get()));
  }
  
  protected IndexOutOfRangeException getOutOfRange(int index, List<ZwlValue> list) {
    return new IndexOutOfRangeException(fromPos.get(), toPos.get(),
        String.format("The specified index isn't within the range of this List. Index given: %s," +
            " List size: %s%s", index, list.size(), lineNColumn.get()));
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
      throw new InvalidRegexPatternException(fromPos.get(), toPos.get(),
          withLineNCol(pse.getMessage()));
    }
    return pattern;
  }
  
  protected String withLineNCol(String s) {
    return s + lineNColumn.get();
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !getClass().getSimpleName().equals(o.getClass().getSimpleName())) return false;
    AbstractFunction that = (AbstractFunction) o;
    return getName().equals(that.getName());
    // if two function's have same name, they are considered equal irrespective to the number of
    // parameters.
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(getName(), minParamsCount(), maxParamsCount());
  }
}
