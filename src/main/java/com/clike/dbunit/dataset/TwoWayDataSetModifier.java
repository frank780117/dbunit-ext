package com.clike.dbunit.dataset;

import org.dbunit.dataset.IDataSet;

import com.github.springtestdbunit.dataset.DataSetModifier;

public interface TwoWayDataSetModifier extends DataSetModifier {

  public IDataSet reverseModify(IDataSet dataSet);
  
}
