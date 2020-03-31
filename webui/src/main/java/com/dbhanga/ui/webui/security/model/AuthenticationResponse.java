package com.dbhanga.ui.webui.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class AuthenticationResponse implements Serializable {

	@Getter
	private final String jwt;
}
