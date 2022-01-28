package img.imaginary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestServiceConfig;
import img.imaginary.dao.StudentDao;
import img.imaginary.service.entity.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfig.class)
class StudentServiceImplTest {

    @Autowired
    StudentService studentServiceImpl;

    @Autowired
    StudentDao studentDao;
    
    List<Student> students = Arrays.asList(
            new Student(1, "foo", "bar", 2, LocalDate.of(2019, Month.AUGUST, 28), "foo@bar.com"),
            new Student(2, "Andrew", "Anderson", 3, LocalDate.of(2020, Month.AUGUST, 25), "andrew@and.com"),
            new Student(3, "Jonh", "Doe", 3, LocalDate.of(2020, Month.AUGUST, 23), "jonh@doe.com"));
   
    @Test
    void findAll_ShouldReturnAllStudents() {
        List<Student> expected = students;
        Mockito.when(studentDao.findAll()).thenReturn(expected);
        assertEquals(expected, studentServiceImpl.findAll());
    }

    @Test
    void findById_ShouldReturnStudentWithSpecifiedID_WhenStudentId() {
        Student expected = students.get(0);
        Mockito.when(studentDao.findById(1)).thenReturn(expected);
        assertEquals(expected, studentServiceImpl.findById(1));
    }
}

