package com.pnc.assignment.aggregator.feignclient.account;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pnc.assignment.aggregator.custom.CustomFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pnc.assignment.aggregator.custom.CustomResponseEntity;
import com.pnc.assignment.aggregator.model.account.Account;
import com.pnc.assignment.aggregator.model.account.AccountStatus;
import com.pnc.assignment.aggregator.model.account.AccountType;

@FeignClient(name="${feign.client.account.url}", configuration = CustomFeignConfiguration.class, fallback = AccountFeignClientFallback.class/*, fallbackFactory = AccountFeignClientFallbackFactory.class*/)
public interface AccountFeignClient {

   @GetMapping("/accounts")
   public CustomResponseEntity<List<Account>> findAll();

   @GetMapping(value = "/accounts/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public CustomResponseEntity<Account> findAccount(@PathVariable("accountId") @NotNull Long accountId);

   @GetMapping(value = "/users/{userId}/accounts")
   public CustomResponseEntity<List<Account>> findByUserId(@PathVariable("userId") @NotBlank Long userId);
   
   @GetMapping(value = "/users/{userId}/accounts/{accountId}")
   public CustomResponseEntity<Account> findByUserIdAndAccountId(@PathVariable("userId") @NotNull Long userId, @PathVariable("accountId") @NotNull Long accountId);
   
   @GetMapping(value = "/users/{userId}/accounts/{accountId}/balance")
   public CustomResponseEntity<Double> getBalance(@PathVariable("userId") @NotNull Long userId, @PathVariable("accountId") @NotNull Long accountId);

   @PutMapping(value = "/users/{userId}/accounts/{accountId}/balance", consumes = MediaType.APPLICATION_JSON_VALUE)
   public CustomResponseEntity<String> updateBalance(@RequestBody @NotNull Map<String, Double> balanceMap, @PathVariable("userId") @NotNull Long userId, @PathVariable("accountId") @NotNull Long accountId);
   
   @PutMapping(value = "/users/{userId}/accounts/{accountId}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
   public CustomResponseEntity<String> updateStatus(@RequestBody @NotNull AccountStatus status, @PathVariable("userId") @NotNull Long userId, @PathVariable("accountId") @NotNull Long accountId);
   
   @PutMapping(value = "/users/{userId}/accounts/{accountId}/type", consumes = MediaType.APPLICATION_JSON_VALUE)
   public CustomResponseEntity<String> updateType(@RequestBody @NotNull AccountType type, @PathVariable("userId") @NotNull Long userId, @PathVariable("accountId") @NotNull Long accountId);

   @PatchMapping(value = "/users/{userId}/accounts/{accountId}", consumes = MediaType.APPLICATION_JSON_VALUE)
   public CustomResponseEntity<String> partialUpdate(@RequestBody Map<String, String> updateRequestedMap, @PathVariable("userId") @NotNull Long userId, @PathVariable("accountId") @NotNull Long accountId);

   @DeleteMapping(value = "/users/{userId}/accounts/{accountId}", produces = { MediaType.APPLICATION_JSON_VALUE })
   public CustomResponseEntity<String> close(@PathVariable("userId") @NotNull Long userId, @PathVariable("accountId") @NotNull Long accountId);
   
   @PutMapping(value = "/users/{userId}/accounts/{accountId}/withdraw", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
   public CustomResponseEntity<String[][]> withraw(@RequestBody Map<String, Double> amountMap, @PathVariable("userId") @NotNull Long userId, @PathVariable("accountId") @NotNull Long accountId);
}
