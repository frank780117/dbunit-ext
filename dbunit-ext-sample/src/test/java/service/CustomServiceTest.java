package service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.clike.dbunit.dataset.ClikeFlatXmlDataSetLoader;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import config.TestApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=TestApplication.class)
@TestExecutionListeners({ 
  DependencyInjectionTestExecutionListener.class,
  DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader=ClikeFlatXmlDataSetLoader.class)
public class CustomServiceTest {

  @Autowired
  private CustomService customService;
  
  @Test
  @DatabaseSetup("/dbtest/setup/customSampleData.xml")
  @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/dbtest/expected/customService_testAddPhone.xml")
  @DatabaseTearDown(type=DatabaseOperation.DELETE, value="/dbtest/expected/customService_testAddPhone.xml")
  public void testAddPhone() {
    customService.addCustomerPhone("CLIKE", "555-1234");
    customService.addCustomerPhone("CLIKE", "02-3456");
  }
  
  @Test
  @DatabaseSetup("/dbtest/setup/customSampleData2.xml")
  public void testCountByLastName() {
    assertThat(customService.countByLastName("CLIKE"), is(1));
  }
  
}
