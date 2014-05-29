package com.zayats.exceptions;

public class EmailOrLoginUsedException extends Exception {
	
	public EmailOrLoginUsedException() {
		super();
	}
	
	public EmailOrLoginUsedException(String message) {
		super(message);
	}
	
	public String getMessage() {
		return "Email or login is already used.";
	}
}
