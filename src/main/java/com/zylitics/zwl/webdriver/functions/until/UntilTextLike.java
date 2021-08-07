package com.zylitics.zwl.webdriver.functions.until;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;

public class UntilTextLike extends AbstractTextValue {
  
  public UntilTextLike(APICoreProperties.Webdriver wdProps,
                       BuildCapability buildCapability,
                       RemoteWebDriver driver,
                       PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "untilTextLike";
  }
  
  @Override
  boolean desiredState(ZwlValue elementId, String textOrValue) {
    String value = doSafeInteraction(elementId, RemoteWebElement::getText);
    return !Strings.isNullOrEmpty(value) && getPattern(textOrValue).matcher(value).find();
  }
}