package com.zylitics.zwl.function.util;

import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class RandomFromRange extends AbstractFunction {
  
  @Override
  public String getName() {
    return "randomFromRange";
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
  
    if (argsCount == 0) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    
    int low = parseDouble(0, args.get(0)).intValue();
    int high = parseDouble(1, args.get(1)).intValue();
    if (high < low) {
      throw new EvalException(fromPos.get(), toPos.get(),
          withLineNCol("Given range is invalid, it must be low to high"));
    }
    return new DoubleZwlValue(new Random().nextInt(high - low) + low);
  }
}
