package img.imaginary.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestDataSourceConfig;
import img.imaginary.service.entity.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDataSourceConfig.class)
@Sql(scripts = "/InsertTestStudents.sql")
class StudentDaoImplTest {

    @Autowired
    StudentDao studentDaoImpl;

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
}
