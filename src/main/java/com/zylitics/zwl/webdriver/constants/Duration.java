package com.zylitics.zwl.webdriver.constants;

import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.model.DurationType;

import java.util.Map;

public class Duration {
  
  public static Map<String, ZwlValue> asMap() {
    return ImmutableMap.<String, ZwlValue>builder()
        .put("seconds", new StringZwlValue(DurationType.SECONDS.name()))
        .put("minutes", new StringZwlValue(DurationType.MINUTES.name()))
        .put("hours", new StringZwlValue(DurationType.HOURS.name()))
        .put("days", new StringZwlValue(DurationType.DAYS.name()))
        .put("weeks", new StringZwlValue(DurationType.WEEKS.name()))
        .put("months", new StringZwlValue(DurationType.MONTHS.name()))
        .put("years", new StringZwlValue(DurationType.YEARS.name()))
        .build();
  }
}
