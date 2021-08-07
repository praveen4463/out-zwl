package com.zylitics.zwl.webdriver.constants;

import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;

import java.util.Map;

public class By {
  
  public static Map<String, ZwlValue> asMap() {
    return ImmutableMap.<String, ZwlValue>builder()
        .put("altText", new StringZwlValue(ByType.ALT_TEXT.name()))
        .put("ariaLabel", new StringZwlValue(ByType.ARIA_LABEL.name()))
        .put("className", new StringZwlValue(ByType.CLASS_NAME.name()))
        .put("cssSelector", new StringZwlValue(ByType.CSS_SELECTOR.name()))
        .put("id", new StringZwlValue(ByType.ID.name()))
        .put("labelText", new StringZwlValue(ByType.LABEL_TEXT.name()))
        .put("linkText", new StringZwlValue(ByType.LINK_TEXT.name()))
        .put("name", new StringZwlValue(ByType.NAME.name()))
        .put("partialLinkText", new StringZwlValue(ByType.PARTIAL_LINK_TEXT.name()))
        .put("placeholderText", new StringZwlValue(ByType.PLACEHOLDER_TEXT.name()))
        .put("role", new StringZwlValue(ByType.ROLE.name()))
        .put("tagName", new StringZwlValue(ByType.TAG_NAME.name()))
        .put("testId", new StringZwlValue(ByType.TEST_ID.name()))
        .put("text", new StringZwlValue(ByType.TEXT.name()))
        .put("title", new StringZwlValue(ByType.TITLE.name()))
        .put("xPath", new StringZwlValue(ByType.XPATH.name()))
        .build();
  }
}
