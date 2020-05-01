package com.zylitics.zwl.webdriver.functions.storage;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.Set;

public class LsGetKeySet extends AbstractGetKeySet {
  
  public LsGetKeySet(APICoreProperties.Webdriver wdProps,
                     BuildCapability buildCapability,
                     RemoteWebDriver driver,
                     PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "lsGetKeySet";
  }
  
  @Override
  protected Set<String> get() {
    return getLocalStorage().keySet();
  }
}
