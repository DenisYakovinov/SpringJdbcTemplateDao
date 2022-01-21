package img.imaginary.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.TimetableClass;

public class TimetableResultSetEtractor implements ResultSetExtractor<List<TimetableClass>> {

    private static final String TIMETABLE_ID = "timetable_class_id";
    
    @Override
    public List<TimetableClass> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<TimetableClass> classes = new ArrayList<>();
        while (rs.next()) {
            TimetableClass nextClass = mapToTimetableClass(rs);
            classes.add(nextClass);
        }
        return classes;
    }

    private TimetableClass mapToTimetableClass(ResultSet rs) throws SQLException {
        return TimetableClass.builder().timetableId(rs.getInt(TIMETABLE_ID))
                                       .subject(new SubjectMapper().mapRow(rs, 0))
                                       .dayOfWeek(DayOfWeek.of(rs.getInt("day_of_week")))
                                       .classNumber(rs.getInt("class_number"))
                                       .audience(new AudienceMapper().mapRow(rs, 0))
                                       .teacher(new TeacherMapper().mapRow(rs, 0))
                                       .group(new ResultSetExtractor<Group>() {
                                
                                           @Override
                                           public Group extractData(ResultSet rs) throws SQLException, DataAccessException {
                                               Group group = new Group();
                                               int timetableClassId = rs.getInt(TIMETABLE_ID);
                                               int groupId = rs.getInt("group_id");
                                               if (groupId != 0) {
                                                   group = new Group(groupId, rs.getString("group_name"), new ArrayList<>(),
                                                           rs.getString("specialty"));
                                                   addStudent(rs, group);
                                               }
                                               while (rs.next()) {
                                                   groupId = rs.getInt("group_id");
                                                   if (group.getGroupId() != groupId 
                                                           || timetableClassId != rs.getInt(TIMETABLE_ID)) {
                                                       rs.previous();
                                                       break;
                                                   }
                                                   addStudent(rs, group);
                                               }
                                               return group;
                                           }

                                           private void addStudent(ResultSet rs, Group group) throws SQLException {
                                               if (rs.getInt("student_id") != 0) {
                                                     group.addStudent(new TimetableStudentMapper().mapRow(rs, 0));
                                               }
                                           }
                                       }.extractData(rs))
                                       .build();
    }
}
