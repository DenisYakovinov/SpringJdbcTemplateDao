package img.imaginary.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import img.imaginary.service.entity.Teacher;

@Component
public class TeacherMapper implements RowMapper<Teacher> {

    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Teacher(rs.getInt("teacher_id"), rs.getString("first_name"), rs.getString("last_name"),
                rs.getString("academic_degree"), rs.getString("email"));
    }
}
