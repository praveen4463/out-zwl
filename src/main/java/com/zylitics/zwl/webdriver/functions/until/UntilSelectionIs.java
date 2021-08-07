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

public class UntilSelectionIs extends AbstractUntilExpectation {
  
  public UntilSelectionIs(APICoreProperties.Webdriver wdProps,
                          BuildCapability buildCapability,
                          RemoteWebDriver driver,
                          PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream, 2, 2);
  }
  
  @Override
  public String getName() {
    return "untilSelectionIs";
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    
    if (args.size() != 2) {
      throw unexpectedEndOfFunctionOverload(args.size());
    }
    Boolean b = parseBoolean(1, args.get(1));
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS);
    return handleWDExceptions(() -> new BooleanZwlValue(wait.until(d ->
        doSafeInteraction(args.get(0), RemoteWebElement::isSelected) == b)));
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
