package img.imaginary.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import img.imaginary.service.entity.Audience;
import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Subject;
import img.imaginary.service.entity.Teacher;
import img.imaginary.service.entity.TimetableClass;

@Component
public class TimetableResultSetExtractor implements ResultSetExtractor<List<TimetableClass>> {

    private static final String TIMETABLE_ID = "timetable_class_id";
    
    private RowMapper<Subject> subjectMapper;
    private RowMapper<Audience> audienceMapper;
    private RowMapper<Teacher> teacherMapper;
    private ResultSetExtractor<Group> timetableGroupExtractor;
    
    public TimetableResultSetExtractor(RowMapper<Subject> subjectMapper, RowMapper<Audience> audienceMapper,
            RowMapper<Teacher> teacherMapper, ResultSetExtractor<Group> timetableGroupExtractor) {
        this.subjectMapper = subjectMapper;
        this.audienceMapper = audienceMapper;
        this.teacherMapper = teacherMapper;
        this.timetableGroupExtractor = timetableGroupExtractor;
    }
    
    @Override
    public List<TimetableClass> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<TimetableClass> classes = new ArrayList<>();
        while (rs.next()) {
            TimetableClass nextClass = new TimetableMapper().mapRow(rs, 0);
            classes.add(nextClass);
        }
        return classes;
    }

    class TimetableMapper implements RowMapper<TimetableClass> {

        @Override
        public TimetableClass mapRow(ResultSet rs, int rowNum) throws SQLException {
            return TimetableClass.builder().timetableId(rs.getInt(TIMETABLE_ID))
                    .subject(subjectMapper.mapRow(rs, 0))
                    .dayOfWeek(DayOfWeek.of(rs.getInt("day_of_week")))
                    .classNumber(rs.getInt("class_number"))
                    .audience(audienceMapper.mapRow(rs, 0))
                    .teacher(teacherMapper.mapRow(rs, 0))
                    .group(timetableGroupExtractor.extractData(rs))
                    .build();
        }        
    }
}
