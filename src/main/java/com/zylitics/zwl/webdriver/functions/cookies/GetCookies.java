package com.zylitics.zwl.webdriver.functions.cookies;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.util.CollectionUtil;
import com.zylitics.zwl.datatype.ListZwlValue;
import com.zylitics.zwl.datatype.MapZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class GetCookies extends AbstractGetCookie {
  
  public GetCookies(APICoreProperties.Webdriver wdProps,
                    BuildCapability buildCapability,
                    RemoteWebDriver driver,
                    PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "getCookies";
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
    
    Set<Cookie> cookies = handleWDExceptions(options::getCookies);
    if (cookies == null || cookies.size() == 0) {
      return new ListZwlValue(Collections.emptyList());
    }
    List<ZwlValue> result = new ArrayList<>(CollectionUtil.getInitialCapacity(cookies.size()));
    cookies.forEach(c -> result.add(new MapZwlValue(cookieToZwlMap(c))));
    return new ListZwlValue(result);
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.COOKIES.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.LIST;
  }
}
