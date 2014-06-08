package com.zayats.dal;

import com.zayats.controller.UserController;
import com.zayats.exceptions.EmailOrLoginUsedException;
import com.zayats.exceptions.LoginOrPasswordException;
import com.zayats.model.User;
import com.zayats.rowmapper.UserLoginRowMapper;
import com.zayats.rowmapper.UsersRowMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUserRepository implements UserRepository {

	private final String GET_USER = "SELECT * FROM users WHERE username=:username";
	private final String CREATE_USER_STRING = "INSERT INTO users(email, username, password, first_name, last_name) VALUES (:email, :username, :password, :first_name, :last_name)";
	private final String lOGIN_USER_STRING = "SELECT users.username, users.user_id, users.password, user_roles.authority FROM users, user_roles WHERE users.user_id=user_roles.user_id AND username=:username";
	private final String GET_USER_ID_STRING = "SELECT user_id from users where username=:username OR email=:username";
	private final String SET_USER_ROLE_STRING = "INSERT INTO user_roles(user_id, authority) VALUES (:userId, :authority)";
	private final String GET_USERS = "SELECT DISTINCT users.user_id, users.username, users.first_name, users.last_name, users.email " +
			"FROM users, users2events WHERE users.username LIKE :userString OR users.first_name LIKE :userString OR users.last_name LIKE :userString " +
			"AND users.username NOT IN (SELECT DISTINCT username FROM users2events WHERE event_id=:eventId)";
	
	private static final Logger logger = Logger.getLogger(UserController.class);

	private NamedParameterJdbcTemplate jdbcTemplate;

	public JDBCUserRepository() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public HashMap<String, String> login(String username) throws LoginOrPasswordException {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("username", username);
		
		logger.info("Getting user from table.");
		try {
			logger.info(username);
			List<HashMap<String, String>> res = jdbcTemplate.query(lOGIN_USER_STRING, parameters, new UserLoginRowMapper());
			logger.info("User returned.");
			return res.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("username or password is incorrect.");
			throw new LoginOrPasswordException();
		}
	}
	
	public boolean register(User user) throws EmailOrLoginUsedException {
		Map<String, Object> parameters = new HashMap<String, Object>();

		String hashedPass = getMD5(user.getPassword());

		parameters.put("email", user.getEmail());
		parameters.put("username", user.getUsername());
		parameters.put("password", user.getPassword());
		parameters.put("first_name", user.getFirstName());
		parameters.put("last_name", user.getLastName());
		
		logger.info("Inserting user to table.");

		try {
			jdbcTemplate.update(CREATE_USER_STRING, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Inserting user to table failed. Email or username is used.");
			throw new EmailOrLoginUsedException();
		}
		setUserRole(user.getUsername());
		logger.info("User is registered.");
		return true;
	}
	
	public int getUserId(String username) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("username", username);
		return jdbcTemplate.queryForInt(GET_USER_ID_STRING, parameters);
	}
	
	private boolean setUserRole(String username){
		int userId = getUserId(username);
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("userId", userId);
		parameters.put("authority", "ROLE_USER");
		jdbcTemplate.update(SET_USER_ROLE_STRING, parameters);
		
		return false;
	}
	
	
	private static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public List<User> searchUsers(String userString, int eventId) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("userString", userString + "%");
		parameters.put("eventId", eventId);
		List<User> users = jdbcTemplate.query(GET_USERS, parameters, new UsersRowMapper());
		return users;
	}

	public List<User> getProfile(String username) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("username", username);
		return jdbcTemplate.query(GET_USER, parameters, new UsersRowMapper());
	}
}
