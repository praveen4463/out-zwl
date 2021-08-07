package com.zylitics.zwl.webdriver.locators;

import java.io.Serializable;
import java.util.*;

public class ByLabelText extends AbstractByUsingJs implements Serializable {
  
  private static final long serialVersionUID = -2854163217936790921L;
  private final String labelText;
  
  public ByLabelText(String labelText) {
    if (labelText == null) {
      throw new IllegalArgumentException("Cannot find elements when the label text is null");
    }
    
    this.labelText = labelText;
  }
  
  @Override
  protected String noSuchElementEx() {
    return String.format(
        "Cannot locate an element associated with label having labelText=%s. Either there is no" +
            " such label or it is not associated with an element."
        , labelText);
  }
  
  @Override
  protected String stringToSearch() {
    return labelText;
  }
  
  @Override
  protected String funcName() {
    return "findAllByLabelText";
  }
  
  @Override
  protected String fileName() {
    return "findElementsByLabelText.min.js";
  }
  
  @Override
  public String toString() {
    return "By.labelText: " + labelText;
  }
  
  @SuppressWarnings("unused")
  private Map<String, Object> toJson() {
    Map<String, Object> asJson = new HashMap<>();
    asJson.put("using", "labelText");
    asJson.put("value", labelText);
    return Collections.unmodifiableMap(asJson);
  }
}
