package com.zylitics.zwl.api;

import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.Map;

public interface ZwlDryRunProperties {
  
  PrintStream getPrintStream();
  
  Capabilities getCapabilities();
  
  Variables getVariables();
  
  interface Capabilities {
  
    @Nullable
    String getBrowserName();
  
    @Nullable
    String getBrowserVersion();
  
    @Nullable
    String getPlatformName();
  }
  
  interface Variables {
  
    @Nullable
    Map<String, String> getBuildVariables();
    
    @Nullable
    Map<String, String> getPreferences();
    
    @Nullable
    Map<String, String> getGlobal();
  }
}
