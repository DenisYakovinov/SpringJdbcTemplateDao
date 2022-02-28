package img.imaginary.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import img.imaginary.dao.mapper.SubjectMapper;
import img.imaginary.exception.DaoException;
import img.imaginary.config.TestDaoConfig;
import img.imaginary.service.entity.Subject;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/insertTestSubjects.sql")
class SubjectDaoImplTest {

    @Autowired
    SubjectDao subjectDaoImpl;
    
    @Autowired
    @Qualifier("noConnectionDataSource")
    BasicDataSource noConnectionDataSource;

    @Test
    void findAll_ShouldReturnAllSubjects() {
        List<Subject> expected = Arrays.asList(new Subject(1, "math", "base course"),
                new Subject(2, "history", "base course"), new Subject(3, "economy", "base course"));
        assertEquals(expected, subjectDaoImpl.findAll());
    }

    @Test
    void findById_ShouldReturnSubjectWithSpecifiedID_WhenSubjectId() {
        Subject expected = new Subject(1, "math", "base course");
        assertEquals(expected, subjectDaoImpl.findById(1));
    }

    @Test
    void findById_ShouldThrowDaoException_WhenSubjectNotFound() {
        assertThrows(DaoException.class, () -> subjectDaoImpl.findById(0));
    }

    @Test
    void findAll_ShouldThrowDaoException_WhenNotCorrectConnection() {
        SubjectDao subjectDaoImpl = new SubjectDaoImpl(new NamedParameterJdbcTemplate(noConnectionDataSource),
                new JdbcTemplate(noConnectionDataSource), new GeneratedKeyHolder(), new SubjectMapper());
        assertThrows(DaoException.class, () -> subjectDaoImpl.findAll());
    }

    @DirtiesContext
    @Sql("/dropAllobjects.sql")
    @Test
    void findAll_ShouldThrowDaoException_WhenTablesNotExist() {
        assertThrows(DaoException.class, () -> subjectDaoImpl.findAll());
    }
}