package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PutIn extends AbstractFunction {
  
  @Override
  public String getName() {
    return "putIn";
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
    
    if (argsCount != 3) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    
    Map<String, ZwlValue> map = tryCastMap(0, args.get(0));
    String key = tryCastString(1, args.get(1));
    map.put(key, args.get(2));
    return _void;
  }
}
