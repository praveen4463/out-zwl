package com.zylitics.zwl.webdriver;

public class Configuration {
  
  public int getTimeouts(APICoreProperties.Webdriver wdProps,
                         BuildCapability buildCapability,
                         TimeoutType timeoutType) {
    switch (timeoutType) {
      case ELEMENT_ACCESS:
      default:
        return buildCapability.getWdTimeoutsElementAccess() >= 0
            ? buildCapability.getWdTimeoutsElementAccess()
            : wdProps.getDefaultTimeoutElementAccess();
      case PAGE_LOAD:
        return buildCapability.getWdTimeoutsPageLoad() >= 0
            ? buildCapability.getWdTimeoutsPageLoad()
            : wdProps.getDefaultTimeoutPageLoad();
      case JAVASCRIPT:
        return buildCapability.getWdTimeoutsScript() >= 0
            ? buildCapability.getWdTimeoutsScript()
            : wdProps.getDefaultTimeoutScript();
      case NEW_WINDOW:
        return wdProps.getDefaultTimeoutNewWindow();
      // currently new window timeout isn't accepted through build caps.
    }
  }
}
