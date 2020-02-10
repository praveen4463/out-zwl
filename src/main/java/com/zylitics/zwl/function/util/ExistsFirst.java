package com.zylitics.zwl.function.util;

import com.zylitics.zwl.datatype.NothingZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

/**
 * <p>ExistsFirst accepts one or more arguments of any supported type.
 * It returns the first argument that exists.
 * <p>If a non empty value is not found, nothing is returned which can be checked using
 * {@link Exists}</p>
 * <p>A list can also be given as argument which will be expanded internally into arguments.</p>
 */
public class ExistsFirst extends AbstractFunction {
  
  @Override
  public String getName() {
    return "existsFirst";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return Integer.MAX_VALUE;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    assertArgs(args);
    int argsCount = args.size();
    
    if (argsCount == 1) {
      return existsFirst(tryCastList(0, args.get(0)));
    }
  
    return existsFirst(args);
  }
  
  private ZwlValue existsFirst(List<ZwlValue> l) {
    return l.stream()
        .filter(z -> !z.getNothingValue().isPresent()).findFirst().orElse(new NothingZwlValue());
  }
}
