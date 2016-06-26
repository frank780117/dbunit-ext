package com.clike.dbunit.datatype;

import java.util.HashMap;
import java.util.Map;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomableDataTypeFactory extends DefaultDataTypeFactory {

  private static final Logger logger = LoggerFactory.getLogger(CustomableDataTypeFactoryTest.class);

  private Map<String, DataType> customDataTypeMap = new HashMap<String, DataType>();

  public CustomableDataTypeFactory() {
    super();
  }

  public CustomableDataTypeFactory(Map<String, DataType> customDataTypeMap) {
    super();
    this.customDataTypeMap = customDataTypeMap;
  }

  @Override
  public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
    logger.debug("createDataType(sqlType={}, sqlTypeName={}) - start", new Integer(sqlType),
        sqlTypeName);

    DataType customDataType = customDataTypeMap.get(sqlTypeName);
    if (customDataType != null) {
      logger.debug("use custom dataType(sqlTypeName={}, dataType=)", sqlTypeName, customDataType);
      return customDataType;
    } else {
      return super.createDataType(sqlType, sqlTypeName);
    }
  }

  public void addCustomType(String sqlTypeName, DataType dataType) {
    if (sqlTypeName == null || "".equals(sqlTypeName)) {
      throw new NullPointerException("sqlType is null or empty.");
    }

    if (dataType == null) {
      throw new NullPointerException("dataType is null or empty.");
    }

    logger.debug("add custom type ('{}' is {})", sqlTypeName, dataType.getClass());
    customDataTypeMap.put(sqlTypeName, dataType);

  }

  public void removeCustomType(String sqlTypeName) {
    if (sqlTypeName == null || "".equals(sqlTypeName)) {
      throw new NullPointerException("sqlType is null or empty.");
    }

    customDataTypeMap.remove(sqlTypeName);

  }

}
