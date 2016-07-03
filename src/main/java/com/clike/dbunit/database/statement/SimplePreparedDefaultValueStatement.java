package com.clike.dbunit.database.statement;

import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.database.statement.SimplePreparedStatement;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clike.dbunit.dataset.setup.SqlDefaultValueObject;

public class SimplePreparedDefaultValueStatement extends SimplePreparedStatement {

  private static final Logger logger =
      LoggerFactory.getLogger(SimplePreparedDefaultValueStatement.class);


  public SimplePreparedDefaultValueStatement(String sql, Connection connection)
      throws SQLException {
    super(sql, connection);
  }

  @Override
  public void addValue(Object value, DataType dataType) throws TypeCastException, SQLException {
    logger.debug("addValue(value={}, dataType={}) - start", value, dataType);
    if(value instanceof SqlDefaultValueObject) {
      value = SqlDefaultValueObject.getDefaultValue(dataType.getSqlType());
    }
    
    super.addValue(value, dataType);
  }

}
