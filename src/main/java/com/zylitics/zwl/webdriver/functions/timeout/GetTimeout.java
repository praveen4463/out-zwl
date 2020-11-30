package com.zylitics.zwl.webdriver.functions.timeout;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.Configuration;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.ZwlLangException;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class GetTimeout extends AbstractWebdriverFunction {
  
  public GetTimeout(APICoreProperties.Webdriver wdProps,
                    BuildCapability buildCapability,
                    RemoteWebDriver driver,
                    PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "getTimeout";
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
    
    if (args.size() != 1) {
      throw unexpectedEndOfFunctionOverload(args.size());
    }
    String t = tryCastString(0, args.get(0));
    try {
      TimeoutType timeoutType = parseEnum(0, args.get(0), TimeoutType.class);
      if (buildCapability.isDryRunning()) {
        return evaluateDefValue(defaultValue);
      }
      return new DoubleZwlValue(new Configuration().getTimeouts(wdProps, buildCapability,
          timeoutType));
    } catch (IllegalArgumentException i) {
      throw new ZwlLangException(fromPos.get(), toPos.get(),
          "Given timeout type " + t + " isn't valid.", i);
    }
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.THOUSAND.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.NUMBER;
  }
}
