package com.clike.dbunit.dataset.setup;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlDefaultValueObject {

  private static final Logger logger = LoggerFactory.getLogger(SqlDefaultValueObject.class);

  private static Map<Integer, String> JDBC_MAPPING = getAllJdbcTypeNames();

  private Object orignValue;

  public static Object getDefaultValue(int sqlType) {

    logger.debug("get default value from {}.", JDBC_MAPPING.get(sqlType));

    switch (sqlType) {
      case Types.BIT: return false;
      case Types.TINYINT: return 1;
      case Types.SMALLINT: return 1;
      case Types.INTEGER: return 1;
      case Types.BIGINT: return 1;
      case Types.FLOAT: return 1;
      case Types.REAL: return 1;
      case Types.DOUBLE: return 1;
      case Types.NUMERIC: return 1;
      case Types.DECIMAL: return 1;
      case Types.CHAR: return "1";
      case Types.VARCHAR: return "1";
      case Types.LONGVARCHAR: return "1";
      case Types.DATE: return new Date();
      case Types.TIME: return new Date();
      case Types.TIMESTAMP: return new Date();
      case Types.BINARY: return "1".getBytes();
      case Types.VARBINARY: return "1".getBytes();
      case Types.LONGVARBINARY: return "1".getBytes();
      case Types.NULL: return null;
      case Types.BLOB: return "1".getBytes();
      case Types.CLOB: return "1".getBytes();
      case Types.BOOLEAN: return false;
      case Types.NCHAR: return "1";
      case Types.NVARCHAR: return "1";
      case Types.LONGNVARCHAR: return "1";
      default: 
        logger.error("No default value with {}, return null.", JDBC_MAPPING.get(sqlType));
        return null;
    }
  }

  public static Map<Integer, String> getAllJdbcTypeNames() throws IllegalArgumentException {

    Map<Integer, String> result = new HashMap<Integer, String>();

    for (Field field : Types.class.getFields()) {
      try {
        result.put((Integer) field.get(null), field.getName());
      } catch (IllegalAccessException e) {
        logger.error("Not access field {} to JDBC_MAPPING", field.getName(), e);
      }
    }

    return result;
  }

  public SqlDefaultValueObject(Object orignValue) {
    this.orignValue = orignValue;
  }

  public String toString() {
    return orignValue.toString();
  }

}
