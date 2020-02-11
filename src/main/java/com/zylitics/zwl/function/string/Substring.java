package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.IndexOutOfRangeException;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

/**
 * The behaviour of Substring when start and end indexes are given is as follows:
 * It selects the character at startIndex and selects characters upto (endIndex - 1) means the
 * endIndex is exclusive, i.e total characters in substring will be endIndex - startIndex
 */
public class Substring extends AbstractFunction {
  
  @Override
  public String getName() {
    return "substring";
  }
  
  @Override
  public int minParamsCount() {
    return 2;
  }
  
  @Override
  public int maxParamsCount() {
    return 3;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
    
    String substring;
    switch (argsCount) {
      case 2:
        substring = substring(tryCastString(0, args.get(0)),
            tryCastDouble(1, args.get(1)).intValue());
        break;
      case 3:
        substring = substring(tryCastString(0, args.get(0)),
            tryCastDouble(1, args.get(1)).intValue(),
            tryCastDouble(2, args.get(2)).intValue());
        break;
      default:
        throw unexpectedEndOfFunctionOverload(argsCount);
    }
    
    return new StringZwlValue(substring);
  }
  
  private String substring(String s, int beginIndex) {
    try {
      return s.substring(beginIndex);
    } catch (IndexOutOfBoundsException i) {
      throw new IndexOutOfRangeException(withLineNCol(i.getMessage()), i);
    }
  }
  
  private String substring(String s, int beginIndex, int endIndex) {
    try {
      return s.substring(beginIndex, endIndex);
    } catch (IndexOutOfBoundsException i) {
      throw new IndexOutOfRangeException(withLineNCol(i.getMessage()), i);
    }
  }
}
