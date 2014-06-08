package com.zayats.controller;

import com.zayats.dal.EventRepository;
import com.zayats.dal.InvitationRepository;
import com.zayats.dal.UserRepository;
import com.zayats.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "home/users")
public class UsersController {

	@Autowired
	UserRepository userRepository;

	@Autowired
    EventRepository eventRepository;

	@Autowired
	InvitationRepository invitationRepository;

	@RequestMapping(value = "/delete/{userId}/{familyId}")
	public @ResponseBody List<Boolean> deleteUserFromFamily(@PathVariable Integer userId,
			@PathVariable int familyId, Model model, Principal principal) {
		List<Boolean> result = new ArrayList<Boolean>();
		result.add(eventRepository.deleteUserFromEvent(userId, familyId));

		return result;
	}
	
	@RequestMapping(value = "/profile/{username}")
	public @ResponseBody List<User> getUserProfile(@PathVariable String username,
			Model model, Principal principal) {
		List<User> result = new ArrayList<User>();
		result.add((userRepository.getProfile(username)).get(0));

		return result;
	}

	@RequestMapping(value = "/{userString}/{eventId}", method = RequestMethod.GET)
	public @ResponseBody
	List<User> searchUsers(@PathVariable String userString,
			@PathVariable int eventId, Model model, Principal principal) {
		return userRepository.searchUsers(userString, eventId);
	}

}
