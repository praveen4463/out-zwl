package com.zylitics.zwl.webdriver.functions.action;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class DragAndDrop extends AbstractWebdriverFunction {
  
  public DragAndDrop(APICoreProperties.Webdriver wdProps,
                     BuildCapability buildCapability,
                     RemoteWebDriver driver,
                     PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "dragAndDrop";
  }
  
  @Override
  public int minParamsCount() {
    return 2;
  }
  
  @Override
  public int maxParamsCount() {
    return 3;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    int argsCount = args.size();
    
    Actions actions = new Actions(driver);
    return handleWDExceptions(() -> {
      if (argsCount == 2) {
        actions.dragAndDrop(getElement(tryCastString(0, args.get(0))),
            getElement(tryCastString(1, args.get(1))));
      } else if (argsCount == 3) {
        actions.dragAndDropBy(getElement(tryCastString(0, args.get(0))),
            parseDouble(1, args.get(1)).intValue(),
            parseDouble(2, args.get(2)).intValue());
      } else {
        throw unexpectedEndOfFunctionOverload(argsCount);
      }
      actions.perform();
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
