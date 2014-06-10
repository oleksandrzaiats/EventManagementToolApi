package com.zayats.dal;

import com.zayats.exceptions.DataAccessDbException;
import com.zayats.exceptions.EventNotExistsException;
import com.zayats.model.Event;
import com.zayats.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

public interface EventRepository {

    @Autowired
    void setDataSource(DataSource dataSource);

    boolean createEvent(Event event)
            throws DataAccessDbException;

    boolean deleteEvent(int eventId) throws DataAccessDbException;

    List<Event> getEventsForUser(Integer id);

    boolean assignEventToUser(int eventId, int userId);

    List<User> getParticipants(int eventId)
                    throws EmptyResultDataAccessException, EventNotExistsException;

    List<Event> getAllEventsForUser(Integer userId);

    boolean deleteUserFromEvent(Integer userId, int eventId);

    List<Event> getEventDetail(Integer id) throws EventNotExistsException;

    List<HashMap<String, Object>> getDashboardDataForUser(Integer userId);
}
