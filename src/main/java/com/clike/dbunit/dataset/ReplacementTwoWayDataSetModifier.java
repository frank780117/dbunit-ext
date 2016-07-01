package com.clike.dbunit.dataset;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;

public class ReplacementTwoWayDataSetModifier implements TwoWayDataSetModifier {

  private Map<Object, Object> replacementMap = new HashMap<Object, Object>();

  @Override
  public IDataSet modify(IDataSet dataSet) {
    if (!(dataSet instanceof ReplacementDataSet)) {
      dataSet = new ReplacementDataSet(dataSet);
    }
    addReplacements((ReplacementDataSet) dataSet);
    return dataSet;
  }

  private void addReplacements(ReplacementDataSet dataSet) {
    for(Entry<Object, Object> entry : replacementMap.entrySet()) {
      dataSet.addReplacementObject(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public IDataSet reverseModify(IDataSet dataSet) {
    if (!(dataSet instanceof ReplacementDataSet)) {
      dataSet = new ReplacementDataSet(dataSet);
    }
    addReverseReplacements((ReplacementDataSet) dataSet);
    return dataSet;
  }

  public void addReplement(Object key, Object value) {
    replacementMap.put(key, value);
  }
  
  public void removeReplement(Object key) {
    replacementMap.remove(key);
  }

  protected void addReverseReplacements(ReplacementDataSet dataSet) {
    for(Entry<Object, Object> entry : replacementMap.entrySet()) {
      dataSet.addReplacementObject(entry.getValue(), entry.getKey());
    }
  }

}
