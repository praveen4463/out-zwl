package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class Length extends AbstractFunction {
  
  @Override
  protected boolean doNotExpandListToArguments() {
    return true;
  }
  
  @Override
  public String getName() {
    return "length";
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
  
    if (argsCount == 0) {
      throw unexpectedEndOfFunctionOverload(0);
    }
  
    ZwlValue val = args.get(0);
    if (!(val.getMapValue().isPresent() || val.getListValue().isPresent() ||
        val.getStringValue().isPresent())) {
      throw new EvalException(fromPos.get(), toPos.get(), withLineNCol(getName() +
          " works for only Map, List and String types."));
    }
    int length;
    if (val.getListValue().isPresent()) {
      length = val.getListValue().get().size();
    } else if (val.getMapValue().isPresent()) {
      length = val.getMapValue().get().size();
    } else {
      length = val.getStringValue().get().length();
    }
    return new DoubleZwlValue(length);
  }
}
