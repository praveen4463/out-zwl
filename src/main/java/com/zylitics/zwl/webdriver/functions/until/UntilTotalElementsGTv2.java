package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class UntilTotalElementsGTv2 extends AbstractUntilExpectation {
  
  public UntilTotalElementsGTv2(APICoreProperties.Webdriver wdProps,
                                BuildCapability buildCapability,
                                RemoteWebDriver driver,
                                PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream, 3, 3);
  }
  
  @Override
  public String getName() {
    return "untilTotalElementsGTv2";
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    
    if (args.size() != 3) {
      throw unexpectedEndOfFunctionOverload(args.size());
    }
    RemoteWebElement element = getElement(tryCastString(0, args.get(0)), false);
    String selector = tryCastString(1, args.get(1));
    int total = parseDouble(2, args.get(2)).intValue();
    
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS);
    return handleWDExceptions(() ->
        wait.until(d -> {
          List<RemoteWebElement> e = findElements(element, selector, false);
          if (e.size() == 0) {
            return null;
          }
          return e.size() > total ? convertIntoZwlElemIds(e) : null;
        }));
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.ELEMENT_IDS.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.LIST;
  }
}