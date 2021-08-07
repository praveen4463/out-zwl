package com.zylitics.zwl.webdriver.functions.elements.state;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class GetElementAttributeOrCssValue extends AbstractElementNamedProperty {
  
  public GetElementAttributeOrCssValue(APICoreProperties.Webdriver wdProps,
                                       BuildCapability buildCapability,
                                       RemoteWebDriver driver,
                                       PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "getElementAttributeOrCssValue";
  }
  
  @Override
  protected String get(ZwlValue elementId, String propertyName) {
    String value = doSafeInteraction(elementId, el -> {
      return el.getAttribute(propertyName);
    });
    if (Strings.isNullOrEmpty(value)) {
      value = doSafeInteraction(elementId, el -> {
        return el.getCssValue(propertyName);
      });
    }
    return value;
  }
}