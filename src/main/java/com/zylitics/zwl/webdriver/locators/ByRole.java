package com.zylitics.zwl.webdriver.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ByRole extends By implements Serializable {
  
  private static final long serialVersionUID = 1333358463696707572L;
  private final String role;
  
  public ByRole(String role) {
    if (role == null) {
      throw new IllegalArgumentException("Cannot find elements when role is null");
    }
    
    this.role = role;
  }
  
  @Override
  public WebElement findElement(SearchContext context) {
    return By.cssSelector("*[role='" + role + "']").findElement(context);
  }
  
  @Override
  public List<WebElement> findElements(SearchContext context) {
    return By.cssSelector("*[role='" + role + "']").findElements(context);
  }
  
  @Override
  public String toString() {
    return "By.role: " + role;
  }
  
  @SuppressWarnings("unused")
  private Map<String, Object> toJson() {
    Map<String, Object> asJson = new HashMap<>();
    asJson.put("using", "role");
    asJson.put("value", role);
    return Collections.unmodifiableMap(asJson);
  }
}
