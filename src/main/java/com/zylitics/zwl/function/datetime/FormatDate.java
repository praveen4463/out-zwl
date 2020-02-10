package com.zylitics.zwl.function.datetime;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.function.AbstractFunction;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Supplier;

/**
 * Formats a date given in ISO-8601 format using the given formatter. The format can use the pattern
 * specified in
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns">Patterns</a>
 */
public class FormatDate extends AbstractFunction {
  
  @Override
  public String getName() {
    return "formatDate";
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
  
    if (args.size() == 2) {
      return new StringZwlValue(formatDate(tryCastString(0, args.get(0)),
          tryCastString(1, args.get(1))));
    }
  
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private String formatDate(String date, String format) {
    if (Strings.isNullOrEmpty(date) || Strings.isNullOrEmpty(format)) {
      throw new EvalException(getName() + " requires date and format both should be non empty");
    }
  
    ZonedDateTime dateTime;
    DateTimeFormatter formatter;
    try {
      dateTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
      formatter = DateTimeFormatter.ofPattern(format);
    } catch (DateTimeParseException | IllegalArgumentException ex ) {
      throw new EvalException(ex.getMessage(), ex);
    }
    
    return dateTime.format(formatter);
  }
}
