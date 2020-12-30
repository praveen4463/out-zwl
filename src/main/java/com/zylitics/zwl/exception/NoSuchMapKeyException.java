package com.zylitics.zwl.exception;

public class NoSuchMapKeyException extends EvalException {
  
  private static final long serialVersionUID = -2080522235735549259L;
  
  public NoSuchMapKeyException(String fromPos, String toPos, String msg) {
    super(fromPos, toPos, msg);
  }
}
