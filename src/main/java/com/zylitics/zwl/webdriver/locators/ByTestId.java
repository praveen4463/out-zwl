package com.zylitics.zwl.webdriver.locators;

import org.openqa.selenium.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ByTestId extends By implements Serializable {
  
  private static final long serialVersionUID = 2844120101092830634L;
  private final String dataTestId;
  
  public ByTestId(String dataTestId) {
    if (dataTestId == null) {
      throw new IllegalArgumentException("Cannot find elements when data-testid is null");
    }
    
    this.dataTestId = dataTestId;
  }
  
  @Override
  public WebElement findElement(SearchContext context) {
    return By.cssSelector("*[data-testid='" + dataTestId + "']").findElement(context);
  }
  
  @Override
  public List<WebElement> findElements(SearchContext context) {
    return By.cssSelector("*[data-testid='" + dataTestId + "']").findElements(context);
  }
  
  @Override
  public String toString() {
    return "By.data-testid: " + dataTestId;
  }
  
  @SuppressWarnings("unused")
  private Map<String, Object> toJson() {
    Map<String, Object> asJson = new HashMap<>();
    asJson.put("using", "data-testid");
    asJson.put("value", dataTestId);
    return Collections.unmodifiableMap(asJson);
  }
}
