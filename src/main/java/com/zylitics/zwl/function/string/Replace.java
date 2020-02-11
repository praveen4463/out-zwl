package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class Replace extends AbstractFunction {
  
  @Override
  public String getName() {
    return "replace";
  }
  
  @Override
  public int minParamsCount() {
    return 3;
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
  
    if (argsCount == 3) {
      return new StringZwlValue(replace(tryCastString(0, args.get(0)),
          tryCastString(1, args.get(1)),
          tryCastString(2, args.get(2))));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  String replace(String s, String substring, String replacement) {
    return s.replace(substring, replacement);
  }
}
