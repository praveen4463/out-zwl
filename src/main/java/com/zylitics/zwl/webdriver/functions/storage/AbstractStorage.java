package com.zylitics.zwl.webdriver.functions.storage;

import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;

abstract class AbstractStorage extends AbstractWebdriverFunction {
  
  public AbstractStorage(APICoreProperties.Webdriver wdProps,
                         BuildCapability buildCapability,
                         RemoteWebDriver driver,
                         PrintStream printStream) {
    super(wdProps, buildCapability, driver, printStream);
  }
  
  LocalStorage getLocalStorage() {
    if (driver instanceof WebStorage) {
      WebStorage storage = (WebStorage) driver;
      return storage.getLocalStorage();
    } else {
      return new JsBasedWebStorage(StorageType.LOCAL, driver);
    }
  }
  
  SessionStorage getSessionStorage() {
    if (driver instanceof WebStorage) {
      WebStorage storage = (WebStorage) driver;
      return storage.getSessionStorage();
    } else {
      return new JsBasedWebStorage(StorageType.SESSION, driver);
    }
  }
}
