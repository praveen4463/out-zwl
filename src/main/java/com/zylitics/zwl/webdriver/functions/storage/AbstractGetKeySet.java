package com.zylitics.zwl.webdriver.functions.storage;

import com.zylitics.zwl.datatype.NothingZwlValue;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

abstract class AbstractGetKeySet extends AbstractStorage {
  
  public AbstractGetKeySet(APICoreProperties.Webdriver wdProps,
                           BuildCapability buildCapability,
                           RemoteWebDriver driver,
                           PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
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
    
    Set<String> keySet = handleWDExceptions(this::get);
    if (keySet == null) {
      return new NothingZwlValue();
    }
    return tryGetStringZwlValues(keySet);
  }
  
  protected abstract Set<String> get();
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.STORAGE_KEYS.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.LIST;
  }
}
