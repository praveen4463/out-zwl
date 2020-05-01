package com.zylitics.zwl.webdriver.functions.storage;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class SsClear extends AbstractClear {
  
  public SsClear(APICoreProperties.Webdriver wdProps,
                 BuildCapability buildCapability,
                 RemoteWebDriver driver,
                 PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "ssClear";
  }
  
  @Override
  protected void clear() {
    getSessionStorage().clear();
  }
}
