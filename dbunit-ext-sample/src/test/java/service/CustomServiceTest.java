package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.clike.dbunit.dataset.ClikeFlatXmlDataSetLoader;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

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
  @ExpectedDatabase("/dbtest/expected/customService_testAddPhone.xml")
  public void testAddPhone() {
    customService.addCustomerPhone("CLIKE", "555-1234");
    customService.addCustomerPhone("CLIKE", "02-3456");
  }
  
}
