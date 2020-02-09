package com.zylitics.zwl.function.numeric;

import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Max extends AbstractFunction {
  
  @Override
  public String getName() {
    return "max";
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
    assertArgs(args);
    Optional<Double> max = IntStream.range(0, args.size())
        .mapToObj(i -> tryCastDouble(i, args.get(i))).max(Double::compareTo);
    if (max.isPresent()) {
      return new DoubleZwlValue(max.get());
    }
    
    throw new RuntimeException("Some problem while getting max out of list of numbers");
  }
}
