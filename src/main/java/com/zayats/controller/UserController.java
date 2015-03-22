package com.zayats.controller;

import com.zayats.dal.UserRepository;
import com.zayats.exceptions.EmailOrLoginUsedException;
import com.zayats.exceptions.LoginOrPasswordException;
import com.zayats.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> registerUser(@RequestBody User user)
			throws EmailOrLoginUsedException {
		logger.info("new user");
		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		if (user.getFirstName().length() < 2)
			resultHashMap.put("firstNameError", "First name is too short.");
		if (user.getLastName().length() < 2)
			resultHashMap.put("lastNameError", "Last name is too short.");
		if (user.getPassword().length() < 7)
			resultHashMap.put("passwordError", "Password is too short.");
		Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher m = emailPattern.matcher(user.getEmail());
		if (!m.matches())
			resultHashMap.put("emailError", "Email address is invalid.");
		if (user.getUsername().length() < 2)
			resultHashMap.put("username", "Username is too short.");
		resultHashMap.put("isRegister",
				userRepository.register(user));

		return resultHashMap;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	HashMap<String, String> loginUser(
			@RequestBody HashMap<String, String> params, WebRequest request)
			throws LoginOrPasswordException {
		logger.info("login user");
		logger.info(request);
		String username = params.get("username");
		logger.info(username);
		HashMap<String, String> uDetails = userRepository.login(username);
		System.out.println(uDetails.get("password"));
		return uDetails;
	}
}
