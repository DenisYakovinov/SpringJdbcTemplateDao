package img.imaginary.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import img.imaginary.dao.mapper.TimetableLineMapper;
import img.imaginary.service.entity.TimetableLine;

@Repository
public class TimetableLineDaoImpl implements TimetableLineDao {
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder;
    
    @Autowired
    public TimetableLineDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate,
            KeyHolder keyHolder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = keyHolder;
    }
    
    @Override
    public Optional<Integer> add(TimetableLine timetableLine) {
        SqlParameterSource namedParameters = fillNamedParameters(timetableLine);
        String query = String.join(System.lineSeparator(),
                       "INSERT INTO timetable_lines (day_of_week, class_number, ",
                       "subject_id, group_id, audience_id, teacher_id) ",
                       "VALUES (:dayOfWeek, :classNumber, :subjectId, ",
                       ":groupId, :audienceId, :teacherId)");
        namedParameterJdbcTemplate.update(query, namedParameters, keyHolder, new String[] { "timetable_line_id" });
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue); 
    }

    @Override
    public List<TimetableLine> findAll() {
        return jdbcTemplate.query("SELECT * FROM timetable_lines", new TimetableLineMapper());
    }

    @Override
    public TimetableLine findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM timetable_lines WHERE timetable_line_id = ?",
                new TimetableLineMapper(), id);
    }

    @Override
    public void update(TimetableLine timetableLine) {
        SqlParameterSource namedParameters = fillNamedParameters(timetableLine); 
        String query = String.join(System.lineSeparator(),
                       "UPDATE timetable_lines SET (day_of_week, class_number, ",
                       "subject_id, group_id, audience_id, teacher_id) = ",
                       "(:dayOfWeek, :classNumber, :subjectId, ",
                       ":groupId, :audienceId, :teacherId)",
                       "WHERE timetable_line_id = :timetablelineId");
        namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM timetable_lines WHERE timetable_line_id = ?", id);
    }

    @Override
    public void delete(TimetableLine timetableLine) {
        delete(timetableLine.getTimetableId());
    }

    private SqlParameterSource fillNamedParameters(TimetableLine timetableLine) {
        return new MapSqlParameterSource("timetablelineId", timetableLine.getTimetableId())
                .addValue("dayOfWeek", timetableLine.getDayOfWeek().getValue())
                .addValue("classNumber", timetableLine.getClassnumber())
                .addValue("subjectId", timetableLine.getSubjectId())
                .addValue("groupId", timetableLine.getGroupId())
                .addValue("audienceId", timetableLine.getAudienceid())
                .addValue("teacherId", timetableLine.getTeacherId());
    }
}

