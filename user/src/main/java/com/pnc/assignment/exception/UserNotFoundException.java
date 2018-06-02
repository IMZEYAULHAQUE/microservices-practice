package com.pnc.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7886737402276524171L;
	
	
	public UserNotFoundException(Long userId) {
		super("User with user id [" + userId + "] not found. ");
	}
}


