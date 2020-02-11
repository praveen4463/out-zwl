package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class Length extends AbstractFunction {
  
  @Override
  public String getName() {
    return "length";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return 1;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
    
    if (argsCount == 1) {
      return new DoubleZwlValue(length(tryCastString(0, args.get(0))));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private int length(String s) {
    return s.length();
  }
}
