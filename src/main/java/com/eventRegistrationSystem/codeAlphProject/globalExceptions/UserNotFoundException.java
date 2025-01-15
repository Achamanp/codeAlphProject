package com.eventRegistrationSystem.codeAlphProject.globalExceptions;

public class UserNotFoundException extends RuntimeException{
	public UserNotFoundException(String message) {
		super(message);
	}
}
