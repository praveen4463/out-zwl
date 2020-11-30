package com.zylitics.zwl.webdriver.functions.util;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.ZwlLangException;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class Sleep extends AbstractWebdriverFunction {
  
  public Sleep(APICoreProperties.Webdriver wdProps,
               BuildCapability buildCapability,
               RemoteWebDriver driver,
               PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "sleep";
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
    
    try {
      Thread.sleep(parseDouble(0, args.get(0)).longValue());
      return _void;
    } catch (InterruptedException i) {
      throw new ZwlLangException(fromPos.get(), toPos.get(),
          withLineNCol("Sleep was interrupted due to some problem."), i);
    }
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
