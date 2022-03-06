package com.zylitics.zwl.function.datetime;

import java.time.ZonedDateTime;

public class DateIsAfter extends AbstractDateCompare {
  
  @Override
  public String getName() {
    return "dateIsAfter";
  }
  
  @Override
  protected boolean compare(ZonedDateTime dateTime1, ZonedDateTime dateTime2) {
    return dateTime1.isAfter(dateTime2);
  }
}
