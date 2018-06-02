package com.pnc.assignment.account.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "user_account")
@EntityListeners(AuditingEntityListener.class)
public class Account {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Long accountId;

   @NotNull
   @Column(name = "user_id", nullable = false, updatable = false)
   private Long userId;

   @NotNull
   @Enumerated(EnumType.STRING)
   @Column(name = "account_type")
   private AccountType type;

   @NotNull
   @Column
   private Double balance;

   @NotNull
   @Enumerated(EnumType.STRING)
   @Column(name = "account_status")
   private AccountStatus status;

   @NotNull
   @Column(name="created_date", nullable = false, updatable = false)
   @CreatedDate
   private LocalDateTime createdDate;

   @NotNull
   @Column(name="updated_date", nullable = false)
   @LastModifiedDate
   private LocalDateTime updatedDate;

   @CreatedBy
   @Column(name="created_by", nullable = false, updatable = false)
   private String createdBy;

   @LastModifiedBy
   @Column(name="updated_by", nullable = false)
   private String updatedBy;

   public Account() {
      this.status = AccountStatus.INITIAL_SETUP;
   }

   public Account(@NotNull Long userId, @NotNull AccountType type, @NotNull Double balance, @NotNull AccountStatus status, @NotNull LocalDateTime createdDate, @NotNull LocalDateTime updatedDate, String createdBy, String updatedBy) {
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
      return "Account [accountId=" + accountId + ", userId=" + userId + ", type=" + type + ", balance=" + balance + ", status=" + status + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
   }
}
