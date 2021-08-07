package com.zylitics.zwl.webdriver.functions.elements.state;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class IsStale extends AbstractWebdriverFunction {
  
  public IsStale(APICoreProperties.Webdriver wdProps,
                 BuildCapability buildCapability,
                 RemoteWebDriver driver,
                 PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "isStale";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return 1;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    
    if (args.size() == 0) {
      throw unexpectedEndOfFunctionOverload(0);
    }
    return handleWDExceptions(() -> {
      boolean stale = false;
      try {
        RemoteWebElement e = getWebElementUsingElemId(args.get(0));
        if (buildCapability.isDryRunning()) {
          return evaluateDefValue(defaultValue);
        }
        e.isEnabled();
      } catch (StaleElementReferenceException s) {
        stale = true;
      }
      return new BooleanZwlValue(stale);
    });
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.TRUE.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.BOOLEAN;
  }
}
