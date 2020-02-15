package com.zylitics.zwl.api;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.util.VarUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

/**
 * This test class also shows how to add an external variable into the interpreter.
 */
public class AllTests {
  // Important, don't make more than 1000, under test.
  public static final int FOR_LOOP_MAX_ITERATION = 1000;
  
  @Test
  void basicSyntaxTest() throws IOException {
    run("BasicSyntaxTest.zwl");
  }
  
  @Test
  void typeConversionTest() throws IOException {
    run("TypeConversionTest.zwl");
  }
  
  @Test
  void syntaxValidationTest() throws IOException {
    run("SyntaxValidationTest.zwl");
  }
  
  @Test
  void builtInFunctionTest() throws IOException {
    run("BuiltInFunctionTest.zwl");
  }
  
  @Test
  void operationsTest() throws IOException {
    run("OperationsTest.zwl");
  }
  
  @Test
  void advanceSyntaxTest() throws IOException {
    run("AdvanceSyntaxTest.zwl");
  }
  
  private void run(String file) throws IOException {
    Main main = new Main("resources/" + file, Charsets.UTF_8);
    // set external variable(s) into interpreter.
    
    main.interpretDevOnly(zwlInterpreter -> {
      // exceptions
      zwlInterpreter.setReadOnlyVariable("exceptions",
          new MapZwlValue(VarUtil.getExceptionsVars()));
      // preferences (test values only)
      zwlInterpreter.setReadOnlyVariable("preferences", new MapZwlValue(ImmutableMap.of(
          "forLoopMaxIterations", new DoubleZwlValue(FOR_LOOP_MAX_ITERATION)
      )));
      // testTools to get custom values required for testing.
      zwlInterpreter.setReadOnlyVariable("testTools", new MapZwlValue(ImmutableMap.of(
          "Nothing", new NothingZwlValue()
      )));
    });
  }
}
