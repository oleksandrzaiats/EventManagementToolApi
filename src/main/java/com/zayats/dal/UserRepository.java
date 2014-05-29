package com.zayats.dal;

import java.util.HashMap;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.zayats.exceptions.EmailOrLoginUsedException;
import com.zayats.exceptions.LoginOrPasswordException;
import com.zayats.model.User;

public interface UserRepository {
	
	public boolean register(User user) throws EmailOrLoginUsedException;
	
	public HashMap<String, String> login(String login) throws LoginOrPasswordException; 
	
	public int getUserId(String login);
	
	public List<User>getProfile(String username); 
	
	public List<User> searchUsers(String userString, int familyId);
	
}
