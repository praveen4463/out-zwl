package com.zylitics.zwl.webdriver.functions.action;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

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
        // We've two elements here in the same interaction. If we catch staleEx during interaction,
        // we'd not know which el got stale that is why the only option is to check for stale beforehand
        // and get the new element.
        RemoteWebElement el1 = getValidElement(args.get(0));
        RemoteWebElement el2 = getValidElement(args.get(1));
        WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS,
            "waiting for element to become intractable");
        wait.until(d -> {
          try {
            actions.dragAndDrop(el1, el2);
            return true;
          } catch (InvalidElementStateException ie) {
            return false;
          }
        });
      } else if (argsCount == 3) {
        waitUntilInteracted(args.get(0), el ->
            actions.dragAndDropBy(el,
                parseDouble(1, args.get(1)).intValue(),
                parseDouble(2, args.get(2)).intValue()));
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
