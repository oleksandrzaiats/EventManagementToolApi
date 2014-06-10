package com.zayats.dal;

import com.zayats.controller.UserController;
import com.zayats.exceptions.DataAccessDbException;
import com.zayats.exceptions.EventNotExistsException;
import com.zayats.model.Event;
import com.zayats.model.User;
import com.zayats.rowmapper.EventsRowMapper;
import com.zayats.rowmapper.UsersRowMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

public class JDBCEventRepository implements EventRepository {

	private static final String CREATE_EVENT_STRING = "INSERT INTO events(name, description, date, address, owner_id)" +
            " VALUES (:name, :description, :date, :address, :id)";
	private static final String GET_EVENTS_FOR_USER_STRING = "SELECT events.id," +
            " events.name," +
            " events.description," +
            " events.date," +
            " events.address," +
            " events.owner_id," +
            " users.first_name," +
            " users.last_name," +
            " users.username" +
            " FROM events, users, users2events WHERE users2events.user_id=:id AND users2events.event_id=events.id AND users.user_id=users2events.user_id";
    private static final String GET_EVENT_DETAIL = "SELECT events.id," +
            " events.name," +
            " events.description," +
            " events.date," +
            " events.address," +
            " events.owner_id," +
            " users.first_name," +
            " users.last_name," +
            " users.username" +
            " FROM events, users WHERE events.id=:id AND events.owner_id=users.user_id";
	private static final String ASSIGN_EVENT_FOR_USER_STRING = "INSERT INTO users2events(user_id, event_id) VALUES(:user_id, :event_id)";
	private static final String GET_EVENT_ID_STRING = "SELECT id FROM events WHERE name=:name AND owner_id=:id";
	private static final String GET_ALL_EVENTS_FOR_USER_STRING = "SELECT events.*, users.* "
			+ "FROM events,users2events, users "
			+ "WHERE users2events.user_id=:userId "
            + "AND users.user_id=:userId "
			+ "AND events.id = users2events.event_id";
	private static final String DELETE_EVENT_STRING = "DELETE FROM events WHERE id=:eventId";
	private static final String GET_PARTICIPANTS_STRING = "SELECT * FROM users WHERE users.user_id IN (SELECT users2events.user_id FROM users2events WHERE event_id=:eventId)";
	private static final String GET_EVENT_STRING = "SELECT name FROM events WHERE event_id=:eventId";
	private static final String GET_EVENT_TASKS_COUNT = "SELECT COUNT(*) FROM tasks WHERE eventid=:eventId";
	private static final String GET_EVENT_DONE_TASKS_COUNT = "SELECT COUNT(*) FROM tasks WHERE eventid=:eventId AND status='DONE'";
	private static final String DELETE_USER_FROM_EVENT_STRING = "DELETE FROM users2events WHERE event_id = :eventId AND user_id = :userId";

	private static final Logger logger = Logger.getLogger(UserController.class);

	private NamedParameterJdbcTemplate jdbcTemplate;

	public JDBCEventRepository() {
		// TODO Auto-generated constructor stub
	}

	@Override
    @Autowired
    public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
    public boolean createEvent(Event event)
			throws DataAccessDbException {
        SqlParameterSource parameters = new MapSqlParameterSource("name", event.getName())
		.addValue("description", event.getDescription())
		.addValue("date", new Timestamp(event.getDate().getTime()))
		.addValue("address", event.getAddress())
		.addValue("id", event.getOwner().getUserId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
		logger.info("Put event to db.");
		try {
            jdbcTemplate.update(CREATE_EVENT_STRING, parameters, keyHolder);
		} catch (Exception e) {
			logger.info("Insert event exception.");
			throw new DataAccessDbException();
		}

		assignEventToUser(Integer.parseInt(keyHolder.getKeys().get("id").toString()), event.getOwner().getUserId());

		return true;
	}

	@Override
    public boolean deleteEvent(int eventId) throws DataAccessDbException {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("eventId", eventId);

		logger.info("Put event to db.");
		try {
			jdbcTemplate.update(DELETE_EVENT_STRING, parameters);
		} catch (Exception e) {
			logger.info("Delete event exception.");
			throw new DataAccessDbException();
		}

		return true;
	}

	@Override
    public List<Event> getEventsForUser(Integer id) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("id", id);
		List<Event> events = null;
		logger.info("Get events for user:" + id + ".");
		try {
			events = jdbcTemplate.query(GET_EVENTS_FOR_USER_STRING,
					parameters, new EventsRowMapper());
		} catch (EmptyResultDataAccessException e) {
			logger.info("User:" + id + " has no events.");
			return null;
		}
		return events;

	}

    @Override
    public List<Event> getEventDetail(Integer id) throws EventNotExistsException {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("id", id);
        List<Event> events = null;
        logger.info("Get event detail, event id=:" + id + ".");
        try {
            events = jdbcTemplate.query(GET_EVENT_DETAIL,
                    parameters, new EventsRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Event:" + id + " not exists.");
            throw new EventNotExistsException();
        }
        return events;
    }

    @Override
    public boolean assignEventToUser(int eventId, int userId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user_id", userId);
		parameters.put("event_id", eventId);
		logger.info("Assign event to user.");
		try {
			jdbcTemplate.update(ASSIGN_EVENT_FOR_USER_STRING, parameters);
		} catch (Exception e) {
			logger.info("Can't assign event to user.");
			return false;
		}

		return true;
	}

	@Override
    public List<User> getParticipants(int eventId)
			throws EmptyResultDataAccessException, EventNotExistsException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("eventId", eventId);

		List<User> users = null;
		logger.info("Get participant for event:" + eventId + ".");
		users = jdbcTemplate.query(GET_PARTICIPANTS_STRING, parameters,
				new UsersRowMapper());
		return users;
	}

	@Override
    public List<Event> getAllEventsForUser(Integer userId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userId", userId);

		List<Event> events = null;
		logger.info("Get all events for user:" + userId + ".");
		try {
			events = jdbcTemplate.query(GET_ALL_EVENTS_FOR_USER_STRING,
					parameters, new EventsRowMapper());
		} catch (EmptyResultDataAccessException e) {
			logger.info("User:" + userId + " has no events.");
			return null;
		}
		return events;
	}

	@Override
    public boolean deleteUserFromEvent(Integer userId, int eventId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("eventId", eventId);
		parameters.put("userId", userId);

		jdbcTemplate.update(DELETE_USER_FROM_EVENT_STRING, parameters);

		return true;
	}

    @Override
    public List<HashMap<String, Object>> getDashboardDataForUser(Integer userId) {
        List<HashMap<String, Object>> result = new LinkedList<HashMap<String, Object>>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("userId", userId);

        List<Event> events = null;
        logger.info("Get all events for user:" + userId + ".");
        try {
            events = jdbcTemplate.query(GET_ALL_EVENTS_FOR_USER_STRING,
                    parameters, new EventsRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User:" + userId + " has no events.");
            return null;
        }
        if(events != null && events.size() !=  0) {
            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    long diff = o2.getDate().getTime() - o1.getDate().getTime();
                    if(diff > 0) {
                        return 1;
                    } else if(diff == 0) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
            for (Event event : events) {
                HashMap<String, Object> eventsProgress = new HashMap<String, Object>();
                Map<String, Object> eventParameters = new HashMap<String, Object>();
                eventParameters.put("eventId", event.getId());
                Integer count = jdbcTemplate.queryForInt(GET_EVENT_TASKS_COUNT, eventParameters);
                Integer done = jdbcTemplate.queryForInt(GET_EVENT_DONE_TASKS_COUNT, eventParameters);
                BigDecimal decimal = BigDecimal.ZERO;
                if(count != null && count != 0 && done != null && done != 0) {
                    decimal = new BigDecimal((100. / count) * done);
                }
                eventsProgress.put("name", event.getName());
                eventsProgress.put("id", event.getId());
                eventsProgress.put("progress", decimal.setScale(0).intValue());
                eventsProgress.put("date", event.getDate());
                result.add(eventsProgress);
            }


            return result;
        } else {
            return null;
        }
    }
}
