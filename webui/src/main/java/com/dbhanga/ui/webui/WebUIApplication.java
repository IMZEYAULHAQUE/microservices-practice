package com.dbhanga.ui.webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class WebUIApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebUIApplication.class, args);
	}

}
