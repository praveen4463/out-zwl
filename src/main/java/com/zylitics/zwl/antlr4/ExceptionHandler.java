package com.zylitics.zwl.antlr4;

import com.zylitics.zwl.exception.ZwlLangException;
import org.antlr.v4.runtime.RecognitionException;

import java.util.List;

class ExceptionHandler {
  
  private static final String POS_FORMAT = "%s:%s";
  
  private final List<?> errorListeners;
  
  ExceptionHandler(List<?> errorListeners) {
    this.errorListeners = errorListeners;
  }
  
  ZwlLangException handle(RecognitionException e) {
    String fromPos = null;
    String toPos = null;
    if (errorListeners.size() > 0) {
      Object l = errorListeners.get(0);
      if (l instanceof StoringErrorListener) {
        StoringErrorListener s = (StoringErrorListener) l;
        fromPos = String.format(POS_FORMAT, s.getLine(), s.getCharPositionInLine());
        toPos = String.format(POS_FORMAT, s.getLine(), s.getCharPositionInLine() + 1);
      }
    }
    return new ZwlLangException(fromPos, toPos, e);
  }
}
