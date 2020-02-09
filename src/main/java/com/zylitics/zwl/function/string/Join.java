package com.zylitics.zwl.function.string;

import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p>Join takes two arguments, a {@link String} {@code separator} and a {@code list} of type
 * {@link ZwlValue}. The actual type in the list is expected to be {@link StringZwlValue}, for other
 * types, the result of invoking {@link Object#toString()} will be used rather than re-evaluating
 * any contained {@link List} or {@link Map}.</p>
 * <p>It then concatenates all elements in the list separated by the given {@code separator}.</p>
 */
public class Join extends AbstractFunction {
  
  @Override
  public String getName() {
    return "join";
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
      List<ZwlValue> strings = tryCastList(1, args.get(1));
      return new StringZwlValue(join(separator,
          strings.stream().map(Objects::toString).collect(Collectors.toList())));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private String join(String separator, List<String> strings) {
    return String.join(separator, strings);
  }
}
