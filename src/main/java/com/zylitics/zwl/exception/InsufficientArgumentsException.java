package com.zylitics.zwl.exception;

public class InsufficientArgumentsException extends EvalException {
  
  private static final long serialVersionUID = -957380948215727616L;
  
  public InsufficientArgumentsException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
}
