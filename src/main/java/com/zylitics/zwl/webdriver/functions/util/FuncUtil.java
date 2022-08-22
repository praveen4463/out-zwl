package com.zylitics.zwl.webdriver.functions.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.Locale;

class FuncUtil {
  
  static final String ARGS_KEY_SUFFIX = "_args";
  
  static final String RETURN_VAL_KEY_SUFFIX = "_return";
  
  static String getUniqueKeyFromTestPath(String testPath) throws IllegalArgumentException {
    List<String> path = Splitter.on('/').omitEmptyStrings().trimResults().splitToList(testPath);
    if (path.size() < 2) {
      throw new IllegalArgumentException("Invalid test path given");
    }
    // Unique key is made of file and test name
    String fileName = normalizeNamesForKey(path.get(0));
    String testName = normalizeNamesForKey(path.get(1));
    return fileName + "_" + testName;
  }
  
  private static String normalizeNamesForKey(String name) {
    String invalidChars = "[^A-Za-z0-9_]";
    return name.replaceAll(invalidChars, "_").toLowerCase(Locale.US);
  }
  
  static String getFuncArgsKey(String funcUniqueKey) {
    return String.format("%s%s", funcUniqueKey, ARGS_KEY_SUFFIX);
  }
  
  static String getFuncReturnKey(String funcUniqueKey) {
    return String.format("%s%s", funcUniqueKey, RETURN_VAL_KEY_SUFFIX);
  }
}
