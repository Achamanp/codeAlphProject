package com.eventRegistrationSystem.codeAlphProject.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eventRegistrationSystem.codeAlphProject.globalExceptions.DefaultRoleDoesNotFound;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.EmailAllreadyAssociatedToAnotherAccountException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.InvalidOtpException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.InvalidTokenException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.OtpExpiredException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.UserNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.payloads.AuthResponse;
import com.eventRegistrationSystem.codeAlphProject.payloads.LoginRequest;
import com.eventRegistrationSystem.codeAlphProject.payloads.UserDto;
import com.eventRegistrationSystem.codeAlphProject.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World !!";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        logger.debug("Received signup request for email: {}", userDto.getEmail());

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // Collect all validation errors
            List<String> errors = bindingResult.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
                
            String errorMessage = String.join(", ", errors);
            logger.error("Validation failed for signup request: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        try {
            String response = userService.signUp(userDto);
            logger.info("Successfully created user with email: {}", userDto.getEmail());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DefaultRoleDoesNotFound e) {
            logger.error("Default role not found error: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EmailAllreadyAssociatedToAnotherAccountException e) {
            logger.error("Email already exists error: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    
    @PutMapping("/admin/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUserRole(@Valid @RequestBody UserDto userDto) {
        String response = userService.updateUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        String response = userService.deleteUser();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<UserDto> users = userService.getAllUser(pageNumber, pageSize, sortDir, sortBy);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
    //    try {
            String response = userService.forgotPassword(email);
            return new ResponseEntity<>(response, HttpStatus.OK);
       // } catch (UserNotFoundException e) {
          //  return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        //} catch (RuntimeException e) {
           // return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        //}
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam Integer otp,
            @RequestParam String newPassword) throws Exception {
       // try {
            String response = userService.resetPassword(token, otp, newPassword);
            return new ResponseEntity<>(response, HttpStatus.OK);
       // } catch (InvalidTokenException | OtpExpiredException | InvalidOtpException e) {
          //  return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        //} catch (RuntimeException e) {
           // return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      //  }
    }
    
    @PutMapping("/update")
    public ResponseEntity<String> updateUserInfo(@Valid @RequestBody UserDto userDto) {
        String response = userService.updateForNormalUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
  
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            String token = userService.login(loginRequest);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
