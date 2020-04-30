package com.zylitics.zwl.webdriver.functions.elements.interaction;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;
import java.util.List;

public class ClearAll extends MultiClickClear {
  
  public ClearAll(APICoreProperties.Webdriver wdProps,
                  BuildCapability buildCapability,
                  RemoteWebDriver driver,
                  PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "clearAll";
  }
  
  @Override
  protected void perform(List<RemoteWebElement> elements) {
    elements.forEach(RemoteWebElement::clear);
  }
}
