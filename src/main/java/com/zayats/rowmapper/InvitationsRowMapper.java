package com.zayats.rowmapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class InvitationsRowMapper implements RowMapper {
	public Object mapRow(ResultSet result, int arg1) throws SQLException {

		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put("eventId", result.getString("id"));
		temp.put("invitationId", result.getString("invitation_id"));
		temp.put("name", result.getString("name"));
		temp.put("fromUsername", result.getString("username_from"));
		temp.put("firstName", result.getString("first_name"));
		temp.put("lastName", result.getString("last_name"));
		return temp;
	}
}
