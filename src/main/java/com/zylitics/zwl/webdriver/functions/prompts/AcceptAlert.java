package com.zylitics.zwl.webdriver.functions.prompts;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class AcceptAlert extends DismissAccept {
  
  public AcceptAlert(APICoreProperties.Webdriver wdProps,
                     BuildCapability buildCapability,
                     RemoteWebDriver driver,
                     PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "acceptAlert";
  }
  
  @Override
  protected void alertOperation() {
    targetLocator.alert().accept();
  }
}
