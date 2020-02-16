package com.zylitics.zwl.api;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.util.VarUtil;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * This test class also shows how to add an external variable into the interpreter.
 */
public class AllTests {
  public static List<ANTLRErrorListener> DEFAULT_TEST_LISTENERS =
      ImmutableList.of(ConsoleErrorListener.INSTANCE, new DiagnosticErrorListener());
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
    // for these tests, we'll just add console and diagnostic listener.
    Main main = new Main("resources/" + file, Charsets.UTF_8, DEFAULT_TEST_LISTENERS);
    // set external variable(s) into de only interpreter. It's important to use dev only so that
    // we can detect ambiguities in grammar early, they will be shown at the top of the test
    // result.
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
