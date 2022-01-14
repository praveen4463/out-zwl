package com.zylitics.zwl.webdriver.constants;

import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.TimeoutType;

import java.util.Map;

public class Breakpoints {
  
  public static Map<String, ZwlValue> asMap() {
    return ImmutableMap.<String, ZwlValue>builder()
        .put("xs", new DoubleZwlValue(0))
        .put("sm", new DoubleZwlValue(600))
        .put("md", new DoubleZwlValue(960))
        .put("lg", new DoubleZwlValue(1280))
        .put("xl", new DoubleZwlValue(1920))
        .build();
  }
}
