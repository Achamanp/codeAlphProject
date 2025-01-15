package com.eventRegistrationSystem.codeAlphProject.globalExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {
	@ExceptionHandler(EmailAllreadyAssociatedToAnotherAccountException.class)
	public ResponseEntity<ErrorResponse> handleEmailOrUserIdAlreadyAssociatedException(EmailAllreadyAssociatedToAnotherAccountException ex){
		ErrorResponse err = new ErrorResponse(HttpStatus.CONFLICT,ex.getMessage());
		return new ResponseEntity<ErrorResponse>(err,HttpStatus.CONFLICT);
	}
	@ExceptionHandler(DefaultRoleDoesNotFound.class)
	public ResponseEntity<ErrorResponse> handleDefaultRoleDoesNotFoundException(DefaultRoleDoesNotFound ex){
		ErrorResponse err = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
		return new ResponseEntity<ErrorResponse>(err,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException userNotFoundException){
		ErrorResponse err = new ErrorResponse(HttpStatus.NOT_FOUND, userNotFoundException.getMessage());
		return new ResponseEntity<ErrorResponse>(err,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorResponse> invalidTokenExceptionHandler(InvalidTokenException ex){
		ErrorResponse err = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
		return new  ResponseEntity<ErrorResponse>(err,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(OtpExpiredException.class)
	public ResponseEntity<ErrorResponse> handleOtpExpiredException(OtpExpiredException ex){
		ErrorResponse err = new ErrorResponse(HttpStatus.GONE, ex.getMessage());
		return new  ResponseEntity<ErrorResponse>(err,HttpStatus.GONE);	
	}
	@ExceptionHandler(InvalidOtpException.class)
	public ResponseEntity<ErrorResponse> invalidOtpExceptionHandler(InvalidOtpException ex){
		ErrorResponse err = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY,ex.getMessage());
		return new ResponseEntity<ErrorResponse>(err, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(EventAlreadyCreatedException.class)
	public ResponseEntity<ErrorResponse> handleEventAlreadyCreatedException(EventAlreadyCreatedException ex){
		ErrorResponse err = new ErrorResponse(HttpStatus.ALREADY_REPORTED, ex.getMessage());
		return new ResponseEntity<ErrorResponse>(err,HttpStatus.ALREADY_REPORTED);
	}
	@ExceptionHandler(EventNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEventNotFoundException(EventNotFoundException ex){
		ErrorResponse err = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
		return new ResponseEntity<ErrorResponse>(err, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex){
		ErrorResponse err = new  ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
		return new ResponseEntity<ErrorResponse>(err,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ConcurrencyException.class)
	public ResponseEntity<ErrorResponse> handleConcurrencyException(ConcurrencyException ex){
		 ErrorResponse err = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
		    return new ResponseEntity<>(err, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(TicketNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleTicketNotFoundException(TicketNotFoundException ex){
		ErrorResponse err = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
		return new ResponseEntity<ErrorResponse>(err,HttpStatus.NOT_FOUND);
				
	}
	@ExceptionHandler(RegistrationNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleRegistrationNotFoundException(RegistrationNotFoundException ex){
		ErrorResponse err = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
		return new ResponseEntity<ErrorResponse>(err, HttpStatus.NOT_FOUND);
	
				
	}
	
}
