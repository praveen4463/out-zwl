package com.zylitics.zwl.webdriver.functions;

import com.zylitics.zwl.datatype.*;
import com.zylitics.zwl.exception.InvalidTypeException;
import com.zylitics.zwl.exception.ZwlLangException;
import com.zylitics.zwl.function.AbstractFunction;
import com.zylitics.zwl.util.CollectionUtil;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.Configuration;
import com.zylitics.zwl.webdriver.TimeoutType;
import com.zylitics.zwl.webdriver.constants.ByType;
import com.zylitics.zwl.webdriver.locators.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.remote.UselessFileDetector;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.io.PrintStream;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/*
 * Important consideration for webdriver functions:
 * 1. each function should invoke webdriver related methods within handleWDExceptions context so
 *    that any webdriver exception could be wrapped inside ZwlLangException which is what Zwl
 *    expect, all other exceptions are treated as system related exceptions and not relayed to user.
 * 2. Any implementing function may save state but that will remain same for the lifetime of the
      build, if that function is used further in build, the same state will be used because
      functions are instantiated only once per build. Thus seldom use state, if you really have to,
      don't forget to reset it on each invocation of the same function.
 */
public abstract class AbstractWebdriverFunction extends AbstractFunction {
  
  protected final APICoreProperties.Webdriver wdProps;
  
  protected final BuildCapability buildCapability;
  
  protected final RemoteWebDriver driver;
  
  protected final PrintStream printStream;
  
  protected final WebDriver.Window window;
  
  protected final WebDriver.Options options;
  
  protected final WebDriver.TargetLocator targetLocator;
  
  // Accepts everything needed to run the webdriver function.
  // In this abstract class we accept RemoteWebDriver rather than WebDriver so that we could
  // access other interfaces that RemoteWebDriver implements like JavascriptExecutor, Interactive,
  // TakesScreenshot etc without a cast.
  protected AbstractWebdriverFunction(APICoreProperties.Webdriver wdProps,
                            BuildCapability buildCapability,
                            RemoteWebDriver driver,
                            PrintStream printStream) {
    this.wdProps = wdProps;
    this.buildCapability = buildCapability;
    this.driver = driver;
    this.printStream = printStream;
    // little ugly due to the support for dry run later during development where driver is null
    if (driver != null) {
      options = driver.manage();
      window = options.window();
      targetLocator = driver.switchTo();
    } else {
      options = null;
      window = null;
      targetLocator = null;
    }
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    
    // write function execution update after invoking super's so we don't write for illegal calls
    writeFunctionExecutionUpdate(args);
    return _void;
  }
  
  /**
   * Writes function execution update as build output. This method should be overridden by functions
   * that wish to write a custom function execution update.
   * @param args arguments that function takes.
   */
  // we're going to add all the arguments if function takes any, and finally when it's going to
  // be saved, runner should trim if it's too long, and if not it can remain intact.
  protected void writeFunctionExecutionUpdate(List<ZwlValue> args) {
    String toWrite;
    if (args.size() == 0) {
      toWrite = "Executing function " + getName();
    } else {
      toWrite = String.format("Executing function %s with arguments %s", getName(),
          args.stream().map(Objects::toString).collect(Collectors.joining(",")));
    }
    writeBuildOutput(toWrite);
  }
  
  protected void writeBuildOutput(String text) {
    printStream.println(withLineNCol(text));
  }
  
  /*
   Sometimes when something provided by test can't properly run in the browser, it goes into an
   infinite wait, causing halt in driver > browser and in turn selenium > driver. I'm implementing
   a few things so that users aren't getting cryptic errors such as 'Error communicating with the remote browser.'
   or 'Supplied function might have stalled' that are thrown from RemoteWebDriver and FluentWait
   respectively.
   
   Script and page load timeouts are defined at webdriver level. If current command uses a script to
   work or a url is being loaded, driver uses these timeouts and bail out if they expire. Upon timeout
   expire, driver server that is listening on some port, shuts down.
   
   Selenium uses Netty to connect to driver server and have a read timeout of 60 seconds which
   means if it doesn't get a response to one of the commands within that time, a netty timeout
   occurs.
   
   When a command is run using explicit wait, it usually check on the driver to see the status of the
   command result. But when driver has shut down (due to bad commands running infinitely in server),
   the first check waits for a response. This wait is ended only when the give wait timeout is reached.
   This cases java.util.concurrent.TimeoutException and selenium returns 'Supplied function might have stalled'
   message to user which is really cryptic.
   
   On the other hand, if the wait timeout is larger than or equal to the netty read timeout, exception
   is relayed to RemoteWebDriver.execute which in turn throws a UnreachableBrowserException (assuming
   that the driver server has shut down) that is also cryptic for user.
   
   When some command like openUrl runs, that uses timeout at webdriver level, and the timeout is more
   than netty timeout, a UnreachableBrowserException exception first occurs and thrown. If its lesser,
   test waits for receiving a response from selenium which is waiting for driver, and waits until
   netty timeout before throwing error.
   
   When driver server shuts down and you attempt to interact with it, selenium waits until netty
   timeout before throwing UnreachableBrowserException exception.
   
   We're checking in this function, if one of those errors are throw, convert them to a generic
   error that is more understandable and related to a timeout so that users can know current command
   has something wrong as it's taking more time to finish.
   
   TODO: We've to fix problems that cause these errors. Look at our scripts and find out why text
    finding doesn't work with pattern like '/(\\w|.)+@[a-z0-9]+.[a-z]+/ and why openUrl hangs with
    some of localhost and ngrok urls.
    Also keep a watch on these errors.
   */
  public <V> V handleWDExceptions(Callable<V> code) {
    try {
      return code.call();
    } catch (ZwlLangException z) {
      throw z;
    } catch (WebDriverException wdEx) {
      WebDriverException modifiedEx = wdEx;
      if (wdEx instanceof UnreachableBrowserException || (wdEx.getMessage() != null
          && wdEx.getMessage().contains("stalled") && wdEx instanceof TimeoutException)) {
        String message = String.format("A timeout has occurred while executing '%s'.", getName());
        modifiedEx = new WebDriverException(message, wdEx.getCause()); // np if cause is null
      }
      // wrap webdriver exceptions and throw.
      throw new ZwlLangException(fromPos.get(), toPos.get(), lineNColumn.get(), modifiedEx);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
  
  protected ZwlValue tryGetStringZwlValue(String val) {
    return val == null ? new NothingZwlValue() : new StringZwlValue(val);
  }
  
  protected ZwlValue tryGetStringZwlValues(Collection<String> values) {
    return new ListZwlValue(values.stream()
        .map(s -> s == null ? new NothingZwlValue() : new StringZwlValue(s))
        .collect(Collectors.toList()));
  }
  
  protected WindowType parseWindowType(String winType) {
    try {
      return Enum.valueOf(WindowType.class, winType);
    } catch (IllegalArgumentException ae) {
      // if user gives an invalid type, don't throw exception but choose a type that suits most.
      return WindowType.TAB;
    }
  }
  
  public BuildCapability getBuildCapability() {
    return buildCapability;
  }
  
  protected boolean isValidElemInternalId(String elemInternalId) {
    // Either 72BDB3367CEFA9029FF357CFF54B920A_element_51 or 4a572a82-4134-43ee-9f4d-9db24fa83bb4
    return elemInternalId.matches("[a-zA-Z0-9]{32}_element_[0-9]+")
        || elemInternalId
        .matches("[a-zA-Z0-9]{8}-?[a-zA-Z0-9]{4}-?[a-zA-Z0-9]{4}-?[a-zA-Z0-9]{4}-?[a-zA-Z0-9]{12}");
  }
  
  protected boolean isValidElementId(ZwlValue elementId) {
    Optional<String> optionalStringElemId = elementId.getStringValue();
    if (optionalStringElemId.isPresent()) {
      return isValidElemInternalId(optionalStringElemId.get());
    }
    Optional<Map<String, ZwlValue>> optionalMapElemId = elementId.getMapValue();
    if (!optionalMapElemId.isPresent()) {
      return false;
    }
    Map<String, ZwlValue> mapElemId = optionalMapElemId.get();
    ZwlValue id = mapElemId.get("id");
    if (id == null) {
      return false;
    }
    optionalStringElemId = id.getStringValue();
    return optionalStringElemId.filter(this::isValidElemInternalId).isPresent();
  }
  
  private RemoteWebElement buildWebElementUsingInternalId(String elemInternalId) {
    if (!isValidElemInternalId(elemInternalId)) {
      throw new ZwlLangException(fromPos.get(), toPos.get(), withLineNCol("Given string "
          + elemInternalId + " is not a valid elementInternalId"));
    }
    RemoteWebElement element = new RemoteWebElement();
    element.setParent(driver);
    element.setId(elemInternalId);
    element.setFileDetector(new UselessFileDetector());
    return element;
  }
  
  protected RemoteWebElement getWebElementUsingElemId(ZwlValue elemId) {
    Optional<String> optionalStringElemId = elemId.getStringValue();
    if (optionalStringElemId.isPresent()) {
      return buildWebElementUsingInternalId(optionalStringElemId.get());
    }
    Optional<Map<String, ZwlValue>> optionalMapElemId = elemId.getMapValue();
    if (!optionalMapElemId.isPresent()) {
      throw new ZwlLangException(fromPos.get(), toPos.get(), withLineNCol("Given value: " + elemId +
          " is not a valid elementId"));
    }
    Map<String, ZwlValue> mapElemId = optionalMapElemId.get();
    return buildWebElementUsingInternalId(
        mapElemId.get("id").getStringValue().orElseThrow(() ->
            new IllegalArgumentException("element's 'id' property must be a String"))
    );
  }
  
  protected RemoteWebElement renewOnStaleEx(ZwlValue elGoneStale,
                                            StaleElementReferenceException staleEx) {
    // When elGoneStale is not a map type elementId, we can't find it again because
    // we don't know how was it found.
    Map<String, ZwlValue> staledElementId = elGoneStale.getMapValue().orElseThrow(() -> staleEx);
    String byType = staledElementId.get("byType").getStringValue().orElseThrow(() ->
        new IllegalArgumentException("element's 'byType' property must be a String"));
    String using = staledElementId.get("using").getStringValue().orElseThrow(() ->
        new IllegalArgumentException("element's 'using' property must be a String"));
    // when we are finding a staled element again, we will wait for it to become available,
    // regardless of the originally provided 'wait' preference.
    RemoteWebElement staledElementFoundAgain;
    By staledElBy = getBy(ByType.valueOf(byType), using);
    if (staledElementId.get("from") == null) {
      staledElementFoundAgain = findElement(driver, staledElBy, true);
    } else {
      staledElementFoundAgain = findElement(staledElementId.get("from"), staledElBy, true);
    }
    // rewrite the stale id with fresh one so that future queries see the fresh id. I hate mutating
    // things like this but it would be very tricky to find the variable that is holding this value
    // and reassign it the new one.
    staledElementId.put("id", new StringZwlValue(staledElementFoundAgain.getId()));
    return staledElementFoundAgain;
  }
  
  protected RemoteWebElement findElement(ZwlValue fromElement, By by, boolean wait) {
    WebDriverWait waitAccess = getWait(TimeoutType.ELEMENT_ACCESS,
        "waiting for element to render after staling");
    return waitAccess.until(d -> findElement0(fromElement, by, wait));
  }
  
  private RemoteWebElement findElement0(ZwlValue fromElement, By by, boolean wait) {
    try {
      return findElement(getWebElementUsingElemId(fromElement), by, wait);
    } catch (StaleElementReferenceException staleEx) {
      // fromElement contains stale reference.
      renewOnStaleEx(fromElement, staleEx);
      return null;
    }
  }
  
  protected List<RemoteWebElement> findElements(ZwlValue fromElement, By by, boolean wait) {
    WebDriverWait waitAccess = getWait(TimeoutType.ELEMENT_ACCESS,
        "waiting for element to render after staling");
    return waitAccess.until(d -> findElements0(fromElement, by, wait));
  }
  
  private List<RemoteWebElement> findElements0(ZwlValue fromElement, By by, boolean wait) {
    try {
      return findElements(getWebElementUsingElemId(fromElement), by, wait);
    } catch (StaleElementReferenceException staleEx) {
      // fromElement contains stale reference.
      renewOnStaleEx(fromElement, staleEx);
      return null;
    }
  }
  
  protected RemoteWebElement findElement(SearchContext ctx, By by, boolean wait) {
    // findElement throws exception when no element is found
    Supplier<RemoteWebElement> s = () ->
        (RemoteWebElement) ctx.findElement(by);
    return waitOrNot(s, wait, "trying to find element by: " + by);
  }
  
  protected List<RemoteWebElement> findElements(SearchContext ctx, By by, boolean wait) {
    // findElements don't throw exception rather sends an empty list.
    Supplier<List<RemoteWebElement>> s = () -> {
      List<WebElement> elements = ctx.findElements(by);
      if (elements.size() == 0 && wait) {
        return null; // so that Wait can continue waiting when nothing found.
      }
      return elements.stream().map(e -> (RemoteWebElement) e).collect(Collectors.toList());
    };
    try {
      return waitOrNot(s, wait, "trying to find elements by: " + by);
    } catch (Exception t) {
      return Collections.emptyList();
    }
  }
  
  private <T> T waitOrNot(Supplier<T> s, boolean wait, String timeoutMsg) {
    if (wait) {
      // create new instance every time to prevent any threading issue in future.
      return getWait(TimeoutType.ELEMENT_ACCESS, timeoutMsg).until(d -> s.get());
    }
    return s.get();
  }
  
  /**
   * Note that even if a wait by default ignores some exception, it doesn't mean the code using
   * it can't handle ignored exceptions and return a custom value. When you handle that ignored
   * exception on your own, it doesn't propagate to 'until' block and doesn't let it decide
   * whether to ignore or throw. Ignoring an exception just gives you cleaner code so that you
   * don't have to catch that every time and return a false/null to re-evaluate.
   */
  protected WebDriverWait getWait(TimeoutType timeoutType, String timeoutMsg) {
    // if user has provided custom timeouts for this function try use that first
    int timeout = new Configuration().getTimeouts(wdProps, buildCapability, timeoutType);
    WebDriverWait wait =
        new WebDriverWait(driver, Duration.ofMillis(timeout));
    wait.withMessage(timeoutMsg);
    return wait;
  }
  
  // Generally the Id of an element will be a Map type containing everything we need to re query the
  // element in DOM but sometimes we don't have by, using and other details. For instance, an element
  // returned by a js script or an <option> element return by Select api functions. In those cases
  // we won't be able to re query the element. These functions accepting just the element are here
  // for that type of scenarios.
  protected ZwlValue convertIntoZwlElemId(RemoteWebElement remoteWebElement) {
    if (remoteWebElement == null) {
      return new NothingZwlValue();
    }
    return new StringZwlValue(remoteWebElement.getId());
  }
  
  protected ZwlValue convertIntoZwlElemIds(List<RemoteWebElement> remoteWebElements) {
    if (remoteWebElements == null) {
      return new NothingZwlValue();
    }
    return new ListZwlValue(remoteWebElements.stream()
        .map(this::convertIntoZwlElemId).collect(Collectors.toList()));
  }
  
  private ZwlValue createZWLMapId(RemoteWebElement remoteWebElement,
                                  @Nullable ZwlValue fromElement,
                                  String using,
                                  ByType byType) {
    // Immutable map is not taken because we overwrite the id when the element goes stale.
    Map<String, ZwlValue> elemId = new HashMap<>(CollectionUtil.getInitialCapacity(4));
    elemId.put("id", new StringZwlValue(remoteWebElement.getId()));
    if (fromElement != null) {
      elemId.put("from", fromElement);
    }
    elemId.put("using", new StringZwlValue(using));
    elemId.put("byType", new StringZwlValue(byType.toString()));
    return new MapZwlValue(elemId);
  }
  
  protected ZwlValue convertIntoZwlElemId(RemoteWebElement remoteWebElement,
                                          String using,
                                          ByType byType) {
    if (remoteWebElement == null) {
      return new NothingZwlValue();
    }
    return createZWLMapId(remoteWebElement, null, using, byType);
  }
  
  protected ZwlValue convertIntoZwlElemId(RemoteWebElement remoteWebElement,
                                          ZwlValue fromElement,
                                          String using,
                                          ByType byType) {
    if (remoteWebElement == null) {
      return new NothingZwlValue();
    }
    return createZWLMapId(remoteWebElement, fromElement, using, byType);
  }
  
  protected ZwlValue convertIntoZwlElemIds(List<RemoteWebElement> remoteWebElements,
                                           String using,
                                           ByType byType) {
    return new ListZwlValue(remoteWebElements.stream()
        .map(el -> convertIntoZwlElemId(el, using, byType)).collect(Collectors.toList()));
  }
  
  protected ZwlValue convertIntoZwlElemIds(List<RemoteWebElement> remoteWebElements,
                                           ZwlValue fromElement,
                                           String using,
                                           ByType byType) {
    return new ListZwlValue(remoteWebElements.stream()
        .map(el -> convertIntoZwlElemId(el, fromElement, using, byType))
        .collect(Collectors.toList()));
  }
  
  /**
   * Once a response is received from functions in {@link JavascriptExecutor}
   * , this method interprets them and converts to appropriate ZwlValue so that the result can be
   * returned to user. When a List or Map is returned, it's items are iterated and given to this
   * method recursively to produce a List/Map or ZwlValue which is then returned.
   * @param o the value to interpret.
   * @return Interpreted ZwlValue
   */
  protected ZwlValue transformJsResponse(Object o) {
    if (o == null) {
      return new NothingZwlValue();
    }
    
    if (o instanceof Double) {
      return new DoubleZwlValue((Double) o);
    }
    
    if (o instanceof Long) {
      return new DoubleZwlValue((Long) o);
    }
    
    if (o instanceof Boolean) {
      return new BooleanZwlValue((Boolean) o);
    }
    
    if (o instanceof WebElement) {
      // safe cast, this is a RemoteWebElement as per JsonToWebElementConverter which converts
      // json web element into WebElement
      return convertIntoZwlElemId((RemoteWebElement) o);
    }
    
    if (o instanceof List<?>) {
      List<?> lo = (List<?>) o;
      List<ZwlValue> lz = new ArrayList<>(CollectionUtil.getInitialCapacity(lo.size()));
      lo.forEach(item -> lz.add(transformJsResponse(item)));
      return new ListZwlValue(lz);
    }
    
    if (o instanceof Map<?, ?>) {
      Map<?, ?> mo = (Map<?, ?>) o;
      Map<String, ZwlValue> mz = new HashMap<>(CollectionUtil.getInitialCapacity(mo.size()));
      mo.forEach((k, v) -> mz.put(k.toString(), transformJsResponse(v)));
      return new MapZwlValue(mz);
    }
    
    return new StringZwlValue(o.toString());
  }
  
  protected By getBy(ByType byType, String using) {
    By by;
    switch (byType) {
      case ALT_TEXT:
        by = new ByAltText(using);
        break;
      case ARIA_LABEL:
        by = new ByAriaLabel(using);
        break;
      case CLASS_NAME:
        by = By.className(using);
        break;
      case CSS_SELECTOR:
      default:
        by = By.cssSelector(using);
        break;
      case ID:
        by = By.id(using);
        break;
      case LABEL_TEXT:
        by = new ByLabelText(using);
        break;
      case LINK_TEXT:
        by = By.linkText(using);
        break;
      case NAME:
        by = By.name(using);
        break;
      case PARTIAL_LINK_TEXT:
        by = By.partialLinkText(using);
        break;
      case PLACEHOLDER_TEXT:
        by = new ByPlaceholderText(using);
        break;
      case ROLE:
        by = new ByRole(using);
        break;
      case TAG_NAME:
        by = By.tagName(using);
        break;
      case TEST_ID:
        by = new ByTestId(using);
        break;
      case TEXT:
        by = new ByText(using);
        break;
      case TITLE:
        by = new ByTitle(using);
        break;
      case XPATH:
        by = By.xpath(using);
        break;
    }
    return by;
  }
  
  protected abstract ZwlValue getFuncDefReturnValue();
  
  protected abstract String getFuncReturnType();
  
  protected ZwlValue evaluateDefValue(Supplier<ZwlValue> userSuppliedDefValue) {
    String funcReturnType = getFuncReturnType();
    // If this function's return type is 'void', return void without checking anything else.
    if (funcReturnType.equals(Types.VOID)) {
      return _void;
    }
    ZwlValue funcDefReturnValue = getFuncDefReturnValue();
    ZwlValue userSupplied = userSuppliedDefValue.get();
    // if user didn't supply a value, return function's default
    if (userSupplied.getNothingValue().isPresent()) {
      return funcDefReturnValue;
    }
    // if user supplied a value, check what is the return type of function. If it's OBJECT,
    // return whatever user has supplied else check the type of user's value and match it with the
    // return type of function.
    if (funcReturnType.equals(Types.OBJECT)
        || userSupplied.getType().equals(funcReturnType)) {
      return userSupplied;
    }
    // throw if the type doesn't match.
    throw new InvalidTypeException(fromPos.get(), toPos.get(),
        String.format("The type of supplied value is '%s' whereas the function '%s' returns value" +
                " of type '%s'%s", userSupplied.getType(), getName(), funcReturnType,
            lineNColumn.get()));
  }
  
  // Expected position of arguments: fromElement, using, by
  @SuppressWarnings("SameParameterValue")
  protected List<RemoteWebElement> findElementsDetectingArgs(List<ZwlValue> args, boolean wait) {
    int argsCount = args.size();
    switch (argsCount) {
      case 3:
        return findElements(args.get(0),
            getBy(parseEnum(2, args.get(2), ByType.class), tryCastString(1, args.get(1))), wait);
      case 2:
        return findElements(driver,
            getBy(parseEnum(1, args.get(1), ByType.class), tryCastString(0, args.get(0))), wait);
      default:
        throw new RuntimeException("findElementsDetectingArgs got unexpected no of arguments: " +
            argsCount);
    }
  }
  
  // Expected position of arguments: fromElement, using, by
  @SuppressWarnings("SameParameterValue")
  protected RemoteWebElement findElementDetectingArgs(List<ZwlValue> args, boolean wait) {
    int argsCount = args.size();
    switch (argsCount) {
      case 3:
        return findElement(args.get(0),
            getBy(parseEnum(2, args.get(2), ByType.class), tryCastString(1, args.get(1))), wait);
      case 2:
        return findElement(driver,
            getBy(parseEnum(1, args.get(1), ByType.class), tryCastString(0, args.get(0))), wait);
      default:
        throw new RuntimeException("findElementDetectingArgs got unexpected no of arguments: " +
            argsCount);
    }
  }
  
  public void doSafeInteraction(ZwlValue elementId, Consumer<RemoteWebElement> interaction) {
    WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS,
        "waiting for element to render after staling");
    wait.until(d -> doSafeInteraction0(elementId, interaction));
  }
  
  private boolean doSafeInteraction0(ZwlValue elementId, Consumer<RemoteWebElement> interaction) {
    RemoteWebElement el = getWebElementUsingElemId(elementId);
    try {
      interaction.accept(el);
      return true;
    } catch (StaleElementReferenceException staleEx) {
      renewOnStaleEx(elementId, staleEx);
      return false;
    }
  }
  
  public <R> R doSafeInteraction(ZwlValue elementId, Function<RemoteWebElement, R> interaction) {
    WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS,
        "waiting for element to render after staling");
    return wait.until(d -> doSafeInteraction0(elementId, interaction)).orElse(null);
  }
  
  private <R> Optional<R> doSafeInteraction0(ZwlValue elementId,
                                             Function<RemoteWebElement, R> interaction) {
    RemoteWebElement el = getWebElementUsingElemId(elementId);
    try {
      // Don't return the result of 'interaction' as-is because this method is executed within a
      // WebDriver wait, which will retry the interaction if a 'false' or 'null' value is returned.
      // The 'interaction' can always return a false or null value and the caller must get that
      // actual value.
      // Keep the return value in an optional, and return an empty Optional if there was no value (i.e null).
      return Optional.ofNullable(interaction.apply(el));
    } catch (StaleElementReferenceException staleEx) {
      renewOnStaleEx(elementId, staleEx);
      //noinspection OptionalAssignedToNull
      return null; // Return a null so that the 'interaction' is retried.
    }
  }
  
  protected RemoteWebElement getValidElement(ZwlValue elementId) {
    RemoteWebElement el = getWebElementUsingElemId(elementId);
    try {
      el.isEnabled();
    } catch (StaleElementReferenceException staleEx) {
      el = renewOnStaleEx(elementId, staleEx);
    }
    return el;
  }
  
  public void waitUntilInteracted(ZwlValue elementId, Consumer<RemoteWebElement> interaction) {
    WebDriverWait wait = getWait(TimeoutType.ELEMENT_ACCESS,
        "waiting for element to become intractable");
    wait.until(d -> {
      try {
        doSafeInteraction(elementId, interaction);
        return true;
      } catch (InvalidElementStateException ie) {
        return false;
      }
    });
  }
}
