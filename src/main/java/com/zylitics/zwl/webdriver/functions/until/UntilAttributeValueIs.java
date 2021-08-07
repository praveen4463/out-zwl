package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.PrintStream;

public class UntilAttributeValueIs extends AbstractAttribute {
  
  public UntilAttributeValueIs(APICoreProperties.Webdriver wdProps,
                               BuildCapability buildCapability,
                               RemoteWebDriver driver,
                               PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "untilAttributeValueIs";
  }
  
  @Override
  boolean desiredState(ZwlValue elementId, String attribute, String value) {
    Boolean res = doSafeInteraction(elementId, el -> {
      return ExpectedConditions.attributeToBe(el, attribute, value).apply(driver);
    });
    return res != null && res;
  }
}
