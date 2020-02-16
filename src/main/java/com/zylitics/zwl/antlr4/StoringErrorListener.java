package com.zylitics.zwl.antlr4;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * <p>When a {@link RecognitionException} occurs, stores the arguments passed to
 * {@link org.antlr.v4.runtime.ANTLRErrorListener#syntaxError}. All arguments can be accessed using
 * respective get methods.</p>
 * <p>A new instance of this listener should be instantiated every time a new parse request
 * comes in.</p>
 */
public class StoringErrorListener extends BaseErrorListener {
  
  private Recognizer<?, ?> recognizer;
  private Object offendingSymbol;
  private int line;
  private int charPositionInLine;
  private String msg;
  private RecognitionException e;
  
  @Override
  public void syntaxError(Recognizer<?, ?> recognizer,
                          Object offendingSymbol,
                          int line,
                          int charPositionInLine,
                          String msg,
                          RecognitionException e) {
    this.recognizer = recognizer;
    this.offendingSymbol = offendingSymbol;
    this.line = line;
    this.charPositionInLine = charPositionInLine;
    this.msg = msg;
    this.e = e;
  }
  
  public Recognizer<?, ?> getRecognizer() {
    return recognizer;
  }
  
  public Object getOffendingSymbol() {
    return offendingSymbol;
  }
  
  public int getLine() {
    return line;
  }
  
  public int getCharPositionInLine() {
    return charPositionInLine;
  }
  
  public String getMsg() {
    return msg;
  }
  
  public RecognitionException getE() {
    return e;
  }
}
