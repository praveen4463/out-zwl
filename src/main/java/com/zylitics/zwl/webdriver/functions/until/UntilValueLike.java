package com.zylitics.zwl.webdriver.functions.until;

import com.google.common.base.Strings;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;

public class UntilValueLike extends AbstractTextValue {
  
  public UntilValueLike(APICoreProperties.Webdriver wdProps,
                        BuildCapability buildCapability,
                        RemoteWebDriver driver,
                        PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "untilValueLike";
  }
  
  @Override
  boolean desiredState(RemoteWebElement element, String textOrValue) {
    String value = element.getAttribute("value");
    return !Strings.isNullOrEmpty(value) && getPattern(textOrValue).matcher(value).find();
  }
}