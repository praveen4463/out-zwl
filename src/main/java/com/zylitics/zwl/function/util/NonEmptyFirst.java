package com.zylitics.zwl.function.util;

import com.zylitics.zwl.datatype.NothingZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

/**
 * <p>NonEmptyFirst accepts either a list or any number of arguments of any supported type that
 * can be mixed. It returns the first list element or first argument that exists and is non empty.
 * A Boolean or Number are considered non empty if they exist. A String is considered non empty
 * if after trimming whitespaces its length > 0. A List and Map is considered non empty if their
 * size > 0 which means they have at least one element or key-value pair.</p>
 * <p>If a non empty value is not found, nothing is returned which can be checked using
 * {@link Exists}</p>
 */
public class NonEmptyFirst extends AbstractFunction {
  
  @Override
  public String getName() {
    return "nonEmptyFirst";
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
  
    if (args.size() == 1) {
      return nonEmptyFirst(tryCastList(0, args.get(0)));
    }
  
    return nonEmptyFirst(args);
  }
  
  private ZwlValue nonEmptyFirst(List<ZwlValue> l) {
    return l.stream().filter(z -> !z.getNothingValue().isPresent()
        && (z.getBooleanValue().isPresent() || z.getDoubleValue().isPresent()
        || z.getStringValue().isPresent() ? z.getStringValue().get().trim().length() > 0
            : z.getListValue().get().size() > 0 || z.getMapValue().get().size() > 0))
        .findFirst().orElse(new NothingZwlValue());
  }
}
