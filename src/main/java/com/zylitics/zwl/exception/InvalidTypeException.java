package com.zylitics.zwl.exception;

public class InvalidTypeException extends EvalException {
  
  private static final long serialVersionUID = 3469760246450266966L;
  
  public InvalidTypeException(String msg) {
    super(msg);
  }
  
  public InvalidTypeException(String msg, Throwable cause) {super(msg, cause);}
}
