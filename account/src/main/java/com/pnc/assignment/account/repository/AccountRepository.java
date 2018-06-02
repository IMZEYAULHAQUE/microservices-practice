package com.pnc.assignment.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.assignment.account.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
   
   public Optional<List<Account>> findByUserId(Long userId);
   
   public Optional<Account> findByAccountIdAndUserId(Long accountId, Long userId);

}
