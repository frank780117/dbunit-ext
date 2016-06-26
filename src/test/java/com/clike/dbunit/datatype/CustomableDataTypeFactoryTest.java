package com.clike.dbunit.datatype;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.junit.Test;

public class CustomableDataTypeFactoryTest {
  
  @Test
  public void testAddCustomType() throws DataTypeException {
    CustomableDataTypeFactory factory = new CustomableDataTypeFactory();
    factory.addCustomType("MYTYPE", DataType.BOOLEAN);
    factory.addCustomType("MYTYPE2", DataType.BOOLEAN);
    
    DataType dataType = factory.createDataType(55, "MYTYPE");
    DataType dataType2 = factory.createDataType(1, "MYTYPE2");
    DataType dataType3 = factory.createDataType(1, "VARCHAR");
    
    assertThat(dataType, is(DataType.BOOLEAN));
    assertThat(dataType2, is(DataType.BOOLEAN));
    assertThat(dataType3, not(DataType.BOOLEAN));
  }
  
  @Test
  public void testRemoveCustomType() throws DataTypeException {
    CustomableDataTypeFactory factory = new CustomableDataTypeFactory();
    
    DataType customedDatatype = DataType.DOUBLE;
    DataType customDatatype = DataType.BOOLEAN;
    
    factory.addCustomType(customedDatatype.toString(), customDatatype);
    
    DataType dataType = factory.createDataType(customedDatatype.getSqlType(), customedDatatype.toString());
    
    factory.removeCustomType(customedDatatype.toString());
    
    DataType dataType2 = factory.createDataType(customedDatatype.getSqlType(), customedDatatype.toString());

    assertThat(dataType, is(customDatatype));
    assertThat(dataType2, not(customDatatype));
  }

}
