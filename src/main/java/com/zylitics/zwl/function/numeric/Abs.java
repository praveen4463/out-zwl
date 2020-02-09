package com.zylitics.zwl.function.numeric;

import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class Abs extends AbstractFunction {
  
  @Override
  public String getName() {
    return "abs";
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
    assertArgs(args);
    int argsCount = args.size();
    
    if (argsCount == 1) {
      return new DoubleZwlValue(abs(tryCastDouble(0, args.get(0))));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private double abs(double d) {
    return Math.abs(d);
  }
}
