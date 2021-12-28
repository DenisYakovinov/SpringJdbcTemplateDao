package img.imaginary.dao;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import img.imaginary.dao.mapper.AudienceMapper;
import img.imaginary.service.entity.Audience;


@Repository
public class AudienceDaoImpl implements AudienceDao {
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder;

    @Autowired
    public AudienceDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate,
            KeyHolder keyHolder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = keyHolder;
    }

    @Override
    public Optional<Integer> add(Audience audience) {
        SqlParameterSource namedParameters = fillNamedParameters(audience);
        namedParameterJdbcTemplate.update(
                "INSERT INTO audiences (audience_type, audience_number, open_time, closing_time)"
                        + " VALUES (:type, :number, :open, :close)",
                namedParameters, keyHolder, new String[] { "audience_id" });       
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue);
    }

    @Override
    public List<Audience> findAll() {
        return jdbcTemplate.query("SELECT * FROM audiences", new AudienceMapper());
    }

    @Override
    public Audience findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM audiences WHERE audience_id = ?", new AudienceMapper(), id);
    }

    @Override
    public void update(Audience audience) {
        SqlParameterSource namedParameters = fillNamedParameters(audience);
        namedParameterJdbcTemplate
                .update("UPDATE audiences SET (audience_type, audience_number, open_time, closing_time) = "
                        + "(:type, :number, :close) WHERE audience_id = :id", namedParameters);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM audiences WHERE audience_id = ?", id);
    }

    @Override
    public void delete(Audience audience) {
        delete(audience.getAudienceId());
    }
    
    private SqlParameterSource fillNamedParameters(Audience audience) {
        return new MapSqlParameterSource("id", audience.getAudienceId())
                .addValue("type", audience.getType())
                .addValue("number", audience.getNumber())
                .addValue("open", Time.valueOf(audience.getOpeningTime()))
                .addValue("close", Time.valueOf(audience.getClosingTime()));
    }
}
