package com.zylitics.zwl.api;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.util.CollectionUtil;
import com.zylitics.zwl.util.IOUtil;
import com.zylitics.zwl.webdriver.*;
import com.zylitics.zwl.datatype.MapZwlValue;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.webdriver.constants.Browsers;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.annotation.Nullable;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Webdriver tests meant to run locally.
 * It contains tests for individual zwl files and a test for running all of them together in the
 * same session as a suite. Whenever need to run all of them, always use {@link #allTests}
 * method.
 * Don't run all methods of this class, use tag name while running (either mvn or IDE)
 */
public class WebdriverTests {
  
  // Note that ConsoleErrorListener does prints line and message in the console, just find output
  // by 'line'
  private static final List<ANTLRErrorListener> DEFAULT_TEST_LISTENERS =
      ImmutableList.of(ConsoleErrorListener.INSTANCE, new DiagnosticErrorListener());
  
  private static final String ELEMENT_SHOT_DIR = "element-shot";
  private static final String ELEMENT_SHOT_NAME = "small-select";
  private static final String USER_DATA_BUCKET = "zl-user-data";
  private static final Boolean BROWSER_START_MAXIMIZE = true;
  private static final String EXAMPLE_USER_UPLOAD_CLOUD_PATH = "11021/uploads";
  
  final PrintStream printStream = System.out;
  
  Path fakeBuildDir;
  RemoteWebDriver driver;
  ZwlWdTestProperties zwlWdTestProperties;
  ZwlInterpreterVisitor interpreterVisitor;
  
  @BeforeEach
  void checks() {
    Preconditions.checkNotNull(System.getProperty("os"), "os should be set as system property");
  }
  
  // This executes all zwl tests in a single webdriver session, clearing cookies, windows before
  // running each test.
  @Tag("all")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void allTests(Browsers browser) throws Exception {
    Assumptions.assumeFalse(shouldSkip(browser), "Skipped");
    setup(browser);
    for (AllWebdriverTests t : AllWebdriverTests.values()) {
      //-------------------------------Sanitize start-----------------------------------------------
      // delete any open windows and leave just one with about:blank, delete all cookies before
      // reading new test
      List<String> winHandles = new ArrayList<>(driver.getWindowHandles());
      for (int i = 0; i < winHandles.size(); i++) {
        driver.switchTo().window(winHandles.get(i));
        if (i < winHandles.size() - 1) {
          driver.close();
        }
      }
      if (BROWSER_START_MAXIMIZE) {
        driver.manage().window().maximize();
      }
      driver.get("about:blank"); // "about local scheme" can be given to 'get' as per webdriver spec
      driver.manage().deleteAllCookies(); // delete all cookies
      
      //-------------------------------Sanitize end-------------------------------------------------
      String file = t.getFile();
      printStream.println("Reading and executing from " + file);
      ZwlApi zwlApi = new ZwlApi(Paths.get("resources/webdriver/" + t.getFile()), Charsets.UTF_8,
          DEFAULT_TEST_LISTENERS);
      zwlApi.interpretDevOnly(zwlWdTestProperties, null, interpreterVisitor);
      
      if (t == AllWebdriverTests.ELEMENT_CAPTURE_TEST) {
        // element capture requires I/O access to check created file.
        assertElementCapture();
      }
    }
  }
  
  @Tag("basic")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void basicWdTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.BASIC_WD_TEST.getFile());
  }
  
  @Tag("actions")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void actionsTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.ACTIONS_TEST.getFile());
  }
  
  @Tag("color")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void colorTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.COLOR_TEST.getFile());
  }
  
  @Tag("context")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void contextTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.CONTEXT_TEST.getFile());
  }
  
  @Tag("cookie")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void cookieTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.COOKIE_TEST.getFile());
  }
  
  @Tag("document")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void documentTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.DOCUMENT_TEST.getFile());
  }
  
  @Tag("einteraction")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void eInteractionTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.E_INTERACTION_TEST.getFile());
  }
  
  @Tag("setfiles")
  @Tag("slow")
  @Tag("io")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void setFilesTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.SET_FILES_TEST.getFile());
  }
  
  @Tag("einteractionkeys")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void eInteractionKeysTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.E_INTERACTION_KEYS_TEST.getFile());
  }
  
  @Tag("elementretrieval")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void elementRetrievalTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.ELEMENT_RETRIEVAL_TEST.getFile());
  }
  
  @Tag("elementstate")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void elementStateTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.ELEMENT_STATE_TEST.getFile());
  }
  
  @Tag("navigation")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void navigationTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.NAVIGATION_TEST.getFile());
  }
  
  @Tag("prompts")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void promptsTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.PROMPTS_TEST.getFile());
  }
  
  @Tag("select")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void selectTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.SELECT_TEST.getFile());
  }
  
  @Tag("storage")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void storageTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.STORAGE_TEST.getFile());
  }
  
  @Tag("timeout")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void timeoutTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.TIMEOUT_TEST.getFile());
  }
  
  @Tag("until")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void untilTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.UNTIL_TEST.getFile());
  }
  
  @Tag("elemcapture")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void elementScreenCaptureTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.ELEMENT_CAPTURE_TEST.getFile());
    assertElementCapture();
  }
  
  private void assertElementCapture() throws Exception {
    Path elemShotDir = fakeBuildDir.resolve(ELEMENT_SHOT_DIR);
    if (!Files.isDirectory(elemShotDir)) {
      return;
    }
    try (DirectoryStream<Path> pathStream = Files.newDirectoryStream(elemShotDir, "*.png")) {
      Iterator<Path> pathIterator = pathStream.iterator();
      assertTrue(pathIterator.hasNext());
      Path elemShotPath = pathIterator.next();
      String name = elemShotPath.getFileName().toString();
      assertTrue(name.startsWith(ELEMENT_SHOT_NAME));
      assertTrue(Files.size(elemShotPath) >= 1000); // should be atleast 1 KB
      assertFalse(pathIterator.hasNext());
    }
  }
  
  @Tag("tabs")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void tabsTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.EDITOR_TABS_TEST.getFile());
  }
  
  @Tag("explorer")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void explorerTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.EXPLORER_TEST.getFile());
  }
  
  @Tag("editor")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void editorTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.EDITOR_TEST.getFile());
  }
  
  @Tag("buildVars")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void buildVarsTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.BUILD_VARS_TEST.getFile());
  }
  
  @Tag("globalVars")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void globalVarsTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.GLOBAL_VARS_TEST.getFile());
  }
  
  @Tag("dryConfig")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void dryConfigTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.DRY_CONFIG_TEST.getFile());
  }
  
  @Tag("buildConfig")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void buildConfigTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.BUILD_CONFIG_TEST.getFile());
  }
  
  @Tag("buildCaps")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void buildCapsTest(Browsers browser) throws Exception {
    run(browser, AllWebdriverTests.BUILD_CAPABILITIES_TEST.getFile());
  }
  
  private void run(Browsers browser, String file) throws Exception {
    Assumptions.assumeFalse(shouldSkip(browser), "Skipped");
    setup(browser);
    ZwlApi zwlApi = new ZwlApi(Paths.get("resources/webdriver/" + file), Charsets.UTF_8,
        DEFAULT_TEST_LISTENERS);
    zwlApi.interpretDevOnly(zwlWdTestProperties, null, interpreterVisitor);
  }
  
  private boolean shouldSkip(Browsers browser) {
    String toSkip = System.getProperty("skipBrowser");
    if (Strings.isNullOrEmpty(toSkip)) {
      return false;
    }
    List<String> browsersToSkip = Arrays.asList(toSkip.split(","));
    // can given either ie, IE or internet explorer in skip list.
    return browsersToSkip.stream().anyMatch(b ->
        browser.name().equalsIgnoreCase(b) || browser.getName().equalsIgnoreCase(b));
  }
  
  private void setup(Browsers browser) throws Exception {
    BuildCapability buildCapability = getBuildCapability(browser);
    APICoreProperties.Webdriver wdProps = getDefaultWDProps();
    Capabilities caps = getCapabilities(buildCapability, wdProps);
    
    if (browser.equals(Browsers.CHROME)) {
      ChromeOptions chrome = new ChromeOptions();
      chrome.merge(caps);
      driver = new ChromeDriver(ChromeDriverService.createDefaultService(), chrome);
    } else if (browser.equals(Browsers.FIREFOX)) {
      FirefoxOptions ff = new FirefoxOptions();
      String logLevel = System.getProperty("webdriver.firefox.loglevel");
      if (logLevel != null) {
        ff.setLogLevel(FirefoxDriverLogLevel.fromString(logLevel));
      }
      ff.merge(caps);
      driver = new FirefoxDriver(GeckoDriverService.createDefaultService(), ff);
    } else if (browser.equals(Browsers.IE)) {
      InternetExplorerOptions ie = new InternetExplorerOptions();
      ie.merge(caps);
      ie.withAttachTimeout(Duration.ofMillis(5000));
      ie.waitForUploadDialogUpTo(Duration.ofMillis(5000));
      driver = new InternetExplorerDriver(InternetExplorerDriverService.createDefaultService(), ie);
    } else {
      throw new RuntimeException("can't run local build on " + browser.getName());
    }
    
    // do some actions on driver based on build capabilities
    if (BROWSER_START_MAXIMIZE) {
      driver.manage().window().maximize();
    }
    
    fakeBuildDir = Paths.get(Configuration.SYS_DEF_TEMP_DIR, "build-111111");
    
    if (Files.isDirectory(fakeBuildDir)) {
      IOUtil.deleteDir(fakeBuildDir);
    }
    Files.createDirectory(fakeBuildDir);
    
    zwlWdTestProperties = getZwlWdTestProperties(wdProps, buildCapability);
    
    interpreterVisitor = zwlInterpreter -> {
      // readonly variables
      // test specific only
      Map<String, ZwlValue> staticSite = ImmutableMap.of(
          "urlPrefix", new StringZwlValue("http://static.wditp.zylitics.io/html/")
      );
      zwlInterpreter.addReadOnlyVariable("staticSite", new MapZwlValue(staticSite));
    };
  }
  
  @AfterEach
  void tearDown() {
    if (driver == null) {
      return;
    }
    long hold = Integer.getInteger("holdWdCloseFor", 0); // in seconds.
    if (hold > 0) {
      try {
        Thread.sleep(hold * 1000);
      } catch (InterruptedException ignored) {
        // ignore
      }
    }
    driver.quit();
  }
  
  private APICoreProperties.Webdriver getDefaultWDProps() {
    APICoreProperties.Webdriver wd = new APICoreProperties.Webdriver();
    wd.setUserDataBucket(USER_DATA_BUCKET);
    wd.setDefaultTimeoutElementAccess(30_000);
    wd.setDefaultTimeoutPageLoad(60_000);
    wd.setDefaultTimeoutScript(30_000);
    wd.setDefaultTimeoutNewWindow(10_000);
    wd.setElementShotDir(ELEMENT_SHOT_DIR);
    return wd;
  }
  
  private BuildCapability getBuildCapability(Browsers browser) {
    BuildCapability b = new BuildCapability();
    b.setWdBrowserName(browser.getName());
    b.setWdBrowserVersion("11.11"); // any version just to satisfy validation
    b.setWdPlatformName(System.getProperty("os"));
    // all timeouts in build caps are initialized with -1 in db if no value is given.
    b.setWdTimeoutsPageLoad(-1);
    b.setWdTimeoutsElementAccess(-1);
    b.setWdTimeoutsScript(-1);
    return b;
  }
  
  private Capabilities getCapabilities(BuildCapability buildCapability,
                                       APICoreProperties.Webdriver wdProps) {
    MutableCapabilities caps = new MutableCapabilities();
    
    caps.setCapability(CapabilityType.PLATFORM_NAME, buildCapability.getWdPlatformName());
    
    caps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, false);
    
    caps.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "eager");
  
    Map<String, Object> timeouts = new HashMap<>(CollectionUtil.getInitialCapacity(3));
    timeouts.put("script",
        new Configuration().getTimeouts(wdProps, buildCapability, TimeoutType.JAVASCRIPT));
    timeouts.put("pageLoad",
        new Configuration().getTimeouts(wdProps, buildCapability, TimeoutType.PAGE_LOAD));
    caps.setCapability("timeouts", timeouts);

    caps.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, "ignore");
    caps.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, "ignore");
    
    return caps;
  }
  
  // it's kind of weird we're creating Defaults and Capabilities of ZwlWdTestProperties from
  // APICoreProperties.Webdriver and BuildCapability, and ZwlWdTestPropsAssigner will then
  // do the exact opposite but this is happening just for this test. For parent application code
  // (application that will use zwl),
  // APICoreProperties.Webdriver and BuildCapability will be much larger objects and totally
  // different from what is in here. So, Defaults and Capabilities are actually just a part of
  // APICoreProperties.Webdriver and BuildCapability in parent application.
  private ZwlWdTestProperties getZwlWdTestProperties(APICoreProperties.Webdriver wdProps,
                                                     BuildCapability buildCapability) {
    return new ZwlWdTestProperties() {
      @Override
      public RemoteWebDriver getDriver() {
        return driver;
      }
  
      @Override
      public PrintStream getPrintStream() {
        return printStream;
      }
  
      @Override
      public Storage getStorage() {
        return StorageOptions.getDefaultInstance().getService();
      }
  
      @Override
      public String getUserUploadsCloudPath() {
        return EXAMPLE_USER_UPLOAD_CLOUD_PATH;
      }
  
      @Override
      public Path getBuildDir() {
        return fakeBuildDir;
      }
  
      @Override
      public Defaults getDefault() {
        return new Defaults() {
          @Override
          public String getUserDataBucket() {
            return wdProps.getUserDataBucket();
          }
  
          @Override
          public Integer getDefaultTimeoutElementAccess() {
            return wdProps.getDefaultTimeoutElementAccess();
          }
  
          @Override
          public Integer getDefaultTimeoutPageLoad() {
            return wdProps.getDefaultTimeoutPageLoad();
          }
  
          @Override
          public Integer getDefaultTimeoutScript() {
            return wdProps.getDefaultTimeoutScript();
          }
  
          @Override
          public Integer getDefaultTimeoutNewWindow() {
            return wdProps.getDefaultTimeoutNewWindow();
          }
  
          @Override
          public String getElementShotDir() {
            return wdProps.getElementShotDir();
          }
        };
      }
  
      @Override
      public Capabilities getCapabilities() {
        return new Capabilities() {
          @Override
          public String getBrowserName() {
            return buildCapability.getWdBrowserName();
          }
  
          @Override
          public String getBrowserVersion() {
            return buildCapability.getWdBrowserVersion();
          }
  
          @Override
          public String getPlatformName() {
            return buildCapability.getWdPlatformName();
          }
  
          @Override
          public Integer getCustomTimeoutElementAccess() {
            return buildCapability.getWdTimeoutsElementAccess();
          }
  
          @Override
          public Integer getCustomTimeoutPageLoad() {
            return buildCapability.getWdTimeoutsPageLoad();
          }
  
          @Override
          public Integer getCustomTimeoutScript() {
            return buildCapability.getWdTimeoutsScript();
          }
        };
      }
  
      @Override
      public Variables getVariables() {
        return new Variables() {
          @Nullable
          @Override
          public Map<String, String> getBuildVariables() {
            return null;
          }
  
          @Nullable
          @Override
          public Map<String, String> getPreferences() {
            return null;
          }
  
          @Nullable
          @Override
          public Map<String, String> getGlobal() {
            return new ZwlMainAppGlobals().get();
          }
        };
      }
    };
  }
}
