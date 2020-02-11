package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.ListZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Values extends AbstractFunction {
  
  @Override
  public String getName() {
    return "values";
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
    
    if (args.size() == 1) {
      return new ListZwlValue(new ArrayList<>(tryCastMap(0, args.get(0)).values()));
    }
    
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
}
