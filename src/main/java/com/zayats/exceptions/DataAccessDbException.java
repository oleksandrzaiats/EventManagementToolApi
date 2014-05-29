package com.zayats.exceptions;

public class DataAccessDbException extends Exception {

	public DataAccessDbException() {
		super();
	}
	
	public DataAccessDbException(String message) {
		super(message);
	}
	
	public String getMessage() {
		return "Can't connect to database. Please, try later.";
	}
	
}
