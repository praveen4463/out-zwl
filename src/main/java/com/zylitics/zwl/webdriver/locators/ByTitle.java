package com.zylitics.zwl.webdriver.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ByTitle extends By implements Serializable {
  
  private static final long serialVersionUID = 1333358463696707572L;
  private final String title;
  
  public ByTitle(String title) {
    if (title == null) {
      throw new IllegalArgumentException("Cannot find elements when title is null");
    }
    
    this.title = title;
  }
  
  @Override
  public WebElement findElement(SearchContext context) {
    return By.cssSelector("*[title='" + title + "']").findElement(context);
  }
  
  @Override
  public List<WebElement> findElements(SearchContext context) {
    return By.cssSelector("*[title='" + title + "']").findElements(context);
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