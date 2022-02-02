package img.imaginary.dao;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestDaoConfig;
import img.imaginary.service.entity.Teacher;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class)
@Sql(scripts = "/insertTestTeachers.sql")
class TeacherDaoImplTest {

    @Autowired
    TeacherDao teacherDaoImpl;

    @Test
    void findAll_ShouldReturnAllTeachers() {
        List<Teacher> expected = Arrays.asList(new Teacher(1, "jonh", "Doe", "Professional degree", "jonh@doe.com"),
                new Teacher(2, "Uinston", "Smith", "Doctoral degree", "uinston@smith.com"),
                new Teacher(3, "foo", "bar", "foobar degree", "foo@bar.com"));
        assertEquals(expected, teacherDaoImpl.findAll());
    }

    @Test
    void findById_ShouldReturnTeacherWithSpecifiedId_WhenTeacherId() {
        Teacher expected = new Teacher(1, "jonh", "Doe", "Professional degree", "jonh@doe.com");
        assertEquals(expected, teacherDaoImpl.findById(1));
    }
}