package config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.clike.dbunit.datatype.ClikeDataTypeFactory;
import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;

@SpringBootApplication(scanBasePackages = "service")
@EntityScan(basePackages="entity")
@EnableJpaRepositories(basePackages="repository")
public class TestApplication {

  @Autowired
  private DataSource dataSource;
  
  @Bean
  public ClikeDataTypeFactory clikeDataTypeFactory() {
    return new ClikeDataTypeFactory();
  }
  
  @Bean
  public DatabaseConfigBean databaseConfigBean(){
    
    DatabaseConfigBean databaseConfigBean = new DatabaseConfigBean();
    databaseConfigBean.setDatatypeFactory(clikeDataTypeFactory());
    
    return databaseConfigBean;
  }
  
  @Bean(name = "dbUnitDatabaseConnection")
  public DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactoryBean() {
    DatabaseDataSourceConnectionFactoryBean dsf = new DatabaseDataSourceConnectionFactoryBean();
    dsf.setDatabaseConfig(databaseConfigBean());
    dsf.setDataSource(dataSource);
    return dsf;
  }
}
