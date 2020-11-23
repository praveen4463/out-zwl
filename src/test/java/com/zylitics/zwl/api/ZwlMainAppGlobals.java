package com.zylitics.zwl.api;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ZwlMainAppGlobals {
  
  Map<String, String> get() {
    return new ImmutableMap.Builder<String, String>()
        .put("ROLE_TREE", "*[role='tree']")
        .put("TEST_ID_VERSION_TREE_ITEM", "*[data-testid='VERSION-treeItem']")
        .put("TEST_ID_TEST_TREE_ITEM", "*[data-testid='TEST-treeItem']")
        .put("TEST_ID_FILE_TREE_ITEM", "*[data-testid='FILE-treeItem']")
        .put("TEST_ID_VERSION_TREE_ITEM_NAME", "*[data-testid='VERSION-treeItemName']")
        .put("TEST_ID_TEST_TREE_ITEM_NAME", "*[data-testid='TEST-treeItemName']")
        .put("TEST_ID_FILE_TREE_ITEM_NAME", "*[data-testid='FILE-treeItemName']")
        .put("TEST_ID_TEST_TREE_ITEM_CONTENT", "*[data-testid='TEST-treeItemContent']")
        .put("TEST_ID_FILE_TREE_ITEM_CONTENT", "*[data-testid='FILE-treeItemContent']")
        .put("TEST_ID_BREADCRUMB_TAB_PANEL", "[data-testid='tab-panel-breadcrumb']")
        .put("TEST_ID_LATEST_VERSION", "*[data-testid='latestVersion']")
        .put("BUTTON_DELETE_TREE_ITEM", "button[aria-label='Delete']")
        .put("BUTTON_RENAME_TREE_ITEM", "button[aria-label='Rename']")
        .put("BUTTON_NEW_VERSION_TREE_ITEM", "button[aria-label='Create New Version']")
        .put("BUTTON_UNLOAD", "button[title='Unload File From Workspace']")
        .put("BUTTON_NEW_TEST", "button[aria-label='Create New Test']")
        .put("BUTTON_DELETE_DIALOG", "*[role='dialog'] button[aria-label='delete']")
        .put("BUTTON_UNLOAD_DIALOG", "*[role='dialog'] button[aria-label='unload']")
        .put("BREADCRUMB_FORMAT", "%s > %s > %s")
        .put("TITLE_TAB_FORMAT", "%s/%s/%s")
        .put("FOCUSED_TAB_COLOR_PROP", "border-bottom-color")
        .put("FOCUSED_TAB_COLOR", "#03dac4")
        .put("ROLE_TAB_BUTTON", "button[role='tab']")
        .put("TEST_ID_CLOSE_ICON", "*[data-testid='closeIcon']")
        .put("TEST_ID_TAB", "*[data-testid='tab']")
        .put("SELECTOR_TAB_TEXT_NODE", "*[data-testid='tab'] :first-child")
        .put("FONT_TEMP_TAB", "italic")
        .put("EXPLORER", "*[data-testid='explorer']")
        .put("BUTTON_NEW_FILE", "button[aria-label='New File']")
        .put("EXPLORER_ERROR", "*[data-testid='explorerError']")
        .put("3URL_LOCAL", "http://localhost:3000/") // kept to see non identifiers work
        .put("URL_LOCAL", "http://localhost:3000/?project=1&file=1")
        .put("LINE_COL_FORMAT", "Ln %s, Col %s")
        .put("SELECTOR_HINT", "ul.CodeMirror-hints")
        .put("SAMPLE_CODE", "# This code is not typed by hand\n" +
            "chromeHomePageOffer = findElements(\"div#gbw a.gb_Nd\", true)\n" +
            "if size(chromeHomePageOffer) > 0 {\n" +
            "  click(chromeHomePageOffer[0])\n" +
            "  click(\"input[name='q']\")") // no closing bracket, will be placed by editor
        .put("TEST_ID_LINE_COL", "lineColContainer")
        .put("TEST_ID_EDITOR", "codeEditor")
        .put("TEST_ID_EDITOR_STATUS", "editorStatusMessage")
        .put("TEST_ID_EDITOR_OUTPUT", "editorOutput")
        .put("TEST_ID_EDITOR_OUTPUT_BUILD_BTN", "outputPanelRunBuild")
        .put("TEST_ID_EDITOR_OUTPUT_PARSE_BTN", "outputPanelParse")
        .put("TEST_ID_EDITOR_OUTPUT_DRY_BTN", "outputPanelDryRun")
        .put("COLOR_EXP_ITEM_ERROR", "#f63a56")
        .put("EDITOR_OUTPUT_PASS_STATUS_MSG_FORMAT", "%s passed, expand for output")
        .put("EDITOR_OUTPUT_FAIL_STATUS_MSG_FORMAT", "%s failed, expand for error")
        .put("RUN_TYPE_NAME_PARSE", "parsing")
        .put("RUN_TYPE_NAME_DRY", "dry run")
        .put("RUN_TYPE_NAME_BUILD", "build run")
        .put("EDIT_ICON", "*[aria-label='edit menu']")
        .put("BUILD_VAR_MENU_TEXT", "Build Variables")
        .put("GLOBAL_VAR_MENU_TEXT", "Global Variables")
        .put("DRY_CONFIG_MENU_TEXT", "Dry Run Config")
        .put("BUILD_CONFIG_MENU_TEXT", "Build Config")
        .put("BUILD_CAPS_MENU_TEXT", "Build Capabilities")
        .put("VARS_ADD_BUTTON", "button[aria-label='add']")
        .build();
  }
}
