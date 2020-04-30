package com.zylitics.zwl.webdriver.functions.context.resize;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class MoveWinBy extends SetWinRect {
  
  public MoveWinBy(APICoreProperties.Webdriver wdProps,
                   BuildCapability buildCapability,
                   RemoteWebDriver driver,
                   PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "moveWinBy";
  }
  
  @Override
  protected void set(int a, int b) {
    Point currentPosition = window.getPosition();
    window.setPosition(new Point(a + currentPosition.x, b + currentPosition.y));
  }
}
