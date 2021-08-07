package com.zylitics.zwl.webdriver.functions.elements.interaction.keys;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TypeIntoElements extends AbstractWebdriverFunction {
  
  public TypeIntoElements(APICoreProperties.Webdriver wdProps,
                          BuildCapability buildCapability,
                          RemoteWebDriver driver,
                          PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "typeIntoElements";
  }
  
  @Override
  public int minParamsCount() {
    return 2;
  }
  
  @Override
  public int maxParamsCount() {
    return Integer.MAX_VALUE;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    int argsCount = args.size();
    if (argsCount < 2) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    String text = args.get(0).toString();
    return handleWDExceptions(() -> {
      args.subList(1, argsCount).forEach(eId -> waitUntilInteracted(eId, el -> el.sendKeys(text)));
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
