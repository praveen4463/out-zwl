package com.zylitics.zwl.webdriver.functions.document;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class ExecuteAsyncScript extends AbstractExecuteScript {
  
  public ExecuteAsyncScript(APICoreProperties.Webdriver wdProps,
                            BuildCapability buildCapability,
                            RemoteWebDriver driver,
                            PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "executeAsyncScript";
  }
  
  @Override
  protected Object execute(String script, Object... args) {
    return driver.executeAsyncScript(script, args);
  }
}
