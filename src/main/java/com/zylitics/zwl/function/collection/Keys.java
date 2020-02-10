package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.ListZwlValue;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Keys extends AbstractFunction {
  
  @Override
  public String getName() {
    return "keys";
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
    
    if (args.size() == 1) {
      return new ListZwlValue(tryCastMap(0, args.get(0)).keySet().stream().map(StringZwlValue::new)
          .collect(Collectors.toList()));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
}
