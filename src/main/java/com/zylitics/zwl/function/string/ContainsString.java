package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class ContainsString extends AbstractFunction {
  
  @Override
  public String getName() {
    return "containsString";
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
    assertArgs(args);
    int argsCount = args.size();
    
    if (argsCount == 2) {
      return new BooleanZwlValue(containsString(tryCastString(0, args.get(0)),
          tryCastString(1, args.get(1))));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private boolean containsString(String s, String substring) {
    return s.contains(substring);
  }
}
