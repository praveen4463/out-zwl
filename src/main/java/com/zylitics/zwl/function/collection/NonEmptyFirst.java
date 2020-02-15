package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.NothingZwlValue;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;
import com.zylitics.zwl.function.util.Exists;
import com.zylitics.zwl.util.ParseUtil;

import java.util.List;
import java.util.function.Supplier;

/**
 * <p>NonEmptyFirst accepts one or more arguments of any supported type.
 * It returns the first argument that exists and is non empty.
 * A Boolean or Number are considered non empty if they exist. A String is considered non empty
 * if after trimming whitespaces its length > 0. A List and Map is considered non empty if their
 * size > 0 which means they have at least one element or key-value pair.</p>
 * <p>If a non empty value is not found, nothing is returned which can be checked using
 * {@link Exists}</p>
 * <p>A list can also be given as argument which will be expanded internally into arguments.</p>
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
    super.invoke(args, defaultValue, lineNColumn);
  
    if (args.size() == 1) {
      return nonEmptyFirst(tryCastList(0, args.get(0)));
    }
  
    return nonEmptyFirst(args);
  }
  
  private ZwlValue nonEmptyFirst(List<ZwlValue> l) {
    return l.stream().filter(ParseUtil::isNonEmpty).findFirst().orElse(new NothingZwlValue());
  }
}
