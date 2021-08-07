package com.zylitics.zwl.webdriver.functions.until;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;

public class UntilTextNonEmpty extends AbstractTextValueNonEmpty {
  
  public UntilTextNonEmpty(APICoreProperties.Webdriver wdProps,
                           BuildCapability buildCapability,
                           RemoteWebDriver driver,
                           PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "untilTextNonEmpty";
  }
  
  @Override
  String textOrValue(ZwlValue elementId) {
    return doSafeInteraction(elementId, RemoteWebElement::getText);
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.ELEMENT_TEXT.getDefValue();
  }
}