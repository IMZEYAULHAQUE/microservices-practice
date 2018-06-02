package com.pnc.assignment.account.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pnc.assignment.account.exception.AccountNotFoundException;
import com.pnc.assignment.account.exception.InvalidAmountWithdrawException;
import com.pnc.assignment.account.model.Account;
import com.pnc.assignment.account.model.AccountStatus;
import com.pnc.assignment.account.model.AccountType;
import com.pnc.assignment.account.repository.AccountRepository;

@Service
public class AccountService {

   @Autowired
   private AccountRepository accountRepository;
   
   public List<Account> findAll() {
      return accountRepository.findAll();
   }

   public Account findById(@NotNull Long accountId) {
      return accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
   }

   public List<Account> findByUserId(@NotBlank Long userId) {
      return accountRepository.findByUserId(userId).orElseThrow(() -> new AccountNotFoundException(userId, null /*accountId*/));
   }
   
   public Account findByAccountIdAndUserId(Long userId, Long accountId) {
      return accountRepository.findByAccountIdAndUserId(accountId, userId).orElseThrow(() -> new AccountNotFoundException(userId, accountId));
   }

   public void updateAccountStatus(Long userId, Long accountId, AccountStatus status) {
      Account account = this.findByAccountIdAndUserId(userId, accountId);
      account.setStatus(status);
      accountRepository.save(account);
   }
   
   public void updateAccountType(Long userId, Long accountId, AccountType type) {
      Account account = this.findByAccountIdAndUserId(userId, accountId);
      account.setType(type);
      accountRepository.save(account);
   }
   
   public void updateBalance(Long userId, Long accountId, Double balance) {
      Account account = this.findByAccountIdAndUserId(userId, accountId);
      account.setBalance(balance);
      accountRepository.save(account);
   }
   
   public Double getBalance(Long userId, Long accountId) {
      Account account = this.findByAccountIdAndUserId(userId, accountId);
      return account.getBalance();
   }

   public Account partialUpdate(Long userId, Long accountId, Map<String, String> accountFieldsUpdateMap) {
      
      Account account = this.findByAccountIdAndUserId(userId, accountId);
      LocalDateTime existingCreatedDate = account.getCreatedDate();
      
      accountFieldsUpdateMap.remove("createdDate");  // remove createdDate in case passed in by caller. 
      
      accountFieldsUpdateMap.forEach((k, v) -> {
         PropertyAccessor propertyAccessor = PropertyAccessorFactory.forBeanPropertyAccess(account);
         propertyAccessor.setPropertyValue(k, v);
      });
      
      account.setAccountId(accountId); //required because map might have id as input with different value
      account.setUserId(userId);
      account.setCreatedDate(existingCreatedDate);
      return accountRepository.save(account);
   }

   public void delete(Long userId, Long accountId) {
      if (this.findByAccountIdAndUserId(userId, accountId) != null) {
         this.updateAccountStatus(userId, accountId, AccountStatus.CLOSED);
      }
   }
   
   public Double withdraw(Long userId, Long accountId, Double withdrawAmount) {
      
      Account account = this.findByAccountIdAndUserId(userId, accountId);
      
      if (withdrawAmount < 0) {
         throw new InvalidAmountWithdrawException("Withdraw amount can't be negative.");
      }
      
      if (account.getBalance() < withdrawAmount) {
         throw new InvalidAmountWithdrawException("Insufficient Balance. ");
      }
      
      account.setBalance(account.getBalance() - withdrawAmount);
      accountRepository.save(account);
      
      return account.getBalance();
   }
}
