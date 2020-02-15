package com.zylitics.zwl.util;

import com.google.common.collect.ImmutableMap;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.*;

import java.util.Map;

public class VarUtil {
  
  private VarUtil() {}
  
  public static Map<String, ZwlValue> getExceptionsVars() {
    return ImmutableMap.<String, ZwlValue>builder()
        .put("invalidTypeEx", new StringZwlValue(InvalidTypeException.class.getSimpleName()))
        .put("evalEx", new StringZwlValue(EvalException.class.getSimpleName()))
        .put("unknownDateTimeEx",
            new StringZwlValue(UnknownDateTimeException.class.getSimpleName()))
        .put("invalidRegexEx",
            new StringZwlValue(InvalidRegexPatternException.class.getSimpleName()))
        .put("insufficientArgsEx",
            new StringZwlValue(InsufficientArgumentsException.class.getSimpleName()))
        .put("indexOutOfRangeEx",
            new StringZwlValue(IndexOutOfRangeException.class.getSimpleName()))
        .put("illegalStringFormatEx",
            new StringZwlValue(IllegalStringFormatException.class.getSimpleName()))
        .put("illegalIdentifierEx",
            new StringZwlValue(IllegalIdentifierException.class.getSimpleName()))
        .put("dateTimeFormatEx",
            new StringZwlValue(DateTimeFormatException.class.getSimpleName()))
        .put("assertionFailedEx",
            new StringZwlValue(AssertionFailedException.class.getSimpleName()))
        .put("noSuchMapKeyEx", new StringZwlValue(NoSuchMapKeyException.class.getSimpleName()))
        .put("noSuchVariableEx", new StringZwlValue(NoSuchVariableException.class.getSimpleName()))
        .build();
  }
}
