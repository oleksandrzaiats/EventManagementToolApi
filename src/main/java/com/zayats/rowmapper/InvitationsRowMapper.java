package com.zayats.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public class InvitationsRowMapper implements RowMapper {
	public Object mapRow(ResultSet result, int arg1) throws SQLException {

		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put("familyId", result.getString("family_id"));
		temp.put("invitationId", result.getString("invitation_id"));
		temp.put("name", result.getString("name"));
		temp.put("fromUsername", result.getString("username_from"));
		temp.put("firstName", result.getString("first_name"));
		temp.put("lastName", result.getString("last_name"));
		return temp;
	}
}
