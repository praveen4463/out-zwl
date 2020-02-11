package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class Size extends AbstractFunction {
  
  @Override
  public String getName() {
    return "size";
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
  
    if (args.size() != 1) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
  
    ZwlValue val = args.get(0);
    if (!(val.getMapValue().isPresent() || val.getListValue().isPresent())) {
      throw new EvalException(withLineNCol(getName() + " works for only Map and List types."));
    }
    return new DoubleZwlValue(val.getListValue().isPresent()
        ? val.getListValue().get().size()
        : val.getMapValue().get().size());
  }
}
