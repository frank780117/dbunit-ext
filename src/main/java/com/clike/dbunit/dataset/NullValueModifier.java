package com.clike.dbunit.dataset;

public class NullValueModifier extends ReplacementTwoWayDataSetModifier {

  private String nullKey;
  
  public NullValueModifier() {
    this("null");
  }
  
  public NullValueModifier(String nullKey) {
    super.addReplement("[" + nullKey + "]", null);
    this.nullKey = nullKey;
  }

  public String getNullKey() {
    return nullKey;
  }

  public void setNullKey(String nullKey) {
    super.removeReplement("[" + nullKey + "]");
    this.nullKey = nullKey;
    super.addReplement("[" + nullKey + "]", null);
  }
  
}
