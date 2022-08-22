package com.zylitics.zwl.webdriver.functions.util;

import com.zylitics.zwl.datatype.NothingZwlValue;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Call extends AbstractWebdriverFunction {
  
  private final Consumer<String> callTestHandler;
  
  private final Map<String, ZwlValue> _global;
  
  public Call(APICoreProperties.Webdriver wdProps,
                  BuildCapability buildCapability,
                  RemoteWebDriver driver,
                  PrintStream printStream,
                  Consumer<String> callTestHandler,
                  Map<String, ZwlValue> _global) {
    super(wdProps, buildCapability, driver, printStream);
    
    this.callTestHandler = callTestHandler;
    this._global = _global;
  }
  
  @Override
  public String getName() {
    return "call";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return 2;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
    
    int argsSize = args.size();
    
    if (argsSize == 0) {
      throw unexpectedEndOfFunctionOverload(0);
    }
    
    try {
      String testPath = args.get(0).toString();
      String uniqueKeyFromTestPath = FuncUtil.getUniqueKeyFromTestPath(testPath);
      if (argsSize == 2) {
        ZwlValue funcArgs = args.get(1);
        setFuncArgsToGlobal(uniqueKeyFromTestPath, funcArgs);
      }
      callTestHandler.accept(testPath);
      
      // Check for a return value
      String returnValKey = FuncUtil.getFuncReturnKey(uniqueKeyFromTestPath);
      ZwlValue returnVal = new NothingZwlValue();
      if (_global.containsKey(returnValKey)) {
        returnVal = _global.get(returnValKey);
        // Clear this key from _global
        _global.remove(returnValKey);
      }
      
      // Clear argsKey from _global as well
      removeFuncArgsFromGlobal(uniqueKeyFromTestPath);
      
      return returnVal;
    } catch (IllegalArgumentException i) {
      // Only handle IllegalArgumentException that may only be thrown during validation of everything
      // involved here. Rest will be handled when running the given test/function.
      throw new EvalException(fromPos.get(), toPos.get(), withLineNCol(i.getMessage()));
    }
  }
  
  private void setFuncArgsToGlobal(String funcUniqueKey, ZwlValue funcArgs)
      throws IllegalArgumentException {
    // Make sure args are either Map or List
    if (!(funcArgs.getListValue().isPresent() || funcArgs.getMapValue().isPresent())) {
      throw new IllegalArgumentException("Argument type must be either a Map or List");
    }
    
    String argsKey = FuncUtil.getFuncArgsKey(funcUniqueKey);
    _global.put(argsKey, funcArgs);
  }
  
  private void removeFuncArgsFromGlobal(String funcUniqueKey) {
    String argsKey = FuncUtil.getFuncArgsKey(funcUniqueKey);
    _global.remove(argsKey);
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return new NothingZwlValue();
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.OBJECT;
  }
  
  @Override
  protected boolean doNotExpandListToArguments() {
    return true;
  }
}