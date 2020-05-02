package com.zylitics.zwl.api;

import com.google.common.base.Preconditions;
import com.zylitics.zwl.function.debugging.Print;
import com.zylitics.zwl.function.debugging.PrintF;
import com.zylitics.zwl.interpret.DefaultZwlInterpreter;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.WebdriverFunctions;

class ZwlDryRunPropsAssigner {
  
  private final DefaultZwlInterpreter interpreter;
  
  private final ZwlDryRunProperties props;
  
  ZwlDryRunPropsAssigner(DefaultZwlInterpreter interpreter,
                         ZwlDryRunProperties props) {
    this.interpreter = interpreter;
    this.props = props;
  }
  
  void assign() {
    validate();
  
    // replace functions
    interpreter.replaceFunction(new Print(props.getPrintStream()));
    interpreter.replaceFunction(new PrintF(props.getPrintStream()));
  
    // instantiate BuildCapability
    ZwlDryRunProperties.Capabilities capabilities = props.getCapabilities();
    
    BuildCapability buildCapability = new BuildCapability().setDryRunning(true);
    // instantiate WebDriverFunctions and assign
    WebdriverFunctions wdFunc = new WebdriverFunctions(buildCapability, props.getPrintStream());
    interpreter.addFunctions(wdFunc.get());
  
    // add readonly variables
    ReadOnlyVariablesAssigner rva = new ReadOnlyVariablesAssigner(interpreter);
    rva.assignBrowser(capabilities.getBrowserName(), capabilities.getBrowserVersion());
    rva.assignPlatform(capabilities.getPlatformName());
    ZwlDryRunProperties.Variables variables = props.getVariables();
    rva.assignPreferences(variables.getPreferences());
    rva.assignGlobal(variables.getGlobal());
  }
  
  private void validate() {
    Preconditions.checkNotNull(props.getPrintStream(), "printStream can't be null");
  }
}
