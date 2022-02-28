package img.imaginary.dao;

import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import img.imaginary.aspect.Loggable;
import img.imaginary.aspect.NonLoggableParameter;
import img.imaginary.exception.DaoException;
import img.imaginary.service.entity.TimetableClass;

@Loggable
@Repository
public class TimetableClassDaoImpl implements TimetableClassDao {
        
    private static final String NOT_FOUND_TIMETABLE_CLASS = "The timetableClass with id = %d wasn't found";
    private static final String NOT_FOUND_TIMETABLE = "timetable wasn't found (group hasn't found or the timetable is"
            + " not exist in specified days)";
    
    private static final String BASE_QUERY = String.join(System.lineSeparator(),
            "SELECT * FROM timetable_classes AS tl",
            "LEFT JOIN subjects AS sb ON tl.subject_id = sb.subject_id",
            "LEFT JOIN audiences AS ad ON tl.audience_id = ad.audience_id",
            "LEFT JOIN teachers AS tc ON tl.teacher_id = tc.teacher_id",
            "LEFT JOIN groups AS gr ON tl.group_id = gr.group_id",
            "LEFT JOIN students AS st ON gr.group_id = st.group_id");
    private static final String ORDER_BY = "ORDER BY day_of_week, class_number";
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder;
    private ResultSetExtractor<List<TimetableClass>> timetableExtractor;
    
    @Autowired
    public TimetableClassDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate,
            KeyHolder keyHolder,
            @Qualifier("timetableResultSetExtractor") ResultSetExtractor<List<TimetableClass>> timetableExtractor) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = keyHolder;
        this.timetableExtractor = timetableExtractor;
    }
    
    @Override
    public Optional<Integer> add(TimetableClass timetableClass) {
        SqlParameterSource namedParameters = fillNamedParameters(timetableClass);
        String query = String.join(System.lineSeparator(),
                       "INSERT INTO timetable_classes (day_of_week, class_number, ",
                       "subject_id, group_id, audience_id, teacher_id) ",
                       "VALUES (:dayOfWeek, :classNumber, :subjectId, ",
                       ":groupId, :audienceId, :teacherId)");
        try {
            namedParameterJdbcTemplate.update(query, namedParameters, keyHolder, 
                    new String[] { "timetable_class_id" });
        } catch (DataAccessException e) {
            throw new DaoException(
                    String.format("timetableClass %s can't be added (%s)", timetableClass.toString(), e.getMessage()),
                    e);
        }
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue);
    }

    @Override
    public List<TimetableClass> findAll() {
        String query = String.join(System.lineSeparator(), BASE_QUERY, ORDER_BY);
        PreparedStatementCreatorFactory pc = new PreparedStatementCreatorFactory(query);
        pc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        pc.setUpdatableResults(false);
        try {
            return jdbcTemplate.query(pc.newPreparedStatementCreator(new ArrayList<>()), timetableExtractor);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't get timetableClasses (%s)", e.getMessage()), e);
        }
    }

    @Override
    public TimetableClass findById(int id) {
        String query = String.join(System.lineSeparator(), BASE_QUERY, "WHERE timetable_class_id = ?", ORDER_BY);
        PreparedStatementCreatorFactory pc = new PreparedStatementCreatorFactory(query);
        pc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        pc.setUpdatableResults(false);
        List<TimetableClass> timetableClasses;
        try {
            timetableClasses = jdbcTemplate.query(pc.newPreparedStatementCreator(new ArrayList<>()),
                    ps -> ps.setInt(1, id), timetableExtractor);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't find timetableClass with id = %d (%s)", id, e.getMessage()));
        }
        if (CollectionUtils.isEmpty(timetableClasses)) {
            throw new DaoException(String.format(NOT_FOUND_TIMETABLE_CLASS, id),
                    new EmptyResultDataAccessException(1));
        }
        if (timetableClasses.size() > 1) {
            throw new DaoException("Incorrect number of timetableClasses",
                    new IncorrectResultSizeDataAccessException(1, timetableClasses.size()));
        }
        return timetableClasses.iterator().next();
    }

    @Override
    public void update(TimetableClass timetableClass) {
        SqlParameterSource namedParameters = fillNamedParameters(timetableClass); 
        String query = String.join(System.lineSeparator(),
                       "UPDATE timetable_classes SET (day_of_week, class_number, ",
                       "subject_id, group_id, audience_id, teacher_id) = ",
                       "(:dayOfWeek, :classNumber, :subjectId, ",
                       ":groupId, :audienceId, :teacherId)",
                       "WHERE timetable_class_id = :timetableClassId");
        int id = timetableClass.getTimetableId();
        try {
            int updatedRowsNumber = namedParameterJdbcTemplate.update(query, namedParameters);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_TIMETABLE_CLASS, id));
            }
        } catch (DataAccessException e) {
            throw new DaoException(
                    String.format("Can't update timetableClass with id = %d (%s)", id, e.getMessage()), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            int updatedRowsNumber = jdbcTemplate.update("DELETE FROM timetable_classes WHERE timetable_class_id = ?",
                    id);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_TIMETABLE_CLASS, id));
            }
        } catch (DataAccessException e) {
            throw new DaoException(
                    String.format("Can't delete timetableClass with id = %d (%s)", id, e.getMessage()), e);
        }
    }

    @Override
    public void delete(TimetableClass timetableClass) {
        delete(timetableClass.getTimetableId());
    }

    @Override
    public List<TimetableClass> getStudentTimetable(@NonLoggableParameter int groupId, Set<DayOfWeek> days) {
        String query = String.join(System.lineSeparator(), BASE_QUERY, " WHERE day_of_week IN", buildRangeToQuery(days),
                "AND gr.group_id = ?", ORDER_BY);
        PreparedStatementCreatorFactory pc = new PreparedStatementCreatorFactory(query);
        pc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        pc.setUpdatableResults(false);
        List<TimetableClass> classes;
        try {
            classes = jdbcTemplate.query(pc.newPreparedStatementCreator(new ArrayList<>()), ps -> ps.setInt(1, groupId),
                    timetableExtractor);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't get studentTimetable (%s)", e.getMessage()), e);
        }
        if (CollectionUtils.isEmpty(classes)) {
            throw new DaoException(String.format("Student %s ", NOT_FOUND_TIMETABLE));               
        }
        return classes;
    }

    @Override
    public List<TimetableClass> getTeacherTimetable(int teacherId, Set<DayOfWeek> days) {
        String query = String.join(System.lineSeparator(), BASE_QUERY, " WHERE day_of_week IN", buildRangeToQuery(days),
                "AND tc.teacher_id = ?", ORDER_BY);
        PreparedStatementCreatorFactory pc = new PreparedStatementCreatorFactory(query);
        pc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        pc.setUpdatableResults(false);
        List<TimetableClass> classes;
        try {
            classes = jdbcTemplate.query(pc.newPreparedStatementCreator(new ArrayList<>()),
                    ps -> ps.setInt(1, teacherId), timetableExtractor);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't get teacherTimetable (%s)", e.getMessage()), e);
        }
        if (CollectionUtils.isEmpty(classes)) {
            throw new DaoException(String.format("Teacher %s ", NOT_FOUND_TIMETABLE));
        }
        return classes;
    }
    
    public boolean isAdienceBusy(DayOfWeek day, int classNumber, int audienceId) {
        String query = "SELECT count(*) FROM timetable_classes WHERE day_of_week = ? AND class_number = ? AND audience_id = ?";
        try {
            return jdbcTemplate.queryForObject(query, Integer.class, day.getValue(), classNumber, audienceId) > 0;
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Сan't check if audience is busy (%s)", e.getMessage()), e);
        }
    }
    
    public boolean isTeacherBusy(DayOfWeek day, int classNumber, int teacherId) {
        String query = "SELECT count(*) FROM timetable_classes WHERE day_of_week = ? AND class_number = ? AND teacher_id = ?";
        try {
            return jdbcTemplate.queryForObject(query, Integer.class, day.getValue(), classNumber, teacherId) > 0;
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Сan't check if teacher is busy (%s)", e.getMessage()), e);
        }
    }

    public boolean isGroupBusy(DayOfWeek day, int classNumber, int groupId) {
        String query = "SELECT count(*) FROM timetable_classes WHERE day_of_week = ? AND class_number = ? AND group_id = ?";
        try {
            return jdbcTemplate.queryForObject(query, Integer.class, day.getValue(), classNumber, groupId) > 0;
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't check if group is busy (%s)", e.getMessage()), e);
        }
    }

    private SqlParameterSource fillNamedParameters(TimetableClass timetableClass) {
        return new MapSqlParameterSource("timetableClassId", timetableClass.getTimetableId())
                .addValue("dayOfWeek", timetableClass.getDayOfWeek().getValue())
                .addValue("classNumber", timetableClass.getClassNumber())
                .addValue("subjectId", timetableClass.getSubject().getSubjectId())
                .addValue("groupId", timetableClass.getGroup().getGroupId())
                .addValue("audienceId", timetableClass.getAudience().getAudienceId())
                .addValue("teacherId", timetableClass.getTeacher().getTeacherId());
    }

    private String buildRangeToQuery(Set<DayOfWeek> days) {
        return days.stream().mapToInt(DayOfWeek::getValue).mapToObj(String::valueOf)
                .collect(Collectors.joining(", ", "(", ")"));
    }
}
