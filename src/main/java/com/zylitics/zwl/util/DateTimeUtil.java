package com.zylitics.zwl.util;

import com.zylitics.zwl.exception.UnknownDateTimeException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

public class DateTimeUtil {
  
  public static ZonedDateTime tryParseDate(String date,
                                           Function<String, UnknownDateTimeException> exFunc) {
    ZonedDateTime dateTime;
    try {
      dateTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    } catch (DateTimeParseException d) {
      throw exFunc.apply(d.getMessage());
    }
    return dateTime;
  }
}
