package img.imaginary.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;

import org.springframework.jdbc.core.RowMapper;

import img.imaginary.service.entity.TimetableLine;

public class TimetableLineMapper implements RowMapper<TimetableLine> {

    @Override
    public TimetableLine mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TimetableLine.builder().timetableId(rs.getInt("timetable_line_id"))
                                      .dayOfWeek(DayOfWeek.of(rs.getInt("day_of_week")))
                                      .classnumber(rs.getInt("class_number"))
                                      .subjectId(rs.getInt("subject_id"))
                                      .groupId(rs.getInt("group_id"))
                                      .audienceid(rs.getInt("audience_id"))
                                      .teacherId(rs.getInt("teacher_id"))
                                      .build();
    }
}
