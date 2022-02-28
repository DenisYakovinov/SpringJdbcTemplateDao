package img.imaginary.dao;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import img.imaginary.exception.DaoException;
import img.imaginary.service.entity.Student;

@Loggable
@Repository
public class StudentDaoImpl implements StudentDao {
    
    private static final String NOT_FOUND_STUDENT = "The student with id = %d wasn't found";
    
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
        try {
            namedParameterJdbcTemplate.update(
                    "INSERT INTO students (first_name, last_name, year_number, admission, email) "
                            + "VALUES (:first_name, :last_name, :year, :admission, :email)",
                    namedParameters, keyHolder, new String[] { "student_id" });
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Student %s %s can't be added (%s)", student.getFirstName(),
                    student.getLastName(), e.getMessage()), e);
        }
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue);
    }

    @Override
    public List<Student> findAll() {
        try {
            return jdbcTemplate.query("SELECT * FROM students", studentMapper);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't get students (%s)", e.getMessage()), e);
        }
    }

    @Override
    public Student findById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM students WHERE student_id = ?", studentMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException(String.format(NOT_FOUND_STUDENT, id), e);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't find student with id = %d (%s)", id, e.getMessage()));
        }
    }

    @Override
    public void update(Student student) {
        int id = student.getStudentId();
        SqlParameterSource namedParameters = fillNamedParameters(student);
        try {
            int updatedRowsNumber = namedParameterJdbcTemplate.update(
                    "UPDATE students SET (first_name, last_name, year_number, admission, email) ="
                            + " (:first_name, :last_name, :year, :admission, :email) WHERE student_id = :id",
                    namedParameters);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_STUDENT, id));
            }
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't update student with %d = id (%s)", id, e.getMessage()), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            int updatedRowsNumber = jdbcTemplate.update("DELETE FROM students WHERE student_id = ?", id);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_STUDENT, id));
            }
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't delete student with id = '%d' (%s)", id, e.getMessage()), e);
        }
    }

    @Override
    public void delete(Student student) {
        delete(student.getStudentId());
    }

    @Override
    public void addToGroup(int groupId, int studentId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("student_id", studentId).addValue("group_id",
                groupId);
        try {
            int updatedRowsNumber = namedParameterJdbcTemplate
                    .update("UPDATE students SET group_id = :group_id WHERE student_id = :student_id", namedParameters);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_STUDENT, studentId));
            }
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't update student with id = '%d' (%s)", studentId, e.getMessage()),
                    e);
        }
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

