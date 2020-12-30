package com.zylitics.zwl.exception;

public class InvalidTypeException extends EvalException {
  
  private static final long serialVersionUID = 3469760246450266966L;
  
  public InvalidTypeException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
}
