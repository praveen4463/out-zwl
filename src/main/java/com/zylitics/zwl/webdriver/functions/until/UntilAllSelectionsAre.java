package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.datatype.ListZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class UntilAllSelectionsAre extends AbstractUntilExpectation {
  
  public UntilAllSelectionsAre(APICoreProperties.Webdriver wdProps,
                               BuildCapability buildCapability,
                               RemoteWebDriver driver,
                               PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream, 2, Integer.MAX_VALUE);
  }
  
  @Override
  public String getName() {
    return "untilAllSelectionsAre";
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    
    int argsCount = args.size();
    if (argsCount < 2) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    Boolean selectionToBe = parseBoolean(0, args.get(0));
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    List<ZwlValue> elementIds = args.subList(1, argsCount);
  
    for (ZwlValue eId : elementIds) {
      WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS);
      handleWDExceptions(() -> wait.until(d ->
          doSafeInteraction(eId, RemoteWebElement::isSelected) == selectionToBe));
    }
    return new ListZwlValue(elementIds);
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