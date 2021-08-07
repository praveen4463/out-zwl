package com.zylitics.zwl.webdriver.functions.context;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.InvalidTypeException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>Accepts one argument that could be of one of following types:</p>
 * <p>1) If it's a numeric value, treat it as index.</p>
 * <p>2) If it's a UUID, treat it as elemId.</p>
 * <p>3) If it's a string, treat it as selector</p>
 */
public class SwitchFrame extends AbstractWebdriverFunction {
  
  public SwitchFrame(APICoreProperties.Webdriver wdProps,
                     BuildCapability buildCapability,
                     RemoteWebDriver driver,
                     PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "switchFrame";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return 1;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    if (args.size() != 1) {
      throw unexpectedEndOfFunctionOverload(args.size());
    }
    return handleWDExceptions(() -> {
      execute(0, args.get(0));
      return _void;
    });
  }
  
  private void execute(@SuppressWarnings("SameParameterValue") int argIndex, ZwlValue arg) {
    // is the argument a frame index?
    try {
      int index = parseDouble(argIndex, arg).intValue();
      targetLocator.frame(index);
      return;
    } catch (InvalidTypeException ignore) {
      // ignore
    }
    doSafeInteraction(arg, (Consumer<RemoteWebElement>) targetLocator::frame);
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return _void;
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.VOID;
  }
}
