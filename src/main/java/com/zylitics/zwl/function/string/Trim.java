package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

/**
 * Trim takes either one or two arguments as {@link String}. If one argument is given, the leading
 * and trailing whitespace of given {@code string} are removed. When two arguments are given,
 * the second {@code string} is removed from the beginning and end of the first {@code string}.
 */
public class Trim extends AbstractFunction {
  
  @Override
  public String getName() {
    return "trim";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return 2;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
    
    String s;
    switch (argsCount) {
      case 1:
        s = trim(tryCastString(0, args.get(0)));
        break;
      case 2:
        s = trim(tryCastString(0, args.get(0)), tryCastString(1, args.get(1)));
        break;
      default:
        throw unexpectedEndOfFunctionOverload(argsCount);
    }
    
    return new StringZwlValue(s);
  }
  
  private String trim(String s) {
    return s.trim();
  }
  
  private String trim(String s, String stringToTrim) {
    int trimLength = stringToTrim.length();
    if (s.startsWith(stringToTrim)) {
      s = s.substring(trimLength);
    }
    if (s.endsWith(stringToTrim)) {
      s = s.substring(0, s.lastIndexOf(stringToTrim));
    }
    return s;
  }
}
