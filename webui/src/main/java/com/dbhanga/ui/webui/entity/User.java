package com.dbhanga.ui.webui.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;


//@TODO Replace this User with User object provided by spring security
@Entity
@Table(name = "users")
@Data
public class User {
	@Id
	private String username;
	private String password;
	private Boolean enabled;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<UserAuthority> authorities;

}
