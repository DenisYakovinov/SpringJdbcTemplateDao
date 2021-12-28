package img.imaginary.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import img.imaginary.service.entity.Student;

public class StudentMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Student(rs.getInt("student_id"), rs.getString("first_name"), rs.getString("last_name"),
                rs.getInt("year_number"), rs.getDate("admission").toLocalDate(), rs.getString("email"));
    }
}
