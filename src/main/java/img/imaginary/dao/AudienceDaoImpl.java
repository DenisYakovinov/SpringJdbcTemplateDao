package img.imaginary.dao;

import java.sql.Time;
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
import img.imaginary.service.entity.Audience;


@Loggable
@Repository
public class AudienceDaoImpl implements AudienceDao {
        
    private static final String NOT_FOUND_AUDIENCE = "The audience with id = %d wasn't found";
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder;
    private RowMapper<Audience> audienceMapper;

    @Autowired
    public AudienceDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate,
            KeyHolder keyHolder, RowMapper<Audience> audienceMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = keyHolder;
        this.audienceMapper = audienceMapper;
    }

    @Override
    public Optional<Integer> add(Audience audience) {
        SqlParameterSource namedParameters = fillNamedParameters(audience);
        try {
            namedParameterJdbcTemplate.update(
                    "INSERT INTO audiences (audience_type, audience_number, open_time, closing_time)"
                            + " VALUES (:type, :number, :open, :close)",
                    namedParameters, keyHolder, new String[] { "audience_id" });
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Audience %d can't be added", audience.getNumber()), e);
        }
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue);
    }

    @Override
    public List<Audience> findAll() {
        try {
            return jdbcTemplate.query("SELECT * FROM audiences", audienceMapper);

        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't get audiences (%s)", e.getMessage()), e);
        }
    }

    @Override
    public Audience findById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM audiences WHERE audience_id = ?", audienceMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException(String.format(NOT_FOUND_AUDIENCE, id), e);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't find audience with id = %d (%s)", id, e.getMessage()));
        }
    }

    @Override
    public void update(Audience audience) {
        SqlParameterSource namedParameters = fillNamedParameters(audience);
        try {
            int updatedRowsNumber = namedParameterJdbcTemplate
                    .update("UPDATE audiences SET (audience_type, audience_number, open_time, closing_time) = "
                            + "(:type, :number, :open, :close) WHERE audience_id = :id", namedParameters);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_AUDIENCE, audience.getAudienceId()));
            }
        } catch (DataAccessException e) {
            throw new DaoException(
                    String.format("Can't update audience with id = %d (%s)", audience.getAudienceId(), e.getMessage()),
                    e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            int updatedRowsNumber = jdbcTemplate.update("DELETE FROM audiences WHERE audience_id = ?", id);
            if (updatedRowsNumber == 0) {
                throw new DaoException(String.format(NOT_FOUND_AUDIENCE, id));
            }
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can't delete audience with id = '%d' (%s)", id, e.getMessage()), e);
        }
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
