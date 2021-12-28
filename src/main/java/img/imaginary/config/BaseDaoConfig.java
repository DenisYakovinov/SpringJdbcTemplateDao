package img.imaginary.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@Configuration
public class BaseDaoConfig {
   
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(BasicDataSource basicDataSource) {
       return new NamedParameterJdbcTemplate(basicDataSource);
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(BasicDataSource basicDataSource) {
       return new JdbcTemplate(basicDataSource);
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public static KeyHolder keyHolder() {
        return new GeneratedKeyHolder();
    }
}
