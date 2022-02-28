package img.imaginary.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.Month;
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

import img.imaginary.dao.mapper.StudentMapper;
import img.imaginary.exception.DaoException;
import img.imaginary.config.TestDaoConfig;
import img.imaginary.service.entity.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "/insertTestGroups.sql",
        "/insertTestStudents.sql" })
class StudentDaoImplTest {

    @Autowired
    StudentDao studentDaoImpl;

    @Autowired
    @Qualifier("noConnectionDataSource")
    BasicDataSource noConnectionDataSource;

    @Test
    void findAll_ShouldReturnAllStudents() {
        List<Student> expected = Arrays.asList(
                new Student(1, "foo", "bar", 2, LocalDate.of(2019, Month.AUGUST, 28), "foo@bar.com"),
                new Student(2, "Andrew", "Anderson", 3, LocalDate.of(2020, Month.AUGUST, 25), "andrew@and.com"),
                new Student(3, "Jonh", "Doe", 3, LocalDate.of(2020, Month.AUGUST, 23), "jonh@doe.com"));
        assertEquals(expected, studentDaoImpl.findAll());
    }

    @Test
    void findById_ShouldReturnStudentWithSpecifiedID_WhenStudentId() {
        Student expected = new Student(1, "foo", "bar", 2, LocalDate.of(2019, Month.AUGUST, 28), "foo@bar.com");
        assertEquals(expected, studentDaoImpl.findById(1));
    }

    @Test
    void findById_ShouldThrowDaoException_WhenStudentNotFound() {
        assertThrows(DaoException.class, () -> studentDaoImpl.findById(0));
    }

    @Test
    void findAll_ShouldThrowDaoException_WhenNotCorrectConnection() {
        StudentDao studentDaoImpl = new StudentDaoImpl(new NamedParameterJdbcTemplate(noConnectionDataSource),
                new JdbcTemplate(noConnectionDataSource), new GeneratedKeyHolder(), new StudentMapper());
        assertThrows(DaoException.class, () -> studentDaoImpl.findAll());
    }
    
    @DirtiesContext
    @Sql("/dropAllobjects.sql")
    @Test
    void findAll_ShouldThrowDaoException_WhenTablesNotExist() {
        assertThrows(DaoException.class, () -> studentDaoImpl.findAll());
    }
}
