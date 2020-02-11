package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.ListZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * RemoveEmpty takes a list of elements of any supported type. It removes any element that is
 * non existing or empty from list and returns the modified list.
 * Note that RemoveEmpty is not recursive, so any list or map contained within the list that are
 * non empty won't be further taken for empty removal.
 */
public class RemoveEmpty extends AbstractFunction {
  
  @Override
  public String getName() {
    return "removeEmpty";
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
  
    if (argsCount == 1) {
      return new ListZwlValue(removeEmpty(tryCastList(0, args.get(0))));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private List<ZwlValue> removeEmpty(List<ZwlValue> l) {
    //noinspection OptionalGetWithoutIsPresent
    return l.stream().filter(z -> !z.getNothingValue().isPresent()
        && (z.getBooleanValue().isPresent() || z.getDoubleValue().isPresent()
        || z.getStringValue().isPresent() ? z.getStringValue().get().trim().length() > 0
        : z.getListValue().get().size() > 0 || z.getMapValue().get().size() > 0))
        .collect(Collectors.toList());
  }
}
