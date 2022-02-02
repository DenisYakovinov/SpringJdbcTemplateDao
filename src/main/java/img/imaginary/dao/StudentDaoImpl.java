package img.imaginary.dao;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import img.imaginary.service.entity.Student;

@Repository
public class StudentDaoImpl implements StudentDao {
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder;
    private RowMapper<Student> studentMapper;
    
    @Autowired
    public StudentDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate,
            KeyHolder keyHolder, @Qualifier("studentMapper") RowMapper<Student> studentMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = keyHolder;
        this.studentMapper = studentMapper;
    }
    
    @Override
    public Optional<Integer> add(Student student) {
        SqlParameterSource namedParameters = fillNamedParameters(student);
        namedParameterJdbcTemplate.update(
                "INSERT INTO students (first_name, last_name, year_number, admission, email) "
                        + "VALUES (:first_name, :last_name, :year, :admission, :email)",
                namedParameters, keyHolder, new String[] { "student_id" });
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue);    
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query("SELECT * FROM students", studentMapper);
    }

    @Override
    public Student findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM students WHERE student_id = ?", studentMapper, id);
    }

    @Override
    public void update(Student student) {
        SqlParameterSource namedParameters = fillNamedParameters(student);
        namedParameterJdbcTemplate.update(
                "UPDATE students SET (first_name, last_name, year_number, admission, email) ="
                        + " (:first_name, :last_name, :year, :admission, :email) WHERE student_id = :id",
                namedParameters);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM students WHERE student_id = ?", id);
    }

    @Override
    public void delete(Student student) {
        delete(student.getStudentId());
    }

    @Override
    public void addToGroup(int groupId, int studentId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("student_id", studentId).addValue("group_id",
                groupId);
        namedParameterJdbcTemplate.update("UPDATE students SET group_id = :group_id WHERE student_id = :student_id",
                namedParameters);
    }
    
    private SqlParameterSource fillNamedParameters(Student student) {
        return new MapSqlParameterSource("id", student.getStudentId())
                .addValue("first_name", student.getFirstName())
                .addValue("last_name", student.getLastName())
                .addValue("year", student.getYearNumber())
                .addValue("admission",student.getAmdission(), Types.DATE)
                .addValue("email", student.getEmail());
    }
}

