package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class UntilRemoved extends AbstractUntilExpectation {
  
  public UntilRemoved(APICoreProperties.Webdriver wdProps,
                      BuildCapability buildCapability,
                      RemoteWebDriver driver,
                      PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream, 1, 3);
  }
  
  @Override
  public String getName() {
    return "untilRemoved";
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
    
    return handleWDExceptions(() ->
        new BooleanZwlValue(wait.until(d -> {
            List<RemoteWebElement> e = findElementsDetectingArgs(args, false);
            return e.size() == 0; // when successfully found, we need to find again.
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
