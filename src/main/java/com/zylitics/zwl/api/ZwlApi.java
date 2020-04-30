package com.zylitics.zwl.api;

import com.zylitics.zwl.antlr4.ZwlLexer;
import com.zylitics.zwl.antlr4.ZwlParser;
import com.zylitics.zwl.constants.Exceptions;
import com.zylitics.zwl.datatype.MapZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.antlr4.BailErrorStrategy;
import com.zylitics.zwl.antlr4.BailZwlLexer;
import com.zylitics.zwl.interpret.DefaultZwlInterpreter;
import com.zylitics.zwl.interpret.Function;
import com.zylitics.zwl.exception.*;
import com.zylitics.zwl.webdriver.WebdriverFunctions;
import com.zylitics.zwl.webdriver.constants.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// To underline offending tokens, see page 155
// To specify common errors from grammar, see error alternatives 9.4.
public final class ZwlApi {
  
  private final ZwlLexer lexer;
  private final List<ANTLRErrorListener> errorListeners;
  
  public ZwlApi(String code, List<ANTLRErrorListener> errorListeners) {
    this.errorListeners = errorListeners;
    lexer = getLexer(code);
  }
  
  @SuppressWarnings("unused")
  public ZwlApi(InputStream codeStream, Charset charset, long streamLength,
                List<ANTLRErrorListener> errorListeners) throws IOException {
    this.errorListeners = errorListeners;
    lexer = getLexer(codeStream, charset, streamLength);
  }
  
  public ZwlApi(Path file, Charset charset,
                List<ANTLRErrorListener> errorListeners) throws IOException {
    this.errorListeners = errorListeners;
    lexer = getLexer(file, charset);
  }
  
  /**
   * Parses the given code, any lexer or parser level exception will be thrown encapsulated in
   * a RuntimeException's cause field. The message field contains the text that can be shown to
   * user. Note that no interpretation of input is done at this level but just a ParseTree is
   * created.
   * @throws RuntimeException When an exception occurs during parsing such as 'Invalid token'.
   * Actual exception which are subclasses of {@link org.antlr.v4.runtime.RecognitionException} is
   * encapsulated as cause field. Caller should check whether the cause field contain this exception
   * and if so, may relay back to user.
   */
  @SuppressWarnings("unused")
  public void parse() throws RuntimeException {
    getParser(lexer).compilationUnit();
  }
  
  void parseDevOnly() throws RuntimeException {
    getParserDevOnly(lexer).compilationUnit();
  }
  
  // remove it
  public void interpret(ZwlInterpreterVisitor interpreterVisitor) throws ZwlLangException {
    ParseTree tree = getParser(lexer).compilationUnit();
    DefaultZwlInterpreter interpreter = new DefaultZwlInterpreter();
    interpreter.accept(interpreterVisitor);
    interpreter.visit(tree);
  }
  
  /**
   * Interprets and executes the given code.
   * @param webdriverFunctions Webdriver functions to be added into the set of functions for the
   *                           interpreter.
   * @param interpreterVisitor The interpreter visitor has access to public interpreter methods
   *                           It can be used to add multiple {@link Function} implementations and
   *                           read-only variables. For custom functionality, implementations of
   *                           {@link Function} should be given, any existing implementation will
   *                           be overwritten by any custom one.
   * @throws ZwlLangException Any section in the code that lead to an exception throws an
   *                       {@link ZwlLangException}, for example illegal use of variable or any
   *                       semantic errors. This is the superclass of all specific error types
   *                       such as {@link EvalException}, {@link IndexOutOfRangeException} or
   *                       {@link IllegalIdentifierException}. Caller should check whether the
   *                       exception is of type {@link ZwlLangException} and if so, it can compose
   *                       a meaningful error message and relay back to user, else it may show a
   *                       generic error because that will likely be our own problem rather than
   *                       user code's.
   */
  public void interpret(WebdriverFunctions webdriverFunctions,
                        @Nullable ZwlInterpreterVisitor interpreterVisitor)
      throws ZwlLangException {
    ParseTree tree = getParser(lexer).compilationUnit();
    DefaultZwlInterpreter interpreter = new DefaultZwlInterpreter();
    interpreter.setFunctions(webdriverFunctions.get());
    addReadOnlyVariables(interpreter);
  
    if (interpreterVisitor != null) {
      interpreter.accept(interpreterVisitor);
    }
    interpreter.visit(tree);
  }
  
  private void addReadOnlyVariables(DefaultZwlInterpreter interpreter) {
    Map<String, ZwlValue> exceptions =
        new HashMap<>(Exceptions.asMap());
    exceptions.putAll(com.zylitics.zwl.webdriver.constants.Exceptions.asMap());
    interpreter.setReadOnlyVariable("exceptions",
        new MapZwlValue(Collections.unmodifiableMap(exceptions)));
  
    interpreter.setReadOnlyVariable("colors", new MapZwlValue(Colorz.asMap()));
    
    interpreter.setReadOnlyVariable("keys", new MapZwlValue(Keyz.asMap()));
  
    interpreter.setReadOnlyVariable("by", new MapZwlValue(By.asMap()));
  
    interpreter.setReadOnlyVariable("timeouts", new MapZwlValue(Timeouts.asMap()));
  }
  
  /**
   * Gets interpreter that additionally detects ambiguity in grammar.
   */
  void interpretDevOnly(@Nullable ZwlInterpreterVisitor interpreterVisitor)
      throws ZwlLangException {
    ZwlParser parser = getParserDevOnly(lexer);
    DefaultZwlInterpreter interpreter = new DefaultZwlInterpreter();
    if (interpreterVisitor != null) {
      interpreter.accept(interpreterVisitor);
    }
    interpreter.visit(parser.compilationUnit());
  }
  
  private ZwlLexer getLexer(String code) {
    ZwlLexer lexer = new BailZwlLexer(CharStreams.fromString(code));
    addErrorListener(lexer);
    return lexer;
  }
  
  private ZwlLexer getLexer(InputStream codeStream, Charset charset, long streamLength)
      throws IOException {
    ZwlLexer lexer = new BailZwlLexer(CharStreams.fromStream(codeStream, charset, streamLength));
    addErrorListener(lexer);
    return lexer;
  }
  
  private ZwlLexer getLexer(Path file, Charset charset) throws IOException {
    ZwlLexer lexer = new BailZwlLexer(CharStreams.fromPath(file, charset));
    addErrorListener(lexer);
    return lexer;
  }
  
  private ZwlParser getParser(ZwlLexer lexer) {
    ZwlParser parser = new ZwlParser(new CommonTokenStream(lexer));
    parser.setBuildParseTree(true);
    parser.setErrorHandler(new BailErrorStrategy());
    addErrorListener(parser);
    return parser;
  }
  
  private ZwlParser getParserDevOnly(ZwlLexer lexer) {
    ZwlParser parser = getParser(lexer);
    parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
    return parser;
  }
  
  private void addErrorListener(Recognizer<?, ?> recognizer) {
    recognizer.removeErrorListeners();
    errorListeners.forEach(recognizer::addErrorListener);
  }
}
