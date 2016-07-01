package com.clike.dbunit.dataset.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import com.clike.dbunit.dataset.TwoWayDataSetModifier;

public class FlatXmlDataSetGenerator implements DataSetGenerator {

  private QueryDataSet dataSet;

  private TwoWayDataSetModifier[] modifiers;
  
  public FlatXmlDataSetGenerator(DataSource dataSource, TwoWayDataSetModifier... modifiers) throws DatabaseUnitException, SQLException {
    DatabaseConnection connection = new DatabaseConnection(dataSource.getConnection());
    dataSet = new QueryDataSet(connection);
    this.modifiers = modifiers;
  }
  
  public void addQueryData(String table, String sqlQuery) throws SQLException, DatabaseUnitException {
    
    QueryDataSet dataset = dataSet;
    
    dataset.addTable(table, sqlQuery);

  }


  @Override
  public String generateString() throws IOException, SQLException, DatabaseUnitException {
    StringWriter writer = new StringWriter();
    try {
      IDataSet modifiedDataSet = modify(dataSet);
      FlatXmlDataSet.write(modifiedDataSet, writer);
      return writer.toString();
    } finally {
      writer.close();
    }
  }

  @Override
  public void writeDataSetString(File file) throws IOException, SQLException, DatabaseUnitException {
    OutputStream outputStream = new FileOutputStream(file);
    try {
      IDataSet modifiedDataSet = modify(dataSet);
      FlatXmlDataSet.write(modifiedDataSet, outputStream);
    } finally {
      outputStream.close();
    }
  }

  private IDataSet modify(IDataSet orginDataSet) {
    IDataSet tempDataSet = orginDataSet;
    for(TwoWayDataSetModifier modifier : modifiers) {
      tempDataSet = modifier.reverseModify(orginDataSet);
    }
    return tempDataSet;
  }
  
}
