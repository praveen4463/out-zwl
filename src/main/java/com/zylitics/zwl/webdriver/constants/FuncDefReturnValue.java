package com.zylitics.zwl.webdriver.constants;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.*;

import java.util.Map;

public enum FuncDefReturnValue {
  
  ELEMENT_ID (D.E1),
  ELEMENT_IDS(new ListZwlValue(ImmutableList.of(D.E1, D.E2))),
  WINDOW_HANDLE (D.W1),
  WINDOW_HANDLES(new ListZwlValue(ImmutableList.of(D.W1, D.W2))),
  TRUE (D.TRUE),
  FALSE (D.FALSE),
  POSITION (new MapZwlValue(ImmutableMap.of("x", D.HUNDRED, "y", D.HUNDRED))),
  SIZE (new MapZwlValue(ImmutableMap.of("width", D.FIVE_HUNDRED, "height", D.FIVE_HUNDRED))),
  RECT (new MapZwlValue(ImmutableMap.of("x", D.HUNDRED, "y", D.HUNDRED,
      "width", D.FIVE_HUNDRED, "height", D.FIVE_HUNDRED))),
  COOKIE (D.CK1),
  COOKIES (new ListZwlValue(ImmutableList.of(D.CK1, D.CK2))),
  PAGE_SOURCE (D.PAGE_SOURCE),
  ELEMENT_TEXT (D.E_TEXT),
  ELEMENT_VALUE (D.E_VALUE),
  ELEMENT_TAG (D.E_TAG),
  UNKNOWN (D.UNKNOWN),
  URL (D.URL),
  TITLE( D.TITLE),
  ALERT_TEXT (D.ALERT_TEXT),
  STORAGE_KEYS (D.STORAGE_KEYS),
  TEN (D.TEN),
  THOUSAND (D.THOUSAND),
  ;
  
  private final ZwlValue defValue;
  
  FuncDefReturnValue(ZwlValue defValue) {
    this.defValue = defValue;
  }
  
  public ZwlValue getDefValue() {
    return defValue;
  }
  
  // lists all default values available in a separate class because enum static fields are
  // initialized after the own statics of enum and can't reference other static fields.
  // https://stackoverflow.com/a/444000/1624454
  private static class D {
  
    private static final ZwlValue E1 = new StringZwlValue("44b68dc3-9788-4b86-9d4c-3450e3727d19");
    private static final ZwlValue E2 = new StringZwlValue("c64e8a6a-5cE6-4ef0-9ae0-fdfd45a7a326");
    private static final ZwlValue W1 = new StringZwlValue("B81C49985C162A8445E45575EE4726C6");
    private static final ZwlValue W2 = new StringZwlValue("C81C49985W162A8445E45575EE4726S9");
    private static final ZwlValue TEN = new DoubleZwlValue(10);
    private static final ZwlValue THOUSAND = new DoubleZwlValue(1000);
    private static final ZwlValue HUNDRED = new DoubleZwlValue(100);
    private static final ZwlValue FIVE_HUNDRED = new DoubleZwlValue(500);
    private static final ZwlValue TRUE = new BooleanZwlValue(true);
    private static final ZwlValue FALSE = new BooleanZwlValue(false);
    private static final ZwlValue CK1 = new MapZwlValue(new ImmutableMap.Builder<String, ZwlValue>()
        .put("name", new StringZwlValue("_ga"))
        .put("value", new StringZwlValue("GA1"))
        .put("domain", new StringZwlValue("www.google.com"))
        .put("path", new StringZwlValue("/"))
        .put("expiry", new DoubleZwlValue(1000))
        .put("secure", FALSE)
        .put("httpOnly", FALSE)
        .build());
    private static final ZwlValue CK2 = new MapZwlValue(new ImmutableMap.Builder<String, ZwlValue>()
        .put("name", new StringZwlValue("_gq"))
        .put("value", new StringZwlValue("GQ1"))
        .put("domain", new StringZwlValue("www.google.com"))
        .put("path", new StringZwlValue("/"))
        .put("expiry", new DoubleZwlValue(1000))
        .put("secure", FALSE)
        .put("httpOnly", FALSE)
        .build());
    private static final ZwlValue PAGE_SOURCE = new StringZwlValue(
        "<html><head><title>example.com</title></head>" +
        "<body><div><h2>Welcome to example.com</h2></div></body></html>");
    private static final ZwlValue E_TEXT = new StringZwlValue("I'm feeling lucky");
    private static final ZwlValue E_VALUE = new StringZwlValue("Write something");
    private static final ZwlValue E_TAG = new StringZwlValue("input");
    private static final ZwlValue UNKNOWN = new StringZwlValue("unknown");
    private static final ZwlValue URL = new StringZwlValue("https://www.google.com");
    private static final ZwlValue TITLE = new StringZwlValue("Google");
    private static final ZwlValue ALERT_TEXT = new StringZwlValue("Are you sure?");
    private static final ZwlValue STORAGE_KEYS = new ListZwlValue(
        ImmutableList.of(new StringZwlValue("_ga"), new StringZwlValue("_gp")));
  }
}
