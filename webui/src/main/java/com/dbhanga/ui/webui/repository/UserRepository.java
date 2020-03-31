package com.dbhanga.ui.webui.repository;

import com.dbhanga.ui.webui.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
