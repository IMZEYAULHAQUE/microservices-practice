package com.pnc.assignment.aggregator.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

   private Long accountId;

   @NotNull
   private Long userId;

   @NotNull
   private AccountType type;

   @NotNull
   private Double balance;

   @NotNull
   private AccountStatus status;

   @NotNull
   private LocalDateTime createdDate;

   @NotNull
   private LocalDateTime updatedDate;

   @NotNull
   private String createdBy;

   @NotNull
   private String updatedBy;

   public Account() {
      this.status = AccountStatus.INITIAL_SETUP;
   }

   public Account(@NotNull Long userId, @NotNull AccountType type, @NotNull Double balance, @NotNull AccountStatus status, @NotNull LocalDateTime createdDate, @NotNull LocalDateTime updatedDate, @NotNull String createdBy, @NotNull String updatedBy) {
      super();
      this.userId      = userId;
      this.type        = type;
      this.balance     = balance;
      this.status      = status;
      this.createdDate = createdDate;
      this.updatedDate = updatedDate;
      this.createdBy   = createdBy;
      this.updatedBy   = updatedBy;
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

   public AccountType getType() {
      return type;
   }

   public void setType(AccountType type) {
      this.type = type;
   }

   public Double getBalance() {
      return balance;
   }

   public void setBalance(Double balance) {
      this.balance = balance;
   }

   public AccountStatus getStatus() {
      return status;
   }

   public void setStatus(AccountStatus status) {
      this.status = status;
   }

   public LocalDateTime getCreatedDate() {
      return createdDate;
   }

   public void setCreatedDate(LocalDateTime createdDate) {
      this.createdDate = createdDate;
   }

   public LocalDateTime getUpdatedDate() {
      return updatedDate;
   }

   public void setUpdatedDate(LocalDateTime updatedDate) {
      this.updatedDate = updatedDate;
   }

   public String getCreatedBy() {
      return createdBy;
   }

   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   public String getUpdatedBy() {
      return updatedBy;
   }

   public void setUpdatedBy(String updatedBy) {
      this.updatedBy = updatedBy;
   }

   @Override
   public String toString() {
      return "Account{" +
              "accountId=" + accountId +
              ", userId=" + userId +
              ", type=" + type +
              ", balance=" + balance +
              ", status=" + status +
              ", createdDate=" + createdDate +
              ", updatedDate=" + updatedDate +
              ", createdBy='" + createdBy + '\'' +
              ", updatedBy='" + updatedBy + '\'' +
              '}';
   }
}
