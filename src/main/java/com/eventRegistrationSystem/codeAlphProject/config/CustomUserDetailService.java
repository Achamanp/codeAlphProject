package com.eventRegistrationSystem.codeAlphProject.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.eventRegistrationSystem.codeAlphProject.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CustomUserDetailService implements UserDetails{
	
	private final static  long serialVersionUID = 1L;
	
	private String username;
	private String email;
	
	@JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
	

    public CustomUserDetailService(Long id, String username, String email, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }
    
    public static CustomUserDetailService build(User user) {
    	GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName());
    	return new CustomUserDetailService(user.getId(), user.getName(), user.getEmail(), user.getPassword(), List.of(authority));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        // Should return email if that's what you're using for login
        return email;  // Changed from username to email
    }

    // Add these required methods
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return email;
    }


}
