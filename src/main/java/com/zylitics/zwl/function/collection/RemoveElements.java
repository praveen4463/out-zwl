package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.ListZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;
import com.zylitics.zwl.util.ParseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * RemoveEmpty takes a list and elements to exclude (either in a list or varargs) and returns a new
 * list excluding given elements. Elements are matched based on reference equality.
 * If two args are supplied and second is a list, it is assumed that it contains the elements to
 * remove, so if just one element was supposed to be removed and it's itself a list, it should be
 * supplied as wrapped in an outer list. This function unpacks elements from supplied list and
 * removes those. If some elements don't match, they're just skipped and no error is thrown.
 */
public class RemoveElements extends AbstractFunction {
  
  @Override
  protected boolean doNotExpandListToArguments() {
    return true;
  }
  
  @Override
  public String getName() {
    return "removeElements";
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
    int argsCount = args.size();
  
    if (argsCount < 2) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
  
    List<ZwlValue> toRemove = new ArrayList<>();
    if (argsCount == 2 && args.get(1).getListValue().isPresent()) {
      // elements to remove are packed in a list
      toRemove.addAll(args.get(1).getListValue().get());
    } else {
      toRemove.addAll(args.subList(1, argsCount));
    }
    return new ListZwlValue(removeElements(tryCastList(0, args.get(0)), toRemove));
  }
  
  private List<ZwlValue> removeElements(List<ZwlValue> targetList, List<ZwlValue> toRemove) {
    // don't use removeAll as it mutates the original list
    return targetList.stream().filter(z -> !toRemove.contains(z)).collect(Collectors.toList());
  }
}
