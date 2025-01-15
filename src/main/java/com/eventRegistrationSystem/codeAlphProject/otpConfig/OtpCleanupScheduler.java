package com.eventRegistrationSystem.codeAlphProject.otpConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import com.eventRegistrationSystem.codeAlphProject.entities.PasswordResetToken;
import com.eventRegistrationSystem.codeAlphProject.repository.PasswordResetTokenRepository;

@Component
public class OtpCleanupScheduler {
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	
	@Scheduled(fixedRate = 60000) 
	public void expiredOtp() {
		LocalDateTime now = LocalDateTime.now();
		List<PasswordResetToken> expiredToken = this.passwordResetTokenRepository.findAllExpiredTokens(now);
		if(!expiredToken.isEmpty()) {
			this.passwordResetTokenRepository.deleteAll(expiredToken);
			System.out.println("Deleted expired OTP tokens at: " + now);
		}
	}
}
