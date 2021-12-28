package img.imaginary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestDataSourceConfig;
import img.imaginary.dao.TimetableLineDao;
import img.imaginary.service.entity.TimetableLine;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDataSourceConfig.class)
@Sql(scripts = { "/insertTestSubjects.sql", "/insertTestAudiences.sql", "/insertTestTeachers.sql",
        "/insertTestGroups.sql", "/insertTestStudents.sql", "/insertTestTimetableLines.sql" })
class TimetableLineDaoImplTest {

    @Autowired
    TimetableLineDao timetableLineDaoImpl;
    
    @Test
    void findAll_ShouldReturnAllStudents() {
        List<TimetableLine> expected = Arrays.asList(
                TimetableLine.builder().dayOfWeek(DayOfWeek.MONDAY)
                                       .timetableId(1)
                                       .classnumber(1)
                                       .subjectId(1)
                                       .groupId(1)
                                       .audienceid(1)
                                       .teacherId(1) 
                                       .build(), 
                TimetableLine.builder().dayOfWeek(DayOfWeek.MONDAY)
                                       .timetableId(2)
                                       .classnumber(2)
                                       .subjectId(2)
                                       .groupId(1)
                                       .audienceid(2)
                                       .teacherId(3) 
                                       .build(),
                TimetableLine.builder().dayOfWeek(DayOfWeek.WEDNESDAY)
                                       .timetableId(3)
                                       .classnumber(1)
                                       .subjectId(2)
                                       .groupId(2)
                                       .audienceid(1)
                                       .teacherId(1) 
                                       .build());
        assertEquals(expected, timetableLineDaoImpl.findAll());
    }
    
    void findById_ShouldReturnTimetableLinesWithSpecifiedID_WhenTimetableLineId() {
        TimetableLine expected = TimetableLine.builder().dayOfWeek(DayOfWeek.MONDAY)
                                                        .timetableId(1)
                                                        .classnumber(1)
                                                        .subjectId(1)
                                                        .groupId(1)
                                                        .audienceid(1)
                                                        .teacherId(1) 
                                                        .build();
        assertEquals(expected, timetableLineDaoImpl.findById(1));
    }
}