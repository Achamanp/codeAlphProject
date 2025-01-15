package com.eventRegistrationSystem.codeAlphProject.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        final String requestHeader = request.getHeader("Authorization"); // Correct header name
        String userName = null;
        String token = null;
        try {
        	if(requestHeader!=null && requestHeader.startsWith("Bearer ")) {
        		token = requestHeader.substring(7);
        		userName = jwtHelper.getUsernameFromTOken(token);
        		if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
        			UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userName);
        			if(jwtHelper.validateToken(token)) {
        				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        			}
        		}
        	}
        	
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
		
	}

}
