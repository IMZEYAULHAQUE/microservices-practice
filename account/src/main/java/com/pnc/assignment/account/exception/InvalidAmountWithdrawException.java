package com.pnc.assignment.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class InvalidAmountWithdrawException extends RuntimeException {

   private static final long serialVersionUID = -7979058867077822817L;

   public InvalidAmountWithdrawException(String message) {
      super(message);
   }

}
