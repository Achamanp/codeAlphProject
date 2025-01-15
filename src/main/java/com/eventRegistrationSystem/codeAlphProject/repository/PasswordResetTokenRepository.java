package com.eventRegistrationSystem.codeAlphProject.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventRegistrationSystem.codeAlphProject.entities.PasswordResetToken;
import com.eventRegistrationSystem.codeAlphProject.entities.User;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{

	PasswordResetToken findByToken(String token);

	void deleteByUser(User user);
	
	@Query("SELECT u FROM PasswordResetToken u WHERE u.expirationTime < :now")
	List<PasswordResetToken> findAllExpiredTokens(@Param("now") LocalDateTime now);

}
