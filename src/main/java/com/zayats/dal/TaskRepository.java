package com.zayats.dal;

import com.zayats.model.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskRepository {

	public boolean deleteTask(int listId);

    HashMap<String,Integer> getChartData(int eventId);

    List<Task> getTasksForEvent(int eventId);

    Boolean addTask(Task task);

    List<Task> getTasksForUser(int eventId, int userId);

    Task taskDetail(int taskId);

    Boolean editTask(int taskId, String status, Integer responsibleId);
}
