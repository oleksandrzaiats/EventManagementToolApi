package com.zayats.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.zayats.model.User;

public class UsersRowMapper implements RowMapper {
	public User mapRow(ResultSet result, int arg1) throws SQLException {

		User temp = new User();
		temp.setUserId(result.getInt("user_id"));
		temp.setUsername(result.getString("username"));
		temp.setFirstName(result.getString("first_name"));
		temp.setLastName(result.getString("last_name"));
		temp.setEmail(result.getString("email"));
		return temp;
	}
}
