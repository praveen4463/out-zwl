package com.zylitics.zwl.function.string;

import com.google.common.base.Splitter;
import com.zylitics.zwl.datatype.ListZwlValue;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Split requires two arguments, a separator and value need to be split, both should be of type
 * {@link String}. After applying standard {@code split} method, the {@code list} of {@code String}
 * is returned. If
 */
public class Split extends AbstractFunction {
  
  @Override
  public String getName() {
    return "split";
  }
  
  @Override
  public int minParamsCount() {
    return 2;
  }
  
  @Override
  public int maxParamsCount() {
    return 2;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    assertArgs(args);
    int argsCount = args.size();
  
    if (argsCount == 2) {
      String separator = tryCastString(0, args.get(0));
      String val = tryCastString(1, args.get(1));
      
      List<String> list = split(separator, val);
      // List could be empty if the value was empty.
      if (list.size() > 0) {
        return getListZwlValue(list);
      }
      // return the original value as list
      return new ListZwlValue(Collections.singletonList(args.get(1)));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private List<String> split(String separator, String val) {
    return Splitter.on(separator).omitEmptyStrings().trimResults().splitToList(val);
  }
}
