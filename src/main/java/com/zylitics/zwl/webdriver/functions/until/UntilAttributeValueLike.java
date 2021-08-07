package com.zylitics.zwl.webdriver.functions.until;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.PrintStream;

public class UntilAttributeValueLike extends AbstractAttribute {
  
  public UntilAttributeValueLike(APICoreProperties.Webdriver wdProps,
                                 BuildCapability buildCapability,
                                 RemoteWebDriver driver,
                                 PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "untilAttributeValueLike";
  }
  
  @Override
  boolean desiredState(ZwlValue elementId, String attribute, String value) {
    String attributeValue = doSafeInteraction(elementId, el -> {
      return el.getAttribute(attribute);
    });
    if (Strings.isNullOrEmpty(attributeValue)) {
      attributeValue = doSafeInteraction(elementId, el -> {
        return el.getCssValue(attribute);
      });
    }
    return !Strings.isNullOrEmpty(attributeValue)
        && getPattern(value).matcher(attributeValue).find();
  }
}