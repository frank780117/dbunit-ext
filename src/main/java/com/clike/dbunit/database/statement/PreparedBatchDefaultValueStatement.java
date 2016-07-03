package com.clike.dbunit.database.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.dbunit.database.statement.IPreparedBatchStatement;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clike.dbunit.dataset.setup.SqlDefaultValueObject;

public class PreparedBatchDefaultValueStatement implements IPreparedBatchStatement {

  private static final Logger logger = LoggerFactory.getLogger(PreparedBatchDefaultValueStatement.class);

  private int _index;

  protected final PreparedStatement _statement;

  public PreparedBatchDefaultValueStatement(String sql, Connection connection) throws SQLException {
    _statement = connection.prepareStatement(sql);
    _index = 0;
  }

  public void close() throws SQLException {
    logger.debug("close() - start");

    _statement.close();
  }

  ////////////////////////////////////////////////////////////////////////////
  // IPreparedBatchStatement interface

  public void addValue(Object value, DataType dataType) throws TypeCastException, SQLException {
    logger.debug("addValue(value={}, dataType={}) - start", value, dataType);
    
    if(value instanceof SqlDefaultValueObject) {
      value = SqlDefaultValueObject.getDefaultValue(dataType.getSqlType());
    }
    
    // Special NULL handling
    if (value == null || value == ITable.NO_VALUE) {
      _statement.setNull(++_index, dataType.getSqlType());
      return;
    }

    dataType.setSqlValue(value, ++_index, _statement);
  }

  public void addBatch() throws SQLException {
    logger.debug("addBatch() - start");

    _statement.addBatch();
    _index = 0;
  }

  public int executeBatch() throws SQLException {
    logger.debug("executeBatch() - start");

    int[] results = _statement.executeBatch();
    int result = 0;
    for (int i = 0; i < results.length; i++) {
      result += results[i];
    }
    return result;
  }

  public void clearBatch() throws SQLException {
    logger.debug("clearBatch() - start");
    _statement.clearBatch();
  }

}
