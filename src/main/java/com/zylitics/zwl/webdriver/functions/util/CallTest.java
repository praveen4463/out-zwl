package com.zylitics.zwl.webdriver.functions.util;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CallTest extends AbstractWebdriverFunction {
  
  private final Consumer<String> callTestHandler;
  
  public CallTest(APICoreProperties.Webdriver wdProps,
                  BuildCapability buildCapability,
                  RemoteWebDriver driver,
                  PrintStream printStream,
                  Consumer<String> callTestHandler) {
    super(wdProps, buildCapability, driver, printStream);
    
    this.callTestHandler = callTestHandler;
  }
  
  @Override
  public String getName() {
    return "callTest";
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
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    if (args.size() == 0) {
      throw unexpectedEndOfFunctionOverload(0);
    }
  
    callTestHandler.accept(args.get(0).toString());
    
    return _void;
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