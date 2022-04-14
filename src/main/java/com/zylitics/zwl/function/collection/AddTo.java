package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class AddTo extends AbstractFunction {
  
  @Override
  public String getName() {
    return "addTo";
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
    ZwlValue element = args.get(1);
    list.add(element);
    return _void;
  }
  
  @Override
  protected boolean doNotExpandListToArguments() {
    return true;
  }
}
