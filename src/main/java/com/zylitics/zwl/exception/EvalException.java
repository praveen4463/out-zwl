package com.zylitics.zwl.exception;

public class EvalException extends RuntimeException {
  
  private static final long serialVersionUID = -2737215880481504769L;
  
  public EvalException(String msg) {
    super(msg);
  }
  
  public EvalException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
