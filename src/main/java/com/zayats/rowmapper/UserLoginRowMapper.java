package com.zayats.rowmapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UserLoginRowMapper implements RowMapper {
	public HashMap<String, String> mapRow(ResultSet result, int arg1) throws SQLException {

		HashMap<String, String> user = new HashMap<String, String>();
		user.put("password", result.getString("password"));
		user.put("authority", result.getString("authority"));
		user.put("username", result.getString("username"));
		user.put("id", result.getString("user_id"));
		return user;
	}
}
