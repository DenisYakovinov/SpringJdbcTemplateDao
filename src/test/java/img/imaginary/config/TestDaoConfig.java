package img.imaginary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DaoConfig.class, TestDataSourceConfig.class})
public class TestDaoConfig {
}
