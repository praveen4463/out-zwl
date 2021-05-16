package com.zylitics.zwl.webdriver.constants;

import org.openqa.selenium.remote.BrowserType;

// TODO: previously I had IE as internet explorer esdb browser index and on server, that's
//  why the alias is taken. Later refactor it to remove this enum and take whatever browser is coming
//  from user as is.
public enum Browsers {
  
  CHROME  (BrowserType.CHROME, BrowserType.CHROME),
  FIREFOX (BrowserType.FIREFOX, BrowserType.FIREFOX),
  IE (BrowserType.IE, "IE");
  
  private final String name;
  private final String alias;
  
  Browsers(String name, String alias) {
    this.name = name;
    this.alias = alias;
  }
  
  public static Browsers valueByName(String name) {
    for (Browsers browser : Browsers.values()) {
      if (browser.getName().equalsIgnoreCase(name) || browser.getAlias().equalsIgnoreCase(name)) {
        return browser;
      }
    }
    throw new IllegalArgumentException("No instance of Browsers matched the given name " + name);
  }
  
  public String getName() {
    return name;
  }
  
  public String getAlias() {
    return alias;
  }
}
