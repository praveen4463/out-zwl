package com.zylitics.zwl.webdriver.functions.elements.state;

import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GetElementViewportCoordinates extends AbstractWebdriverFunction {
  
  public GetElementViewportCoordinates(APICoreProperties.Webdriver wdProps,
                                       BuildCapability buildCapability,
                                       RemoteWebDriver driver,
                                       PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "getElementViewportCoordinates";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return 2;
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
    
    Point p =
        handleWDExceptions(() -> {
          boolean scrollIntoViewport = true;
          if (args.size() == 2) {
            scrollIntoViewport = parseBoolean(1, args.get(1));
          }
          // we're using element twice here and one of them is used in script. Better approach is to
          // get a valid one.
          RemoteWebElement element = getValidElement(args.get(0));
          if (scrollIntoViewport) {
            return element.getCoordinates().inViewPort();
          }
          // doing this may be useless because if user don't want to scroll to vieport they can
          // just uses GetElementRect which also gives coordinates relative to viewport and webdriver
          // internally use the same getBoundingClientRect()
          String script = "var e = arguments[0]; var rect = e.getBoundingClientRect();" +
              " return {'x': rect.left, 'y': rect.top};";
          @SuppressWarnings("unchecked")
          Map<String, Number> mapped = (Map<String, Number>) driver.executeScript(script, element);
          return new Point(mapped.get("x").intValue(), mapped.get("y").intValue());
        });
    
    if (p == null) {
      return new NothingZwlValue();
    }
    return new MapZwlValue(ImmutableMap.of(
        "x", new DoubleZwlValue(p.x),
        "y", new DoubleZwlValue(p.y)
    ));
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.POSITION.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.MAP;
  }
}
