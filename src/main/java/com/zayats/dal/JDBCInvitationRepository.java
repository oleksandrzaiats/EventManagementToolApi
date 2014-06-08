package com.zayats.dal;

import com.zayats.controller.UserController;
import com.zayats.rowmapper.InvitationsRowMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCInvitationRepository implements InvitationRepository {

	public final String CREATE_INVITATION_STRING = "INSERT INTO invitations(username_from, username_to, event_id) VALUES (:fromUsername, :toUsername, :eventId)";
	public final String GET_INVITATION_ID = "SELECT invitation_id FROM invitations WHERE username_from = :fromUsername AND username_to = :toUsername AND event_id = :eventId";
	public final String DELETE_INVITATION_STRING = "DELETE FROM invitations WHERE invitation_id = :invitationId";
	public final String GET_USER_INVITSTIONS_STRING = "SELECT b.first_name, b.last_name, events.id, invitations.invitation_id, events.name, invitations.username_from " +
			"FROM users a, users b, invitations, events " +
			"WHERE username_to = :toUsername " +
				"AND events.id = invitations.event_id " +
				"AND username_to = a.username " + 
				"AND username_from = b.username";
	
	private static final Logger logger = Logger.getLogger(UserController.class);

	private NamedParameterJdbcTemplate jdbcTemplate;

	public JDBCInvitationRepository() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public boolean createInvitation(int eventId, String fromUsername,
			String toUsername) {
		boolean created = checkInvitation(eventId, fromUsername, toUsername);
		if (created) {
			return false;
		}
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("eventId", eventId);
		parameters.put("fromUsername", fromUsername);
		parameters.put("toUsername", toUsername);
		logger.info("Inserting invitation: " + parameters);
		jdbcTemplate.update(CREATE_INVITATION_STRING, parameters);
		return true;
	}

	public boolean deleteInvitation(int invitationId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("invitationId", invitationId);
		try{
			jdbcTemplate.update(DELETE_INVITATION_STRING, parameters);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	/**
	 * True if invitation exists, false in opposite way
	 */
	private boolean checkInvitation(int eventId, String fromUsername,
			String toUsername) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("eventId", eventId);
		parameters.put("fromUsername", fromUsername);
		parameters.put("toUsername", toUsername);
		int res = -1;
		try {
		res = jdbcTemplate.queryForInt(GET_INVITATION_ID, parameters);
		} catch (EmptyResultDataAccessException e) {
			return false;
		}
		if(res != -1)
			return true;
		return false;
	}

	public List<HashMap<String, String>> getUserInvitations(String toUsername) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("toUsername", toUsername);
		List<HashMap<String, String>> invitationsList = jdbcTemplate.query(GET_USER_INVITSTIONS_STRING, parameters, new InvitationsRowMapper()); 
		
		return invitationsList;
	}
}
