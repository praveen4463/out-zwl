package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.datatype.ListZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

/**
 * Multi element variant of {@link AbstractVisibleEnable}
 */
abstract class AbstractVisibleEnableMulti extends AbstractUntilExpectation {
  
  public AbstractVisibleEnableMulti(APICoreProperties.Webdriver wdProps,
                                    BuildCapability buildCapability,
                                    RemoteWebDriver driver,
                                    PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream, 1, Integer.MAX_VALUE);
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    if (args.size() == 0) {
      throw unexpectedEndOfFunctionOverload(0);
    }
    // when multiple elemIds are given, separate wait is used for everyone so that the
    // element access timeout doesn't exhaust when working with many elemIds.
    for (ZwlValue arg : args) {
      WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS);
      handleWDExceptions(() -> wait.until(d -> desiredState(arg)));
    }
    return new ListZwlValue(args);
  }
  
  abstract boolean desiredState(ZwlValue elementId);
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.TRUE.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.BOOLEAN;
  }
}
