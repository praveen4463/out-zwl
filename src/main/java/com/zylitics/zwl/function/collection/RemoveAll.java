package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RemoveAll extends AbstractFunction {
  
  @Override
  protected boolean doNotExpandListToArguments() {
    return true;
  }
  
  @Override
  public String getName() {
    return "removeAll";
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
    
    if (argsCount != 1) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    
    ZwlValue collection = args.get(0);
    if (collection.getListValue().isPresent()) {
      List<ZwlValue> list = collection.getListValue().get();
      list.clear();
    } else if (collection.getMapValue().isPresent()) {
      Map<String, ZwlValue> map = collection.getMapValue().get();
      map.clear();
    } else {
      throw getWrongTypeException(collection, String.format("%s or %s", Types.MAP, Types.LIST), 0);
    }
    return _void;
  }
}
