package img.imaginary.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import img.imaginary.aspect.Loggable;
import img.imaginary.dao.TeacherDao;
import img.imaginary.exception.DaoException;
import img.imaginary.service.entity.Teacher;

@Loggable
@Repository
public class TeacherDaoImpl implements TeacherDao {
    
    private static final String NOT_FOUND_TEACHER = "The teacher with id = %d wasn't found";
    
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
        try {
            namedParameterJdbcTemplate.update(
                    "INSERT INTO teachers (first_name, last_name, academic_degree, email) VALUES"
                            + " (:first_name, :last_name, :academic_degree, :email)",
                    namedParameters, keyHolder, new String[] { "teacher_id" });
            return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Teacher %s %s can't be added (%s)", teacher.getFirstName(),
                    teacher.getLastName(), e.getMessage()), e);
        }
    }

    @Override
    public List<Teacher> findAll() {
        try {
            return jdbcTemplate.query("SELECT * FROM teachers", teacherMapper);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't get subjects (%s)", e.getMessage()), e);
        }
    }

    @Override
    public Teacher findById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM teachers WHERE teacher_id = ?", teacherMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException(String.format(NOT_FOUND_TEACHER, id), e);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't find teacher with id = %d (%s)", id, e.getMessage()));
        }
    }

    @Override
    public void update(Teacher teacher) {
        SqlParameterSource namedParameters = fillNamedParameters(teacher);
        int id = teacher.getTeacherId();
        try {
            int updatedRowsNumber = namedParameterJdbcTemplate.update(
                    "UPDATE teachers set (first_name, last_name, academic_degree, email) = "
                            + "(:first_name, :last_name, :academic_degree, :email) WHERE teacher_id = :id",
                    namedParameters);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_TEACHER, id));
            }
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't update subject with id = %d (%s)", id, e.getMessage()), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            int updatedRowsNumber = jdbcTemplate.update("DELETE FROM teachers WHERE teacher_id =?", id);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_TEACHER, id));
            }
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't delete subject with id = %d (%s)", id, e.getMessage()), e);
        }
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

