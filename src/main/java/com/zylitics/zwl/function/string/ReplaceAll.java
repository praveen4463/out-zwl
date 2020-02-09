package com.zylitics.zwl.function.string;

import java.util.regex.Matcher;

/**
 * Same as {@link Matcher#replaceAll(String)}
 * Description should mention the use of capture groups in string, by name or number.
 */
public class ReplaceAll extends Replace {
  
  @Override
  public String getName() {
    return "replaceAll";
  }
  
  @Override
  String replace(String s, String substring, String replacement) {
    Matcher matcher = getPattern(substring).matcher(s);
    return matcher.replaceAll(replacement);
  }
}
