package com.pnc.assignment.account.exception;


import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

   private static final long serialVersionUID = -1104775853014714704L;

   private Long accountId;
   private Long userId;
   
   public AccountNotFoundException(@NotNull Long accountId) {
		super("Account with account id [" + accountId + "] not found. ");
	}
   
   public AccountNotFoundException(@NotNull Long userId, @Nullable Long accountId) {
      super("Account(s) " + (accountId !=  null ? "with account id [" + accountId + "] " : "") + " for user id [" + userId + "] not found. ");
   }
   
   public Long getAccountId() {
      return accountId;
   }


   public void setAccountId(Long accountId) {
      this.accountId = accountId;
   }


   public Long getUserId() {
      return userId;
   }


   public void setUserId(Long userId) {
      this.userId = userId;
   }
}


