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
    if (errorListeners.size() > 0) {
      Object l = errorListeners.get(0);
      if (l instanceof StoringErrorListener) {
        StoringErrorListener s = (StoringErrorListener) l;
        String fromPos = String.format(POS_FORMAT, s.getLine(), s.getCharPositionInLine());
        String toPos = String.format(POS_FORMAT, s.getLine(), s.getCharPositionInLine() + 1);
        return new ZwlLangException(fromPos, toPos, String.format("%s - %s:%s", s.getMsg(),
            s.getLine(), s.getCharPositionInLine() + 1)); // msg formatted same as
        // DefaultZwlInterpreter's lineNColumn
      }
    }
    throw new RuntimeException("Couldn't derive intended exception after catching a parse error");
  }
}
