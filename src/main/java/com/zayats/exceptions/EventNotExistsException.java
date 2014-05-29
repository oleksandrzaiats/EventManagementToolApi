package com.zayats.exceptions;

public class EventNotExistsException extends Exception {
	
	public EventNotExistsException() {
		super();
	}

	public EventNotExistsException(String message) {
		super(message);
	}

	public String getMessage() {
		return "Event does not exists. Please, go to previous page and select another family.";
	}
}
