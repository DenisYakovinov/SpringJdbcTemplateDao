package img.imaginary.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import img.imaginary.service.entity.Audience;

@Component
public class AudienceMapper implements RowMapper<Audience> {

    @Override
    public Audience mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Audience(rs.getInt("audience_id"), rs.getString("audience_type"), rs.getInt("audience_number"),
                rs.getTime("open_time").toLocalTime(), rs.getTime("closing_time").toLocalTime());
    }
}
