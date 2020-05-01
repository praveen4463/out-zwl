package com.zylitics.zwl.webdriver.functions.storage;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class LsSize extends AbstractSize {
  
  public LsSize(APICoreProperties.Webdriver wdProps,
                BuildCapability buildCapability,
                RemoteWebDriver driver,
                PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "lsSize";
  }
  
  @Override
  protected int size() {
    return getLocalStorage().size();
  }
}
