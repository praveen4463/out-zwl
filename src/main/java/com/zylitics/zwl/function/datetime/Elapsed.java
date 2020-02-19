package com.zylitics.zwl.function.datetime;

import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * <p>Requires a captured {@link Instant} and optionally a time unit. It returns the time difference
 * between current time and previously captured time in the given unit. When no unit is given,
 * default in milliseconds. Unit options are: </p>
 * <ul>
 *   <li>sec</li>
 *   <li>milli</li>
 *   <li>micro</li>
 * </ul>
 */
public class Elapsed extends AbstractFunction {
  
  @Override
  public String getName() {
    return "elapsed";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
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
    
    if (argsCount >= 1) {
      return new DoubleZwlValue(elapsed(parseDouble(0, args.get(0)).longValue(),
          argsCount == 2 ? args.get(1).toString() : null));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private long elapsed(long start, @Nullable String unit) {
    long elapsed = System.nanoTime() - start;
    if (unit == null) {
      return TimeUnit.NANOSECONDS.toMillis(elapsed);
    }
    switch (unit.toLowerCase()) {
      case "sec":
        return TimeUnit.NANOSECONDS.toSeconds(elapsed);
      case "micro":
        return TimeUnit.NANOSECONDS.toMicros(elapsed);
      default:
        return TimeUnit.NANOSECONDS.toMillis(elapsed);
    }
  }
}
