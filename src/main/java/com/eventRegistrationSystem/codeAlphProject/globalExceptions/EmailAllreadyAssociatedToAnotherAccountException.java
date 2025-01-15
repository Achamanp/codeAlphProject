package com.eventRegistrationSystem.codeAlphProject.globalExceptions;

public class EmailAllreadyAssociatedToAnotherAccountException extends RuntimeException {
	public EmailAllreadyAssociatedToAnotherAccountException(String msg) {
		super(msg);
	}
}
