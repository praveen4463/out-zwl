package com.zylitics.zwl.webdriver.functions.elements.state;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

abstract class AbstractElementNamedProperty extends AbstractWebdriverFunction {
  
  public AbstractElementNamedProperty(APICoreProperties.Webdriver wdProps,
                                      BuildCapability buildCapability,
                                      RemoteWebDriver driver,
                                      PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public int minParamsCount() {
    return 2;
  }
  
  @Override
  public int maxParamsCount() {
    return 2;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
  
    if (args.size() < 2) {
      throw unexpectedEndOfFunctionOverload(args.size());
    }
    String propertyName = tryCastString(1, args.get(1));
    String propertyValue = handleWDExceptions(() ->
        get(args.get(0), propertyName));
    
    return tryGetStringZwlValue(propertyValue);
  }
  
  protected abstract String get(ZwlValue elementId, String propertyName);
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.UNKNOWN.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.STRING;
  }
}
