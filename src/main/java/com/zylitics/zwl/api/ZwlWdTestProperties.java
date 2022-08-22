package com.zylitics.zwl.api;

import com.google.cloud.storage.Storage;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.annotation.Nullable;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

public interface ZwlWdTestProperties {
  
  RemoteWebDriver getDriver();
  
  PrintStream getPrintStream();
  
  Consumer<String> getCallTestHandler();
  
  Storage getStorage();
  
  String getUserUploadsCloudPath();
  
  Path getBuildDir();
  
  Defaults getDefault();
  
  Capabilities getCapabilities();
  
  Variables getVariables();
  
  String getVMResolution();
  
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
    
    String getMeDeviceResolution();
  
    Integer getCustomTimeoutElementAccess();
  
    Integer getCustomTimeoutPageLoad();
  
    Integer getCustomTimeoutScript();
  }
  
  interface Variables {
  
    @Nullable
    Map<String, String> getBuildVariables();
    
    @Nullable
    Map<String, String> getPreferences();
  
    @Nullable
    Map<String, String> getGlobal();
  
    Map<String, ZwlValue> get_Global();
  }
}
