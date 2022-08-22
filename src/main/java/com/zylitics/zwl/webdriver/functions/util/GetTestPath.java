package com.zylitics.zwl.webdriver.functions.util;

import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class GetTestPath extends AbstractWebdriverFunction {
  
  private final String testPath;
  
  public GetTestPath(APICoreProperties.Webdriver wdProps,
                 BuildCapability buildCapability,
                 RemoteWebDriver driver,
                 PrintStream printStream,
                 String testPath) {
    super(wdProps, buildCapability, driver, printStream);
    
    this.testPath = testPath;
  }
  
  @Override
  public String getName() {
    return "getTestPath";
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
    
    return new StringZwlValue(testPath);
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.UNKNOWN.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.STRING;
  }
}
