package com.zylitics.zwl.webdriver.functions.elements.state;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class GetElementAttribute extends AbstractElementNamedProperty {
  
  public GetElementAttribute(APICoreProperties.Webdriver wdProps,
                             BuildCapability buildCapability,
                             RemoteWebDriver driver,
                             PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "getElementAttribute";
  }
  
  @Override
  protected String get(ZwlValue elementId, String propertyName) {
    return doSafeInteraction(elementId, el -> {
      return el.getAttribute(propertyName);
    });
  }
}
