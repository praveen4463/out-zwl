package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.NothingZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

/**
 * This method is deprecated because it suffers from the same problem for which we moved exists to
 * interpreter level. If user supplied non existent value, it will raise an exception.
 * <p>ExistsFirst accepts one or more arguments of any supported type.
 * It returns the first argument that exists.
 * <p>If a non empty value is not found, nothing is returned which can be checked using
 * exists</p>
 * <p>A list can also be given as argument which will be expanded internally into arguments.</p>
 */
@Deprecated
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
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
    
    if (argsCount >= 1) {
      return existsFirst(args);
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private ZwlValue existsFirst(List<ZwlValue> l) {
    return l.stream()
        .filter(z -> !z.getNothingValue().isPresent()).findFirst().orElse(new NothingZwlValue());
  }
}
