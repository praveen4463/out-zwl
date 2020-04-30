package com.zylitics.zwl.webdriver.functions.elements.retrieval;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.constants.ByType;
import com.zylitics.zwl.webdriver.constants.FuncDefReturnValue;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

public class FindElement extends AbstractFindElement {
  
  public FindElement(APICoreProperties.Webdriver wdProps,
                     BuildCapability buildCapability,
                     RemoteWebDriver driver,
                     PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  @Override
  public String getName() {
    return "findElement";
  }
  
  @Override
  protected ZwlValue find(String using, ByType byType, boolean wait) {
    return convertIntoZwlElemId(findElement(driver, getBy(byType, using), wait));
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
