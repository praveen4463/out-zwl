package com.zylitics.zwl.api;

import com.google.common.base.Preconditions;
import com.zylitics.zwl.antlr4.ZwlLexer;
import com.zylitics.zwl.antlr4.ZwlParser;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.antlr4.BailErrorStrategy;
import com.zylitics.zwl.antlr4.BailZwlLexer;
import com.zylitics.zwl.interpret.DefaultZwlInterpreter;
import com.zylitics.zwl.exception.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

// To underline offending tokens, see page 155
// To specify common errors from grammar, see error alternatives 9.4.
public final class ZwlApi {
  
  private final ZwlLexer lexer;
  private final List<ANTLRErrorListener> errorListeners;
  
  public ZwlApi(String code, List<ANTLRErrorListener> errorListeners) {
    this(errorListeners, CharStreams.fromString(code));
  }
  
  public ZwlApi(Path file, Charset charset,
                List<ANTLRErrorListener> errorListeners) throws IOException {
    this(errorListeners, CharStreams.fromPath(file, charset));
  }
  
  ZwlApi(List<ANTLRErrorListener> errorListeners, CharStream input) {
    this.errorListeners = errorListeners;
    lexer = new BailZwlLexer(input);
    addErrorListener(lexer);
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
  public void parse() throws RuntimeException {
    getParser().compilationUnit();
  }
  
  void parseDevOnly() throws RuntimeException {
    getParserDevOnly().compilationUnit();
  }
  
  /**
   * Interprets a Zwl webdriver test.
   * @param zwlWdTestProperties An instance of {@link ZwlWdTestProperties}
   * @param zwlInterpreterVisitor Can be used to add functionality to the provided
   *                              {@link ZwlInterpreter}.
   * @throws ZwlLangException Any section in the code that lead to an exception throws an
   *                          {@link ZwlLangException}, for example illegal use of variable or any
   *                          semantic errors. This is the superclass of all specific error types
   *                          such as {@link EvalException}, {@link IndexOutOfRangeException} or
   *                          {@link IllegalIdentifierException}. Caller should check whether the
   *                          exception is of type {@link ZwlLangException}, if so, it can compose
   *                          a meaningful error message and relay back to user, else it may show a
   *                          generic error because that will likely be our own problem rather than
   *                          user code's.
   */
  public void interpret(ZwlWdTestProperties zwlWdTestProperties,
                        @Nullable ZwlInterpreterVisitor zwlInterpreterVisitor) {
    Preconditions.checkNotNull(zwlWdTestProperties, "zwlWdTestProperties can't be null");
  
    interpret(i -> new ZwlWdTestPropsAssigner(i, zwlWdTestProperties).assign(),
        zwlInterpreterVisitor, getParser().compilationUnit());
  }
  
  /**
   * Interprets a Zwl dry run test.
   * @param zwlDryRunProperties An instance of {@link ZwlDryRunProperties}
   * @param zwlInterpreterVisitor Can be used to add functionality to the provided
   *                              {@link ZwlInterpreter}.
   * @throws ZwlLangException Any section in the code that lead to an exception throws an
   *                          {@link ZwlLangException}, for example illegal use of variable or any
   *                          semantic errors. This is the superclass of all specific error types
   *                          such as {@link EvalException}, {@link IndexOutOfRangeException} or
   *                          {@link IllegalIdentifierException}. Caller should check whether the
   *                          exception is of type {@link ZwlLangException}, if so, it can compose
   *                          a meaningful error message and relay back to user, else it may show a
   *                          generic error because that will likely be our own problem rather than
   *                          user code's.
   */
  public void interpret(ZwlDryRunProperties zwlDryRunProperties,
                        @Nullable ZwlInterpreterVisitor zwlInterpreterVisitor) {
    Preconditions.checkNotNull(zwlDryRunProperties, "zwlDryRunProperties can't be null");
    
    interpret(i -> new ZwlDryRunPropsAssigner(i, zwlDryRunProperties).assign(),
        zwlInterpreterVisitor, getParser().compilationUnit());
  }
  
  /**
   * Gets interpreter that additionally detects ambiguity in grammar.
   * Meant to be used for locally running tests. Works for all type of tests including webdriver,
   * dry run and Zwl-lang-test-only.
   */
  void interpretDevOnly(@Nullable ZwlWdTestProperties zwlWdTestProperties,
                        @Nullable ZwlDryRunProperties zwlDryRunProperties,
                        @Nullable ZwlInterpreterVisitor zwlInterpreterVisitor)
      throws ZwlLangException {
    interpret(i -> {
          // either wdTest or dryRun else none
          if (zwlWdTestProperties != null) {
            new ZwlWdTestPropsAssigner(i, zwlWdTestProperties).assign();
          } else if (zwlDryRunProperties != null) {
            new ZwlDryRunPropsAssigner(i, zwlDryRunProperties).assign();
          }
        }, zwlInterpreterVisitor, getParserDevOnly().compilationUnit());
  }
  
  private void interpret(Consumer<DefaultZwlInterpreter> zwlPropertiesAssigner,
                         @Nullable ZwlInterpreterVisitor zwlInterpreterVisitor,
                         ParseTree tree) {
    DefaultZwlInterpreter interpreter = new DefaultZwlInterpreter();
    // assign given properties (should occur first so that internally assigned functions and
    // variables take precedence)
    zwlPropertiesAssigner.accept(interpreter);
    // now visit the visitor
    if (zwlInterpreterVisitor != null) {
      interpreter.accept(zwlInterpreterVisitor);
    }
    // now visit parse tree
    interpreter.visit(tree);
  }
  
  private ZwlParser getParser() {
    ZwlParser parser = new ZwlParser(new CommonTokenStream(lexer));
    parser.setBuildParseTree(true);
    parser.setErrorHandler(new BailErrorStrategy());
    addErrorListener(parser);
    return parser;
  }
  
  private ZwlParser getParserDevOnly() {
    ZwlParser parser = getParser();
    parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
    return parser;
  }
  
  private void addErrorListener(Recognizer<?, ?> recognizer) {
    recognizer.removeErrorListeners();
    errorListeners.forEach(recognizer::addErrorListener);
  }
}
