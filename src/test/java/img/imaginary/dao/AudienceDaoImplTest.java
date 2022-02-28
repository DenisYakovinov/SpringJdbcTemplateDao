package img.imaginary.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestDaoConfig;
import img.imaginary.dao.mapper.AudienceMapper;
import img.imaginary.exception.DaoException;
import img.imaginary.service.entity.Audience;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@Sql(scripts = "/insertTestAudiences.sql")
class AudienceDaoImplTest {

    @Autowired
    AudienceDao audienceDaoImpl;
    
    @Autowired
    @Qualifier("noConnectionDataSource")
    BasicDataSource noConnectionDataSource;
    
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
    
    @Test
    void findById_ShouldThrowDaoException_WhenAudienceNotFound() {
        assertThrows(DaoException.class, () -> audienceDaoImpl.findById(0));
    }
    
    @Test
    void findAll_ShouldThrowDaoException_WhenNotCorrectConnection() {
        AudienceDaoImpl audienceDaoImpl = new AudienceDaoImpl(new NamedParameterJdbcTemplate(noConnectionDataSource),
                new JdbcTemplate(noConnectionDataSource), new GeneratedKeyHolder(), new AudienceMapper());
        assertThrows(DaoException.class, () -> audienceDaoImpl.findAll());
    }
    
    @DirtiesContext
    @Sql("/dropAllobjects.sql")
    @Test
    void findAll_ShouldThrowDaoException_WhenTablesNotExist() {
        assertThrows(DaoException.class, () -> audienceDaoImpl.findAll());
    }
}


