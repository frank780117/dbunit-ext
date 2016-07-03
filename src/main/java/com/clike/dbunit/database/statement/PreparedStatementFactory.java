package com.clike.dbunit.database.statement;

import java.sql.SQLException;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.AutomaticPreparedBatchStatement;
import org.dbunit.database.statement.IPreparedBatchStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreparedStatementFactory
    extends org.dbunit.database.statement.PreparedStatementFactory {

  private static final Logger logger = LoggerFactory.getLogger(PreparedStatementFactory.class);

  @Override
  public IPreparedBatchStatement createPreparedBatchStatement(String sql,
      IDatabaseConnection connection) throws SQLException {
    if (logger.isDebugEnabled()) {
      logger.debug("createPreparedBatchStatement(sql={}, connection={}) - start", sql, connection);
    }

    Integer batchSize =
        (Integer) connection.getConfig().getProperty(DatabaseConfig.PROPERTY_BATCH_SIZE);

    IPreparedBatchStatement statement = null;
    if (supportBatchStatement(connection)) {
      statement = new PreparedBatchDefaultValueStatement(sql, connection.getConnection());
    } else {
      statement = new SimplePreparedDefaultValueStatement(sql, connection.getConnection());
    }
    return new AutomaticPreparedBatchStatement(statement, batchSize.intValue());
  }

}
