package com.zylitics.zwl.function.datetime;

import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.function.AbstractFunction;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Supplier;

/**
 * Gets the current date and time in UTC as per ISO-8601 standard format.
 */
public class Timestamp extends AbstractFunction {
  
  @Override
  public String getName() {
    return "timestamp";
  }
  
  @Override
  public int minParamsCount() {
    return 0;
  }
  
  @Override
  public int maxParamsCount() {
    return 0;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    return new StringZwlValue(ZonedDateTime.now(ZoneId.of("UTC"))
        .format(DateTimeFormatter.ISO_DATE_TIME));
  }
}
