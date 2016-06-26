# Introduction

dbunit-ext provides [spring-test-dbunit] [spring-test-dbunit] extension function. It allow you to use the project datasetLoader 
to set special value like 'null' or 'date'. You can use the project datatypeFactory to expected 'current date', 
your testing don't need **mock new Date()** to compare orignal date and expected date.

# Configuration

## change datasetLoader

you can modify datasetLoader to ClikeFlatXmlDataSetLoader in @DbUnitConfiguration

``` java
import com.clike.dbunit.dataset.ClikeFlatXmlDataSetLoader;

@DbUnitConfiguration(dataSetLoader=ClikeFlatXmlDataSetLoader.class, databaseConnection={"dataSource", "dataSource2"})
```

## change datatypeFactory

you can modify datatypeFactory to ClikeDataTypeFactory in DatabaseConfigBean bean

``` xml
<bean id="clikeDataTypeFactory" class="com.clike.dbunit.datatype.ClikeDataTypeFactory">
    <property name="currentIntervalSecondLength" value="5"/>
</bean>

<bean id="dbUnitDatabaseConfig" class="com.github.springtestdbunit.bean.DatabaseConfigBean">
    <property name="skipOracleRecyclebinTables" value="true"/>
    <property name="datatypeFactory" ref="clikeDataTypeFactory"/>
</bean>
```


[spring-test-dbunit]: https://github.com/springtestdbunit/spring-test-dbunit
