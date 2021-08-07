package com.zylitics.zwl.webdriver.locators;

import com.google.common.io.Resources;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractByUsingJs extends By {
  
  protected abstract String noSuchElementEx();
  
  protected abstract String stringToSearch();
  
  protected abstract String funcName();
  
  protected abstract String fileName();
  
  @Override
  public WebElement findElement(SearchContext context) {
    List<WebElement> elements = this.findElements(context);
    if (elements.size() == 0) {
      throw new NoSuchElementException(noSuchElementEx());
    }
    return elements.get(0);
  }
  
  @Override
  public List<WebElement> findElements(SearchContext context) {
    RemoteWebElement element = null;
    RemoteWebDriver driver = null;
    if (context instanceof RemoteWebElement) {
      element = (RemoteWebElement) context;
      driver = (RemoteWebDriver) element.getWrappedDriver();
    }
    if (driver == null && !(context instanceof RemoteWebDriver)) {
      throw new RuntimeException("Expected " + context + " to be either a WebDriver or WebElement");
    }
    if (driver == null) {
      driver = (RemoteWebDriver) context;
    }
    Object o = driver.executeScript(
        String.format("%s\nreturn %s(arguments[0] || document.querySelector('*'), arguments[1]);",
            getAtom(), funcName()),
        element, stringToSearch());
    if (o instanceof List<?>) {
      List<?> lo = (List<?>) o;
      if (lo.stream().allMatch(w -> w instanceof WebElement)) {
        return lo.stream().map(w -> (WebElement)w).collect(Collectors.toList());
      }
    }
    return Collections.emptyList();
  }
  
  private String getAtom() {
    try {
      String scriptName = "/" + fileName();
      URL url = getClass().getResource(scriptName);
      Objects.requireNonNull(url);
      //noinspection UnstableApiUsage
      return Resources.toString(url, StandardCharsets.UTF_8);
    } catch (IOException | NullPointerException e) {
      throw new WebDriverException(e);
    }
  }
}
