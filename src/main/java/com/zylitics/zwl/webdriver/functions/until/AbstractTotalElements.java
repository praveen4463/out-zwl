package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.constants.ByType;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

abstract class AbstractTotalElements extends AbstractUntilExpectation {
  
  public AbstractTotalElements(APICoreProperties.Webdriver wdProps,
                               BuildCapability buildCapability,
                               RemoteWebDriver driver,
                               PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream, 2, 4);
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
    
    if (argsCount < 3) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    int total = parseDouble(0, args.get(0)).intValue();
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS);
    return handleWDExceptions(() ->
        wait.until(d -> {
          List<RemoteWebElement> e = findElementsDetectingArgs(args.subList(1, argsCount), false);
          if (e.size() == 0) {
            return null;
          }
          return desiredState(e.size(), total) ? convertIntoZwlElemIds(e, args) : null;
        }));
  }
  
  protected ZwlValue convertIntoZwlElemIds(List<RemoteWebElement> els,
                                           List<ZwlValue> args) {
    int argsCount = args.size();
    if (argsCount == 3) {
      return super.convertIntoZwlElemIds(els,
          tryCastString(1, args.get(1)),
          parseEnum(2, args.get(2), ByType.class));
    } else if (argsCount == 4) {
      return super.convertIntoZwlElemIds(els,
          args.get(1),
          tryCastString(2, args.get(2)),
          parseEnum(3, args.get(3), ByType.class));
    } else {
      throw new IllegalArgumentException("Unexpected number of arguments received");
    }
  }
  
  abstract boolean desiredState(int totalElementsFound, int givenTotal);
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.ELEMENT_IDS.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.LIST;
  }
}
