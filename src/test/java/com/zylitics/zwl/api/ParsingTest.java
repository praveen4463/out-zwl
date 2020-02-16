package com.zylitics.zwl.api;

import com.zylitics.zwl.antlr4.StoringErrorListener;
import com.zylitics.zwl.util.StringUtil;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.NoViableAltException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Parsing errors occur during the parse tree creation, before interpretation. Thus we can't use
 * ZWL for testing parser. We must use java.
 * TODO: There are a few things we need to improve in parsers.
 *  1. Currently we're not using any recovery methods, understand how it works and see if it's
 *     worth to implement them.
 *  2. We're bailing out on first error so that program can fail fast rather than later when
 *     recovery fails, this has some downsides like the error reporting happens one by one, we
 *     can't use recovery methods and due to that some precise errors need to be converted into
 *     generate InputMismatchError like extraneous input and missing token etc.
 *  3. Test more and see if our current error reporting is easy enough for users to fix issues.
 */
public class ParsingTest {
  
  // If a token isn't in our grammar, a token recognition error should should appear. Anytime if
  // the code has a lexical error (i.e unknown tokens) that will be reported first before going into
  // parse phase.
  @Test
  void basicTokenRecognitionErrorTest() {
    String code = "a = @10";
    parseTest(code, "token recognition error at: '@'", LexerNoViableAltException.class);
  }
  
  // Input mismatch occurs when parser detects that the grammar is correct but some tokens are
  // either at wrong place where they shouldn't be or some tokens are missing. When there is
  // single token missing or wrongly placed, it tries to point to it clearly.
  @Test
  void basicInputMismatchErrorTest() {
    String code = "a = {age: 10, phone: 98100099922";
    parseTest(code, "mismatched input '<EOF>' expecting '}'", InputMismatchException.class);
  }
  
  // No viable error occurs when parse can't find match suitable for the input in grammar.
  // It tells that there are no rules found that can be used to match the given input.
  @Test
  void basicNoViableAlternativeErrorTest() {
    String code =  "a+= 1";
    parseTest(code, "no viable alternative at input 'a+'", NoViableAltException.class);
  }
  
  // This should've generated "extraneous input ')' expecting <EOF>" and no exception had we were
  // using recoverInline method, but since we're not recovering and bailing out, the overload at
  // BailErrorStrategy throws InputMismatchException and reports mismatched error so that we get
  // some error rather than no error.
  @Test
  void extraneousTokenErrorTest() {
    String code = "a = ((3 + 2) * 7) * (8/7))";
    parseTest(code, "mismatched input ')' expecting <EOF>", InputMismatchException.class);
  }
  
  // This should've generated "missing ')' at <EOF>" (same as extraneousTokenErrorTest description)
  @Test
  void missingTokenErrorTest() {
    String code = "a = ((3 + 2) * 7) * (8/7";
    parseTest(code, "mismatched input '<EOF>' expecting ')'", InputMismatchException.class);
  }
  
  // Since we bail out from parser on the first error, even if there are more than one errors,
  // we get the first error only. Thus multiple errors will be reported one by one (may need fix).
  @Test
  void moreThanOneInputMismatchErrorTest() {
    StoringErrorListener listener = new StoringErrorListener();
    String code = String.join(StringUtil.getPlatformLineSeparator(),
        "a = {",
        "age = 23",
        "phone = 9810101992",
        "}"
    );
    parseTest(code, "mismatched input '=' expecting ':'", InputMismatchException.class, listener);
    assertEquals(2, listener.getLine()); // first error is at line 2
  }
  
  private void parseTest(String code, String expectedErrorMsg,
                         Class<? extends Throwable> expectedException) {
    parseTest(code, expectedErrorMsg, expectedException, new StoringErrorListener());
  }
  
  private void parseTest(String code, String expectedErrorMsg,
                         Class<? extends Throwable> expectedException,
                         StoringErrorListener listener) {
    boolean gotExpected = false;
    try {
      List<ANTLRErrorListener> listeners = Collections.singletonList(listener);
      Main main = new Main(code, listeners);
      main.parseDevOnly();
    } catch (RuntimeException r) {
      gotExpected = matchCause(r, expectedException);
    }
    assertTrue(gotExpected);
    assertNotNull(listener.getMsg());
    assertTrue(listener.getLine() > 0 && listener.getCharPositionInLine() > 0);
    if (expectedErrorMsg.startsWith("/") && expectedErrorMsg.endsWith("/")) {
      String regex = expectedErrorMsg.substring(1, expectedErrorMsg.length() - 1);
      assertTrue(listener.getMsg().matches(regex));
    } else {
      assertEquals(expectedErrorMsg, listener.getMsg());
    }
  }
  
  private boolean matchCause(Throwable t, Class<? extends Throwable> causeToMatch) {
    while (t.getCause() != null) {
      t = t.getCause();
      if (t.getClass() == causeToMatch) {
        return true;
      }
    }
    return false;
  }
}
