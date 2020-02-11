package com.zylitics.zwl.api;

import com.zylitics.zwl.antlr4.ZwlLexer;
import com.zylitics.zwl.antlr4.ZwlParser;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.antlr4.BailErrorStrategy;
import com.zylitics.zwl.antlr4.BailZwlLexer;
import com.zylitics.zwl.interpret.DefaultZwlInterpreter;
import com.zylitics.zwl.interpret.Function;
import com.zylitics.zwl.exception.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

// To underline offending tokens, see page 155
// To specify common errors from grammar, see error alternatives 9.4.
public final class Main {
  
  private final ZwlLexer lexer;
  
  public Main(String code) {
    lexer = getLexer(code);
  }
  
  public Main(InputStream codeStream, Charset charset, long streamLength) throws IOException {
    lexer = getLexer(codeStream, charset, streamLength);
  }
  
  public Main(String fileName, Charset charset) throws IOException {
    lexer = getLexer(fileName, charset);
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
    getParser(lexer).compilationUnit();
  }
  
  /**
   * Interprets and executes the given code.
   * @param interpreterVisitor The interpreter visitor has access to public interpreter methods
   *                           It can be used to add multiple {@link Function} implementations and
   *                           read-only variables. For custom functionality, implementations of
   *                           {@link Function} should be given, any existing implementation will
   *                           be overwritten by any custom one. This is useful for dry-running vs
   *                           actual run of code when different implementations of various
   *                           {@link Function}s may be required.
   * @throws EvalException Any section in the code that lead to an exception throws an
   *                       {@link EvalException}, for example illegal use of variable or any
   *                       semantic errors. This is the superclass of all specific error types
   *                       such as {@link IndexOutOfRangeException} or
   *                       {@link IllegalIdentifierException}. Caller should check whether the
   *                       exception is of type {@link EvalException} and if so, it can relay
   *                       that back to user, else it may show a generic error because that will
   *                       likely be our own problem rather than user code's.
   */
  public void interpret(ZwlInterpreterVisitor interpreterVisitor) throws EvalException {
    ParseTree tree = getParser(lexer).compilationUnit();
    DefaultZwlInterpreter interpreter = new DefaultZwlInterpreter();
    interpreter.accept(interpreterVisitor);
    interpreter.visit(tree);
  }
  
  /**
   * Gets interpreter that additionally detects ambiguity in grammar.
   */
  void interpretDevOnly(@Nullable ZwlInterpreterVisitor interpreterVisitor)
      throws EvalException {
    ZwlParser parser = getParser(lexer);
    parser.removeErrorListeners();
    parser.addErrorListener(new DiagnosticErrorListener());
    parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
  
    DefaultZwlInterpreter interpreter = new DefaultZwlInterpreter();
    if (interpreterVisitor != null) {
      interpreter.accept(interpreterVisitor);
    }
    interpreter.visit(parser.compilationUnit());
  }
  
  private ZwlLexer getLexer(String code) {
    return new BailZwlLexer(CharStreams.fromString(code));
  }
  
  private ZwlLexer getLexer(InputStream codeStream, Charset charset, long streamLength)
      throws IOException {
    return new BailZwlLexer(CharStreams.fromStream(codeStream, charset, streamLength));
  }
  
  private ZwlLexer getLexer(String fileName, Charset charset) throws IOException {
    return new BailZwlLexer(CharStreams.fromFileName(fileName, charset));
  }
  
  private ZwlParser getParser(ZwlLexer lexer) {
    ZwlParser parser = new ZwlParser(new CommonTokenStream(lexer));
    parser.setBuildParseTree(true);
    parser.setErrorHandler(new BailErrorStrategy());
    return parser;
  }
}
