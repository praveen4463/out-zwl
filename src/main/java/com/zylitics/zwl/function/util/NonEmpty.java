package com.zylitics.zwl.function.util;

import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;
import com.zylitics.zwl.util.ParseUtil;

import java.util.List;
import java.util.function.Supplier;

/**
 * Works for all Zwl types. To know when those types are considered non empty,
 * see {@link com.zylitics.zwl.function.collection.NonEmptyFirst}
 */
public class NonEmpty extends AbstractFunction {
  
  @Override
  public String getName() {
    return "nonEmpty";
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
    
    if (argsCount == 0) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    return new BooleanZwlValue(ParseUtil.isNonEmpty(args.get(0)));
  }
}