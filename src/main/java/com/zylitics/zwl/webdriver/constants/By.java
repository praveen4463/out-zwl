package com.zylitics.zwl.webdriver.constants;

import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;

import java.util.Map;

public class By {
  
  public static Map<String, ZwlValue> asMap() {
    return ImmutableMap.<String, ZwlValue>builder()
        .put("id", new StringZwlValue(ByType.ID.name()))
        .put("linkText", new StringZwlValue(ByType.LINK_TEXT.name()))
        .put("partialLinkText", new StringZwlValue(ByType.PARTIAL_LINK_TEXT.name()))
        .put("name", new StringZwlValue(ByType.NAME.name()))
        .put("tagName", new StringZwlValue(ByType.TAG_NAME.name()))
        .put("xPath", new StringZwlValue(ByType.XPATH.name()))
        .put("className", new StringZwlValue(ByType.CLASS_NAME.name()))
        .put("cssSelector", new StringZwlValue(ByType.CSS_SELECTOR.name()))
        .put("text", new StringZwlValue(ByType.TEXT.name()))
        .put("testId", new StringZwlValue(ByType.TEST_ID.name()))
        .put("role", new StringZwlValue(ByType.ROLE.name()))
        .put("title", new StringZwlValue(ByType.TITLE.name()))
        .build();
  }
}
