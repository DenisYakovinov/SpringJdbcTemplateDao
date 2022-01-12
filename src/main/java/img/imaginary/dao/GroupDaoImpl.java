package img.imaginary.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import img.imaginary.dao.mapper.GroupResultSetExtractor;
import img.imaginary.dao.mapper.StudentMapper;
import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Student;

@Repository
public class GroupDaoImpl implements GroupDao {
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder;

    @Autowired
    public GroupDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate,
            KeyHolder keyHolder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = keyHolder;
    }

    @Override
    public Optional<Integer> add(Group group) {
        SqlParameterSource namedParameters = fillNamedParameters(group);
        namedParameterJdbcTemplate.update("INSERT INTO groups (group_name, specialty) VALUES (:name, :specialty)",
                namedParameters, keyHolder, new String[] { "group_id" });
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue);
    }

    @Override
    public List<Group> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM groups AS g LEFT JOIN students AS s ON g.group_id = s.group_id ORDER BY g.group_id",
                new GroupResultSetExtractor());
    }

    @Override
    public Group findById(int id) {
        List<Group> groups = jdbcTemplate.query(
                "SELECT * FROM groups AS g LEFT JOIN students AS s ON g.group_id = s.group_id WHERE g.group_id = ?"
                        + " ORDER BY g.group_id",
                new GroupResultSetExtractor(), id);
        if (CollectionUtils.isEmpty(groups)) {
            throw new EmptyResultDataAccessException(1);
        }
        if (groups.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, groups.size());
        }
        return groups.iterator().next();
    }

    @Override
    public void update(Group group) {
        SqlParameterSource namedParameters = fillNamedParameters(group);
        namedParameterJdbcTemplate.update(
                "UPDATE groups SET (group_name, specialty) = VALUES (:name, :specialty) WHERE group_id = :id",
                namedParameters);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM groups WHERE group_id = ?", id);
    }

    @Override
    public void delete(Group group) {
        delete(group.getGroupId());
    }

    @Override
    public List<Student> getStudents(int groupId) {
        return jdbcTemplate.query(
                "SELECT * FROM students AS s JOIN groups AS g ON s.group_id = g.group_id WHERE g.group_id = ?",
                new StudentMapper(), groupId);
    }

    private SqlParameterSource fillNamedParameters(Group group) {
        return new MapSqlParameterSource("id", group.getGroupId()).addValue("name", group.getGroupName())
                .addValue("specialty", group.getSpecialty());
    }
}

