package com.zylitics.zwl.webdriver.functions.context.resize;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class MaximizeWin extends MaximizeFullScreen {
  
  public MaximizeWin(APICoreProperties.Webdriver wdProps,
                     BuildCapability buildCapability,
                     RemoteWebDriver driver,
                     PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "maximizeWin";
  }
  
  @Override
  protected void operation() {
    window.maximize();
  }
}
