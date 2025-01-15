package com.eventRegistrationSystem.codeAlphProject.globalExceptions;

import java.lang.reflect.Executable;

public class EventNotFoundException extends Exception{
	public EventNotFoundException(String message) {
		super(message);
	}
}
