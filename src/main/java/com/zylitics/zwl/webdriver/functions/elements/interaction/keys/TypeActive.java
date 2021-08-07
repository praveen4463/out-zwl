package com.zylitics.zwl.webdriver.functions.elements.interaction.keys;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class TypeActive extends AbstractWebdriverFunction {
  
  public TypeActive(APICoreProperties.Webdriver wdProps,
                    BuildCapability buildCapability,
                    RemoteWebDriver driver,
                    PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "typeActive";
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
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    int argsCount = args.size();
    if (argsCount == 0) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    String[] keys = args.stream().map(Objects::toString).toArray(String[]::new);
    WebDriverWait waitAccess = getWait(TimeoutType.ELEMENT_ACCESS,
        "waiting for an active typeable element");
    return handleWDExceptions(() -> {
      WebElement e = waitAccess.until(d -> getTypeableElementOrNull(targetLocator.activeElement()));
      WebDriverWait waitType = getWait(TimeoutType.ELEMENT_ACCESS,
          "waiting for element to become typeable");
      waitType.until(d -> {
        try {
          e.sendKeys(keys);
          return true;
        } catch (InvalidElementStateException ie) {
          return false;
        }
      });
      return _void;
    });
  }
  
  private WebElement getTypeableElementOrNull(WebElement element) {
    String tag = element.getTagName();
    String contentEditable = element.getAttribute("isContentEditable");
    if (tag.equals("textarea")
        || (!Strings.isNullOrEmpty(contentEditable) && contentEditable.equals("true"))) {
      return element;
    }
    if (tag.equals("input")) {
      String type = element.getAttribute("type");
      List<String> editableInputs = new ArrayList<>(Arrays.asList("email", "number", "password",
          "search", "tel", "text", "url"));
      if (editableInputs.contains(type)) {
        return element;
      }
    }
    return null;
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
