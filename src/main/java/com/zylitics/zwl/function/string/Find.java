package com.zylitics.zwl.function.string;

/**
 * Finds a subsequence of the input sequence that matches the pattern. It returns true only if some
 * subsequence from input matches the regex pattern.
 */
public class Find extends Matches {
  
  @Override
  public String getName() {
    return "find";
  }
  
  @Override
  protected boolean matches(String s, String regex) {
    return getPattern(regex).matcher(s).find();
  }
}
