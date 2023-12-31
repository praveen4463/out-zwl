package com.zylitics.zwl.webdriver.functions.until;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

abstract class AbstractTextValueNonEmpty extends AbstractUntilExpectation {
  
  public AbstractTextValueNonEmpty(APICoreProperties.Webdriver wdProps,
                                   BuildCapability buildCapability,
                                   RemoteWebDriver driver,
                                   PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream, 1, 1);
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    
    if (args.size() != 1) {
      throw unexpectedEndOfFunctionOverload(args.size());
    }
    
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS);
    return handleWDExceptions(() ->
        new StringZwlValue(wait.until(d -> {
          String textOrValue = textOrValue(args.get(0));
          // trim removes new line, tabs etc including whitespaces.
          return !Strings.isNullOrEmpty(textOrValue) && textOrValue.trim().length() > 0
              ? textOrValue : null;
        })));
  }
  
  abstract String textOrValue(ZwlValue elementId);
  
  @Override
  protected String getFuncReturnType() {
    return Types.STRING;
  }
}