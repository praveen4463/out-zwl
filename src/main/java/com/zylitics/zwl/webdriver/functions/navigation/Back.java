package com.zylitics.zwl.webdriver.functions.navigation;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class Back extends BackForward {
  
  public Back(APICoreProperties.Webdriver wdProps,
              BuildCapability buildCapability,
              RemoteWebDriver driver,
              PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "back";
  }
  
  @Override
  protected void goInHistory() {
    driver.navigate().back();
  }
}
