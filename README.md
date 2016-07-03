# Introduction

dbunit-ext provides [spring-test-dbunit] [spring-test-dbunit] extension function. It allow you to use the project datasetLoader 
to set special value like 'null' or 'date'. You can use the project datatypeFactory to expected 'current date', 
your testing don't need **mock new Date()** to compare orignal date and expected date.

# Configuration

## change datasetLoader & statementFactory

you can modify datasetLoader to ClikeFlatXmlDataSetLoader in @DbUnitConfiguration

``` java
import com.clike.dbunit.dataset.ClikeFlatXmlDataSetLoader;

@DbUnitConfiguration(dataSetLoader=ClikeFlatXmlDataSetLoader.class, databaseConnection={"dataSource", "dataSource2"})
```

and you can change statementFactory to com.clike.dbunit.statement.PreparedStatementFactory in DatabaseConfigBean bean.

``` xml
<bean id="preparedStatementFactory" class="com.clike.dbunit.statement.PreparedStatementFactory" />

<bean id="dbUnitDatabaseConfig" class="com.github.springtestdbunit.bean.DatabaseConfigBean">
    <property name="skipOracleRecyclebinTables" value="true"/>
    <property name="statementFactory" ref="preparedStatementFactory"/>
</bean>
```

## change datatypeFactory

you can change datatypeFactory to ClikeDataTypeFactory in DatabaseConfigBean bean

``` xml
<bean id="clikeDataTypeFactory" class="com.clike.dbunit.datatype.ClikeDataTypeFactory">
    <property name="currentIntervalSecondLength" value="5"/>
</bean>

<bean id="dbUnitDatabaseConfig" class="com.github.springtestdbunit.bean.DatabaseConfigBean">
    <property name="skipOracleRecyclebinTables" value="true"/>
    <property name="datatypeFactory" ref="clikeDataTypeFactory"/>
</bean>
```

# feature

## ClikeFlatXmlDataSetLoader & PreparedStatementFactory

you can assign special value like null value, current date in data xml.

* all xml setting table

    | xml value       | replacement java Object|
    | ----------------|:----------------------:|
    | \[null\]                  | null                   |
    | \[now\]                   | new Date\(\)           |
    | \[setup:past\]            | \[now\] - 35 days      |
    | \[setup:future\]          | \[now\] + 35 days      |
    | \[setup:anyNotNullValue\] | any not null value for any type | 

    **If special xml value is start with 'setup', then the value should not to set expected data.**

[past] and [future] can append '~' meaning the date distance farther than without '~',
one '~' means 35 days, default the max '~' count is 2.

| xml value     | replacement java Object|
| ------------- |:----------------------:|
| [setup:past~]       | [now] - 70 days        |
| [setup:past~~]      | [now] - 105 days       |
| [setup:future~]     | [now] + 70 days        |
| [setup:future~~]    | [now] + 105 days       |

you can change **dateflowSize** property to config '~' max count.


## ClikeDataTypeFactory
 
you can expected current date without mock.
 
The implement is that accept a little diffTime in current date.
 
if now is 2016/06/24 14:15:21.000, it's define 'now' is a range  2016/06/24 14:15:11.000 ~ 2016/06/24 14:15:31.000,

in other words, it's define 'now' is **new Date() +- 10 sec**.
 
You can change by range by 'currentIntervalSecondLength' property, the 'now' range is **new Date() +- ${currentIntervalSecondLength} sec**, currentIntervalSecondLength default value is 10. 

# basic example

**first: integration ClikeDataTypeFactory and ClikeFlatXmlDataSetLoader in your test**

your source code:

``` java
@Transactional
public void changeName(long userId, String name) {
    User user = userRepository.find(userId);
    user.setName(name);
    user.setModifiedDate(new Date());
}
```

your test code:

```
@ExpectedDatabase("expectedData.xml")
public void testChangeName() {
    target.createUser(123L);
    target.changeName(123L, "CLIKE");
}
```

and you expected xml:

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<dataset>
	<User 
	    id="123"
	    name="CLIKE"
	    modifiedDate="[now]" />

</dataset>
```

# Generator

The generator can generate xml value from table, you can give table and sql, then generate xml value.

and you can use Modifier class to modify xml value.


## basic example

**first: you must use java.sql.DataSource in your application.**

your code:

``` java
@Autowired
DataSource dataSource;

public void generate() {
    FlatXmlDataSetGenerator generator = new FlatXmlDataSetGenerator(dataSource);
    generator.addQueryData("CUSTOMER", "SELECT * FROM CUSTOMER WHERE LAST_NAME = 'CLIKE'");
    generator.addQueryData("PHONE", "SELECT * FROM PHONE");
    System.out.println(generator.generateString());
}
```

then console output:

``` xml
<dataset>
  <CUSTOMER RID="1" FIRST_NAME="FRANK" LAST_MODIFIED_DATE="2016-07-02 01:22:06.158" LAST_NAME="CLIKE"/>
  <PHONE RID="1" PHONE_NUMBER="555-1234" COSTOMER_RID="1"/>
  <PHONE RID="2" PHONE_NUMBER="02-3456" PHONE_TYPE="TAIWAN" COSTOMER_RID="1"/>
</dataset>
```

## basic example (use modifier)

**first: you must use java.sql.DataSource in your application.**

your code:

``` java
@Autowired
DataSource dataSource;

public void generate() {
    FlatXmlDataSetGenerator generator = new FlatXmlDataSetGenerator(dataSource, new NullValueModifier("NULL"));
    generator.addQueryData("CUSTOMER", "SELECT * FROM CUSTOMER WHERE LAST_NAME = 'CLIKE'");
    generator.addQueryData("PHONE", "SELECT * FROM PHONE");
    generator.writeDataSetString(new File("/tmp/temp.xml"));
}
```

then `/tmp/temp.xml` content:

``` xml
<dataset>
  <CUSTOMER RID="1" FIRST_NAME="FRANK" LAST_MODIFIED_DATE="2016-07-02 01:22:06.158" LAST_NAME="CLIKE"/>
  <PHONE RID="1" PHONE_NUMBER="555-1234" PHONE_TYPE="[NULL]" COSTOMER_RID="1"/>
  <PHONE RID="2" PHONE_NUMBER="02-3456" PHONE_TYPE="TAIWAN" COSTOMER_RID="1"/>
</dataset>
```

[spring-test-dbunit]: https://github.com/springtestdbunit/spring-test-dbunit
