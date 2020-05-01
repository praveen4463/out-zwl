package com.zylitics.zwl.webdriver.functions;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.api.ZwlApi;
import com.zylitics.zwl.api.ZwlInterpreterVisitor;
import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.function.AbstractFunction;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.WebdriverFunctions;
import com.zylitics.zwl.webdriver.constants.Browsers;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DryRunTest {
  
  private static final List<ANTLRErrorListener> DEFAULT_TEST_LISTENERS =
      ImmutableList.of(ConsoleErrorListener.INSTANCE, new DiagnosticErrorListener());
  
  @Test
  void dryRunTest() throws Exception {
    BuildCapability buildCapability = new BuildCapability().setDryRunning(true);
    
    WebdriverFunctions webdriverFunctions = new WebdriverFunctions(buildCapability, System.out);
    
    ZwlInterpreterVisitor interpreterVisitor = zwlInterpreter -> {
      // function just needed for dry run tests
      zwlInterpreter.setFunction(new IsVoid());
      
      // These tests don't need browser and platform readonly variable but add them in production
      // if they're found in build caps given by user.
    };
  
    ZwlApi zwlApi = new ZwlApi(Paths.get("resources/webdriver/DryRunTest.zwl"), Charsets.UTF_8,
        DEFAULT_TEST_LISTENERS);
    zwlApi.interpret(webdriverFunctions, interpreterVisitor);
  }
  
  public static class IsVoid extends AbstractFunction {
    
    @Override
    public String getName() {
      return "isVoid";
    }
    
    @Override
    public int minParamsCount() {
      return 1;
    }
    
    @Override
    public int maxParamsCount() {
      return 1;
    }
    
    @Override
    public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                           Supplier<String> lineNColumn) {
      super.invoke(args, defaultValue, lineNColumn);
      return new BooleanZwlValue(args.get(0).getType().equals(Types.VOID));
    }
  }
}
