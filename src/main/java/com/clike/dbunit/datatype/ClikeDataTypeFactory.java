package com.clike.dbunit.datatype;

public class ClikeDataTypeFactory extends CustomableDataTypeFactory {

  private long currentIntervalSecondLength = 10;
  
  public ClikeDataTypeFactory() {
    super();
    super.addCustomType("TIMESTAMP", new IntervalNowTimestampType(currentIntervalSecondLength));
  }

  public long getIntervalSecondLength() {
    return currentIntervalSecondLength;
  }

  public void setCurrentIntervalSecondLength(long currentIntervalSecondLength) {
    this.currentIntervalSecondLength = currentIntervalSecondLength;
    super.addCustomType("TIMESTAMP", new IntervalNowTimestampType(currentIntervalSecondLength));
  }
  
}
