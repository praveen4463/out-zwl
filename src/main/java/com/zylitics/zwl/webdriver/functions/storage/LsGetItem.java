package com.zylitics.zwl.webdriver.functions.storage;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class LsGetItem extends AbstractGetItem {
  
  public LsGetItem(APICoreProperties.Webdriver wdProps,
                   BuildCapability buildCapability,
                   RemoteWebDriver driver,
                   PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "lsGetItem";
  }
  
  @Override
  protected String get(String key) {
    return getLocalStorage().getItem(key);
  }
}
