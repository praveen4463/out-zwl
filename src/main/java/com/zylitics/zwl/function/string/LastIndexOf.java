package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class LastIndexOf extends AbstractFunction {
  
  @Override
  public String getName() {
    return "lastIndexOf";
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
      return new DoubleZwlValue(lastIndexOf(tryCastString(0, args.get(0)),
          tryCastString(1, args.get(1))));
    }
    
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private int lastIndexOf(String s, String substring) {
    return s.lastIndexOf(substring);
  }
}
