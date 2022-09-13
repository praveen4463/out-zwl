package com.zylitics.zwl.api;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.constants.Exceptions;
import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.model.ZwlFileTest;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class also shows how to add an external variable into the interpreter.
 */
public class ZwlLangTests {
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
  
  @Tag("zwlFileTest")
  @Test
  void zwlFileTest() throws IOException {
    ZwlApi zwlApi = new ZwlApi(Paths.get("resources/ZwlFileTest.zwl"), Charsets.UTF_8,
        DEFAULT_TEST_LISTENERS);
    List<ZwlFileTest> tests = new ArrayList<>();
    zwlApi.interpret(tests);
    assertEquals(18, tests.size());
    // System.out.println(tests);
    // Make sure the fetched code can run fine
    String miscTestCode = tests.stream()
        .filter(t -> t.getTestName().equals("misc"))
        .findFirst().orElseThrow(() -> new RuntimeException("")).getCode();
    // System.out.println(miscTestCode);
    run(new ZwlApi(miscTestCode, DEFAULT_TEST_LISTENERS));
  }
  
  private void run(ZwlApi zwlApi) {
    // set external variable(s) into de only interpreter. It's important to use dev only so that
    // we can detect ambiguities in grammar early, they will be shown at the top of the test
    // result.
    zwlApi.interpretDevOnly(null, null, null, zwlInterpreter -> {
      // the utility of adding things like exceptions as constants is users don't have
      // copy paste exact string and can choose from suggestions (once we have this functionality
      // in IDE). Also we can shorten the names this way, this is less error prone.
      zwlInterpreter.addReadOnlyVariable("exceptions",
          new MapZwlValue(Exceptions.asMap()));
      // preferences (test values only), note that all global variables such as preferences and
      // globals are loaded from db as string and cast as StringZwlValue. The calling code will
      // convert to the desired type. Emulating that rule below.
      zwlInterpreter.addReadOnlyVariable("preferences", new MapZwlValue(ImmutableMap.of(
          "forLoopMaxIterations", new StringZwlValue(String.valueOf(FOR_LOOP_MAX_ITERATION))
      )));
      // testTools to get custom values required for testing.
      zwlInterpreter.addReadOnlyVariable("testTools", new MapZwlValue(ImmutableMap.of(
          "Nothing", new NothingZwlValue()
      )));
    });
  }
  
  private void run(String file) throws IOException {
    // for these tests, we'll just add console and diagnostic listener.
    ZwlApi zwlApi = new ZwlApi(Paths.get("resources/" + file), Charsets.UTF_8,
        DEFAULT_TEST_LISTENERS);
    run(zwlApi);
  }
}
