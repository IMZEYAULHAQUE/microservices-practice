package com.pnc.assignment.aggregator.controller;

import com.pnc.assignment.aggregator.exception.AccountNotFoundException;
import com.pnc.assignment.aggregator.feignclient.account.AccountFeignClient;
import com.pnc.assignment.aggregator.feignclient.user.UserFeignClient;
import com.pnc.assignment.aggregator.model.account.Account;
import com.pnc.assignment.aggregator.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
public class AggregatorController {

   @Autowired
   private UserFeignClient userClient;
   
   @Autowired
   private AccountFeignClient accountClient;


   //@TODO all these logic should move to service layer
   @GetMapping("/users")
   public List<User> findAllUsers() {
      List<User> users = userClient.findAll().getData();
      //users.forEach((user) -> user.setAccounts(this.findAccountByUserId(user.getUserId())));
      users.forEach((user) -> {
         try {
            user.setAccounts(this.findAccountByUserId(user.getUserId()));
         }
         catch (AccountNotFoundException ex) {
            // Simply ignore this but should work with hystrix in real scenario
         }
      });
      return users;
   }

   @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public User findUser(@PathVariable @NotNull Long userId) {
      User user = userClient.findUser(userId).getData();
      user.setAccounts(this.findAccountByUserId(userId));
      return user;
   }

   @GetMapping(value = "/users/lastname/{lastName}")
   public List<User> findUserByLastName(@PathVariable @NotBlank String lastName) {
      List<User> users = userClient.findUserByLastName(lastName).getData();
      //users.forEach((user) -> user.setAccounts(this.findAccountByUserId(user.getUserId())));

      users.forEach((user) -> {
         try {
            user.setAccounts(this.findAccountByUserId(user.getUserId()));
         }
         catch (AccountNotFoundException ex) {
            // Simply ignore this but should work with hystrix in real scenario
         }
      });

      return users;
   }

   @PutMapping(value = "/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
   public String updateUser(@RequestBody @Valid User user, @PathVariable("userId") @NotBlank Long userId) {
      return userClient.update(user, userId).getData();
   }

   @PatchMapping(value = "/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
   public String partialUpdateUser(@RequestBody Map<String, String> updateRequestedMap, @PathVariable("userId") Long userId) {
      return userClient.partialUpdate(updateRequestedMap, userId).getData();
   }

   @DeleteMapping(value = "/users/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE })
   public String deleteUser(@PathVariable("userId") Long userId) {
      return userClient.delete(userId).getData();
   }

   @GetMapping(value = "/accounts", produces = { MediaType.APPLICATION_JSON_VALUE })
   public List<Account> findAllAccount() {
      return accountClient.findAll().getData();
   }
   
   @GetMapping(value = "/accounts/{accountId}")
   public Account findAccountById(@PathVariable @NotNull Long accountId) {
      return accountClient.findAccount(accountId).getData();
   }

   @GetMapping(value = "/users/{userId}/accounts")
   public List<Account> findAccountByUserId(@PathVariable(name="userId") @NotBlank Long userId) {
      return accountClient.findByUserId(userId).getData();
   }
   
   @DeleteMapping(value = "/users/{userId}/accounts/{accountId}", produces = { MediaType.APPLICATION_JSON_VALUE })
   public String closeAccount(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long accountId) {
      return accountClient.close(userId, accountId).getData();
   }
   
   @GetMapping(value = "/users/{userId}/accounts/{accountId}/balance")
   public Double getBalance(@PathVariable("userId") @NotNull Long userId, @PathVariable("accountId") @NotNull Long accountId) {
      return accountClient.getBalance(userId, accountId).getData();
   }
   
   @PutMapping(value = "/users/{userId}/accounts/{accountId}/withdraw", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
   public String[][] withraw(@RequestBody Map<String, Double> amountMap, @PathVariable("userId") @NotNull Long userId, @PathVariable("accountId") @NotNull Long accountId) {
      return accountClient.withraw(amountMap, userId, accountId).getData();
   }
   
}
