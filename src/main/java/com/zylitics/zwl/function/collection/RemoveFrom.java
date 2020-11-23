package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Takes a list or map and delete given element or entry using given key. If element or key doesn't
 * match up, no error is thrown and false is returned. If successfully removed, true is returned.
 */
public class RemoveFrom extends AbstractFunction {
  
  @Override
  public String getName() {
    return "removeFrom";
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
    
    ZwlValue collection = args.get(0);
    ZwlValue object = args.get(1);
    boolean result;
    if (collection.getListValue().isPresent()) {
      List<ZwlValue> list = collection.getListValue().get();
      result = list.remove(object);
    } else if (collection.getMapValue().isPresent()) {
      Map<String, ZwlValue> map = collection.getMapValue().get();
      result = map.remove(tryCastString(1, object)) != null;
    } else {
      throw getWrongTypeException(collection, String.format("%s or %s", Types.MAP, Types.LIST), 0);
    }
    return new BooleanZwlValue(result);
  }
}
