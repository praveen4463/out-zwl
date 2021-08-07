package com.zylitics.zwl.webdriver.locators;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ByAltText extends AbstractByUsingJs implements Serializable {
  
  private static final long serialVersionUID = 1333358463696707572L;
  private final String alt;
  
  public ByAltText(String alt) {
    if (alt == null) {
      throw new IllegalArgumentException("Cannot find elements when alt is null");
    }
    
    this.alt = alt;
  }
  
  @Override
  protected String noSuchElementEx() {
    return String.format("Cannot locate an element with alt=%s.", alt);
  }
  
  @Override
  protected String stringToSearch() {
    return alt;
  }
  
  @Override
  protected String funcName() {
    return "findAllByAltText";
  }
  
  @Override
  protected String fileName() {
    return "findElementsByAltText.min.js";
  }
  
  @Override
  public String toString() {
    return "By.alt: " + alt;
  }
  
  @SuppressWarnings("unused")
  private Map<String, Object> toJson() {
    Map<String, Object> asJson = new HashMap<>();
    asJson.put("using", "alt");
    asJson.put("value", alt);
    return Collections.unmodifiableMap(asJson);
  }
}