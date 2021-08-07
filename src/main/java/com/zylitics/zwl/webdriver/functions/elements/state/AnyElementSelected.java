package com.zylitics.zwl.webdriver.functions.elements.state;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;
import java.util.List;

public class AnyElementSelected extends AbstractMultiElementState {
  
  public AnyElementSelected(APICoreProperties.Webdriver wdProps,
                            BuildCapability buildCapability,
                            RemoteWebDriver driver,
                            PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "anyElementSelected";
  }
  
  @Override
  protected boolean stateCheck(List<ZwlValue> elementIds) {
    return elementIds.stream()
        .anyMatch(eId -> doSafeInteraction(eId, RemoteWebElement::isSelected));
  }
}
