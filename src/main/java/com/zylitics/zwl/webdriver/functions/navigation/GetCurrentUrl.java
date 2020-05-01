package com.zylitics.zwl.webdriver.functions.navigation;

import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class GetCurrentUrl extends GetFromPage {
  
  public GetCurrentUrl(APICoreProperties.Webdriver wdProps,
                       BuildCapability buildCapability,
                       RemoteWebDriver driver,
                       PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "getCurrentUrl";
  }
  
  @Override
  protected String get() {
    return driver.getCurrentUrl();
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.URL.getDefValue();
  }
}
