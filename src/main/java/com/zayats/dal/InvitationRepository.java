package com.zayats.dal;

import java.util.HashMap;
import java.util.List;

public interface InvitationRepository {

	public boolean createInvitation(int eventId, String fromUsername, String toUsername);
	
	public boolean deleteInvitation(int invitationId);
	
	public List<HashMap<String, String>> getUserInvitations(String toUsername);
}
