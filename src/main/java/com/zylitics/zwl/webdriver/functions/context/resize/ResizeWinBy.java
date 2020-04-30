package com.zylitics.zwl.webdriver.functions.context.resize;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class ResizeWinBy extends SetWinRect {
  
  public ResizeWinBy(APICoreProperties.Webdriver wdProps,
                     BuildCapability buildCapability,
                     RemoteWebDriver driver,
                     PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "resizeWinBy";
  }
  
  @Override
  protected void set(int a, int b) {
    Dimension currentDimension = window.getSize();
    window.setSize(new Dimension(a + currentDimension.width, b + currentDimension.height));
  }
}
