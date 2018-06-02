package com.pnc.assignment.aggregator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

   private static final long serialVersionUID = 5705252862378317164L;

   public UserNotFoundException() {
		super("User not found.");
	}
   
   public UserNotFoundException(Long userId) {
      super("User with user id [" + userId + "] not found. ");
   }
   
   public UserNotFoundException(String message) {
      super(message);
   }
}


