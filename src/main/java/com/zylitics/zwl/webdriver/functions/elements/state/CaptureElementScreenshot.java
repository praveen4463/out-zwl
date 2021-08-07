package com.zylitics.zwl.webdriver.functions.elements.state;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.util.IOUtil;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class CaptureElementScreenshot extends AbstractWebdriverFunction {
  
  private static final Logger LOG = LoggerFactory.getLogger(CaptureElementScreenshot.class);
  
  private final Path buildDir;
  
  public CaptureElementScreenshot(APICoreProperties.Webdriver wdProps,
                                  BuildCapability buildCapability,
                                  RemoteWebDriver driver,
                                  PrintStream printStream,
                                  Path buildDir) {
    super(wdProps, buildCapability, driver, printStream);
  
    this.buildDir = buildDir;
  }
  
  @Override
  public String getName() {
    return "captureElementScreenshot";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return 2;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
  
    if (buildCapability.isDryRunning()) {
      return evaluateDefValue(defaultValue);
    }
  
    int argsCount = args.size();
    if (argsCount == 0) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    String givenFileName = argsCount == 2 ? args.get(1).toString() : "";
    if (!Strings.isNullOrEmpty(givenFileName) && givenFileName.endsWith(".png")) {
      givenFileName = givenFileName.substring(0, givenFileName.lastIndexOf(".png"));
    }
    String fileName = givenFileName + UUID.randomUUID() + ".png";
    byte[] screenshot =
        handleWDExceptions(() -> doSafeInteraction(args.get(0), el -> {
          return el.getScreenshotAs(OutputType.BYTES);
        }));
    // Rather than directly uploading to cloud, write locally and push all after the end of build
    // so that test execution don't delay, as we don't need to show these shots to user during build
    if (!Files.isDirectory(buildDir)) {
      throw new RuntimeException(buildDir.toAbsolutePath() + " isn't a directory");
    }
    Path elemShotDir = buildDir.resolve(wdProps.getElementShotDir());
    try {
      IOUtil.createNonExistingDir(elemShotDir);
      Path elemShotFile = elemShotDir.resolve(fileName);
      // fileName has UUID thus doesn't require checking whether it exists
      Files.write(elemShotFile, screenshot, StandardOpenOption.CREATE_NEW,
          StandardOpenOption.WRITE);
    } catch (Throwable t) {
      LOG.error(t.getMessage(), t);
    }
    return _void;
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return _void;
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.VOID;
  }
}
