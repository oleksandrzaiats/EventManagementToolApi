package com.zayats.dal;

import com.zayats.controller.UserController;
import com.zayats.model.Task;
import com.zayats.model.TaskStatus;
import com.zayats.rowmapper.TasksRowMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCTaskRepository implements TaskRepository {

	private final String CREATE_TASK_STRING = "INSERT INTO tasks (name, " +
            "description," +
            "createDate," +
            "dueDate," +
            "userid," +
            "status," +
            "eventid) VALUES (:name, :description, :createDate, :dueDate, :userId, :status, :eventId)";
	private final String DELETE_TASK_STRING = "DELETE FROM tasks WHERE id=:taskId";
	private final String GET_TASKS_STRING = "SELECT tasks.*, users.* FROM tasks, users WHERE tasks.eventId=:eventId AND users.user_id=tasks.userid";
	private final String GET_TASKS_FOR_USER_STRING = "SELECT tasks.*, users.* FROM tasks, users WHERE tasks.eventId=:eventId AND tasks.userid=:userId AND users.user_id=tasks.userid";
	private final String EDIT_TASK = "UPDATE tasks SET status=:status, userid=:responsible WHERE tasks.id=:taskId";
	private final String GET_TASK_DETAIL = "SELECT tasks.*, users.* FROM tasks, users WHERE tasks.id=:taskId AND users.user_id=tasks.userid";

	private static final Logger logger = Logger.getLogger(UserController.class);

	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public boolean deleteTask(int taskId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("taskId", taskId);

		try {
			jdbcTemplate.update(DELETE_TASK_STRING, parameters);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

/*	public List<Shoplist> getShoplistsForFamily(int familyId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("familyId", familyId);

		logger.info("Getting shoplists for family.");
		List<Shoplist> shoplists = jdbcTemplate.query(
				GET_SHOPLIST_FOR_FAMILY_STRING, parameters,
				new ShoplistsRowMapper());
		return shoplists;
	}*/

    @Override
    public HashMap<String, Integer> getChartData(int eventId) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("eventId", eventId);
        List<Task> tasks;
        try {
            tasks = jdbcTemplate.query(GET_TASKS_STRING,
                    parameters, new TasksRowMapper());
        } catch (Exception e) {
            return null;
        }
        if(tasks != null && tasks.size() > 0) {
            int newTasks = 0;
            int done = 0;
            int progress = 0;
            for(Task task : tasks) {
                switch (task.getStatus()) {
                    case OPEN:
                        newTasks++;
                        break;
                    case IN_PROGRESS:
                        progress++;
                        break;
                    case DONE:
                        done++;
                        break;
                }
            }
            result.put(TaskStatus.DONE.name(), done);
            result.put(TaskStatus.IN_PROGRESS.name(), progress);
            result.put(TaskStatus.OPEN.name(), newTasks);
        }
        return result;
    }

    @Override
    public List<Task> getTasksForEvent(int eventId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("eventId", eventId);
        List<Task> tasks;
        try {
            tasks = jdbcTemplate.query(GET_TASKS_STRING,
                    parameters, new TasksRowMapper());
        } catch (Exception e) {
            return null;
        }
        return tasks;
    }

    @Override
    public Boolean addTask(Task task) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", task.getName());
        parameters.put("dueDate", task.getDueDate());
        parameters.put("createDate", task.getCreateDate());
        parameters.put("userId", task.getResponsible().getUserId());
        parameters.put("status", task.getStatus().name());
        parameters.put("eventId", task.getEventId());
        parameters.put("description", task.getDescription());
        try {
            jdbcTemplate.update(CREATE_TASK_STRING, parameters);
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<Task> getTasksForUser(int eventId, int userId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("eventId", eventId);
        parameters.put("userId", userId);
        List<Task> tasks;
        try {
            tasks = jdbcTemplate.query(GET_TASKS_FOR_USER_STRING,
                    parameters, new TasksRowMapper());
        } catch (Exception e) {
            return null;
        }
        return tasks;
    }

    @Override
    public Task taskDetail(int taskId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("taskId", taskId);
        List<Task> tasks;
        try {
            tasks = jdbcTemplate.query(GET_TASK_DETAIL,
                    parameters, new TasksRowMapper());
        } catch (Exception e) {
            return null;
        }
        return tasks != null ? tasks.get(0) : null;
    }

    @Override
    public Boolean editTask(int taskId, String status, Integer responsibleId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("taskId", taskId);
        parameters.put("status", status);
        parameters.put("responsible", responsibleId);
        try {
            jdbcTemplate.update(EDIT_TASK,
                    parameters);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
