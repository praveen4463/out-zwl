package com.zylitics.zwl.webdriver.functions.elements.state;

import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class GetElementRect extends AbstractWebdriverFunction {
  
  public GetElementRect(APICoreProperties.Webdriver wdProps,
                        BuildCapability buildCapability,
                        RemoteWebDriver driver,
                        PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "getElementRect";
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
  
    if (args.size() == 0) {
      throw unexpectedEndOfFunctionOverload(args.size());
    }
    String elemIdOrSelector = tryCastString(0, args.get(0));
    Rectangle r = handleWDExceptions(() -> getElement(elemIdOrSelector).getRect());
    if (r == null) {
      return new NothingZwlValue();
    }
    return new MapZwlValue(ImmutableMap.of(
        "x", new DoubleZwlValue(r.x),
        "y", new DoubleZwlValue(r.y),
        "width", new DoubleZwlValue(r.width),
        "height", new DoubleZwlValue(r.height)
    ));
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.RECT.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.MAP;
  }
}
