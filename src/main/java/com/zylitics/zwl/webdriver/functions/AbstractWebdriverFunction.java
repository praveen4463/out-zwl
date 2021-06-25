package com.zylitics.zwl.webdriver.functions;

import com.google.api.client.util.Preconditions;
import com.google.cloud.Tuple;
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
import org.openqa.selenium.remote.UselessFileDetector;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintStream;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;
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
  
  public <V> V handleWDExceptions(Callable<V> code) {
    try {
      return code.call();
    } catch (ZwlLangException z) {
      throw z;
    } catch (WebDriverException wdEx) {
      // wrap webdriver exceptions and throw.
      throw new ZwlLangException(fromPos.get(), toPos.get(), lineNColumn.get(), wdEx);
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
  
  protected boolean isValidElemId(String elemId) {
    return elemId
        .matches("[a-zA-Z0-9]{8}-?[a-zA-Z0-9]{4}-?[a-zA-Z0-9]{4}-?[a-zA-Z0-9]{4}-?[a-zA-Z0-9]{12}");
  }
  
  public RemoteWebElement getElement(String elemIdOrSelector) {
    return getElement(elemIdOrSelector, true);
  }
  
  public BuildCapability getBuildCapability() {
    return buildCapability;
  }
  
  protected RemoteWebElement getElement(String elemIdOrSelector,
                                        boolean wait) {
    if (isValidElemId(elemIdOrSelector)) {
      return getWebElementUsingElemId(elemIdOrSelector);
    }
    return findElement(driver, elemIdOrSelector, wait);
  }
  
  protected List<RemoteWebElement> getElements(List<String> elemIdsOrSelectors) {
    return getElements(elemIdsOrSelectors, true);
  }
  
  protected List<RemoteWebElement> getElements(List<String> elemIdsOrSelectors,
                                               @SuppressWarnings("SameParameterValue") boolean wait) {
    return elemIdsOrSelectors.stream().map(s ->
        isValidElemId(s) ? getWebElementUsingElemId(s) : findElement(driver, s, wait))
        .collect(Collectors.toList());
  }
  
  /**
   * Should be used by functions that expect more than one element. User can either send multiple
   * elemIds or selectors or just one selector that is meant to fetch multiple elements. If
   * succeeded, this method is guaranteed to return some element(s).
   * @param args The raw arguments received by function.
   * @return List of {@link RemoteWebElement}s
   */
  protected List<RemoteWebElement> getElementsUnderstandingArgs(List<ZwlValue> args) {
    Preconditions.checkArgument(args.size() > 0, "Expected at least one argument");
  
    if (args.size() == 1) {
      // we got only one argument, it may be an elemId, but if not we expect it multi element
      // selector.
      String s = tryCastString(0, args.get(0));
      if (!isValidElemId(s)) {
        return findElements(driver, s, true);
        // can't return empty because the wait doesn't let 0 sized result.
      }
    }
    // Following can't return empty list because each selector/elemId is evaluated separately,
    // if a selector returns nothing, exception will be thrown
    return getElements(args.stream().map(Objects::toString).collect(Collectors.toList()));
  }
  
  protected RemoteWebElement getWebElementUsingElemId(String elemId) {
    if (!isValidElemId(elemId)) {
      throw new ZwlLangException(fromPos.get(), toPos.get(), withLineNCol("Given string " + elemId +
          " is not a valid elemId"));
    }
    RemoteWebElement element = new RemoteWebElement();
    element.setParent(driver);
    element.setId(elemId);
    element.setFileDetector(new UselessFileDetector());
    return element;
  }
  
  protected RemoteWebElement findElement(SearchContext ctx, String cssSelector, boolean wait) {
    return findElement(ctx, By.cssSelector(cssSelector), wait);
  }
  
  protected List<RemoteWebElement> findElements(SearchContext ctx, String cssSelector,
                                                boolean wait) {
    return findElements(ctx, By.cssSelector(cssSelector), wait);
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
    } catch (TimeoutException timeoutWhileWaiting) {
      // if waiting and a timeout occurs, findElements should return empty list rather than
      // exception.
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
      case ID:
        by = By.id(using);
        break;
      case LINK_TEXT:
        by = By.linkText(using);
        break;
      case PARTIAL_LINK_TEXT:
        by = By.partialLinkText(using);
        break;
      case NAME:
        by = By.name(using);
        break;
      case TAG_NAME:
        by = By.tagName(using);
        break;
      case XPATH:
        by = By.xpath(using);
        break;
      case CLASS_NAME:
        by = By.className(using);
        break;
      case CSS_SELECTOR:
      default:
        by = By.cssSelector(using);
        break;
      case TEXT:
        by = new ByText(using);
        break;
      case TEST_ID:
        by = new ByTestId(using);
        break;
      case ROLE:
        by = new ByRole(using);
        break;
      case ARIA_LABEL:
        by = new ByAriaLabel(using);
        break;
      case TITLE:
        by = new ByTitle(using);
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
  
  private Tuple<SearchContext, By> detectElementSearchArguments(List<ZwlValue> args) {
    int argsCount = args.size();
    SearchContext ctx = driver;
    String using;
    ByType byType = null;
    switch (argsCount) {
      case 3:
        ctx = getElement(tryCastString(0, args.get(0)));
        using = tryCastString(1, args.get(1));
        byType = parseEnum(2, args.get(2), ByType.class);
        break;
      case 2:
        // Check whether last argument is a by, if so first must be using, otherwise first is elements,
        // and second is using with by being default.
        try {
          byType = parseEnum(1, args.get(1), ByType.class);
        } catch (InvalidTypeException ignore) { }
        if (byType == null) {
          byType = ByType.CSS_SELECTOR; // our default By is a css selector.
          ctx = getElement(tryCastString(0, args.get(0)));
          using = tryCastString(1, args.get(1));
        } else {
          using = tryCastString(0, args.get(0));
        }
        break;
      case 1:
        byType = ByType.CSS_SELECTOR;
        using = tryCastString(0, args.get(0));
        break;
      default:
        // args count is checked in calling function, a RunTime exception is thrown only if the function
        // doesn't mention correct max/min args count which is a bug.
        throw new RuntimeException("findElementsDetectingArgs got unexpected no of arguments: " +
            argsCount);
    }
    return Tuple.of(ctx, getBy(byType, using));
  }
  
  // Expected position of arguments: fromElement, using, by
  protected List<RemoteWebElement> findElementsDetectingArgs(List<ZwlValue> args, boolean wait) {
    Tuple<SearchContext, By> searchArgs = detectElementSearchArguments(args);
    return findElements(searchArgs.x(), searchArgs.y(), wait);
  }
  
  // Expected position of arguments: fromElement, using, by
  protected RemoteWebElement findElementDetectingArgs(List<ZwlValue> args, boolean wait) {
    Tuple<SearchContext, By> searchArgs = detectElementSearchArguments(args);
    return findElement(searchArgs.x(), searchArgs.y(), wait);
  }
}
