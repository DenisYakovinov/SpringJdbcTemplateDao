package img.imaginary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestServiceConfig;
import img.imaginary.dao.GroupDao;
import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfig.class)
class GroupServiceImplTest {
    
    @Autowired
    GroupService groupServiceImpl;
    
    @Autowired
    GroupDao groupDao;
    
    List<Student> students = Arrays.asList(
            new Student(1, "foo", "bar", 2, LocalDate.of(2019, Month.AUGUST, 28), "foo@bar.com"),
            new Student(2, "Andrew", "Anderson", 3, LocalDate.of(2020, Month.AUGUST, 25), "andrew@and.com"),
            new Student(3, "Jonh", "Doe", 3, LocalDate.of(2020, Month.AUGUST, 23), "jonh@doe.com"));
    
    @Test
    void findAll_ShouldReturnAllGroups() {
        Group group = new Group(1, "xx-zz", null, "math sciences");
        group.setStudents(students);
        List<Group> expected = Arrays.asList(group, new Group(2, "zz-cc", new ArrayList<>(), "sport"),
                new Group(3, "cc-vv", new ArrayList<>(), "humanitarian sciences"));
        Mockito.when(groupDao.findAll()).thenReturn(expected);
        assertEquals(expected, groupServiceImpl.findAll());
    }
    
    @Test
    void findById_ShouldReturnGroupWithSpecifiedID_WhenGroupId() {
        Group expected = new Group(1, "xx-zz", null, "math sciences");
        expected.setStudents(students);
        Mockito.when(groupDao.findById(1)).thenReturn(expected);
        assertEquals(expected, groupServiceImpl.findById(1));
    }
    
    @Test
    void getStudents_ShouldReturnStudentsLinkedWithGroup_WhenGroupId() {
        List<Student> expected = students;
        Mockito.when(groupDao.getStudents(1)).thenReturn(expected);
        assertEquals(expected, groupServiceImpl.getStudents(1));
    }
    
    @Test
    void findById_ShouldReturnGroupWithSpecifiedIDWithoutStudents_WhenGroupId() {
        Group expected = new Group(1, "xx-zz", new ArrayList<Student>(), "math sciences");
        Mockito.when(groupDao.findById(1)).thenReturn(expected);
        assertEquals(expected, groupServiceImpl.findById(1));
    }
}
