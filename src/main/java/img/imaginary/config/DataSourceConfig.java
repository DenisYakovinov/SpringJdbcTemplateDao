package img.imaginary.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("datasource.properties")
@ComponentScan(basePackages = "img.imaginary.dao")
@Import(BaseDaoConfig.class)
public class DataSourceConfig {
    
    @Value ("${db.class}")
    private String className;
    
    @Value ("${db.url}")
    private String url;
    
    @Value ("${db.login}")
    private String login;
    
    @Value ("${db.password}")
    private String password;
    
    @Bean(destroyMethod = "close")
    public BasicDataSource basicDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(className);
        dataSource.setUrl(url);
        dataSource.setUsername(login);
        dataSource.setPassword(password);
        return dataSource;
    }
}

