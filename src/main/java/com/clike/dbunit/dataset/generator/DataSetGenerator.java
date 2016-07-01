package com.clike.dbunit.dataset.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;

public interface DataSetGenerator {

  public void addQueryData(String table, String sqlQuery) throws SQLException, DatabaseUnitException;
  
  public String generateString() throws DataSetException, IOException, SQLException, DatabaseUnitException ;
  
  public void writeDataSetString(File file) throws FileNotFoundException, DataSetException, IOException, SQLException, DatabaseUnitException;
  
}
