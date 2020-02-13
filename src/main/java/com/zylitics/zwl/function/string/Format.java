package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.IllegalStringFormatException;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.function.Supplier;

/**
 * <p>
 * Format returns a formatted string using the specified format string and arguments. The
 * specification is same as defined in
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html#detail">Formatter</a>
 * Try giving format options to some limit as specified in
 * <a href="https://www.terraform.io/docs/configuration/functions/format.html">format</a></p>
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
    super.invoke(args, defaultValue, lineNColumn);
  
    String formatString = tryCastString(0, args.get(0));
    Object[] formatArgs = args.stream().skip(1).toArray();
    return new StringZwlValue(format(formatString, formatArgs));
  }
  
  protected String format(String s, Object... args) {
    try {
      return String.format(s, args);
    } catch (IllegalFormatException i) {
      throw new IllegalStringFormatException(withLineNCol(i.getMessage()), i);
    }
  }
}
