package com.zylitics.zwl.webdriver.functions;

import com.google.cloud.storage.StorageOptions;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.util.CollectionUtil;
import com.zylitics.zwl.util.IOUtil;
import com.zylitics.zwl.webdriver.*;
import com.zylitics.zwl.api.ZwlApi;
import com.zylitics.zwl.api.ZwlInterpreterVisitor;
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
  
  private static final List<ANTLRErrorListener> DEFAULT_TEST_LISTENERS =
      ImmutableList.of(ConsoleErrorListener.INSTANCE, new DiagnosticErrorListener());
  
  private static final String ELEMENT_SHOT_DIR = "element-shot";
  private static final String ELEMENT_SHOT_NAME = "small-select";
  private static final String USER_DATA_BUCKET = "zl-user-data";
  private static final Boolean BROWSER_START_MAXIMIZE = true;
  
  final PrintStream printStream = System.out;
  
  Path fakeBuildDir;
  RemoteWebDriver driver;
  WebdriverFunctions wdFunctions;
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
    for (ZwlTests t : ZwlTests.values()) {
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
      zwlApi.interpret(wdFunctions, interpreterVisitor);
      
      if (t == ZwlTests.ELEMENT_CAPTURE_TEST) {
        // element capture requires I/O access to check created file.
        assertElementCapture();
      }
    }
  }
  
  @Tag("basic")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void basicWdTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.BASIC_WD_TEST.getFile());
  }
  
  @Tag("actions")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void actionsTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.ACTIONS_TEST.getFile());
  }
  
  @Tag("color")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void colorTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.COLOR_TEST.getFile());
  }
  
  @Tag("context")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void contextTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.CONTEXT_TEST.getFile());
  }
  
  @Tag("cookie")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void cookieTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.COOKIE_TEST.getFile());
  }
  
  @Tag("document")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void documentTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.DOCUMENT_TEST.getFile());
  }
  
  @Tag("einteraction")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void eInteractionTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.E_INTERACTION_TEST.getFile());
  }
  
  @Tag("setfiles")
  @Tag("slow")
  @Tag("io")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void setFilesTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.SET_FILES_TEST.getFile());
  }
  
  @Tag("einteractionkeys")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void eInteractionKeysTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.E_INTERACTION_KEYS_TEST.getFile());
  }
  
  @Tag("elementretrieval")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void elementRetrievalTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.ELEMENT_RETRIEVAL_TEST.getFile());
  }
  
  @Tag("elementstate")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void elementStateTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.ELEMENT_STATE_TEST.getFile());
  }
  
  @Tag("navigation")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void navigationTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.NAVIGATION_TEST.getFile());
  }
  
  @Tag("prompts")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void promptsTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.PROMPTS_TEST.getFile());
  }
  
  @Tag("select")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void selectTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.SELECT_TEST.getFile());
  }
  
  @Tag("storage")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void storageTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.STORAGE_TEST.getFile());
  }
  
  @Tag("timeout")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void timeoutTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.TIMEOUT_TEST.getFile());
  }
  
  @Tag("until")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void untilTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.UNTIL_TEST.getFile());
  }
  
  @Tag("elemcapture")
  @ParameterizedTest
  @EnumSource(value = Browsers.class)
  void elementScreenCaptureTest(Browsers browser) throws Exception {
    run(browser, ZwlTests.ELEMENT_CAPTURE_TEST.getFile());
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
  
  private void run(Browsers browser, String file) throws Exception {
    Assumptions.assumeFalse(shouldSkip(browser), "Skipped");
    setup(browser);
    ZwlApi zwlApi = new ZwlApi(Paths.get("resources/webdriver/" + file), Charsets.UTF_8,
        DEFAULT_TEST_LISTENERS);
    zwlApi.interpret(wdFunctions, interpreterVisitor);
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
    if (buildCapability.isWdBrwStartMaximize()) {
      driver.manage().window().maximize();
    }
    
    fakeBuildDir = Paths.get(Configuration.SYS_DEF_TEMP_DIR, "build-111111");
    
    if (Files.isDirectory(fakeBuildDir)) {
      IOUtil.deleteDir(fakeBuildDir);
    }
    Files.createDirectory(fakeBuildDir);
    
    wdFunctions = new WebdriverFunctions(wdProps,
        buildCapability,
        driver,
        printStream,
        StorageOptions.getDefaultInstance().getService(),
        "11021/uploads",
        fakeBuildDir);
    
    interpreterVisitor = zwlInterpreter -> {
      // readonly variables
      // browser detail, not adding version as it's not required and given in these tests.
      Map<String, ZwlValue> browserDetail = ImmutableMap.of(
          "name", new StringZwlValue(browser.getAlias())
          // add version when required
      );
      zwlInterpreter.setReadOnlyVariable("browser", new MapZwlValue(browserDetail));
      
      // test specific only
      Map<String, ZwlValue> staticSite = ImmutableMap.of(
          "urlPrefix", new StringZwlValue("http://static.wditp.zylitics.io/html/")
      );
      zwlInterpreter.setReadOnlyVariable("staticSite", new MapZwlValue(staticSite));
      
      // single valued
      zwlInterpreter.setReadOnlyVariable("platform",
          new StringZwlValue(buildCapability.getWdPlatformName()));
    };
  }
  
  @AfterEach
  void tearDown() {
    if (driver == null) {
      return;
    }
    int hold = Integer.getInteger("holdWdCloseFor", 0); // in seconds.
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
    b.setWdPlatformName(System.getProperty("os"));
    b.setWdBrwStartMaximize(BROWSER_START_MAXIMIZE);
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
}
