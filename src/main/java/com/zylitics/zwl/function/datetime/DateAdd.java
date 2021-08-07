package com.zylitics.zwl.function.datetime;

import com.google.common.base.Strings;
import com.zylitics.zwl.datatype.StringZwlValue;
import com.zylitics.zwl.datatype.ZwlValue;
import com.zylitics.zwl.exception.EvalException;
import com.zylitics.zwl.exception.UnknownDateTimeException;
import com.zylitics.zwl.function.AbstractFunction;
import com.zylitics.zwl.model.DurationType;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Supplier;

public class DateAdd extends AbstractFunction {
  
  @Override
  public String getName() {
    return "dateAdd";
  }
  
  @Override
  public int minParamsCount() {
    return 3;
  }
  
  @Override
  public int maxParamsCount() {
    return 3;
  }
  
  @Override
  public ZwlValue invoke(List<ZwlValue> args, Supplier<ZwlValue> defaultValue,
                         Supplier<String> lineNColumn) {
    super.invoke(args, defaultValue, lineNColumn);
    int argsCount = args.size();
    
    if (args.size() == 3) {
      return new StringZwlValue(add(tryCastString(0, args.get(0)),
          tryCastString(1, args.get(1)), parseDouble(2, args.get(2)).longValue()));
    }
    
    throw unexpectedEndOfFunctionOverload(argsCount);
  }
  
  private String add(String date, String duration, long unit) {
    if (Strings.isNullOrEmpty(date) || Strings.isNullOrEmpty(duration)) {
      throw new EvalException(fromPos.get(), toPos.get(),
          withLineNCol(getName() + " requires date and duration both should be non empty"));
    }
    
    ZonedDateTime dateTime;
    try {
      dateTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    } catch (DateTimeParseException d ) {
      throw new UnknownDateTimeException(fromPos.get(), toPos.get(),
          withLineNCol(d.getMessage()));
    }
    DurationType durationType;
    try {
      durationType = DurationType.valueOf(duration);
    } catch (IllegalArgumentException i) {
      throw new EvalException(fromPos.get(), toPos.get(),
          String.format("Can't recognize the given duration: %s%s", duration, lineNColumn.get()));
    }
    ZonedDateTime newDateTime;
    
    switch (durationType) {
      case SECONDS:
        newDateTime = dateTime.plusSeconds(unit);
        break;
      case MINUTES:
        newDateTime = dateTime.plusMinutes(unit);
        break;
      case HOURS:
        newDateTime = dateTime.plusHours(unit);
        break;
      case DAYS:
        newDateTime = dateTime.plusDays(unit);
        break;
      case WEEKS:
        newDateTime = dateTime.plusWeeks(unit);
        break;
      case MONTHS:
        newDateTime = dateTime.plusMonths(unit);
        break;
      case YEARS:
        newDateTime = dateTime.plusYears(unit);
        break;
      default:
        throw new RuntimeException("Looks like we're missing this durationType " + durationType);
    }
  
    return newDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
  }
}
