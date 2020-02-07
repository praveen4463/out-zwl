package com.zylitics.zwl.function.util;

import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;
import com.zylitics.zwl.datatype.NothingZwlValue;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Exists takes an expression and expects it to be one of {@link ZwlValue} type. It returns a
 * {@code true} only if the type is not {@link NothingZwlValue}, otherwise a {@code false} is
 * returned because {@link NothingZwlValue} points to non-existent value. The expression can't be
 * {@code null}.
 */
public class Exists extends AbstractFunction {
  
  @Override
  public String getName() {
    return "exists";
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
    assertArgs(args);
    int argsCount = args.size();
    
    if (argsCount == 1) {
      return new BooleanZwlValue(exists(args.get(0)));
    }
    
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private boolean exists(ZwlValue val) {
    Objects.requireNonNull(val, "exists check can't be done on null values.");
    
    return !val.getNothingValue().isPresent();
  }
}
