package com.zylitics.zwl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
  
  public static List<String> getAllCaptureGroupNamesInRegex(String regexToSearch) {
    List<String> groups = new ArrayList<>();
    // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#groupname
    String regexToFindNames = "\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>";
    Matcher matcher = Pattern.compile(regexToFindNames).matcher(regexToSearch);
    while (matcher.find()) {
      groups.add(matcher.group(1));
    }
    return groups;
  }
  
  public static String getPlatformLineSeparator() {
    return System.getProperty("line.separator");
  }
}
