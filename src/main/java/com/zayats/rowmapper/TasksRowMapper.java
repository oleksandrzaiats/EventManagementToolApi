package com.zayats.rowmapper;

import com.zayats.model.Task;
import com.zayats.model.TaskStatus;
import com.zayats.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TasksRowMapper implements RowMapper {
	public Object mapRow(ResultSet result, int arg1) throws SQLException {

		Task temp = new Task();
		temp.setId(result.getInt("id"));
		temp.setName(result.getString("name"));
		temp.setDescription(result.getString("description"));
        temp.setCreateDate(result.getDate("createDate"));
        temp.setDueDate(result.getDate("dueDate"));
        temp.setDoneDate(result.getDate("doneDate"));
        temp.setEventId(result.getInt("eventId"));
        temp.setStatus(TaskStatus.valueOf(result.getString("status").toUpperCase()));
        User user = new User();
        user.setUserId(result.getInt("userid"));
        user.setUsername(result.getString("username"));
        user.setFirstName(result.getString("first_name"));
        user.setLastName(result.getString("last_name"));
        temp.setResponsible(user);
		return temp;
	}
}