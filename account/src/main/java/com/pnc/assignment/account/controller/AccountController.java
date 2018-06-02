package com.pnc.assignment.account.controller;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pnc.assignment.account.model.Account;
import com.pnc.assignment.account.model.AccountStatus;
import com.pnc.assignment.account.model.AccountType;
import com.pnc.assignment.account.service.AccountService;

@RestController
public class AccountController {

   @Autowired
   private AccountService accountService;

   @GetMapping("/accounts")
   public List<Account> findAll() {
      return accountService.findAll();
   }

   @GetMapping(value = "/accounts/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public Account findAccount(@PathVariable @NotNull Long accountId) {
      return accountService.findById(accountId);
   }

   @GetMapping(value = "/users/{userId}/accounts")
   public List<Account> findByUserId(@PathVariable @NotBlank Long userId) {
      return accountService.findByUserId(userId);
   }
   
   @GetMapping(value = "/users/{userId}/accounts/{accountId}")
   public Account findByUserIdAndAccountId(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long accountId) {
      return accountService.findByAccountIdAndUserId(userId, accountId);
   }
   
   @GetMapping(value = "/users/{userId}/accounts/{accountId}/balance")
   public ResponseEntity<Double> getBalance(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long accountId) {
      Double balance = accountService.getBalance(userId, accountId);
      return ResponseEntity.status(HttpStatus.OK).body(balance);
   }

   @PutMapping(value = "/users/{userId}/accounts/{accountId}/balance", consumes = MediaType.APPLICATION_JSON_VALUE)
   public String updateBalance(@RequestBody @NotNull Map<String, Double> balanceMap, @PathVariable @NotNull Long userId, @PathVariable @NotNull Long accountId) {
      accountService.updateBalance(userId, accountId, balanceMap.get("balance"));
      return "Balance for Account id [" + accountId + "] and user id [" + userId + "] updated successfully.";
   }
   
   @PutMapping(value = "/users/{userId}/accounts/{accountId}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
   public String updateStatus(@RequestBody @NotNull AccountStatus status, @PathVariable @NotNull Long userId, @PathVariable @NotNull Long accountId) {
      accountService.updateAccountStatus(userId, accountId, status);
      return "Account Status for Account id [" + accountId + "] and user id [" + userId + "] updated successfully.";
   }
   
   @PutMapping(value = "/users/{userId}/accounts/{accountId}/type", consumes = MediaType.APPLICATION_JSON_VALUE)
   public String updateType(@RequestBody @NotNull AccountType type, @PathVariable @NotNull Long userId, @PathVariable @NotNull Long accountId) {
      accountService.updateAccountType(userId, accountId, type);
      return "Account Type for Account id [" + accountId + "] and user id [" + userId + "] updated successfully.";
   }

   @PatchMapping(value = "/users/{userId}/accounts/{accountId}", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> partialUpdate(@RequestBody Map<String, String> updateRequestedMap, @PathVariable @NotNull Long userId, @PathVariable @NotNull Long accountId) {
      accountService.partialUpdate(userId, accountId, updateRequestedMap);
      return ResponseEntity.<String>ok("Fields [" + updateRequestedMap + "] for Account with id [" + accountId + "] updated successfully.");
   }

   @DeleteMapping(value = "/users/{userId}/accounts/{accountId}", produces = { MediaType.APPLICATION_JSON_VALUE })
   public ResponseEntity<String> close(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long accountId) {
      accountService.delete(userId, accountId);
      return new ResponseEntity<String>("Account with id [" + accountId + "] marked closed.", HttpStatus.OK);
   }
   
   @PutMapping(value = "/users/{userId}/accounts/{accountId}/withdraw", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String[][]> withraw(@RequestBody Map<String, Double> amountMap, @PathVariable @NotNull Long userId, @PathVariable @NotNull Long accountId) {
      Double newBalance = accountService.withdraw(userId, accountId, amountMap.get("withdrawAmount"));
      return ResponseEntity.<String[][]>ok(new String[][] {{"newBalance", String.valueOf(newBalance)}});
   } 
}
