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
import java.util.List;

public class FindElementsFromElement extends AbstractFindFromElement {
  
  public FindElementsFromElement(APICoreProperties.Webdriver wdProps,
                                 BuildCapability buildCapability,
                                 RemoteWebDriver driver,
                                 PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "findElementsFromElement";
  }
  
  @Override
  protected ZwlValue find(ZwlValue fromElement, String using, ByType byType, boolean wait) {
    List<RemoteWebElement> els = findElements(fromElement, getBy(byType, using), wait);
    return convertIntoZwlElemIds(els, fromElement, using, byType);
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.ELEMENT_IDS.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.STRING;
  }
}
