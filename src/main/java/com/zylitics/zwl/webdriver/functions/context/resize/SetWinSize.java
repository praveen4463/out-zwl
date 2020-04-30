package com.zylitics.zwl.webdriver.functions.context.resize;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class SetWinSize extends SetWinRect {
  
  public SetWinSize(APICoreProperties.Webdriver wdProps,
                    BuildCapability buildCapability,
                    RemoteWebDriver driver,
                    PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "setWinSize";
  }
  
  @Override
  protected void set(int a, int b) {
    window.setSize(new Dimension(a, b));
  }
}
