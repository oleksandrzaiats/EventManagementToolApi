package com.zayats.exceptions;

public class LoginOrPasswordException extends Exception {
	public LoginOrPasswordException() {
		super();
	}
	
	public LoginOrPasswordException(String message) {
		super(message);
	}
	
	public String getMessage() {
		return "Login or password is incorrect.";
	}
}
