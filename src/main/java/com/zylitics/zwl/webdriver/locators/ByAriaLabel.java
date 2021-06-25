package com.zylitics.zwl.webdriver.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ByAriaLabel extends By implements Serializable {
  
  private static final long serialVersionUID = 1333358463696707572L;
  private final String ariaLabel;
  
  public ByAriaLabel(String ariaLabel) {
    if (ariaLabel == null) {
      throw new IllegalArgumentException("Cannot find elements when aria-label is null");
    }
    
    this.ariaLabel = ariaLabel;
  }
  
  @Override
  public WebElement findElement(SearchContext context) {
    return By.cssSelector("*[aria-label='" + ariaLabel + "']").findElement(context);
  }
  
  @Override
  public List<WebElement> findElements(SearchContext context) {
    return By.cssSelector("*[aria-label='" + ariaLabel + "']").findElements(context);
  }
  
  @Override
  public String toString() {
    return "By.aria-label: " + ariaLabel;
  }
  
  @SuppressWarnings("unused")
  private Map<String, Object> toJson() {
    Map<String, Object> asJson = new HashMap<>();
    asJson.put("using", "aria-label");
    asJson.put("value", ariaLabel);
    return Collections.unmodifiableMap(asJson);
  }
}
