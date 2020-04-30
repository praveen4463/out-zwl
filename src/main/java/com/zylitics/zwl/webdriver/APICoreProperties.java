package com.zylitics.zwl.webdriver;

public class APICoreProperties {
  
  public static class Webdriver {
  
    private String userDataBucket;
  
    private Integer defaultTimeoutElementAccess;
  
    private Integer defaultTimeoutPageLoad;
  
    private Integer defaultTimeoutScript;
  
    private Integer defaultTimeoutNewWindow;
  
    private String elementShotDir;
  
    public String getUserDataBucket() {
      return userDataBucket;
    }
  
    public Webdriver setUserDataBucket(String userDataBucket) {
      this.userDataBucket = userDataBucket;
      return this;
    }
  
    public Integer getDefaultTimeoutElementAccess() {
      return defaultTimeoutElementAccess;
    }
  
    public Webdriver setDefaultTimeoutElementAccess(Integer defaultTimeoutElementAccess) {
      this.defaultTimeoutElementAccess = defaultTimeoutElementAccess;
      return this;
    }
  
    public Integer getDefaultTimeoutPageLoad() {
      return defaultTimeoutPageLoad;
    }
  
    public Webdriver setDefaultTimeoutPageLoad(Integer defaultTimeoutPageLoad) {
      this.defaultTimeoutPageLoad = defaultTimeoutPageLoad;
      return this;
    }
  
    public Integer getDefaultTimeoutScript() {
      return defaultTimeoutScript;
    }
  
    public Webdriver setDefaultTimeoutScript(Integer defaultTimeoutScript) {
      this.defaultTimeoutScript = defaultTimeoutScript;
      return this;
    }
  
    public Integer getDefaultTimeoutNewWindow() {
      return defaultTimeoutNewWindow;
    }
  
    public Webdriver setDefaultTimeoutNewWindow(Integer defaultTimeoutNewWindow) {
      this.defaultTimeoutNewWindow = defaultTimeoutNewWindow;
      return this;
    }
  
    public String getElementShotDir() {
      return elementShotDir;
    }
  
    public Webdriver setElementShotDir(String elementShotDir) {
      this.elementShotDir = elementShotDir;
      return this;
    }
  }
}
