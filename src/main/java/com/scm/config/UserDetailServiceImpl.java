package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.scm.dao.UserRepositery;
import com.scm.entities.User;

public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepositery userRepositery;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepositery.getUserByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException("Not Found User !!");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(user);

		return customUserDetails;
	}

}
