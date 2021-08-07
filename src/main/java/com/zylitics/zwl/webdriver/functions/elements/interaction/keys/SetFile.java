package com.zylitics.zwl.webdriver.functions.elements.interaction.keys;

import com.google.cloud.storage.Storage;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import com.zylitics.zwl.datatype.ZwlValue;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class SetFile extends AbstractWebdriverFunction {
  
  private final Storage storage;
  
  private final String pathToUploadedFiles;
  
  private final Path buildDir;
  
  /**
   *
   * @param pathToUploadedFiles should be a string containing subdirectories that keep user
   *                            uploaded files. Subdirectories are separated by a forward slash
   *                            , such as 1002/uploads where 1002 is userId.
   */
  public SetFile(APICoreProperties.Webdriver wdProps,
                 BuildCapability buildCapability,
                 RemoteWebDriver driver,
                 PrintStream printStream,
                 Storage storage,
                 String pathToUploadedFiles,
                 Path buildDir) {
    super(wdProps, buildCapability, driver, printStream);
    this.storage = storage;
    this.pathToUploadedFiles = pathToUploadedFiles;
    this.buildDir = buildDir;
  }
  
  @Override
  public String getName() {
    return "setFile";
  }
  
  @Override
  public int minParamsCount() {
    return 2;
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
    
    if (args.size() != 2) {
      throw unexpectedEndOfFunctionOverload(args.size());
    }
    String fileOnCloud = args.get(1).toString();
    // don't cast to string, may be possible the file is named like 322323 with no extension and
    // user sent it that way.
    String localFilePathAfterDownload =
        new FileInputFilesProcessor(storage, wdProps.getUserDataBucket(), pathToUploadedFiles,
            Collections.singleton(fileOnCloud), buildDir, lineNColumn, fromPos, toPos)
            .process().iterator().next();
    return handleWDExceptions(() -> {
      waitUntilInteracted(args.get(0), el -> el.sendKeys(localFilePathAfterDownload));
      return _void;
    });
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
