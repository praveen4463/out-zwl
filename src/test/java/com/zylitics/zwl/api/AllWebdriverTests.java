package com.zylitics.zwl.api;

public enum AllWebdriverTests {
  
  ACTIONS_TEST ("ActionsTest.zwl"),
  BASIC_WD_TEST ("BasicWdTest.zwl"),
  COLOR_TEST ("ColorTest.zwl"),
  CONTEXT_TEST ("ContextTest.zwl"),
  COOKIE_TEST ("CookieTest.zwl"),
  DOCUMENT_TEST ("DocumentTest.zwl"),
  E_INTERACTION_KEYS_TEST ("EInteractionKeysTest.zwl"),
  E_INTERACTION_TEST ("EInteractionTest.zwl"),
  ELEMENT_RETRIEVAL_TEST ("ElementRetrievalTest.zwl"),
  ELEMENT_STATE_TEST ("ElementStateTest.zwl"),
  NAVIGATION_TEST ("NavigationTest.zwl"),
  PROMPTS_TEST ("PromptsTest.zwl"),
  SELECT_TEST ("SelectTest.zwl"),
  SET_FILES_TEST ("SetFilesTest.zwl"),
  STORAGE_TEST ("StorageTest.zwl"),
  TIMEOUT_TEST ("TimeoutTest.zwl"),
  UNTIL_TEST ("UntilTest.zwl"),
  ELEMENT_CAPTURE_TEST ("ElementCaptureTest.zwl"),
  EDITOR_TABS_TEST ("EditorTabsTest.zwl"),
  EXPLORER_TEST ("ExplorerTest.zwl"),
  EDITOR_TEST ("EditorTest.zwl"),
  BUILD_VARS_TEST ("BuildVarsTest.zwl"),
  GLOBAL_VARS_TEST ("GlobalVarsTest.zwl"),
  DRY_CONFIG_TEST ("DryConfigTest.zwl"),
  BUILD_CONFIG_TEST ("BuildConfigTest.zwl"),
  BUILD_CAPABILITIES_TEST ("BuildCapabilitiesTest.zwl"),
  MISC_TEST("MiscTest.zwl"),
  SNAPSHOT_MATCH_TEST("SnapshotMatchingTest.zwl"),
  CALL_TEST("CallTest.zwl");
  
  private final String file;
  
  AllWebdriverTests(String file) {
    this.file = file;
  }
  
  public String getFile() { return file; }
}
