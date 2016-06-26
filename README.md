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

# feature

## ClikeFlatXmlDataSetLoader

you can assign null value, current date, future date, and past date in data xml

| xml value       | replacement java Object|
| ----------------|:----------------------:|
| \[null\]        | null                   |
| \[now\]         | new Date\(\)           |
| \[past\]        | \[now\] - 35 days      |
| \[future\]      | \[now\] + 35 days      |

[past] and [future] can append '~' meaning the date distance farther than without '~',
one '~' means 35 days, default the max '~' count is 2.

| xml value     | replacement java Object|
| ------------- |:----------------------:|
| [past~]       | [now] - 70 days        |
| [past~~]      | [now] - 105 days       |
| [future~]     | [now] + 70 days        |
| [future~~]    | [now] + 105 days       |

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
public changeName(long userId, String name) {
    User user = userRepository.find(userId);
    user.setName(name);
    user.setModifiedDate(new Date());
}
```

your test code:
```
@ExpectedDatabase("expectedData.xml")
public testChangeName() {
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

[spring-test-dbunit]: https://github.com/springtestdbunit/spring-test-dbunit
