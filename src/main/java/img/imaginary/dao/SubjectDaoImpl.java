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

import img.imaginary.service.entity.Subject;

@Repository
public class SubjectDaoImpl implements SubjectDao {

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
        namedParameterJdbcTemplate.update("INSERT INTO subjects (name, description) VALUES (:name, :description)",
                namedParameters, keyHolder,  new String[] { "subject_id" });
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue);  
    }

    @Override
    public List<Subject> findAll() {
        return jdbcTemplate.query("SELECT * FROM subjects", subjectMapper);
    }

    @Override
    public Subject findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM subjects WHERE subject_id = ?", subjectMapper, id);
    }
    
    @Override
    public void update(Subject subject) {
        SqlParameterSource namedParameters = fillNamedParameters(subject);
        namedParameterJdbcTemplate.update(
                "UPDATE subjects set (name, description) = (:name, :description) WHERE subject_id = :id",
                namedParameters);
    } 

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM subjects WHERE subject_id = ?", id);
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

