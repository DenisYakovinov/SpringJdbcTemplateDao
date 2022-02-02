package img.imaginary.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Student;

@Component
public class TimetableGroupExtractor implements ResultSetExtractor<Group> {

    private static final String TIMETABLE_ID = "timetable_class_id";
    
    private RowMapper<Student> timetableStudentMapper;
    
    @Autowired
    public TimetableGroupExtractor(@Qualifier("timetableStudentMapper") RowMapper<Student> timetableStudentMapper) {
        this.timetableStudentMapper = timetableStudentMapper;
    }
    
    @Override
    public Group extractData(ResultSet rs) throws SQLException, DataAccessException {
        Group group = new Group();
        int timetableClassId = rs.getInt(TIMETABLE_ID);
        int groupId = rs.getInt("group_id");
        if (groupId != 0) {
            group = new Group(groupId, rs.getString("group_name"), new ArrayList<>(), rs.getString("specialty"));
            addStudent(rs, group);
        }
        while (rs.next()) {
            groupId = rs.getInt("group_id");
            if (group.getGroupId() != groupId || timetableClassId != rs.getInt(TIMETABLE_ID)) {
                rs.previous();
                break;
            }
            addStudent(rs, group);
        }
        return group;
    }

    private void addStudent(ResultSet rs, Group group) throws SQLException {
        if (rs.getInt("student_id") != 0) {
            group.addStudent(timetableStudentMapper.mapRow(rs, 0));
        }
    }
}
