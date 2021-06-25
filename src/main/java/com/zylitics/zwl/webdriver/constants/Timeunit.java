package com.zylitics.zwl.webdriver.constants;

import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.model.TimeUnitType;

import java.util.Map;

public class Timeunit {
  
  public static Map<String, ZwlValue> asMap() {
    return ImmutableMap.<String, ZwlValue>builder()
        .put("microsecond", new StringZwlValue(TimeUnitType.MICROSECOND.name()))
        .put("millisecond", new StringZwlValue(TimeUnitType.MILLISECOND.name()))
        .put("second", new StringZwlValue(TimeUnitType.SECOND.name()))
        .build();
  }
}
