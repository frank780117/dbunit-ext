package com.clike.dbunit.datatype;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.dbunit.dataset.datatype.DataType;
import org.junit.Test;

public class IntervalNowTimestampTypeTest {

  private final static DataType THIS_TYPE = new IntervalNowTimestampType();

  @Test
  public void testCompareEquals() throws Exception {
    
    long now = (new java.util.Date()).getTime();
    Object[] values1 = {
        null, 
        new Timestamp(1234), 
        new Date(1234), 
        new Time(1234),
        new Timestamp(1234).toString(), 
        new java.util.Date(1234), 
        new java.util.Date(),
        new java.util.Date(), 
        new Timestamp(now), 
        new Timestamp(now + 6789L)
    };

    Timestamp[] values2 = {
        null, 
        new Timestamp(1234), 
        new Timestamp(new Date(1234).getTime()),
        new Timestamp(new Time(1234).getTime()), 
        Timestamp.valueOf(new Timestamp(1234).toString()),
        new Timestamp(1234), 
        new Timestamp(now + 1234L), 
        new Timestamp(now - 2345L), 
        new Timestamp(now + 6789L), 
        new Timestamp(now - 6789L)
    };

    assertThat(values1.length, is(values2.length));

    for (int i = 0; i < values1.length; i++) {
      assertThat(THIS_TYPE.compare(values1[i], values2[i]), is(0));
      assertThat(THIS_TYPE.compare(values2[i], values1[i]), is(0));
    }
  }

  @Test
  public void testCompareDifferent() throws Exception {
    
    long now = (new java.util.Date()).getTime();
    
    Object[] less = {
        null, 
        new java.sql.Time(0), 
        "2015-03-02 00:00:55.0", 
        "2015-03-02 00:01:05.0",
        new Timestamp(now),
        new Timestamp(now - 23450L)
    };

    Object[] greater = {
        new java.sql.Time(1234), 
        new java.sql.Time(System.currentTimeMillis()),
        "2015-03-02 00:00:55.5", 
        "2015-03-02 00:01:18.1",
        new Timestamp(now + 12340L), 
        new Timestamp(now)
    };

    assertThat(less.length, is(greater.length));

    for (int i = 0; i < less.length; i++) {
      assertThat(THIS_TYPE.compare(less[i], greater[i]), lessThan(0));
      assertThat(THIS_TYPE.compare(greater[i], less[i]), greaterThan(0));
    }
  }
}
