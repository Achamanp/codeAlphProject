package com.eventRegistrationSystem.codeAlphProject.config;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHelper {
	
	 private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);
	
	
	@Value("${spring.app.jwtSecret}")
	private String secretKey;
	
	 @Value("${spring.app.jwtExpirationMs}")
	    private int jwtExpirationMs;
	 
	 public String getUsernameFromTOken(String token) {
		 return Jwts.parser()
				 .verifyWith((SecretKey)getKey())
				 .build().parseSignedClaims(token)
				 .getPayload().getSubject();
	 }

	    public Key getKey() {
	    	byte[] keyByte = Decoders.BASE64.decode(secretKey);
	    	return Keys.hmacShaKeyFor(keyByte);
	    }
	
	public Date getExpiration(String token) {
		return Jwts.parser().verifyWith((SecretKey)getKey())
				.build().parseSignedClaims(token)
				.getPayload().getExpiration();
	}
	
	public String generateToken(CustomUserDetailService customUserDetailService) {
    	String userName = customUserDetailService.getUsername();
        String email = customUserDetailService.getEmail();
        String roles = customUserDetailService.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));
		return Jwts.builder()
				  .subject(email)
	                .claim("username", userName)
	                .claim("roles", roles)
	                .issuedAt(new Date())
	                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
	                .signWith(getKey())
	                .compact();
	}
	
	public boolean validateToken(String token) {
		try {
	    	 Jwts.parser()
	    			.verifyWith((SecretKey)getKey())
	    			.build().parseSignedClaims(token);
	    	 return true;
	    	}catch (MalformedJwtException e) {
	            logger.error("Invalid JWT token: {}", e.getMessage());
	        } catch (ExpiredJwtException e) {
	            logger.error("JWT token is expired: {}", e.getMessage());
	        } catch (UnsupportedJwtException e) {
	            logger.error("JWT token is unsupported: {}", e.getMessage());
	        } catch (IllegalArgumentException e) {
	            logger.error("JWT claims string is empty: {}", e.getMessage());
	        }
	        return false;
	}
}
