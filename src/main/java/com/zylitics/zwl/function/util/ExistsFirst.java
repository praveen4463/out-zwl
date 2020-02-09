package com.zylitics.zwl.function.util;

import com.zylitics.zwl.datatype.NothingZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

/**
 * <p>Return the first argument that exists. It either accepts a list or any number of arguments.
 * </p><p>When a list is given, it's first element that is existing will be returned and when
 * multiple arguments are given, the first argument that is existing is returned.</p>
 * <p>If an existing value is not found, nothing is returned which can be checked using
 * {@link Exists}</p>
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
