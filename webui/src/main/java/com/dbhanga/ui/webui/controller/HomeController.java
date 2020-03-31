package com.dbhanga.ui.webui.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "<b>Welcome Guest!!</b>";
	}

	@GetMapping("/user")
	public String user() {
		return "<b>Welcome User!!</b>";
	}

	@GetMapping("/admin")
	public String admin() {
		return "<b>Welcome Admin!!</b>";
	}

}
