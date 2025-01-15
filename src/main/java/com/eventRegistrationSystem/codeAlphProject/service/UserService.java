package com.eventRegistrationSystem.codeAlphProject.service;

import java.util.List;

import com.eventRegistrationSystem.codeAlphProject.globalExceptions.DefaultRoleDoesNotFound;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.InvalidOtpException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.InvalidTokenException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.OtpExpiredException;
import com.eventRegistrationSystem.codeAlphProject.payloads.LoginRequest;
import com.eventRegistrationSystem.codeAlphProject.payloads.UserDto;

public interface UserService {
	public String signUp(UserDto userDto) throws DefaultRoleDoesNotFound;
	public String updateForNormalUser(UserDto userDto);
	public String deleteUser();
	List<UserDto> getAllUser(Integer pageNumber, Integer pageSize, String sortDir, String sortBy);
	String updateUser(UserDto userDto);
	String forgotPassword(String email);
	String resetPassword(String token, Integer otp, String newPassword) throws InvalidTokenException, OtpExpiredException, InvalidOtpException, Exception;
	String login(LoginRequest loginRequest);

}
