package com.clike.dbunit.datatype;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.sql.Types;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.junit.Test;

public class ClikeDataTypeFactoryTest {

  @Test
  public void testCreateDataType() throws DataTypeException {
    ClikeDataTypeFactory factory = new ClikeDataTypeFactory();
    DataType dataType = factory.createDataType(Types.TIMESTAMP, "TIMESTAMP");
    
    assertThat(dataType, is(instanceOf(IntervalNowTimestampType.class)));

  }
  
  @Test
  public void testSetCurrentIntervalSecondLength() throws DataTypeException {
    ClikeDataTypeFactory factory = new ClikeDataTypeFactory();
    
    DataType dataType = factory.createDataType(Types.TIMESTAMP, "TIMESTAMP");
    
    assertThat(dataType, is(instanceOf(IntervalNowTimestampType.class)));
    
    factory.setCurrentIntervalSecondLength(3L);
    DataType dataType2 = factory.createDataType(Types.TIMESTAMP, "TIMESTAMP");
    
    assertThat(dataType, not(dataType2));
  }

}
