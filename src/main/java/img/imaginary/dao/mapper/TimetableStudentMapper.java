package img.imaginary.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import img.imaginary.service.entity.Student;

@Component
public class TimetableStudentMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Student(rs.getInt("student_id"), rs.getString(25), rs.getString(26), rs.getInt("year_number"),
                rs.getDate("admission").toLocalDate(), rs.getString(29));
    }
}
