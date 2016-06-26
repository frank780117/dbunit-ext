package com.clike.dbunit.datatype;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.AbstractDataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntervalNowTimestampType extends AbstractDataType {

  private static final BigInteger ONE_BILLION = new BigInteger("1000000000");
  private static final Pattern TIMEZONE_REGEX =
      Pattern.compile("(.*)(?:\\W([+-][0-2][0-9][0-5][0-9]))");

  private static final Logger logger =
      LoggerFactory.getLogger(IntervalNowTimestampType.class);

  private long intervalSecondLength;

  public IntervalNowTimestampType() {
    this(10L);
  }

  public IntervalNowTimestampType(long intervalSecondLength) {
    super("TIMESTAMP", Types.TIMESTAMP, FlagTimestamp.class, false);
    this.intervalSecondLength = intervalSecondLength;
  }

  @Override
  public FlagTimestamp typeCast(Object value) throws TypeCastException {
    logger.debug("typeCast(value={}) - start", value);

    if (value == null || value == ITable.NO_VALUE) {
      return null;
    }

    if (value instanceof java.sql.Timestamp) {
      return makeFlagTimestamp((Timestamp) value);
    }

    if (value instanceof java.util.Date) {
      java.util.Date date = (java.util.Date) value;
      return makeFlagTimestamp(new Timestamp(date.getTime()));
    }

    if (value instanceof Long) {
      Long date = (Long) value;
      return makeFlagTimestamp(new Timestamp(date));
    }

    if (value instanceof String) {
      String stringValue = value.toString();
      String zoneValue = null;

      Matcher tzMatcher = TIMEZONE_REGEX.matcher(stringValue);
      if (tzMatcher.matches() && tzMatcher.group(2) != null) {
        stringValue = tzMatcher.group(1);
        zoneValue = tzMatcher.group(2);
      }

      FlagTimestamp fts = null;
      if (stringValue.length() == 10) {
        try {
          long time = java.sql.Date.valueOf(stringValue).getTime();
          fts = makeFlagTimestamp(new Timestamp(time));
        } catch (IllegalArgumentException e) {
          // Was not a java.sql.Date, let Timestamp handle this value
        }
      }
      if (fts == null) {
        try {
          fts = makeFlagTimestamp(Timestamp.valueOf(stringValue));
        } catch (IllegalArgumentException e) {
          throw new TypeCastException(value, this, e);
        }
      }

      // Apply zone if any
      if (zoneValue != null) {
        BigInteger time = BigInteger.valueOf(fts.getTime() / 1000 * 1000).multiply(ONE_BILLION)
            .add(BigInteger.valueOf(fts.getNanos()));
        int hours = Integer.parseInt(zoneValue.substring(1, 3));
        int minutes = Integer.parseInt(zoneValue.substring(3, 5));
        BigInteger offsetAsSeconds = BigInteger.valueOf((hours * 3600) + (minutes * 60));
        BigInteger offsetAsNanos =
            offsetAsSeconds.multiply(BigInteger.valueOf(1000)).multiply(ONE_BILLION);
        if (zoneValue.charAt(0) == '+') {
          time = time.subtract(offsetAsNanos);
        } else {
          time = time.add(offsetAsNanos);
        }
        BigInteger[] components = time.divideAndRemainder(ONE_BILLION);
        fts = makeFlagTimestamp(new Timestamp(components[0].longValue()));
        fts.setNanos(components[1].intValue());
      }

      return fts;
    }

    throw new TypeCastException(value, this);
  }

  @Override
  public int compare(Object o1, Object o2) throws TypeCastException {
    logger.debug("compare(o1={}, o2={}) - start", o1, o2);

    try {
      // New in 2.3: Object level check for equality - should give massive performance improvements
      // in the most cases because the typecast can be avoided (null values and equal objects)
      if (areObjectsEqual(o1, o2)) {
        return 0;
      }


      // Comparable check based on the results of method "typeCast"
      Object value1 = typeCast(o1);
      Object value2 = typeCast(o2);

      // Check for "null"s again because typeCast can produce them

      if (value1 == null && value2 == null) {
        return 0;
      }

      if (value1 == null && value2 != null) {
        return -1;
      }

      if (value1 != null && value2 == null) {
        return 1;
      }

      return compareNonNulls(value1, value2);

    } catch (ClassCastException e) {
      throw new TypeCastException(e);
    }
  }

  @Override
  protected int compareNonNulls(Object value1, Object value2) throws TypeCastException {

    logger.debug("compareNonNulls(value1={}, value2={}) - start", value1, value2);

    FlagTimestamp f1 = (FlagTimestamp) value1;
    FlagTimestamp f2 = (FlagTimestamp) value2;

    if (f1.getXDateFlag() != null && f1.getXDateFlag() == f2.getXDateFlag()) {
      return 0;
    } else {
      Comparable value1comp = (Comparable) value1;
      Comparable value2comp = (Comparable) value2;
      return value1comp.compareTo(value2comp);
    }
  }
  
  private FlagTimestamp makeFlagTimestamp(Timestamp timestamp) {
    if (timestamp == null) {
      throw new NullPointerException("timestamp is null");
    }

    long diffTime = timestamp.getTime() - (new Date()).getTime();

    if (isAboutNow(diffTime)) {
      return new FlagTimestamp(timestamp.getTime(), XDateFlag.NOW);
    }
    return new FlagTimestamp(timestamp.getTime());
  }

  private boolean isAboutNow(long diffTime) {
    return Math.abs(diffTime) <= intervalSecondLength * 1000;
  }

  protected static class FlagTimestamp extends Timestamp {

    private XDateFlag xDateFlag;

    public FlagTimestamp(long time) {
      super(time);
    }

    public FlagTimestamp(long time, XDateFlag xDateFlag) {
      super(time);
      this.xDateFlag = xDateFlag;
    }

    public XDateFlag getXDateFlag() {
      return this.xDateFlag;
    }

  }

  public static enum XDateFlag {
    NOW, PAST, FUTURE
  }

}
