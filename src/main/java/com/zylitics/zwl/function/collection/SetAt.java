package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

/**
 * If a non existent index is given, an {@link com.zylitics.zwl.exception.IndexOutOfRangeException}
 * will be thrown.
 */
public class SetAt extends AbstractFunction {
  
  @Override
  public String getName() {
    return "setAt";
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
    
    List<ZwlValue> list = tryCastList(0, args.get(0));
    int index = parseDouble(1, args.get(1)).intValue();
    ZwlValue element = args.get(2);
    try {
      list.set(index, element);
    } catch (IndexOutOfBoundsException e) {
      throw getOutOfRange(index, list);
    }
    return _void;
  }
}
