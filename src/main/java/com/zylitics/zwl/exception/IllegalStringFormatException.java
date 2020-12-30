package com.zylitics.zwl.exception;

public class IllegalStringFormatException extends EvalException {
  
  private static final long serialVersionUID = 1566793980796058597L;
  
  public IllegalStringFormatException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
}
