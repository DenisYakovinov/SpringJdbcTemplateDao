package img.imaginary.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import img.imaginary.service.entity.Teacher;

@Repository
public class TeacherDaoImpl implements TeacherDao {
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder;
    private RowMapper<Teacher> teacherMapper;

    @Autowired
    public TeacherDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate,
            KeyHolder keyHolder, RowMapper<Teacher> teacherMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = keyHolder;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public Optional<Integer> add(Teacher teacher) {
        SqlParameterSource namedParameters = fillNamedParameters(teacher);
        namedParameterJdbcTemplate.update(
                "INSERT INTO teachers (first_name, last_name, academic_degree, email) VALUES"
                + " (:first_name, :last_name, :academic_degree, :email)",
                namedParameters, keyHolder, new String[] { "teacher_id" });
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue);
    }

    @Override
    public List<Teacher> findAll() {
        return jdbcTemplate.query("SELECT * FROM teachers", teacherMapper);
    }

    @Override
    public Teacher findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM teachers WHERE teacher_id = ?", teacherMapper, id);
    }

    @Override
    public void update(Teacher teacher) {
        SqlParameterSource namedParameters = fillNamedParameters(teacher);
        namedParameterJdbcTemplate.update("UPDATE teachers set (first_name, last_name, academic_degree, email) = "
                + "(:first_name, :last_name, :academic_degree, :email) WHERE teacher_id = :id",
                namedParameters);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM teachers WHERE teacher_id =?", id);
    }

    @Override
    public void delete(Teacher teacher) {
        delete(teacher.getTeacherId());
    }
    
    private SqlParameterSource fillNamedParameters(Teacher teacher) {
        return new MapSqlParameterSource("id", teacher.getTeacherId())
                .addValue("first_name", teacher.getFirstName())
                .addValue("last_name", teacher.getLastName())
                .addValue("academic_degree", teacher.getAcademicDegree())
                .addValue("email", teacher.getEmail());
    }
}
