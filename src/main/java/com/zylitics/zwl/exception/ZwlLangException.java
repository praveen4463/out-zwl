package com.zylitics.zwl.exception;

/**
 * <p>Superclass of all exceptions raised in a ZWL program. Exceptions that don't subclass this,</p>
 * <p>should use constructor with 'cause' parameter so that the actual exception could be retrieved.
 * </p>
 * <p>When logging, full exception trace must be logged. When relaying to user, we should compose an
 * exception trace that doesn't reveal class details but still display the entire exception chain.
 * The real cause of the exception should be the deepest cause, thus we should write it in the
 * beginning of the message. Grab the deepest cause, take it's message, associated line no, the
 * exception's name and build a message. After that for each cause in the trace, grab it's message,
 * removing any ZWL specific class names etc and show together with exception name, this way user
 * gets the detail of the problem.</p>
 */
public class ZwlLangException extends RuntimeException {
  
  private static final long serialVersionUID = -8954182436250699689L;
  
  public ZwlLangException(String msg) {
    super(msg);
  }
  
  public ZwlLangException(Throwable cause) {
    super(cause);
  }
  
  /**
   * Should be used when a thrown exception needs to be wrapped in a {@link ZwlLangException}.
   * @param lineNColumn the ZWL program's line and column number where the error encountered
   * @param cause the actual exception thrown.
   */
  public ZwlLangException(String lineNColumn, Throwable cause) {
    super(lineNColumn, cause);
  }
}