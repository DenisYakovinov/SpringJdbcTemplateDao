package img.imaginary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestServiceConfig;
import img.imaginary.dao.TeacherDao;
import img.imaginary.service.entity.Teacher;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfig.class)
class TeacherServiceImplTest {


    @Autowired
    TeacherService teacherServiceImpl;

    @Autowired
    TeacherDao teacherDao;
    
    List<Teacher> teachers = Arrays.asList(new Teacher(1, "jonh", "Doe", "Professional degree", "jonh@doe.com"),
            new Teacher(2, "Uinston", "Smith", "Doctoral degree", "uinston@smith.com"),
            new Teacher(3, "foo", "bar", "foobar degree", "foo@bar.com"));
    
    @Test
    void findAll_ShouldReturnAllTeachers() {
        List<Teacher> expected = teachers;
        Mockito.when(teacherDao.findAll()).thenReturn(expected);
        assertEquals(expected, teacherServiceImpl.findAll());
    }

    @Test
    void findById_ShouldReturnTeacherWithSpecifiedId_WhenTeacherId() {
        Teacher expected = teachers.get(0);
        Mockito.when(teacherDao.findById(1)).thenReturn(expected);
        assertEquals(expected, teacherServiceImpl.findById(1));
    }
}
