package com.zylitics.zwl.function.numeric;

import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Min extends AbstractFunction {
  
  @Override
  public String getName() {
    return "min";
  }
  
  @Override
  public int minParamsCount() {
    return 2;
  }
  
  @Override
  public int maxParamsCount() {
    return Integer.MAX_VALUE;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    Optional<Double> min = IntStream.range(0, args.size())
        .mapToObj(i -> parseDouble(i, args.get(i))).min(Double::compareTo);
    if (min.isPresent()) {
      return new DoubleZwlValue(min.get());
    }
    
    throw new RuntimeException("Some problem while getting min out of list of numbers");
  }
}
