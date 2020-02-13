package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.function.Supplier;

/**
 * Matches match the given regex pattern against the entire input sequence, it returns true only if
 * the pattern matches entire input and not a part of it. To match a subsequence use {@link Find}.
 */
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
  
  protected boolean matches(String s, String regex) {
    return getPattern(regex).matcher(s).matches();
  }
}
