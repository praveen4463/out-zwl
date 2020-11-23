package com.zylitics.zwl.webdriver.constants;

public enum Platforms {
  WINDOWS ("windows");
  
  private final String name;
  
  Platforms(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
}
