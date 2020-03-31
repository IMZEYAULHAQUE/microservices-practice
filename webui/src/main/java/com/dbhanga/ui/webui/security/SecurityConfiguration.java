package com.dbhanga.ui.webui.security;

import com.dbhanga.ui.webui.security.filter.JWTRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${security.authenticationType}")
	private String authenticationType;

	@Value("${spring.h2.console.enabled}")
	private boolean h2ConsoleEnabled;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTRequestFilter jwtRequestFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if ("JDBC".equalsIgnoreCase(authenticationType)) {
			this.configureJDBC(auth);
		}
		else if ("JPA".equalsIgnoreCase(authenticationType)) {
			this.configureJPA(auth);
		}
		else {
			this.configureInMemory(auth);
		}
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {


		if ("JPA".equalsIgnoreCase(authenticationType)) {
			http.csrf().disable()
					.authorizeRequests()
					.antMatchers("/", "/authenticate").permitAll()
					.anyRequest().authenticated()
					.and()
					.exceptionHandling()
					.and()
					// reason for using STATELESS is for not maintaining session at server side bcz jwt contain all we need (it is a VALUE token).
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

			http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		}
		else {
			http.authorizeRequests()
					// matchers should be from most restricted to least restricted
					.antMatchers("/admin", "/console/**").hasAnyRole("ADMIN")
					.antMatchers("/user").hasAnyRole("USER", "ADMIN")
					.antMatchers("/", "static/css", "static/js").permitAll().and()
					.formLogin();

			if (h2ConsoleEnabled) {
				http.csrf().ignoringAntMatchers("/console/**"); //don't apply CSRF protection to /h2-console
				http.headers().frameOptions().sameOrigin();
			}
		}
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	private void configureJPA(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	private void configureJDBC(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
				.dataSource(dataSource)

				// Following setting required if using h2 default schema
				//.withDefaultSchema() // use this one only if schema.sql is not in classpath
				.withUser(User.withUsername("jdbczeyaul").password("test").roles("ADMIN", "USER"))
				.withUser(User.withUsername("jdbchaque").password("test").roles("USER"))
				.withUser(User.withUsername("jdbcdefault").password("test").roles("USER"))

				// This method can be used to adjust sql if schema is different than spring expect.
				// optional if schema is same as schema.sql (spring default)
				.usersByUsernameQuery("select username, password, enabled from users where username = ?")
				.authoritiesByUsernameQuery("select username, authority from authorities where username = ?")
				;
	}

	private void configureInMemory(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("imzeyaul").password("test").roles("ADMIN")
				.and()
				.withUser("imhaque").password("test").roles("USER");
	}
}
