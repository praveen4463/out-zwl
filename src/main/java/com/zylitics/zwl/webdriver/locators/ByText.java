package com.zylitics.zwl.webdriver.locators;

import java.io.Serializable;
import java.util.*;

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
public class ByText extends AbstractByUsingJs implements Serializable {
  
  private static final long serialVersionUID = -2854163217936790921L;
  private final String text;
  
  public ByText(String text) {
    if (text == null) {
      throw new IllegalArgumentException("Cannot find elements when the text is null");
    }
    
    this.text = text;
  }
  
  @Override
  protected String noSuchElementEx() {
    return String.format(
        "Cannot locate an element using text=%s. Make sure the given text is not broken up by" +
            " multiple elements or if you provided a regex, make sure it matches intended text."
        , text);
  }
  
  @Override
  protected String stringToSearch() {
    return text;
  }
  
  @Override
  protected String funcName() {
    return "findAllByText";
  }
  
  @Override
  protected String fileName() {
    return "findElementsByText.min.js";
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
