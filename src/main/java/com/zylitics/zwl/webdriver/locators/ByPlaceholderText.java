package com.zylitics.zwl.webdriver.locators;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ByPlaceholderText extends AbstractByUsingJs implements Serializable {
  
  private static final long serialVersionUID = 1333358463696707572L;
  private final String placeholderText;
  
  public ByPlaceholderText(String placeholderText) {
    if (placeholderText == null) {
      throw new IllegalArgumentException("Cannot find elements when placeholderText is null");
    }
    
    this.placeholderText = placeholderText;
  }
  
  @Override
  protected String noSuchElementEx() {
    return String.format("Cannot locate an element with placeholder=%s.", placeholderText);
  }
  
  @Override
  protected String stringToSearch() {
    return placeholderText;
  }
  
  @Override
  protected String funcName() {
    return "findAllByPlaceholderText";
  }
  
  @Override
  protected String fileName() {
    return "findElementsByPlaceholderText.min.js";
  }
  
  @Override
  public String toString() {
    return "By.placeholderText: " + placeholderText;
  }
  
  @SuppressWarnings("unused")
  private Map<String, Object> toJson() {
    Map<String, Object> asJson = new HashMap<>();
    asJson.put("using", "placeholderText");
    asJson.put("value", placeholderText);
    return Collections.unmodifiableMap(asJson);
  }
}
