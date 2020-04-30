package com.zylitics.zwl.webdriver.functions.context.resize;

import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.DoubleZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.Map;

public class GetWinSize extends GetWinRect {
  
  public GetWinSize(APICoreProperties.Webdriver wdProps,
                    BuildCapability buildCapability,
                    RemoteWebDriver driver,
                    PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "getWinSize";
  }
  
  @Override
  protected Map<String, ZwlValue> get() {
    Dimension d = window.getSize();
    return ImmutableMap.of(
        "width", new DoubleZwlValue(d.width),
        "height", new DoubleZwlValue(d.height)
    );
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.SIZE.getDefValue();
  }
}
