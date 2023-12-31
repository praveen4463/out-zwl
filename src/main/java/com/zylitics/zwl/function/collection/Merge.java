package com.zylitics.zwl.function.collection;

import com.zylitics.zwl.datatype.MapZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.InsufficientArgumentsException;
import com.zylitics.zwl.exception.InvalidTypeException;
import com.zylitics.zwl.function.AbstractFunction;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>Merge accepts two or more arguments of type Map and returns a new map containing
 * key-value pairs from all the maps. If any key appears more than once, it will be associated to
 * the value coming at last. This function is not recursive, means maps inside maps aren't merged
 * but treated same as any other value.</p>
 * <p>A list can also be given as argument containing two or more elements of type Map which will be
 * expanded internally into arguments.</p>
 */
public class Merge extends AbstractFunction {
  
  @Override
  public String getName() {
    return "merge";
  }
  
  // min param count is 1 cause user can also send just one list containing several maps but
  // we should check after the list expansion that args count is no less than 2, as merge requires
  // at least two things.
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
    
    if (args.size() >= 2) {
      return new MapZwlValue(merge(args));
    }
  
    throw new InsufficientArgumentsException(fromPos.get(), toPos.get(),
        withLineNCol(getName() + " requires at least two values"));
  }
  
  private Map<String, ZwlValue> merge(List<ZwlValue> listOfMaps) {
    return IntStream.range(0, listOfMaps.size())
        .mapToObj(i -> tryCastMap(i, listOfMaps.get(i)))
        .flatMap(m -> m.entrySet().stream()).collect(
            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b));
  }
}
