package com.clike.dbunit.dataset;

import java.util.Date;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.springframework.core.io.Resource;

import com.clike.dbunit.dataset.setup.SqlDefaultValueObject;

public class ClikeFlatXmlDataSetLoader
    extends com.github.springtestdbunit.dataset.FlatXmlDataSetLoader {

  private String nullKey = "null";
  private String currentDateKey = "now";
  private String futureDateKey = "future";
  private String pastDateKey = "past";
  private int dateflowSize = 2;


  @Override
  protected IDataSet createDataSet(Resource resource) throws Exception {
    IDataSet dataSet = super.createDataSet(resource);
    ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);

    long timeNow = (new Date()).getTime();
    long timeRange = 3024000000L;

    replacementDataSet.addReplacementObject("[" + nullKey + "]", null);
    replacementDataSet.addReplacementObject("[" + currentDateKey + "]", new Date(timeNow));
    replacementDataSet.addReplacementObject("[setup:" + futureDateKey + "]",
        new Date(timeNow + timeRange));
    replacementDataSet.addReplacementObject("[setup:" + pastDateKey + "]", new Date(timeNow - timeRange));

    replacementDataSet.addReplacementObject("[setup:anyNotNullValue]", new SqlDefaultValueObject(""));
    
    String dateFlowStr = "";
    for (int i = 1; i <= dateflowSize; i++) {
      dateFlowStr += "~";
      replacementDataSet.addReplacementObject("[setup:" + futureDateKey + dateFlowStr + "]",
          new Date(timeNow + timeRange * (i + 1)));
      replacementDataSet.addReplacementObject("[setup:" + pastDateKey + dateFlowStr + "]",
          new Date(timeNow - timeRange * (i + 1)));
    }

    return replacementDataSet;
  }

  public String getNullKey() {
    return nullKey;
  }

  public void setNullKey(String nullKey) {
    this.nullKey = nullKey;
  }

  public String getCurrentDateKey() {
    return currentDateKey;
  }

  public void setCurrentDateKey(String currentDateKey) {
    this.currentDateKey = currentDateKey;
  }

  public void setFutureDateKey(String futureDateKey) {
    this.futureDateKey = futureDateKey;
  }

  public void setPastDateKey(String pastDateKey) {
    this.pastDateKey = pastDateKey;
  }

  public void setDateflowSize(int dateflowSize) {
    this.dateflowSize = dateflowSize;
  }

}
