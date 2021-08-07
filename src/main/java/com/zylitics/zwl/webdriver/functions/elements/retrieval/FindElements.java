package com.zylitics.zwl.webdriver.functions.elements.retrieval;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.ByType;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;
import java.util.List;

public class FindElements extends AbstractFindElement {
  
  public FindElements(APICoreProperties.Webdriver wdProps,
                      BuildCapability buildCapability,
                      RemoteWebDriver driver,
                      PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "findElements";
  }
  
  @Override
  protected ZwlValue find(String using, ByType byType, boolean wait) {
    List<RemoteWebElement> els = findElements(driver, getBy(byType, using), wait);
    return convertIntoZwlElemIds(els, using, byType);
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return FuncDefReturnValue.ELEMENT_IDS.getDefValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.LIST;
  }
}
