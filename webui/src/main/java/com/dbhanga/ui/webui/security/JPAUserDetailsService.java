package com.dbhanga.ui.webui.security;

import com.dbhanga.ui.webui.entity.User;
import com.dbhanga.ui.webui.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JPAUserDetailsService implements UserDetailsService {

	private final static boolean useHardcoding = false;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (useHardcoding) {
			return this.loadStaticUser(username);
		}

		Optional<User> userByName = userRepository.findById(username);
		User user = userByName.orElseThrow(() -> new UsernameNotFoundException("Username not found."));
		//userByName.map(CustomUserDetails::new).get();
		return this.transform(user);
	}

	private UserDetails loadStaticUser(String username) throws UsernameNotFoundException {
		if (!"jpazeyaul".equalsIgnoreCase(username) && !"jpahaque".equalsIgnoreCase(username)) {
			throw new UsernameNotFoundException("Username not found.");
		}

		List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
		if ("jpazeyaul".equalsIgnoreCase(username)) {
			authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		CustomUserDetails.CustomUserDetailsBuilder builder = new CustomUserDetails.CustomUserDetailsBuilder();
		builder.username(username.toLowerCase())
				.password(username.toLowerCase())
				.accountNonLocked(true)
				.accountNotExpired(true)
				.credentialsNonExpired(true).enabled(true)
				.authorities(authorities);

		CustomUserDetails userDetails = builder.build();
		return userDetails;
	}

	private UserDetails transform(User user) throws UsernameNotFoundException {
		CustomUserDetails.CustomUserDetailsBuilder builder = new CustomUserDetails.CustomUserDetailsBuilder();

		if (user.getAuthorities() == null) {
			new UsernameNotFoundException("Username not found.");
		}
		Collection<GrantedAuthority> authorities = user.getAuthorities().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthority())).collect(Collectors.toList());

		builder.username(user.getUsername())
				.password(user.getPassword())
				.accountNonLocked(true)
				.accountNotExpired(true)
				.credentialsNonExpired(true)
				.enabled(user.getEnabled() != null ? user.getEnabled() : false)
				.authorities(authorities);
		return builder.build();
	}
}
