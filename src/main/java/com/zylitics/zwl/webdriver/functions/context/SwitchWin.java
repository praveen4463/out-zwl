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

public class SwitchWin extends AbstractWebdriverFunction {
  
  public SwitchWin(APICoreProperties.Webdriver wdProps,
                   BuildCapability buildCapability,
                   RemoteWebDriver driver,
                   PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "switchWin";
  }
  
  @Override
  public int minParamsCount() {
    return 0;
  }
  
  @Override
  public int maxParamsCount() {
    return 1;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    int argsCount = args.size();
    if (argsCount > 1) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    
    return handleWDExceptions(() -> {
      if (argsCount == 1) {
        targetLocator.window(tryCastString(0, args.get(0)));
      } else {
        targetLocator.defaultContent();
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
