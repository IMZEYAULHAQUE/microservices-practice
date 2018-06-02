package com.pnc.assignment.aggregator.feignclient.account;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pnc.assignment.aggregator.custom.CustomResponseEntity;
import com.pnc.assignment.aggregator.model.account.Account;
import com.pnc.assignment.aggregator.model.account.AccountStatus;
import com.pnc.assignment.aggregator.model.account.AccountType;

/**
 * @ TODO Need to find out how to implement selected method as fallback and leave rest for default failure. Raise a ticket in github 'https://github.com/OpenFeign/feign/issues/681'
 */
@Component
public class AccountFeignClientFallback implements AccountFeignClient {
   
   private Logger logger = LoggerFactory.getLogger(AccountFeignClientFallback.class);
   
   private Throwable cause;
   
   public AccountFeignClientFallback() {
   }
   
   public AccountFeignClientFallback setCause(Throwable cause) {
      this.cause = cause;
      return this;
   }

   @Override
   public CustomResponseEntity<List<Account>> findAll() {
      logger.error(cause.toString());
      return CustomResponseEntity.SUCCESS(Collections.<Account>emptyList());
   }

   @Override
   public CustomResponseEntity<Account> findAccount(@NotNull Long accountId) {
      logger.error(cause.toString());
      return CustomResponseEntity.SUCCESS();
   }

   @Override
   public CustomResponseEntity<List<Account>> findByUserId(@NotBlank Long userId) {
      logger.error(cause.toString());
      return CustomResponseEntity.SUCCESS(Collections.<Account>emptyList());
   }

   @Override
   public CustomResponseEntity<Account> findByUserIdAndAccountId(@NotNull Long userId, @NotNull Long accountId) {
      logger.error(cause.toString());
      return CustomResponseEntity.SUCCESS();
   }

   @Override
   public CustomResponseEntity<Double> getBalance(@NotNull Long userId, @NotNull Long accountId) {
      logger.error(cause.toString());
      return CustomResponseEntity.SUCCESS(Double.valueOf(0.0));
   }

   @Override
   public CustomResponseEntity<String> updateBalance(@NotNull Map<String, Double> balanceMap, @NotNull Long userId, @NotNull Long accountId) {
      logger.error(cause.toString());
      return CustomResponseEntity.FAILURE("Site under maintenance. Please check back in some time.");
   }

   @Override
   public CustomResponseEntity<String> updateStatus(@NotNull AccountStatus status, @NotNull Long userId, @NotNull Long accountId) {
      logger.error(cause.toString());
      return CustomResponseEntity.FAILURE("Site under maintenance. Please check back in some time.");
   }

   @Override
   public CustomResponseEntity<String> updateType(@NotNull AccountType type, @NotNull Long userId, @NotNull Long accountId) {
      logger.error(cause.toString());
      return CustomResponseEntity.FAILURE("Site under maintenance. Please check back in some time.");
   }

   @Override
   public CustomResponseEntity<String> partialUpdate(Map<String, String> updateRequestedMap, @NotNull Long userId, @NotNull Long accountId) {
      logger.error(cause.toString());
      return CustomResponseEntity.FAILURE("Site under maintenance. Please check back in some time.");
   }

   @Override
   public CustomResponseEntity<String> close(@NotNull Long userId, @NotNull Long accountId) {
      logger.error(cause.toString());
      return CustomResponseEntity.FAILURE("Site under maintenance. Please check back in some time.");
   }

   @Override
   public CustomResponseEntity<String[][]> withraw(Map<String, Double> amountMap, @NotNull Long userId, @NotNull Long accountId) {
      logger.error(cause.toString());
      return CustomResponseEntity.FAILURE(new String[][] {{"newBalance", "Site Under maintenance."}});
   }
}
