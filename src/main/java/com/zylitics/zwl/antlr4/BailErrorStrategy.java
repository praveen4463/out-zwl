package com.zylitics.zwl.antlr4;

import org.antlr.v4.runtime.*;

public class BailErrorStrategy extends DefaultErrorStrategy {
  
  @Override
  public void recover(Parser recognizer, RecognitionException e) {
    throw new RuntimeException(e);
  }
  
  @Override
  public Token recoverInline(Parser recognizer) throws RecognitionException {
    InputMismatchException e = new InputMismatchException(recognizer);
    reportInputMismatch(recognizer, e);
    throw new RuntimeException(e);
  }
  
  @Override
  public void sync(Parser recognizer) throws RecognitionException { }
}
