package com.zylitics.zwl.api;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ZwlMainAppGlobals {
  
  Map<String, String> get() {
    return new ImmutableMap.Builder<String, String>()
        .put("ROLE_TREE", "*[role='tree']")
        .put("ROLE_TREE_ITEM", "*[role='treeitem']")
        .put("TEST_ID_VERSION_TREE_ITEM", "*[data-testid='VERSION-treeItemName']")
        .put("TEST_ID_TEST_TREE_ITEM", "*[data-testid='TEST-treeItemName']")
        .put("TEST_ID_FILE_TREE_ITEM", "*[data-testid='FILE-treeItemName']")
        .put("TEST_ID_BREADCRUMB_TAB_PANEL", "[data-testid='tab-panel-breadcrumb']")
        .put("BREADCRUMB_FORMAT", "%s > %s > %s")
        .put("TITLE_TAB_FORMAT", "%s/%s/%s")
        .put("FOCUSED_TAB_COLOR_PROP", "border-bottom-color")
        .put("FOCUSED_TAB_COLOR", "#03dac4")
        .put("ROLE_TAB_BUTTON", "button[role='tab']")
        .put("TEST_ID_CLOSE_ICON", "*[data-testid='closeIcon']")
        .put("TEST_ID_TAB", "*[data-testid='tab']")
        .put("SELECTOR_TAB_TEXT_NODE", "*[data-testid='tab'] :first-child")
        .put("FONT_TEMP_TAB", "italic")
        .put("URL_LOCAL", "http://localhost:3000/")
        .build();
  }
}
