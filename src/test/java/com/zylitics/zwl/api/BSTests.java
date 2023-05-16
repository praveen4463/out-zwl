package com.zylitics.zwl.api;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BSTests {
  
  static WebDriver driver;
  
  @BeforeAll
  static void setup() throws Exception {
    String url = "https://praveentiwari2:R23aUy9qfRJxmECJs4cp@hub.browserstack.com/wd/hub";
  
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("browserName", "chrome");
    capabilities.setCapability("browserVersion", "latest");
    capabilities.setCapability("project", "Marketing Website v2");
    capabilities.setCapability("build", "alpha_0.1.7");
    //capabilities.setCapability("name", "Home page must have a title");
  
    Map<String, Object> browserstackOptions = new HashMap<>();
    browserstackOptions.put("os", "Windows");
    browserstackOptions.put("osVersion", "10");
  
    capabilities.setCapability("bstack:options", browserstackOptions);
    
    driver = new RemoteWebDriver(new URL(url), capabilities);
  }
  
  @AfterAll
  static void tearDown() {
    driver.quit();
  }
  
  @Test
  void th_net_assets_99_8_101_99() {
    List<String> funds = new ArrayList<>();
    funds.add("QYLD");
    funds.add("PTEC");
    List<String> errors = new ArrayList<>();
    for (String fund : funds) {
      driver.get(String.format("https://www.globalxetfs.com/funds/%s/?OUTOMATED=1", fund));
      WebElement topHoldingContainer = driver.findElement(By.id("holdings"));
      WebElement topHoldingsTable = topHoldingContainer.findElement(By.cssSelector("table#top-ten"));
      List<WebElement> tableHeaders = topHoldingsTable.findElements((By.cssSelector("thead th")));
      
      List<WebElement> showAlls = topHoldingContainer.findElements(By.cssSelector(".show-holdings.btn-show"));
      if (!showAlls.isEmpty()) {
      
      }
      
      boolean marketValueTypeFund = fund.equalsIgnoreCase("BITS");
  
      int targetColIndex = 0; // Index of the column we need to take data from
      String fundType = "Net Assets (%)";
      if (marketValueTypeFund) {
        fundType = "Market Value (%)";
        targetColIndex = tableHeaders.size() - 1;
      }
      String col = tableHeaders.get(targetColIndex).getText();
      Assertions.assertEquals(fundType, col,
          String.format("Expected last column on site to be %s, aborting...", fundType));
      /*
      dataCells = findElementsFromElement(topHoldingsTable,
        format('tbody tr td:nth-child(%s)', targetColIndex + 1),
        by.cssSelector)
       */
      List<WebElement> dataCells = topHoldingsTable
          .findElements(By.cssSelector(String.format("tbody tr td:nth-child(%s)", targetColIndex + 1)));
      int siteTotal = 0;
      for (WebElement cell : dataCells) {
        String cellText = cell.getText().trim();
        if (cellText.length() > 0) {
          try {
            siteTotal += Integer.parseInt(cellText);
          } catch (Exception ex) {
            // ignore
          }
        }
      }
      
      /*
      if !(siteTotal >= LOWER_RANGE && siteTotal <= UPPER_RANGE) {
      addTo(errors, format("Unexpectedly the sum on the site is %s", siteTotal))
    }
       */
      if (!(siteTotal >= 99.8 && siteTotal <= 101.99)) {
        errors.add(String.format("Unexpectedly the sum on the CSV is %s", siteTotal));
      }
    }
    if (errors.size() > 0) {
      Assertions.fail(String.join("\n", errors));
    }
  }
}
