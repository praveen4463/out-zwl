package com.zylitics.zwl.api;

import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.Map;

public interface ZwlDryRunProperties {
  
  PrintStream getPrintStream();
  
  ZwlDryRunProperties.Capabilities getCapabilities();
  
  ZwlDryRunProperties.Variables getVariables();
  
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
    Map<String, String> getPreferences();
    
    @Nullable
    Map<String, String> getGlobal();
  }
}
