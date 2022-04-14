package com.zylitics.zwl.webdriver.functions.action;

import com.google.common.io.Resources;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

// TODO: Currently only the 2 args variant works reliably for HTML5 dnd. Either remove the other variants
//  or see if they can be done using the js.
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
        driver.executeScript(
            String.format("%s\ndragAndDrop(arguments[0], arguments[1]);",
                getAtom()),
            el1, el2);
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
  
  private String getAtom() {
    try {
      String scriptName = "/dragAndDrop.min.js";
      URL url = getClass().getResource(scriptName);
      Objects.requireNonNull(url);
      //noinspection UnstableApiUsage
      return Resources.toString(url, StandardCharsets.UTF_8);
    } catch (IOException | NullPointerException e) {
      throw new WebDriverException(e);
    }
  }
}
