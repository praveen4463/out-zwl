package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.InvalidTypeException;
import com.zylitics.zwl.util.ParseUtil;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class UntilSwitchedToFrame extends AbstractUntilExpectation {
  
  public UntilSwitchedToFrame(APICoreProperties.Webdriver wdProps,
                              BuildCapability buildCapability,
                              RemoteWebDriver driver,
                              PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream, 1, 3);
  }
  
  @Override
  public String getName() {
    return "untilSwitchedToFrame";
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    
    if (args.size() == 0) {
      throw unexpectedEndOfFunctionOverload(0);
    }
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS);
    wait.ignoring(NoSuchFrameException.class);
    
    // if first argument is int, it must be index
    try {
      int index = ParseUtil.parseDouble(args.get(0), () ->
          new InvalidTypeException(fromPos.get(), toPos.get(), "")).intValue();
      return handleWDExceptions(() ->
          new BooleanZwlValue(wait.until(d -> {
            targetLocator.frame(index);
            return true;
          })));
    } catch (InvalidTypeException ignore) {}
  
    return handleWDExceptions(() ->
        new BooleanZwlValue(wait.until(d -> {
          WebElement e = findElementDetectingArgs(args, false);
          targetLocator.frame(e);
          return true;
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
