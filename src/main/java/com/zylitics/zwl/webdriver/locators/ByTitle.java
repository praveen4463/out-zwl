package com.zylitics.zwl.webdriver.locators;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ByTitle extends AbstractByUsingJs implements Serializable {
  
  private static final long serialVersionUID = 1333358463696707572L;
  private final String title;
  
  public ByTitle(String title) {
    if (title == null) {
      throw new IllegalArgumentException("Cannot find elements when title is null");
    }
    
    this.title = title;
  }
  
  @Override
  protected String noSuchElementEx() {
    return String.format("Cannot locate an element with title=%s.", title);
  }
  
  @Override
  protected String stringToSearch() {
    return title;
  }
  
  @Override
  protected String funcName() {
    return "findAllByTitle";
  }
  
  @Override
  protected String fileName() {
    return "findElementsByTitle.min.js";
  }
  
  @Override
  public String toString() {
    return "By.title: " + title;
  }
  
  @SuppressWarnings("unused")
  private Map<String, Object> toJson() {
    Map<String, Object> asJson = new HashMap<>();
    asJson.put("using", "title");
    asJson.put("value", title);
    return Collections.unmodifiableMap(asJson);
  }
}