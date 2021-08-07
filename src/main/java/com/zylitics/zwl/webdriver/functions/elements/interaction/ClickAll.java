package com.zylitics.zwl.webdriver.functions.elements.interaction;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;
import java.util.List;

public class ClickAll extends MultiClickClear {
  
  public ClickAll(APICoreProperties.Webdriver wdProps,
                  BuildCapability buildCapability,
                  RemoteWebDriver driver,
                  PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "clickAll";
  }
  
  @Override
  protected void perform(List<ZwlValue> elementIds) {
    elementIds.forEach(eId -> waitUntilInteracted(eId, RemoteWebElement::click));
  }
}
