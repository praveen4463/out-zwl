package com.zylitics.zwl.function.datetime;

import java.time.ZonedDateTime;

public class DateIsBefore extends AbstractDateCompare {
  
  @Override
  public String getName() {
    return "dateIsBefore";
  }
  
  @Override
  protected boolean compare(ZonedDateTime dateTime1, ZonedDateTime dateTime2) {
    return dateTime1.isBefore(dateTime2);
  }
}
