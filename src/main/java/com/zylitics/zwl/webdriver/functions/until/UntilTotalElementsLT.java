package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class UntilTotalElementsLT extends AbstractTotalElements {
  
  public UntilTotalElementsLT(APICoreProperties.Webdriver wdProps,
                              BuildCapability buildCapability,
                              RemoteWebDriver driver,
                              PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "untilTotalElementsLT";
  }
  
  @Override
  boolean desiredState(int totalElementsFound, int givenTotal) {
    return totalElementsFound < givenTotal;
  }
}