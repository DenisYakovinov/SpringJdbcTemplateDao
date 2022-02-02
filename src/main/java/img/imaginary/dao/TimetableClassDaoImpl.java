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

import img.imaginary.service.entity.TimetableClass;

@Repository
public class TimetableClassDaoImpl implements TimetableClassDao {
    
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
        namedParameterJdbcTemplate.update(query, namedParameters, keyHolder, new String[] { "timetable_class_id" });
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue); 
    }

    @Override
    public List<TimetableClass> findAll() {
        String query = String.join(System.lineSeparator(), BASE_QUERY, ORDER_BY);
        PreparedStatementCreatorFactory pc = new PreparedStatementCreatorFactory(query);
        pc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        pc.setUpdatableResults(false);
        return jdbcTemplate.query(pc.newPreparedStatementCreator(new ArrayList<>()), timetableExtractor);
    }

    @Override
    public TimetableClass findById(int id) {
        String query = String.join(System.lineSeparator(), BASE_QUERY, "WHERE timetable_class_id = ?", ORDER_BY);
        PreparedStatementCreatorFactory pc = new PreparedStatementCreatorFactory(query);
        pc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        pc.setUpdatableResults(false);
        List<TimetableClass> timetableClasses = jdbcTemplate.query(pc.newPreparedStatementCreator(new ArrayList<>()),
                ps -> ps.setInt(1, id), timetableExtractor);
        if (CollectionUtils.isEmpty(timetableClasses)) {
            throw new EmptyResultDataAccessException(1);
        }
        if (timetableClasses.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, timetableClasses.size());
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
        namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM timetable_classes WHERE timetable_class_id = ?", id);
    }

    @Override
    public void delete(TimetableClass timetableClass) {
        delete(timetableClass.getTimetableId());
    }

    @Override
    public List<TimetableClass> getStudentTimetable(int groupId, Set<DayOfWeek> days) {
        String query = String.join(System.lineSeparator(), BASE_QUERY, " WHERE day_of_week IN", buildRangeToQuery(days),
                "AND gr.group_id = ?", ORDER_BY);
        PreparedStatementCreatorFactory pc = new PreparedStatementCreatorFactory(query);
        pc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        pc.setUpdatableResults(false);
        return jdbcTemplate.query(pc.newPreparedStatementCreator(new ArrayList<>()), ps -> ps.setInt(1, groupId),
                timetableExtractor);
    }

    @Override
    public List<TimetableClass> getTeacherTimetable(int teacherId, Set<DayOfWeek> days) {        
        String query = String.join(System.lineSeparator(), BASE_QUERY, " WHERE day_of_week IN", buildRangeToQuery(days),
                "AND tc.teacher_id = ?", ORDER_BY);
        PreparedStatementCreatorFactory pc = new PreparedStatementCreatorFactory(query);
        pc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        pc.setUpdatableResults(false);
        return jdbcTemplate.query(pc.newPreparedStatementCreator(new ArrayList<>()), ps -> ps.setInt(1, teacherId),
                timetableExtractor);
    }   
    
    public boolean isAdienceBusy(DayOfWeek day, int classNumber, int audienceId) {
        String query = "SELECT count(*) FROM timetable_classes WHERE day_of_week = ? AND class_number = ? AND audience_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, day.getValue(), classNumber, audienceId) > 0;
    }
    
    public boolean isTeacherBusy(DayOfWeek day, int classNumber, int teacherId) {
        String query = "SELECT count(*) FROM timetable_classes WHERE day_of_week = ? AND class_number = ? AND teacher_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, day.getValue(), classNumber, teacherId) > 0;
    }
    
    public boolean isGroupBusy(DayOfWeek day, int classNumber, int groupId) {
        String query = "SELECT count(*) FROM timetable_classes WHERE day_of_week = ? AND class_number = ? AND group_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, day.getValue(), classNumber, groupId) > 0;
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
