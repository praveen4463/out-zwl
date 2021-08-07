package com.zylitics.zwl.webdriver.functions.elements.retrieval;

import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.ByType;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;

public class FindElementFromElement extends AbstractFindFromElement {
  
  public FindElementFromElement(APICoreProperties.Webdriver wdProps,
                                BuildCapability buildCapability,
                                RemoteWebDriver driver,
                                PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "findElementFromElement";
  }
  
  @Override
  protected ZwlValue find(ZwlValue fromElement, String using, ByType byType, boolean wait) {
    RemoteWebElement el = findElement(fromElement, getBy(byType, using), wait);
    return convertIntoZwlElemId(el, fromElement, using, byType);
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.ELEMENT_ID.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.STRING;
  }
}
