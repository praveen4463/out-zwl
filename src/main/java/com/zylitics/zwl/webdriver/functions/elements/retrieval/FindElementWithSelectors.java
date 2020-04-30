package com.zylitics.zwl.webdriver.functions.elements.retrieval;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class FindElementWithSelectors extends AbstractWebdriverFunction {
  
  public FindElementWithSelectors(APICoreProperties.Webdriver wdProps,
                                  BuildCapability buildCapability,
                                  RemoteWebDriver driver,
                                  PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return Integer.MAX_VALUE;
  }
  
  @Override
  public String getName() {
    return "findElementWithSelectors";
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    return handleWDExceptions(() -> {
      if (args.size() == 0) {
        throw unexpectedEndOfFunctionOverload(args.size());
      }
      RuntimeException lastException = null;
      for (int i = 0; i < args.size(); i++) {
        String selector = tryCastString(i, args.get(i));
        try {
          return convertIntoZwlElemId(findElement(driver, selector, false));
        } catch (NoSuchElementException ne) {
          lastException = ne;
        }
      }
      throw lastException;
    });
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.ELEMENT_ID.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.STRING;
  }
}
