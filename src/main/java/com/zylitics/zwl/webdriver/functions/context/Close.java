package com.zylitics.zwl.webdriver.functions.context;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class Close extends AbstractWebdriverFunction {
  
  public Close(APICoreProperties.Webdriver wdProps,
               BuildCapability buildCapability,
               RemoteWebDriver driver,
               PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "close";
  }
  
  @Override
  public int minParamsCount() {
    return 0;
  }
  
  @Override
  public int maxParamsCount() {
    return 0;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    return handleWDExceptions(() -> {
      // don't allow to close all the windows as it closes the session.
      if (driver.getWindowHandles().size() > 1) {
        driver.close();
      }
      return _void;
    });
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return _void;
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.VOID;
  }
}
