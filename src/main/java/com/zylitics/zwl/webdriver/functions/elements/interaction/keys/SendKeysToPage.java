package com.zylitics.zwl.webdriver.functions.elements.interaction.keys;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SendKeysToPage extends AbstractWebdriverFunction {
  
  public SendKeysToPage(APICoreProperties.Webdriver wdProps,
                        BuildCapability buildCapability,
                        RemoteWebDriver driver,
                        PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "sendKeysToPage";
  }
  
  @Override
  public int minParamsCount() {
    return 2;
  }
  
  @Override
  public int maxParamsCount() {
    return Integer.MAX_VALUE;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    if (args.size() < 2) {
      throw unexpectedEndOfFunctionOverload(args.size());
    }
    String[] keys = args.subList(1, args.size()).stream().map(Objects::toString)
        .toArray(String[]::new);
    return handleWDExceptions(() -> {
      Consumer<Actions> clickOnEl = a -> waitUntilInteracted(args.get(0), a::click);
      new ActionSendKeysCustom(driver).actionSendKeysCustom(clickOnEl, keys);
      return _void;
    });
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
