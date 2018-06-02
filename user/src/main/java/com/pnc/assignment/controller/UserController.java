package com.pnc.assignment.controller;

import com.pnc.assignment.model.User;
import com.pnc.assignment.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> findAll() {
		return userService.findAll();
	}
	
	@GetMapping(value="/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public User findUser(@PathVariable @NotNull Long userId) {
		//return ResponseEntity.ok().body(userService.findUser(userId));
	   return userService.findUser(userId);
	}
	
	@GetMapping(value="/lastname/{lastName}")
	public List<User> findUserByLastName(@PathVariable @NotBlank String lastName) {
		return userService.findByLastName(lastName);
	}
	
	@PutMapping(value="/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String update(@RequestBody @Valid User user, @PathVariable @NotBlank Long userId) {
		userService.update(userId, user);
		return "User with id [" + userId + "] updated successfully.";
	}
	
	@PatchMapping(value="/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> partialUpdate(@RequestBody Map<String, String> updateRequestedMap, @PathVariable Long userId) {
		userService.partialUpdate(userId, updateRequestedMap);
		return ResponseEntity.ok("Fields [" + updateRequestedMap + "] for User with id [" + userId + "] updated successfully.");
	}
	
	@DeleteMapping(value="/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> delete(@PathVariable Long userId) {
		userService.delete(userId);
		return new ResponseEntity<String>("User with id [" + userId + "] deleted successfully.", HttpStatus.OK);
	}

}
