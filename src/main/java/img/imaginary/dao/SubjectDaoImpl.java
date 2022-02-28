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
import img.imaginary.exception.DaoException;
import img.imaginary.service.entity.Subject;

@Loggable
@Repository
public class SubjectDaoImpl implements SubjectDao {
    
    private static final String NOT_FOUND_SUBJECT = "The subject with id = %d wasn't found";
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder;
    private RowMapper<Subject> subjectMapper;
    
    @Autowired
    public SubjectDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate,
            KeyHolder keyHolder, RowMapper<Subject> subjectMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = keyHolder;
        this.subjectMapper = subjectMapper;
    }
    
    @Override
    public Optional<Integer> add(Subject subject) {
        SqlParameterSource namedParameters = fillNamedParameters(subject);
        try {
            namedParameterJdbcTemplate.update("INSERT INTO subjects (name, description) VALUES (:name, :description)",
                    namedParameters, keyHolder, new String[] { "subject_id" });
        } catch (DataAccessException e) {
            throw new DaoException(
                    String.format("Subjects %d can't be added (%s)", subject.getSubjectId(), e.getMessage()), e);
        }
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue);
    }

    @Override
    public List<Subject> findAll() {
        try {
            return jdbcTemplate.query("SELECT * FROM subjects", subjectMapper);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't get subjects (%s)", e.getMessage()), e);
        }
    }

    @Override
    public Subject findById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM subjects WHERE subject_id = ?", subjectMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException(String.format(NOT_FOUND_SUBJECT, id), e);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't find subject with id = %d (%s)", id, e.getMessage()));
        }
    }
    
    @Override
    public void update(Subject subject) {
        SqlParameterSource namedParameters = fillNamedParameters(subject);
        int id = subject.getSubjectId();
        try {
            int updatedRowsNumber = namedParameterJdbcTemplate.update(
                    "UPDATE subjects set (name, description) = (:name, :description) WHERE subject_id = :id",
                    namedParameters);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_SUBJECT, id));
            }
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't update subject with id = %d (%s)", id, e.getMessage()), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            int updatedRowsNumber = jdbcTemplate.update("DELETE FROM subjects WHERE subject_id = ?", id);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_SUBJECT, id));
            }
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't delete subject with id = %d (%s)", id, e.getMessage()), e);
        }
    }

    @Override
    public void delete(Subject subject) {
        delete(subject.getSubjectId());
    }
    
    private SqlParameterSource fillNamedParameters(Subject subject) {
        return new MapSqlParameterSource("id", subject.getSubjectId())
                .addValue("name", subject.getName())
                .addValue("description", subject.getDescription());
    }
}

