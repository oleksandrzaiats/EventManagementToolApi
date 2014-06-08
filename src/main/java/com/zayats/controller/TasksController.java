package com.zayats.controller;

import com.zayats.dal.EventRepository;
import com.zayats.dal.TaskRepository;
import com.zayats.model.Event;
import com.zayats.model.Task;
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
@RequestMapping("/home/tasks")
public class TasksController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    EventRepository eventRepository;

    private static final Logger logger = Logger
            .getLogger(TasksController.class);

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Event> getFamilies(@PathVariable Integer userId, Model model,
                            Principal principal) {
        List<Event> list = null;
        try {
            logger.info("Get events for user.");
            list = eventRepository.getAllEventsForUser(userId);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Can't get events for user. Please, try later.s");
        }

        return list;
    }

    @RequestMapping(value = "/get/{eventId}")
    public
    @ResponseBody
    List<Task> getTasks(@PathVariable int eventId, Model model,
                        Principal principal) {
        List<Task> list = null;
        try {
            logger.info("Get families for user.");
            list = taskRepository.getTasksForEvent(eventId);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Can't get families for user. Please, try later.s");
        }

        return list;
    }

    @RequestMapping(value = "/get/{eventId}/{userId}")
    public
    @ResponseBody
    List<Task> getTasksForUser(@PathVariable int eventId, @PathVariable int userId, Model model,
                        Principal principal) {
        List<Task> list = null;
        try {
            logger.info("Get families for user.");
            list = taskRepository.getTasksForUser(eventId, userId);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Can't get families for user. Please, try later.s");
        }

        return list;
    }

    @RequestMapping(value = "/chart/{eventId}", method = RequestMethod.GET)
    public
    @ResponseBody
    HashMap<String, Integer> getChartData(@PathVariable int eventId) {

        HashMap<String, Integer> result = taskRepository
                .getChartData(eventId);

        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Boolean> taskAdd(@RequestBody Task task, Model model,
                          Principal principal) {
        List<Boolean> result = new ArrayList<Boolean>();
        result.add(taskRepository.addTask(task));
        return result;
    }

    @RequestMapping(value = "/delete/{taskId}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Boolean> deleteTask(@PathVariable int taskId) {
        List<Boolean> result = new ArrayList<Boolean>();

        result.add(taskRepository.deleteTask(taskId));

        return result;
    }

    @RequestMapping(value = "/detail/{taskId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Task taskDetail(@PathVariable int taskId) {
        return taskRepository.taskDetail(taskId);
    }

    @RequestMapping(value = "/edit/{taskId}/{status}/{responsibleId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean editTask(@PathVariable int taskId, @PathVariable String status, @PathVariable Integer responsibleId) {
        Boolean result = taskRepository.editTask(taskId, status, responsibleId);

        return result;
    }

}
