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
      if (this.userDataBucket == null) {
        this.userDataBucket = userDataBucket;
      }
      return this;
    }
  
    public Integer getDefaultTimeoutElementAccess() {
      return defaultTimeoutElementAccess;
    }
  
    public Webdriver setDefaultTimeoutElementAccess(Integer defaultTimeoutElementAccess) {
      if (this.defaultTimeoutElementAccess == null) {
        this.defaultTimeoutElementAccess = defaultTimeoutElementAccess;
      }
      return this;
    }
  
    public Integer getDefaultTimeoutPageLoad() {
      return defaultTimeoutPageLoad;
    }
  
    public Webdriver setDefaultTimeoutPageLoad(Integer defaultTimeoutPageLoad) {
      if (this.defaultTimeoutPageLoad == null) {
        this.defaultTimeoutPageLoad = defaultTimeoutPageLoad;
      }
      return this;
    }
  
    public Integer getDefaultTimeoutScript() {
      return defaultTimeoutScript;
    }
  
    public Webdriver setDefaultTimeoutScript(Integer defaultTimeoutScript) {
      if (this.defaultTimeoutScript == null) {
        this.defaultTimeoutScript = defaultTimeoutScript;
      }
      return this;
    }
  
    public Integer getDefaultTimeoutNewWindow() {
      return defaultTimeoutNewWindow;
    }
  
    public Webdriver setDefaultTimeoutNewWindow(Integer defaultTimeoutNewWindow) {
      if (this.defaultTimeoutNewWindow == null) {
        this.defaultTimeoutNewWindow = defaultTimeoutNewWindow;
      }
      return this;
    }
  
    public String getElementShotDir() {
      return elementShotDir;
    }
  
    public Webdriver setElementShotDir(String elementShotDir) {
      if (this.elementShotDir == null) {
        this.elementShotDir = elementShotDir;
      }
      return this;
    }
  }
}
