package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class StartsWith extends AbstractFunction {
  
  @Override
  public String getName() {
    return "startsWith";
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
      return new BooleanZwlValue(startsWith(
          tryCastString(0, args.get(0)), tryCastString(1, args.get(1))));
    }
    
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private boolean startsWith(String s, String startsWith) {
    return s.startsWith(startsWith);
  }
}
