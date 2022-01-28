package img.imaginary.service;

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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestServiceConfig;
import img.imaginary.dao.TimetableClassDao;
import img.imaginary.service.entity.Audience;
import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Student;
import img.imaginary.service.entity.Subject;
import img.imaginary.service.entity.Teacher;
import img.imaginary.service.entity.TimetableClass;
import img.imaginary.service.timetable.Timetable;
import img.imaginary.service.timetable.TimetableDay;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfig.class)
class TimetableClassServiceTest {

    @Autowired
    TimetableService timetableServiceImpl;
    
    @Autowired
    TimetableClassDao timetableClassDao;
    
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
        Mockito.when(timetableClassDao.findAll()).thenReturn(expected);
        assertEquals(expected, timetableServiceImpl.findAll());
    }   
    
    @Test
    void findById_ShouldReturnTimetableClassesWithSpecifiedID_WhenTimetableLineId() {
        TimetableClass expected = classes.get(0);
        Mockito.when(timetableClassDao.findById(1)).thenReturn(expected);
        assertEquals(expected, timetableServiceImpl.findById(1));
    }

    @Test
    void getStudentTimetable_ShouldReturnStudentTimetable_WhenStudentIdAndDates() {
        List<TimetableClass> timetableClasses = Arrays.asList(classes.get(0), classes.get(1));
        Timetable expected = new Timetable(new ArrayList<>());
        TimetableDay timetableDay = new TimetableDay(LocalDate.of(2022, 1, 24),
                Arrays.asList(classes.get(0), classes.get(1)));
        expected.addDay(timetableDay);
        Mockito.when(timetableClassDao.getStudentTimetable(1,
                new HashSet<>(Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))))
                .thenReturn(timetableClasses);
        assertEquals(expected,
                timetableServiceImpl.getStudentTimetable(1, LocalDate.of(2022, 1, 24), LocalDate.of(2022, 1, 26)));
    }

    @Test
    void getTeacherTimetable_ShouldReturnTeacherTimetable_WhenTeacherIdAndDates() {
        List<TimetableClass> timetableClasses = Arrays.asList(classes.get(0), classes.get(2));
        Timetable expected = new Timetable(new ArrayList<>());
        expected.addDay(new TimetableDay(LocalDate.of(2022, 1, 24), Arrays.asList(classes.get(0))));
        expected.addDay(new TimetableDay(LocalDate.of(2022, 1, 26), Arrays.asList(classes.get(2))));
        Mockito.when(timetableClassDao.getTeacherTimetable(1,
                new HashSet<>(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY))))
                .thenReturn(timetableClasses);
        assertEquals(expected,
                timetableServiceImpl.getTeacherTimetable(1, LocalDate.of(2022, 1, 24), LocalDate.of(2022, 1, 26)));
    }
}
