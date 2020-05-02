package com.zylitics.zwl.api;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.MapZwlValue;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.interpret.DefaultZwlInterpreter;
import com.zylitics.zwl.webdriver.constants.Browsers;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * assigns all non-internal read only variables which values are supplied by tests.
 */
class ReadOnlyVariablesAssigner {
  
  private final ZwlInterpreter interpreter;
  
  ReadOnlyVariablesAssigner(ZwlInterpreter interpreter) {
    this.interpreter = interpreter;
  }
  
  void assignBrowser(@Nullable String browser, @Nullable String version) {
    if (Strings.isNullOrEmpty(browser)) {
      return;
    }
    String browserAlias = Browsers.valueByName(browser).getAlias();
    ImmutableMap.Builder<String, ZwlValue> b = new ImmutableMap.Builder<>();
    b.put("name", new StringZwlValue(browserAlias));
    if (!Strings.isNullOrEmpty(version)) {
      b.put("version", new StringZwlValue(version));
    }
    interpreter.addReadOnlyVariable("browser", new MapZwlValue(b.build()));
  }
  
  void assignPlatform(@Nullable String platform) {
    if (Strings.isNullOrEmpty(platform)) {
      return;
    }
    interpreter.addReadOnlyVariable("platform", new StringZwlValue(platform));
  }
  
  void assignPreferences(@Nullable Map<String, String> preferences) {
    assignFromMap(DefaultZwlInterpreter.PREFERENCES_KEY, preferences);
  }
  
  void assignGlobal(@Nullable Map<String, String> global) {
    assignFromMap("global", global);
  }
  
  private void assignFromMap(String identifier, @Nullable Map<String, String> keyValue) {
    if (keyValue == null || keyValue.size() == 0) {
      return;
    }
    interpreter.addReadOnlyVariable(identifier,
        new MapZwlValue(keyValue.entrySet().stream().collect(
            Collectors.toMap(Map.Entry::getKey, e -> new StringZwlValue(e.getValue())))));
  }
}
