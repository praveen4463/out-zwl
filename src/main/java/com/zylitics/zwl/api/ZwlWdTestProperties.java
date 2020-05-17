package com.zylitics.zwl.api;

import com.google.cloud.storage.Storage;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.annotation.Nullable;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Map;

public interface ZwlWdTestProperties {
  
  RemoteWebDriver getDriver();
  
  PrintStream getPrintStream();
  
  Storage getStorage();
  
  String getUserUploadsCloudPath();
  
  Path getBuildDir();
  
  Defaults getDefault();
  
  Capabilities getCapabilities();
  
  Variables getVariables();
  
  interface Defaults {
    
    String getUserDataBucket();
  
    Integer getDefaultTimeoutElementAccess();
  
    Integer getDefaultTimeoutPageLoad();
  
    Integer getDefaultTimeoutScript();
  
    Integer getDefaultTimeoutNewWindow();
  
    String getElementShotDir();
  }
  
  interface Capabilities {
    
    String getBrowserName();
  
    String getBrowserVersion();
  
    String getPlatformName();
  
    Integer getCustomTimeoutElementAccess();
  
    Integer getCustomTimeoutPageLoad();
  
    Integer getCustomTimeoutScript();
  }
  
  interface Variables {
    
    @Nullable
    Map<String, String> getPreferences();
  
    @Nullable
    Map<String, String> getGlobal();
  }
}