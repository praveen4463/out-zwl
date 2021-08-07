package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class UntilValueContains extends AbstractTextValue {
  
  public UntilValueContains(APICoreProperties.Webdriver wdProps,
                            BuildCapability buildCapability,
                            RemoteWebDriver driver,
                            PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "untilValueContains";
  }
  
  @Override
  boolean desiredState(ZwlValue elementId, String textOrValue) {
    String value = doSafeInteraction(elementId, el -> {
      return el.getAttribute("value");
    });
    return value != null && value.contains(textOrValue);
  }
}