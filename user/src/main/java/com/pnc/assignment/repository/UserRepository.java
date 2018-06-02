package com.pnc.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pnc.assignment.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
	
	public List<User> findByLastName(String lastName);

}
