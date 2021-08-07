package com.zylitics.zwl.webdriver.functions.elements.state;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class GetElementValue extends AbstractElementProperty {
  
  public GetElementValue(APICoreProperties.Webdriver wdProps,
                         BuildCapability buildCapability,
                         RemoteWebDriver driver,
                         PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "getElementValue";
  }
  
  @Override
  protected String getProperty(ZwlValue elementId) {
    return doSafeInteraction(elementId, el -> {
      return el.getAttribute("value");
    });
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.ELEMENT_VALUE.getDefValue();
  }
}
