package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;

public class UntilDisabled extends AbstractVisibleEnable {
  
  public UntilDisabled(APICoreProperties.Webdriver wdProps,
                       BuildCapability buildCapability,
                       RemoteWebDriver driver,
                       PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "untilDisabled";
  }
  
  @Override
  boolean desiredState(RemoteWebElement element) {
    return !element.isEnabled();
  }
}
