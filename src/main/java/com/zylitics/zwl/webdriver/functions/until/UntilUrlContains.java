package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.PrintStream;

public class UntilUrlContains extends AbstractTitleUrl {
  
  public UntilUrlContains(APICoreProperties.Webdriver wdProps,
                          BuildCapability buildCapability,
                          RemoteWebDriver driver,
                          PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "untilUrlContains";
  }
  
  @Override
  ExpectedCondition<Boolean> condition(String s) {
    return ExpectedConditions.urlContains(s);
  }
}
