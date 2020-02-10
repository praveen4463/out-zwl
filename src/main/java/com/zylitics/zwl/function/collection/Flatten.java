package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.ListZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * <p>Flatten accepts a list and recursively flattens the contained list elements. The final result
 * is a list that contains non-list elements only.</p>
 * <p>Flatten can also be used to concatenate two or more lists. For example:</p>
 * <p>{@code a = [1, 2, 3]}</p>
 * <p>{@code b = [4, 5, 6]}</p>
 * <p>{@code c = flatten([a, b])}</p>
 * <p>{@code print(c)}</p>
 * <p>{@code [1, 2, 3, 4, 5, 6]}</p>
 */
public class Flatten extends AbstractFunction {
  
  @Override
  public String getName() {
    return "flatten";
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
    
    if (args.size() == 1) {
      return new ListZwlValue(flatten(tryCastList(0, args.get(0))));
    }
    
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private List<ZwlValue> flatten(List<ZwlValue> list) {
    List<ZwlValue> flat = new ArrayList<>();
    
    for (ZwlValue z : list) {
      if (z.getListValue().isPresent()) {
        flat.addAll(flatten(z.getListValue().get()));
        continue;
      }
      flat.add(z);
    }
    return flat;
  }
}
