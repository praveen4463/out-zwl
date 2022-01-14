package com.zylitics.zwl.api;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zylitics.zwl.function.debugging.Print;
import com.zylitics.zwl.function.debugging.PrintF;
import com.zylitics.zwl.interpret.DefaultZwlInterpreter;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.WebdriverFunctions;

class ZwlWdTestPropsAssigner {
  
  private final DefaultZwlInterpreter interpreter;
  
  private final ZwlWdTestProperties props;
  
  ZwlWdTestPropsAssigner(DefaultZwlInterpreter interpreter,
                         ZwlWdTestProperties props) {
    this.interpreter = interpreter;
    this.props = props;
  }
  
  void assign() {
    validate();
    
    // replace functions
    interpreter.replaceFunction(new Print(props.getPrintStream()));
    interpreter.replaceFunction(new PrintF(props.getPrintStream()));
    
    // instantiate APICoreProperties
    ZwlWdTestProperties.Defaults defaults = props.getDefault();
    
    APICoreProperties.Webdriver wdProps = new APICoreProperties.Webdriver()
        .setUserDataBucket(defaults.getUserDataBucket())
        .setDefaultTimeoutElementAccess(defaults.getDefaultTimeoutElementAccess())
        .setDefaultTimeoutPageLoad(defaults.getDefaultTimeoutPageLoad())
        .setDefaultTimeoutScript(defaults.getDefaultTimeoutScript())
        .setDefaultTimeoutNewWindow(defaults.getDefaultTimeoutNewWindow())
        .setElementShotDir(defaults.getElementShotDir());
    
    // instantiate BuildCapability
    ZwlWdTestProperties.Capabilities capabilities = props.getCapabilities();
  
    BuildCapability buildCapability = new BuildCapability()
        .setWdBrowserName(capabilities.getBrowserName())
        .setWdBrowserVersion(capabilities.getBrowserVersion())
        .setWdPlatformName(capabilities.getPlatformName())
        .setWdMeDeviceResolution(capabilities.getMeDeviceResolution())
        .setWdTimeoutsElementAccess(capabilities.getCustomTimeoutElementAccess())
        .setWdTimeoutsPageLoad(capabilities.getCustomTimeoutPageLoad())
        .setWdTimeoutsScript(capabilities.getCustomTimeoutScript())
        .setDryRunning(false); // have to set to false because the field is non primitive
    
    // instantiate WebDriverFunctions and assign
    WebdriverFunctions wdFunc = new WebdriverFunctions(wdProps, buildCapability, props.getDriver(),
        props.getPrintStream(), props.getCallTestHandler(), props.getStorage(),
        props.getUserUploadsCloudPath(), props.getBuildDir());
    interpreter.addFunctions(wdFunc.get());
    
    // add readonly variables
    ReadOnlyVariablesAssigner rva = new ReadOnlyVariablesAssigner(interpreter);
    rva.assignBrowser(buildCapability.getWdBrowserName(), buildCapability.getWdBrowserVersion());
    rva.assignPlatform(buildCapability.getWdPlatformName());
    rva.assignDeviceDimension(props.getVMResolution(), buildCapability.getWdMeDeviceResolution());
    ZwlWdTestProperties.Variables variables = props.getVariables();
    rva.assignBuildVariables(variables.getBuildVariables());
    rva.assignPreferences(variables.getPreferences());
    rva.assignGlobal(variables.getGlobal());
  }
  
  private void validate() {
    ZwlWdTestProperties.Defaults defaults = props.getDefault();
    Preconditions.checkArgument(!Strings.isNullOrEmpty(defaults.getUserDataBucket()),
        "userDataBucket can't be empty");
    Preconditions.checkNotNull(defaults.getDefaultTimeoutElementAccess(),
        "defaultElementAccessTimeout can't be null");
    Preconditions.checkNotNull(defaults.getDefaultTimeoutPageLoad(),
        "defaultPageLoadTimeout can't be null");
    Preconditions.checkNotNull(defaults.getDefaultTimeoutScript(),
        "defaultScriptTimeout can't be null");
    Preconditions.checkNotNull(defaults.getDefaultTimeoutNewWindow(),
        "defaultNewWindowTimeout can't be null");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(defaults.getElementShotDir()),
        "elementShotDir can't be empty");
  
    ZwlWdTestProperties.Capabilities capabilities = props.getCapabilities();
    Preconditions.checkArgument(!Strings.isNullOrEmpty(capabilities.getBrowserName()),
        "browserName can't be empty");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(capabilities.getBrowserVersion()),
        "browserVersion can't be empty");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(capabilities.getPlatformName()),
        "platformName can't be empty");
    Preconditions.checkNotNull(capabilities.getCustomTimeoutElementAccess(),
        "customElementAccessTimeout can't be null");
    Preconditions.checkNotNull(capabilities.getCustomTimeoutPageLoad(),
        "customPageLoadTimeout can't be null");
    Preconditions.checkNotNull(capabilities.getCustomTimeoutScript(),
        "customScriptTimeout can't be null");
    
    Preconditions.checkNotNull(props.getDriver(), "driver can't be null");
    Preconditions.checkNotNull(props.getPrintStream(), "printStream can't be null");
    Preconditions.checkNotNull(props.getStorage(), "storage can't be null");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(props.getUserUploadsCloudPath()),
        "userUploadsCloudPath can't be empty");
    Preconditions.checkNotNull(props.getBuildDir(), "buildDir can't be null");
  }
}
