package com.zylitics.zwl.webdriver;

public class BuildCapability {
  
  // Although browser and platform details are not used within webdriver function's code, they're
  // still in Build caps for a potential future use when we may implicitly tweak functionality
  // based on browser and platform so that user doesn't have to deal with it in zwl.
  private String wdBrowserName;
  
  private String wdBrowserVersion;
  
  private String wdPlatformName;
  
  private int wdTimeoutsScript;
  
  private int wdTimeoutsPageLoad;
  
  private int wdTimeoutsElementAccess;
  
  private Boolean dryRunning;
  
  public String getWdBrowserName() {
    return wdBrowserName;
  }
  
  public BuildCapability setWdBrowserName(String wdBrowserName) {
    if (this.wdBrowserName == null) {
      this.wdBrowserName = wdBrowserName;
    }
    return this;
  }
  
  public String getWdBrowserVersion() {
    return wdBrowserVersion;
  }
  
  public BuildCapability setWdBrowserVersion(String wdBrowserVersion) {
    if (this.wdBrowserVersion == null) {
      this.wdBrowserVersion = wdBrowserVersion;
    }
    return this;
  }
  
  public String getWdPlatformName() {
    return wdPlatformName;
  }
  
  public BuildCapability setWdPlatformName(String wdPlatformName) {
    if (this.wdPlatformName == null) {
      this.wdPlatformName = wdPlatformName;
    }
    return this;
  }
  
  public int getWdTimeoutsScript() {
    return wdTimeoutsScript;
  }
  
  // not immutable, can be reset by timeout functions any number of times
  public BuildCapability setWdTimeoutsScript(int wdTimeoutsScript) {
    this.wdTimeoutsScript = wdTimeoutsScript;
    return this;
  }
  
  public int getWdTimeoutsPageLoad() {
    return wdTimeoutsPageLoad;
  }
  
  // not immutable, can be reset by timeout functions any number of times
  public BuildCapability setWdTimeoutsPageLoad(int wdTimeoutsPageLoad) {
    this.wdTimeoutsPageLoad = wdTimeoutsPageLoad;
    return this;
  }
  
  public int getWdTimeoutsElementAccess() {
    return wdTimeoutsElementAccess;
  }
  
  // not immutable, can be reset by timeout functions any number of times
  public BuildCapability setWdTimeoutsElementAccess(int wdTimeoutsElementAccess) {
    this.wdTimeoutsElementAccess = wdTimeoutsElementAccess;
    return this;
  }
  
  public Boolean isDryRunning() {
    return dryRunning;
  }
  
  public BuildCapability setDryRunning(Boolean dryRunning) {
    if (this.dryRunning == null) {
      this.dryRunning = dryRunning;
    }
    return this;
  }
}
