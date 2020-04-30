package com.zylitics.zwl.webdriver;

public class BuildCapability {
  
  private String wdBrowserName;
  
  private String wdBrowserVersion;
  
  private String wdPlatformName;
  
  private int wdTimeoutsScript;
  
  private int wdTimeoutsPageLoad;
  
  private int wdTimeoutsElementAccess;
  
  private boolean dryRunning;
  
  public String getWdBrowserName() {
    return wdBrowserName;
  }
  
  public BuildCapability setWdBrowserName(String wdBrowserName) {
    this.wdBrowserName = wdBrowserName;
    return this;
  }
  
  public String getWdBrowserVersion() {
    return wdBrowserVersion;
  }
  
  public BuildCapability setWdBrowserVersion(String wdBrowserVersion) {
    this.wdBrowserVersion = wdBrowserVersion;
    return this;
  }
  
  public String getWdPlatformName() {
    return wdPlatformName;
  }
  
  public BuildCapability setWdPlatformName(String wdPlatformName) {
    this.wdPlatformName = wdPlatformName;
    return this;
  }
  
  public int getWdTimeoutsScript() {
    return wdTimeoutsScript;
  }
  
  public BuildCapability setWdTimeoutsScript(int wdTimeoutsScript) {
    this.wdTimeoutsScript = wdTimeoutsScript;
    return this;
  }
  
  public int getWdTimeoutsPageLoad() {
    return wdTimeoutsPageLoad;
  }
  
  public BuildCapability setWdTimeoutsPageLoad(int wdTimeoutsPageLoad) {
    this.wdTimeoutsPageLoad = wdTimeoutsPageLoad;
    return this;
  }
  
  public int getWdTimeoutsElementAccess() {
    return wdTimeoutsElementAccess;
  }
  
  public BuildCapability setWdTimeoutsElementAccess(int wdTimeoutsElementAccess) {
    this.wdTimeoutsElementAccess = wdTimeoutsElementAccess;
    return this;
  }
  
  public boolean isDryRunning() {
    return dryRunning;
  }
  
  public BuildCapability setDryRunning(boolean dryRunning) {
    this.dryRunning = dryRunning;
    return this;
  }
}
