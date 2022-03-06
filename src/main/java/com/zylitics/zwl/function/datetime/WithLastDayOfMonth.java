package com.zylitics.zwl.function.datetime;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.exception.UnknownDateTimeException;
import com.zylitics.zwl.function.AbstractFunction;
import com.zylitics.zwl.util.DateTimeUtil;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class WithLastDayOfMonth extends AbstractFunction {
  
  @Override
  public String getName() {
    return "withLastDayOfMonth";
  }
  
  @Override
  public int minParamsCount() {
    return 1;
  }
  
  @Override
  public int maxParamsCount() {
    return 1;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
    
    if (args.size() != 1) {
      throw unexpectedEndOfFunctionOverload(argsCount);
    }
    
    String date1 = tryCastString(0, args.get(0));
    
    if (Strings.isNullOrEmpty(date1)) {
      throw new EvalException(fromPos.get(), toPos.get(),
          withLineNCol("Date must be non empty"));
    }
    
    Function<String, UnknownDateTimeException> dateExFunc = (exMsg) ->
        new UnknownDateTimeException(fromPos.get(), toPos.get(), withLineNCol(exMsg));
    
    ZonedDateTime dateTime1 = DateTimeUtil.tryParseDate(date1, dateExFunc);
    
    return new StringZwlValue(dateTime1.with(TemporalAdjusters.lastDayOfMonth())
        .format(DateTimeFormatter.ISO_DATE_TIME));
  }
}
