package com.zayats.controller;

import com.zayats.dal.EventRepository;
import com.zayats.exceptions.DataAccessDbException;
import com.zayats.exceptions.EventNotExistsException;
import com.zayats.model.Event;
import com.zayats.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value="home/events")
public class EventsController {

    private static final Logger logger = Logger
            .getLogger(EventsController.class);

	@Autowired
    EventRepository eventRepository;
	
	@RequestMapping(value = "/{id}")
	public @ResponseBody List<Event> events(@PathVariable Integer id, Model model, Principal principal) {
		List<Event> list = eventRepository.getEventsForUser(id);
		return list;
	}

    @RequestMapping(value = "/detail/{id}")
    public @ResponseBody List<Event> eventDetail(@PathVariable Integer id, Model model, Principal principal) {
        List<Event> list = new ArrayList<Event>();
        try {
            list = eventRepository.getEventDetail(id);
        } catch (EventNotExistsException e) {
            return null;
        }
        return list;
    }
	
	@RequestMapping(value = "/users/{eventId}")
	public @ResponseBody List<User> eventManager(@PathVariable String eventId, Model model,
			Principal principal) {
		List<User> list = null;
		try {
			list = eventRepository.getParticipants(Integer.parseInt(eventId));
		} catch (EventNotExistsException e) {
			return null;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		if (list.size() == 0)
			list = null;
		return list;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody List<Boolean> eventAdd(@RequestBody Event event, Model model,
			Principal principal) {
		List<Boolean> result = new ArrayList<Boolean>();
		try {
			result.add(eventRepository.createEvent(event));
		} catch (DataAccessDbException e) {
			return null;
		}

		return result;
	}
	
	@RequestMapping(value = "/delete/{eventId}")
	public @ResponseBody List<Boolean> eventDelete(@PathVariable String eventId, Model model,
			Principal principal) {
		List<Boolean> result = new ArrayList<Boolean>();
		try {
			result.add(eventRepository.deleteEvent(Integer.parseInt(eventId)));
		} catch (DataAccessDbException e) {
			return null;
		}

		return result;
	}

    @RequestMapping(value = "/dashboard/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<HashMap<String, Object>> getDashboard(@PathVariable Integer userId, Model model,
                            Principal principal) {
        List<HashMap<String, Object>> result = null;
        try {
            logger.info("Get events for user.");
            result = eventRepository.getDashboardDataForUser(userId);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Can't get events for user. Please, try later.s");
        }

        return result;
    }
}
