package com.eventRegistrationSystem.codeAlphProject.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eventRegistrationSystem.codeAlphProject.entities.User;
import com.eventRegistrationSystem.codeAlphProject.repository.UserRepository;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user= this.userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Usename not found " + username));
		
		
		
		return CustomUserDetailService.build(user);
	}

}
