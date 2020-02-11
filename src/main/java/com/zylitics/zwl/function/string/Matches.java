package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

public class Matches extends AbstractFunction {
  
  @Override
  public String getName() {
    return "matches";
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
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
  
    if (argsCount == 2) {
      return new BooleanZwlValue(matches(tryCastString(0, args.get(0)),
          tryCastString(1, args.get(1))));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private boolean matches(String s, String regex) {
    return getPattern(regex).matcher(s).matches();
  }
}
