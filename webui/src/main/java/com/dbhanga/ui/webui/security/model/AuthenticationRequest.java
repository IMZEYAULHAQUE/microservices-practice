package com.dbhanga.ui.webui.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {

	@NotEmpty(message = "username is mandatory")
	private String username;

	@NotEmpty(message = "passwored is mandatory")
	private String password;
}
