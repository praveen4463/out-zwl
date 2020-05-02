package com.zylitics.zwl.api;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.function.AbstractFunction;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
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
    ZwlApi zwlApi = new ZwlApi(Paths.get("resources/webdriver/DryRunTest.zwl"), Charsets.UTF_8,
        DEFAULT_TEST_LISTENERS);
    zwlApi.interpretDevOnly(null, getZwlDryRunProperties(), z -> z.addFunction(new IsVoid()));
  }
  
  // have to instantiate all fields as they can't be null, their properties could be.
  private ZwlDryRunProperties getZwlDryRunProperties() {
    return new ZwlDryRunProperties() {
      @Override
      public PrintStream getPrintStream() {
        return System.out;
      }
    
      @Override
      public Capabilities getCapabilities() {
        return new Capabilities() {
          @Nullable
          @Override
          public String getBrowserName() {
            return null;
          }
        
          @Nullable
          @Override
          public String getBrowserVersion() {
            return null;
          }
        
          @Nullable
          @Override
          public String getPlatformName() {
            return null;
          }
        };
      }
    
      @Override
      public Variables getVariables() {
        return new Variables() {
          @Nullable
          @Override
          public Map<String, String> getPreferences() {
            return null;
          }
        
          @Nullable
          @Override
          public Map<String, String> getGlobal() {
            return null;
          }
        };
      }
    };
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
