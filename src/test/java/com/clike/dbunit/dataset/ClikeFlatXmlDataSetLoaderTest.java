package com.clike.dbunit.dataset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;

import java.util.Date;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.junit.Test;

import com.clike.dbunit.datatype.IntervalNowTimestampType;

public class ClikeFlatXmlDataSetLoaderTest {

  @Test
  public void testDefaultBehavior() throws Exception {
    ClikeFlatXmlDataSetLoader dataSetLoader = new ClikeFlatXmlDataSetLoader();

    IDataSet dataset = dataSetLoader.loadDataSet(this.getClass(), "clike-flat-xmldataset.xml");

    ITable sampleTable = dataset.getTable("Sample");
    
    // sleep 10 millis, to 'now' is later for sampleTable.currentValue
    Thread.sleep(10);
    
    Date now = new Date();
    
    assertThat(sampleTable.getValue(0, "nullValue"), is(nullValue()));
    assertThat(sampleTable.getValue(0, "currentValue"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(0, "futureValue"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(0, "futureValue2"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(0, "futureValue3"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(0, "pastValue"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(0, "pastValue2"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(0, "pastValue3"), is(instanceOf(Date.class)));
    
    // default dateflowSize is 2, not replace value to date.class
    assertThat(sampleTable.getValue(0, "optionValue"), is(instanceOf(String.class)));
    
    assertThat((Date)sampleTable.getValue(0, "currentValue"), lessThan(now));
    assertThat((Date)sampleTable.getValue(0, "futureValue"), greaterThan(now));
    assertThat((Date)sampleTable.getValue(0, "futureValue2"), greaterThan((Date)sampleTable.getValue(0, "futureValue")));
    assertThat((Date)sampleTable.getValue(0, "futureValue3"), greaterThan((Date)sampleTable.getValue(0, "futureValue2")));
    assertThat((Date)sampleTable.getValue(0, "pastValue"), lessThan((Date)sampleTable.getValue(0, "currentValue")));
    assertThat((Date)sampleTable.getValue(0, "pastValue2"), lessThan((Date)sampleTable.getValue(0, "pastValue")));
    assertThat((Date)sampleTable.getValue(0, "pastValue3"), lessThan((Date)sampleTable.getValue(0, "pastValue2")));
  
  }

  @Test
  public void testDataflowSize() throws Exception {
    ClikeFlatXmlDataSetLoader dataSetLoader = new ClikeFlatXmlDataSetLoader();

    IDataSet dataset = dataSetLoader.loadDataSet(this.getClass(), "clike-flat-xmldataset.xml");

    ITable sampleTable = dataset.getTable("Sample");
    
    // default dateflowSize is 2, not replace value to date.class
    assertThat(sampleTable.getValue(0, "optionValue"), is(instanceOf(String.class)));
    
    dataSetLoader.setDateflowSize(3);
    
    IDataSet dataset2 = dataSetLoader.loadDataSet(this.getClass(), "clike-flat-xmldataset.xml");

    ITable sampleTable2 = dataset2.getTable("Sample");
    
    // dateflowSize is 3,  replace value to date.class
    assertThat(sampleTable2.getValue(0, "optionValue"), is(instanceOf(Date.class)));
    
    assertThat((Date)sampleTable2.getValue(0, "optionValue"), greaterThan((Date)sampleTable.getValue(0, "futureValue3")));

  }
  
  @Test
  public void testCustomKey() throws Exception {
    ClikeFlatXmlDataSetLoader dataSetLoader = new ClikeFlatXmlDataSetLoader();

    dataSetLoader.setCurrentDateKey("NOW");
    dataSetLoader.setFutureDateKey("FUTURE");
    dataSetLoader.setPastDateKey("PAST");
    dataSetLoader.setNullKey("NULL");
    
    IDataSet dataset = dataSetLoader.loadDataSet(this.getClass(), "clike-flat-xmldataset.xml");

    ITable sampleTable = dataset.getTable("Sample");
    
    Date now = new Date();
    
    assertThat(sampleTable.getValue(1, "nullValue"), is(nullValue()));
    assertThat(sampleTable.getValue(1, "currentValue"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(1, "futureValue"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(1, "futureValue2"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(1, "futureValue3"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(1, "pastValue"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(1, "pastValue2"), is(instanceOf(Date.class)));
    assertThat(sampleTable.getValue(1, "pastValue3"), is(instanceOf(Date.class)));

    assertThat((Date)sampleTable.getValue(1, "currentValue"), lessThan(now));
    assertThat((Date)sampleTable.getValue(1, "futureValue"), greaterThan(now));
    assertThat((Date)sampleTable.getValue(1, "futureValue2"), greaterThan((Date)sampleTable.getValue(1, "futureValue")));
    assertThat((Date)sampleTable.getValue(1, "futureValue3"), greaterThan((Date)sampleTable.getValue(1, "futureValue2")));
    assertThat((Date)sampleTable.getValue(1, "pastValue"), lessThan((Date)sampleTable.getValue(1, "currentValue")));
    assertThat((Date)sampleTable.getValue(1, "pastValue2"), lessThan((Date)sampleTable.getValue(1, "pastValue")));
    assertThat((Date)sampleTable.getValue(1, "pastValue3"), lessThan((Date)sampleTable.getValue(1, "pastValue2")));
  }
  
}
