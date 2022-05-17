package com.zylitics.zwl.webdriver.functions.util;

import com.google.cloud.storage.Storage;
import com.zylitics.zwl.datatype.BooleanZwlValue;
import com.zylitics.zwl.datatype.Types;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.webdriver.APICoreProperties;
import com.zylitics.zwl.webdriver.BuildCapability;
import com.zylitics.zwl.webdriver.FileInputFilesProcessor;
import com.zylitics.zwl.webdriver.functions.AbstractWebdriverFunction;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Size;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Supplier;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

public class MatchesSnapshot extends AbstractWebdriverFunction {
  
  private static final Logger LOG = LoggerFactory.getLogger(MatchesSnapshot.class);
  
  private static final int MAX_PIXEL_DEVIATION_ALLOWED = 20;
  
  private static final String EXPECTED_COORDINATES_KEY = "expectedCoordinates";
  
  private static final String SCROLL_POS_KEY = "scrollPos";
  
  private static final String ACCEPTABLE_PIXEL_DEVIATION_KEY = "acceptablePixelDeviation";
  
  private static final String INVALID_COORDINATES_MSG =
      "Invalid coordinates, must be in {x: NUM, y: NUM} format.";
  
  private static final String INVALID_SCROLL_POS_MSG =
      "Invalid scrollPos, must be in {x: NUM, y: NUM} format.";
  
  private final Storage storage;
  
  private final String pathToUploadedFiles;
  
  private final Path buildDir;
  
  /**
   *
   * @param pathToUploadedFiles should be a string containing subdirectories that keep user
   *                            uploaded files. Subdirectories are separated by a forward slash
   *                            , such as 1002/uploads where 1002 is orgId.
   */
  public MatchesSnapshot(APICoreProperties.Webdriver wdProps,
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
    return "matchesSnapshot";
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
    String fileOnCloud = tryCastString(0, args.get(0));
    Map<String, ZwlValue> config = tryCastMap(1, args.get(1));
    
    try {
      if (!fileOnCloud.endsWith(".png")) {
        throw new IllegalArgumentException("Snapshot must be a .png image.");
      }
      
      Map<String, ZwlValue> expectedCoordinates =
          Objects.requireNonNull(config.get(EXPECTED_COORDINATES_KEY),
              EXPECTED_COORDINATES_KEY + " is required.").getMapValue()
              .orElseThrow(() -> new IllegalArgumentException(
                  EXPECTED_COORDINATES_KEY + " must be a 'map' of snapshot's 2d coordinates."));
  
      pointValidator(expectedCoordinates, INVALID_COORDINATES_MSG);
      
      int expX = expectedCoordinates.get("x").getDoubleValue()
          .orElseThrow(() ->
              new IllegalArgumentException(INVALID_COORDINATES_MSG))
          .intValue();
      int expY = expectedCoordinates.get("y").getDoubleValue()
          .orElseThrow(() ->
              new IllegalArgumentException(INVALID_COORDINATES_MSG))
          .intValue();
  
      int scrollX = -1;
      int scrollY = -1;
      if (config.get(SCROLL_POS_KEY) != null) {
        Map<String, ZwlValue> scrollPos = config.get(SCROLL_POS_KEY).getMapValue()
            .orElseThrow(() -> new IllegalArgumentException(
                SCROLL_POS_KEY + " must be a 'map' defining desired scroll position."));
  
        pointValidator(scrollPos, INVALID_SCROLL_POS_MSG);
        
        scrollX = scrollPos.get("x").getDoubleValue()
            .orElseThrow(() ->
                new IllegalArgumentException(INVALID_SCROLL_POS_MSG))
            .intValue();
        scrollY = scrollPos.get("y").getDoubleValue()
            .orElseThrow(() ->
                new IllegalArgumentException(INVALID_SCROLL_POS_MSG))
            .intValue();
      }
  
      int acceptedPixelDeviation = 0;
      if (config.get(ACCEPTABLE_PIXEL_DEVIATION_KEY) != null) {
        acceptedPixelDeviation = config.get(ACCEPTABLE_PIXEL_DEVIATION_KEY).getDoubleValue()
            .orElseThrow(() -> new IllegalArgumentException(
                ACCEPTABLE_PIXEL_DEVIATION_KEY + " must be a 'number'."))
            .intValue();
        if (acceptedPixelDeviation > MAX_PIXEL_DEVIATION_ALLOWED) {
          throw new IllegalArgumentException(ACCEPTABLE_PIXEL_DEVIATION_KEY + " must not be" +
              " greater than " + MAX_PIXEL_DEVIATION_ALLOWED + ".");
        }
      }
  
      String snapshotLocalPath =
          new FileInputFilesProcessor(storage, wdProps.getUserDataBucket(), pathToUploadedFiles,
              Collections.singleton(fileOnCloud), buildDir, lineNColumn, fromPos, toPos)
              .process().iterator().next();
      
      if (scrollX > -1 && scrollY > -1) {
        driver.executeScript(String.format("window.scrollBy(%d, %d)", scrollX, scrollY));
      }
      
      byte[] pageScreenshot = driver.getScreenshotAs(OutputType.BYTES);
      Path pageScreenshotPath = buildDir.resolve(UUID.randomUUID() + ".png");
      Files.write(pageScreenshotPath, pageScreenshot, StandardOpenOption.CREATE_NEW,
          StandardOpenOption.WRITE);
      String fullPageScreenshotPath = pageScreenshotPath.toAbsolutePath().toString();
  
      Mat sourceColor = imread(fullPageScreenshotPath);
      Mat sourceGrey = new Mat(sourceColor.size(), CV_8UC1);
      cvtColor(sourceColor, sourceGrey, COLOR_BGR2GRAY);
      // Load in template in grey
      Mat template = imread(snapshotLocalPath, IMREAD_GRAYSCALE); // int = 0
      // Size for the result image
      Size size = new Size(sourceGrey.cols() - template.cols() + 1,
          sourceGrey.rows() - template.rows() + 1);
      Mat result = new Mat(size, CV_32FC1);
      matchTemplate(sourceGrey, template, result, TM_CCORR_NORMED);
  
      DoublePointer minVal = new DoublePointer();
      DoublePointer maxVal = new DoublePointer();
      Point min = new Point();
      Point max = new Point();
      minMaxLoc(result, minVal, maxVal, min, max, null);
  
      int xDiff = Math.abs(max.x() - expX);
      int yDiff = Math.abs(max.y() - expY);
      
      boolean matched = xDiff <= acceptedPixelDeviation && yDiff <= acceptedPixelDeviation;
      
      if (!matched && xDiff <= MAX_PIXEL_DEVIATION_ALLOWED
          && yDiff <= MAX_PIXEL_DEVIATION_ALLOWED) {
        writeBuildOutput(String.format("WARNING: A match is found at (%d, %d). Did you want" +
            " to increase %s?", max.x(), max.y(), ACCEPTABLE_PIXEL_DEVIATION_KEY));
      }
      
      Files.delete(pageScreenshotPath);
      
      return new BooleanZwlValue(matched);
    } catch (Throwable t) {
      LOG.error(t.getMessage(), t);
      throw new EvalException(fromPos.get(), toPos.get(), withLineNCol(t.getMessage()));
    }
  }
  
  @Override
  protected ZwlValue getFuncDefReturnValue() {
    return new BooleanZwlValue(true);
  }
  
  @Override
  protected String getFuncReturnType() {
    return Types.BOOLEAN;
  }
  
  private void pointValidator(Map<String, ZwlValue> point, String exceptionMsg) {
    if (!(point.size() == 2 && point.containsKey("x") && point.containsKey("y"))) {
      throw new IllegalArgumentException(exceptionMsg);
    }
  }
}
