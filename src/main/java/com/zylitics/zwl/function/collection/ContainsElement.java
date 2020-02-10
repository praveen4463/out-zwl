package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class ContainsElement extends AbstractFunction {
  
  @Override
  public String getName() {
    return "containsElement";
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
  
    if (args.size() == 2) {
      return new BooleanZwlValue(tryCastList(0, args.get(0)).contains(args.get(1)));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
}
