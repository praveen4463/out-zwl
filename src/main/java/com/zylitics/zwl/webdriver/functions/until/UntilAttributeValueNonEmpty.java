package com.zylitics.zwl.webdriver.functions.until;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class UntilAttributeValueNonEmpty extends AbstractUntilExpectation {
  
  public UntilAttributeValueNonEmpty(APICoreProperties.Webdriver wdProps,
                                     BuildCapability buildCapability,
                                     RemoteWebDriver driver,
                                     PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream, 2, 2);
  }
  
  @Override
  public String getName() {
    return "untilAttributeValueNonEmpty";
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    
    if (args.size() != 2) {
      throw unexpectedEndOfFunctionOverload(args.size());
    }
    String elemOrSelector = tryCastString(0, args.get(0));
    String attribute = tryCastString(1, args.get(1));
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS);
    if (!isValidElemId(elemOrSelector)) {
      // ignore stale exception default so that even if element goes stale intermittent we can
      // locate it and match text/value.
      wait.ignoring(StaleElementReferenceException.class);
    }
    return handleWDExceptions(() ->
        new BooleanZwlValue(wait.until(d -> {
          RemoteWebElement e = getElement(elemOrSelector, false);
          String attributeValue = e.getAttribute(attribute);
          if (Strings.isNullOrEmpty(attributeValue)) {
            attributeValue = e.getCssValue(attribute);
          }
          return !Strings.isNullOrEmpty(attributeValue) && attributeValue.trim().length() > 0;
        })));
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.TRUE.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.BOOLEAN;
  }
}
