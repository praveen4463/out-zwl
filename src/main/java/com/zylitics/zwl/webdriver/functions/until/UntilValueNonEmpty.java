package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;

public class UntilValueNonEmpty extends AbstractTextValueNonEmpty {
  
  public UntilValueNonEmpty(APICoreProperties.Webdriver wdProps,
                            BuildCapability buildCapability,
                            RemoteWebDriver driver,
                            PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "untilValueNonEmpty";
  }
  
  String textOrValue(ZwlValue elementId) {
    return doSafeInteraction(elementId, el -> {
      return el.getAttribute("value");
    });
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.ELEMENT_VALUE.getDefValue();
  }
}