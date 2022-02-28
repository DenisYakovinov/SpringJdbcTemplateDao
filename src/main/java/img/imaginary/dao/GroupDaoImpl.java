package img.imaginary.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import img.imaginary.aspect.Loggable;
import img.imaginary.exception.DaoException;
import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Student;

@Loggable
@Repository
public class GroupDaoImpl implements GroupDao {
    
    private static final String NOT_FOUND_GROUP = "The group with id = %d wasn't found";
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder;
    private RowMapper<Student> studentmapper;
    private ResultSetExtractor<List<Group>> groupExtractor;

    @Autowired
    public GroupDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate,
            KeyHolder keyHolder, @Qualifier("studentMapper") RowMapper<Student> studentmapper,
            @Qualifier("groupResultSetExtractor") ResultSetExtractor<List<Group>> groupExtractor) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = keyHolder;
        this.studentmapper = studentmapper;
        this.groupExtractor = groupExtractor;
    }

    @Override
    public Optional<Integer> add(Group group) {
        SqlParameterSource namedParameters = fillNamedParameters(group);
        try {
            namedParameterJdbcTemplate.update("INSERT INTO groups (group_name, specialty) VALUES (:name, :specialty)",
                    namedParameters, keyHolder, new String[] { "group_id" });
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Group %s can't be added (%s)", group.getGroupName(), e.getMessage()),
                    e);
        }
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue);
    }

    @Override
    public List<Group> findAll() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM groups AS g LEFT JOIN students AS s ON g.group_id = s.group_id ORDER BY g.group_id",
                    groupExtractor);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't get groups (%s)", e.getMessage()), e);
        }
    }

    @Override
    public Group findById(int id) {
        List<Group> groups;
        try {
            groups = jdbcTemplate.query(
                    "SELECT * FROM groups AS g LEFT JOIN students AS s ON g.group_id = s.group_id WHERE g.group_id = ?"
                            + " ORDER BY g.group_id",
                    groupExtractor, id);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't find group with id = %d (%s)", id, e.getMessage()));
        }
        if (CollectionUtils.isEmpty(groups)) {
            throw new DaoException(String.format(NOT_FOUND_GROUP, id), new EmptyResultDataAccessException(1));
        }
        if (groups.size() > 1) {
            throw new DaoException("Incorrect number of groups",
                    new IncorrectResultSizeDataAccessException(1, groups.size()));
        }
        return groups.iterator().next();
    }

    @Override
    public void update(Group group) {
        SqlParameterSource namedParameters = fillNamedParameters(group);
        try {
            int updatedRowsNumber = namedParameterJdbcTemplate.update(
                    "UPDATE groups SET (group_name, specialty) = VALUES (:name, :specialty) WHERE group_id = :id",
                    namedParameters);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_GROUP, group.getGroupId()));
            }
        } catch (DataAccessException e) {
            throw new DaoException(
                    String.format("Can't update group with id = %d (%s)", group.getGroupId(), e.getMessage()), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            int updatedRowsNumber = jdbcTemplate.update("DELETE FROM groups WHERE group_id = ?", id);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_GROUP, id));
            }
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't delete group with %d = id (%s)", id, e.getMessage()), e);
        }
    }

    @Override
    public void delete(Group group) {
        delete(group.getGroupId());
    }

    @Override
    public List<Student> getStudents(int groupId) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM students AS s JOIN groups AS g ON s.group_id = g.group_id WHERE g.group_id = ?",
                    studentmapper, groupId);
        } catch (DataAccessException e) {
            throw new DaoException(
                    String.format("Can't get students linked with group id %d (%s)", groupId, e.getMessage()), e);
        }
    }

    private SqlParameterSource fillNamedParameters(Group group) {
        return new MapSqlParameterSource("id", group.getGroupId()).addValue("name", group.getGroupName())
                .addValue("specialty", group.getSpecialty());
    }
}

