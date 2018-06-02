package com.pnc.assignment.service;

import com.pnc.assignment.model.User;

import java.util.List;
import java.util.Map;

public interface IUserService {

   public List<User> findAll();

   public User findUser(Long userId);

   public List<User> findByLastName(String lastName);

   public User update(Long userId, User user);

   public User partialUpdate(Long userId, Map<String, String> userFieldsUpdateMap);

   public void delete(Long userId);

   public boolean isUserExist(Long userId);
}
