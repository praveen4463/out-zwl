package com.zylitics.zwl.antlr4;

import org.antlr.v4.runtime.*;

public class BailErrorStrategy extends DefaultErrorStrategy {
  
  @Override
  public void recover(Parser recognizer, RecognitionException e) {
    throw new RuntimeException(e);
  }
  
  // Doing this per the book so that inline recovery doesn't happen as we want to bail out on first
  // error. This method is called when an unexpected symbol is encountered during an inline match
  // operation such as some symbol is placed as an extra or some symbol is missing. In the
  // implement of this, system tries to either delete or put the symbol to recover from problem
  // but if it fails, it raises an extraneous or missing symbol error.
  // Since we are not using the implementation, no error is generated on those situation, to get
  // some error, we use input mismatch error.
  
  // I could also copy the implementation of this method and find out when it's an extra or missing
  // token condition but not doing that for now as the logic may change in future updates. For now
  // reporting input mismatch in those situations is ok.
  @Override
  public Token recoverInline(Parser recognizer) throws RecognitionException {
    InputMismatchException e;
    if (nextTokensContext == null) {
      e = new InputMismatchException(recognizer);
    } else {
      e = new InputMismatchException(recognizer, nextTokensState, nextTokensContext);
    }
    reportInputMismatch(recognizer, e);
    throw new RuntimeException(e);
  }
  
  @Override
  public void sync(Parser recognizer) throws RecognitionException { }
}
