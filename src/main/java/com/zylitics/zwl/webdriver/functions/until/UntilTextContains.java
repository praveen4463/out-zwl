package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;

public class UntilTextContains extends AbstractTextValue {
  
  public UntilTextContains(APICoreProperties.Webdriver wdProps,
                           BuildCapability buildCapability,
                           RemoteWebDriver driver,
                           PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "untilTextContains";
  }
  
  @Override
  boolean desiredState(ZwlValue elementId, String textOrValue) {
    String value = doSafeInteraction(elementId, RemoteWebElement::getText);
    return value != null && value.contains(textOrValue);
  }
}