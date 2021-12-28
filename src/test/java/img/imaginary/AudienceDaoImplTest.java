package img.imaginary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestDataSourceConfig;
import img.imaginary.dao.AudienceDao;
import img.imaginary.service.entity.Audience;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDataSourceConfig.class)
@Sql(scripts = "/insertTestAudiences.sql")
class AudienceDaoImplTest {

    @Autowired
    AudienceDao audienceDaoImpl;
    
    @Test
    void findAll_ShouldReturnAllAudiences() {
        List<Audience> expected = Arrays.asList(
                new Audience(1, "lecture", 1, LocalTime.of(8, 30, 0), LocalTime.of(20, 0, 0)),
                new Audience(2, "seminar", 2, LocalTime.of(8, 30, 0), LocalTime.of(18, 0, 0)),
                new Audience(3, "interactive", 3, LocalTime.of(8, 30, 0), LocalTime.of(18, 0, 0)));      
        assertEquals(expected, audienceDaoImpl.findAll());
    }
    
    @Test
    void findById_ShouldReturnAudienceWithSpecifiedID_WhenAudienceId() {
        Audience expected = new Audience(1, "lecture", 1, LocalTime.of(8, 30, 0), LocalTime.of(20, 0, 0));
        assertEquals(expected, audienceDaoImpl.findById(1));
    }
}

