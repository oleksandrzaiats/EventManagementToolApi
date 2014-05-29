package com.zayats.controller;

import com.zayats.dal.EventRepository;
import com.zayats.dal.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "home/invitations")
public class InvitationsController {

	@Autowired
    EventRepository eventRepository;

	@Autowired
	InvitationRepository invitationRepository;

	@RequestMapping(value = "/{username}")
	public @ResponseBody
	List<HashMap<String, String>> getInvitations(@PathVariable String username,
			Model model, Principal principal) {
		
		List<HashMap<String, String>> list = invitationRepository
				.getUserInvitations(username);

		return list;
	}

	@RequestMapping(value = "/invite/{familyId}/{fromUsername}/{toUsername}", method = RequestMethod.GET)
	public @ResponseBody
	List<Boolean> inviteUser(@PathVariable String toUsername,
			@PathVariable int familyId, @PathVariable String fromUsername,
			Model model, Principal principal) {
		boolean isInvited = invitationRepository.createInvitation(familyId,
				fromUsername, toUsername);
		List<Boolean> result = new ArrayList<Boolean>();
		result.add(isInvited);
		return result;
	}

	@RequestMapping(value = "/accept/{familyId}/{invitationId}/{username}", method = RequestMethod.GET)
	public @ResponseBody
	List<Boolean> acceptInvitation(@PathVariable int familyId,
			@PathVariable int invitationId, @PathVariable Integer username, Model model, Principal principal) {
		invitationRepository.deleteInvitation(invitationId);
		List<Boolean> result = new ArrayList<Boolean>();
		result.add(eventRepository.assignEventToUser(familyId, username));
		return result;
	}
	
	@RequestMapping(value = "/decine/{invitationId}", method = RequestMethod.GET)
	public @ResponseBody
	List<Boolean> decineInvitation(@PathVariable int invitationId, Model model, Principal principal) {
		List<Boolean> result = new ArrayList<Boolean>();
		result.add(invitationRepository.deleteInvitation(invitationId));

		return result;
	}
	
}
