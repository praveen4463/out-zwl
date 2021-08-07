package com.zylitics.zwl.webdriver.locators;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ByAriaLabel extends AbstractByUsingJs implements Serializable {
  
  private static final long serialVersionUID = 1333358463696707572L;
  private final String ariaLabel;
  
  public ByAriaLabel(String ariaLabel) {
    if (ariaLabel == null) {
      throw new IllegalArgumentException("Cannot find elements when aria-label is null");
    }
    
    this.ariaLabel = ariaLabel;
  }
  
  @Override
  protected String noSuchElementEx() {
    return String.format("Cannot locate an element with aria-label=%s.", ariaLabel);
  }
  
  @Override
  protected String stringToSearch() {
    return ariaLabel;
  }
  
  @Override
  protected String funcName() {
    return "findAllByAriaLabel";
  }
  
  @Override
  protected String fileName() {
    return "findElementsByAriaLabel.min.js";
  }
  
  @Override
  public String toString() {
    return "By.ariaLabel: " + ariaLabel;
  }
  
  @SuppressWarnings("unused")
  private Map<String, Object> toJson() {
    Map<String, Object> asJson = new HashMap<>();
    asJson.put("using", "aria-label");
    asJson.put("value", ariaLabel);
    return Collections.unmodifiableMap(asJson);
  }
}
