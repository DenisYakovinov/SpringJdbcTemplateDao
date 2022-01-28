package img.imaginary.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestDataSourceConfig;
import img.imaginary.service.entity.Audience;
import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Student;
import img.imaginary.service.entity.Subject;
import img.imaginary.service.entity.Teacher;
import img.imaginary.service.entity.TimetableClass;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDataSourceConfig.class)
@Sql(scripts = { "/insertTestSubjects.sql", "/insertTestAudiences.sql", "/insertTestTeachers.sql",
        "/insertTestGroups.sql", "/insertTestStudents.sql", "/insertTestTimetableClasses.sql" })
class TimetableClassDaoImplTest {

    @Autowired
    TimetableClassDao timetableLineDaoImpl;
         
    List<Student> students = Arrays.asList(
            new Student(1, "foo", "bar", 2, LocalDate.of(2019, Month.AUGUST, 28), "foo@bar.com"),
            new Student(2, "Andrew", "Anderson", 3, LocalDate.of(2020, Month.AUGUST, 25), "andrew@and.com"),
            new Student(3, "Jonh", "Doe", 3, LocalDate.of(2020, Month.AUGUST, 23), "jonh@doe.com"));   
    
    List<TimetableClass> classes = Arrays.asList(
            TimetableClass.builder().timetableId(1)
                                    .dayOfWeek(DayOfWeek.MONDAY)                                  
                                    .classNumber(1)
                                    .subject(new Subject(1, "math", "base course"))
                                    .group(new Group(1, "xx-zz", students, "math sciences"))
                                    .audience(new Audience(1, "lecture", 1, LocalTime.of(8, 30, 0),
                                           LocalTime.of(20, 0, 0)))
                                    .teacher(new Teacher(1, "jonh", "Doe", "Professional degree", "jonh@doe.com")) 
                                    .build(), 
            TimetableClass.builder().timetableId(2)
                                   .dayOfWeek(DayOfWeek.MONDAY)                                
                                   .classNumber(2)
                                   .subject(new Subject(2, "history", "base course"))
                                   .group(new Group(1, "xx-zz", students, "math sciences"))
                                   .audience(new Audience(2, "seminar", 2, LocalTime.of(8, 30, 0),
                                           LocalTime.of(18, 0, 0)))
                                   .teacher(new Teacher(3, "foo", "bar", "foobar degree", "foo@bar.com")) 
                                   .build(),
            TimetableClass.builder().timetableId(3)
                                   .dayOfWeek(DayOfWeek.WEDNESDAY)                                
                                   .classNumber(1)
                                   .subject(new Subject(2, "history", "base course"))
                                   .group(new Group(2, "zz-cc", new ArrayList<>(), "sport"))
                                   .audience(new Audience(1, "lecture", 1, LocalTime.of(8, 30, 0),
                                           LocalTime.of(20, 0, 0)))
                                   .teacher(new Teacher(1, "jonh", "Doe", "Professional degree", "jonh@doe.com")) 
                                   .build());

    @Test
    void findAll_ShouldReturnAllTimetableClasses() {
        List<TimetableClass> expected = classes;
        assertEquals(expected, timetableLineDaoImpl.findAll());
    }
    
    @Test
    void findById_ShouldReturnTimetableClassesWithSpecifiedID_WhenTimetableLineId() {
        TimetableClass expected = classes.get(0);
        assertEquals(expected, timetableLineDaoImpl.findById(1));
    }
    
    @Test
    void getStudentTimetable_ShouldReturnClassesInSpecifiedDaysAndGroupId_WhenGroupIdAndDaysOfWeek() {
        List<TimetableClass> expected = Arrays.asList(classes.get(0), classes.get(1));
        assertEquals(expected, timetableLineDaoImpl.getStudentTimetable(1,
                new HashSet<>(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))));
    }
    
    @Test
    void getTeacherTimetable_ShouldReturnClassesInSpecifiedDaysAndTeacherId_WhenTeacheridAndDaysOfWeek() {
        List<TimetableClass> expected = Arrays.asList(classes.get(0), classes.get(2));
        assertEquals(expected, timetableLineDaoImpl.getTeacherTimetable(1,
                new HashSet<>(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))));
    }
}
