package com.zylitics.zwl.webdriver.functions.select;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.PrintStream;

public class DeselectByValue extends AbstractSelectDeselectBy {
  
  public DeselectByValue(APICoreProperties.Webdriver wdProps,
                         BuildCapability buildCapability,
                         RemoteWebDriver driver,
                         PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "deselectByValue";
  }
  
  @Override
  void selectDeselect(Select select, ZwlValue value) {
    select.deselectByValue(value.toString());
  }
}
