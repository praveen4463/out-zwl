package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class RemoveAt extends AbstractFunction {
  
  @Override
  public String getName() {
    return "removeAt";
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
    
    if (argsCount != 2) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
  
    List<ZwlValue> list = tryCastList(0, args.get(0));
    int index = parseDouble(1, args.get(1)).intValue();
    try {
      list.remove(index);
    } catch (IndexOutOfBoundsException e) {
      throw getOutOfRange(index, list);
    }
    return _void;
  }
}
