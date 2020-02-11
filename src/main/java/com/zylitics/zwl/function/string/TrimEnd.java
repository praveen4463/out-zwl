package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class TrimEnd extends AbstractFunction {
  
  @Override
  public String getName() {
    return "trimEnd";
  }
  
  @Override
  public int minParamsCount() {
    return 2;
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
    
    if (argsCount == 2) {
      return new StringZwlValue(trimEnd(tryCastString(0, args.get(0)),
          tryCastString(1, args.get(1))));
    }
    
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private String trimEnd(String s, String stringToTrim) {
    if (s.endsWith(stringToTrim)) {
      s = s.substring(0, s.lastIndexOf(stringToTrim));
    }
    return s;
  }
}
