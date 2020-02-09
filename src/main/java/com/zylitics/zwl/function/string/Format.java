package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.function.Supplier;

/**
 * <p>
 * Format returns a formatted string using the specified format string and arguments. A String can
 * be given with any number of %s verb into it, which will be replaced by the arguments appearing at
 * same position as the %s verb. If any argument is not of type string, it will be converted using
 * the type's string representation. For example, 33 will converted into "33" and true to "true".
 * </p>
 * <p>Example: {@code format("Are you coming to %s's party at %s today?", "John", 9)} will
 * produce string {@code "Are you coming to John's party at 9 today"}</p>
 * <p>If the arguments are more than the number of verbs in string, extra arguments will be
 * ignored. To use literal % inside string, use %% to escape it.</p>
 */
public class Format extends AbstractFunction {
  
  @Override
  public String getName() {
    return "format";
  }
  
  @Override
  public int minParamsCount() {
    return 2;
  }
  
  @Override
  public int maxParamsCount() {
    return Integer.MAX_VALUE;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    assertArgs(args);
  
    String formatString = tryCastString(0, args.get(0));
    Object[] formatArgs = args.stream().skip(1).toArray();
    return new StringZwlValue(format(formatString, formatArgs));
  }
  
  private String format(String s, Object... args) {
    try {
      return String.format(s, args);
    } catch (IllegalFormatException i) {
      throw new EvalException(i.getMessage(), i);
    }
  }
}
