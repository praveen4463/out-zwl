package com.zylitics.zwl.webdriver.functions.util;

import com.zylitics.zwl.datatype.NothingZwlValue;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GetArgs extends AbstractWebdriverFunction {
  
  private final Map<String, ZwlValue> _global;
  
  private final String testPath;
  
  public GetArgs(APICoreProperties.Webdriver wdProps,
              BuildCapability buildCapability,
              RemoteWebDriver driver,
              PrintStream printStream,
              Map<String, ZwlValue> _global,
              String testPath) {
    super(wdProps, buildCapability, driver, printStream);
  
    this._global = _global;
    this.testPath = testPath;
  }
  
  @Override
  public String getName() {
    return "getArgs";
  }
  
  @Override
  public int minParamsCount() {
    return 0;
  }
  
  @Override
  public int maxParamsCount() {
    return 0;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
  
    String funcArgsKey = FuncUtil.getFuncArgsKey(FuncUtil.getUniqueKeyFromTestPath(testPath));
    if (_global.containsKey(funcArgsKey)) {
      return _global.get(funcArgsKey);
    }
    return new NothingZwlValue();
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return new NothingZwlValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.OBJECT;
  }
}
