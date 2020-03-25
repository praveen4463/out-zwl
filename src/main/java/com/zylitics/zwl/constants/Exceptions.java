package com.zylitics.zwl.constants;

import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.*;

import java.util.Map;

public class Exceptions {
  
  public static Map<String, ZwlValue> asMap() {
    return ImmutableMap.<String, ZwlValue>builder()
        .put("invalidTypeEx", ex(InvalidTypeException.class))
        .put("evalEx", ex(EvalException.class))
        .put("unknownDateTimeEx", ex(UnknownDateTimeException.class))
        .put("invalidRegexEx", ex(InvalidRegexPatternException.class))
        .put("insufficientArgsEx", ex(InsufficientArgumentsException.class))
        .put("indexOutOfRangeEx", ex(IndexOutOfRangeException.class))
        .put("illegalStringFormatEx", ex(IllegalStringFormatException.class))
        .put("illegalIdentifierEx", ex(IllegalIdentifierException.class))
        .put("dateTimeFormatEx", ex(DateTimeFormatException.class))
        .put("assertionFailedEx", ex(AssertionFailedException.class))
        .put("noSuchMapKeyEx", ex(NoSuchMapKeyException.class))
        .put("noSuchVariableEx", ex(NoSuchVariableException.class))
        .build();
  }
  
  private static ZwlValue ex(Class<? extends ZwlLangException> t) {
    return new StringZwlValue(t.getSimpleName());
  }
}
