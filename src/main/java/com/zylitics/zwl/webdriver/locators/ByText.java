package com.zylitics.zwl.webdriver.locators;

import com.google.common.io.Resources;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 Iterates nodes in the DOM tree and finds node(s) that have textContent matching with the provided
 text.
 The given text can be a string or a regex given as string. When the string starts with a '/' and
 ends with a '/' followed by optional regex flags supported in javascript (gimsuy), the string is
 treated as a regex otherwise it is treated as a string. When the given text contains whitespaces,
 they are not trimmed or collapsed (collapsing means turning more than a single white space into
 single within the string) in anyway, users are advised to trim or collapse before passing if needed.
 When a regex is given as text, and matches any sequence in the node's textContent, that node is
 selected to be returned. If you intend to match entire textContent and not partially, use regex's
 ^ and $ assertions (matches beginning and end of input). Users are advised to test their regex
 beforehand using tools like https://regexr.com/ (Javascript engine).
 When an element is given as 'from' element, it become root otherwise it is the top node in the DOM
 tree. This root's descendants are then iterated ignoring nodes 'html, head, style, script, link,
 meta, title'. Each descendant node that has a Text node and textContent is taken their textContent
 is matched with the given text. Note that the textContent is just trimmed without collapsing
 whitespace, so you should build you regex or string same as what you see in the DOM.
 */
// This custom implementation of By works with latest 4.0 updates.
public class ByText extends By implements Serializable {
  
  private static final long serialVersionUID = -2854163217936790921L;
  private final String text;
  
  public ByText(String text) {
    if (text == null) {
      throw new IllegalArgumentException("Cannot find elements when the text is null");
    }
    
    this.text = text;
  }
  
  @Override
  public WebElement findElement(SearchContext context) {
    List<WebElement> elements = this.findElements(context);
    if (elements.size() == 0) {
      throw new NoSuchElementException(String.format(
          "Cannot locate an element using text=%s. Make sure the given text is not broken up by" +
              " multiple elements or if you provided a regex, make sure it matches intended text."
          , text));
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
        getAtom() + "return findAllByText(arguments[0] || document.querySelector('*'), arguments[1]);",
        element, text);
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
      String scriptName = "/findElementsByText.min.js";
      URL url = getClass().getResource(scriptName);
      Objects.requireNonNull(url);
      //noinspection UnstableApiUsage
      return Resources.toString(url, StandardCharsets.UTF_8);
    } catch (IOException | NullPointerException e) {
      throw new WebDriverException(e);
    }
  }
  
  @Override
  public String toString() {
    return "By.text: " + text;
  }
  
  @SuppressWarnings("unused")
  private Map<String, Object> toJson() {
    Map<String, Object> asJson = new HashMap<>();
    asJson.put("using", "text");
    asJson.put("value", text);
    return Collections.unmodifiableMap(asJson);
  }
}
