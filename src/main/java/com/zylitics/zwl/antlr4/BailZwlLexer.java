package com.zylitics.zwl.antlr4;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.LexerNoViableAltException;

public class BailZwlLexer extends ZwlLexer {
  
  public BailZwlLexer(CharStream input) {
    super(input);
  }
  
  @Override
  public void recover(LexerNoViableAltException e) {
    throw new ExceptionHandler(getErrorListeners()).handle(e);
  }
}
