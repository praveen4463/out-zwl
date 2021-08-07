package com.zylitics.zwl.webdriver.functions.until;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

/**
 * Used in visible/invisible/enable/disable expectations. These should work is same manner because
 * here we want the element to become existent if not found, become non-stale and gone stale before
 * checking whether it's state matches the desired state. ALl of these require element to be on
 * page.
 */
abstract class AbstractVisibleEnable extends AbstractUntilExpectation {
  
  public AbstractVisibleEnable(APICoreProperties.Webdriver wdProps,
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
    
    ZwlValue elementId = args.get(0);
    
    WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS);
    return handleWDExceptions(() ->
        wait.until(d -> desiredState(elementId) ? elementId : null));
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
