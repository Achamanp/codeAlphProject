package com.eventRegistrationSystem.codeAlphProject.serviceImpl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eventRegistrationSystem.codeAlphProject.config.CustomUserDetailService;
import com.eventRegistrationSystem.codeAlphProject.config.JwtHelper;
import com.eventRegistrationSystem.codeAlphProject.entities.PasswordResetToken;
import com.eventRegistrationSystem.codeAlphProject.entities.Role;
import com.eventRegistrationSystem.codeAlphProject.entities.User;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.DefaultRoleDoesNotFound;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.EmailAllreadyAssociatedToAnotherAccountException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.InvalidOtpException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.InvalidTokenException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.OtpExpiredException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.UserNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.payloads.LoginRequest;
import com.eventRegistrationSystem.codeAlphProject.payloads.UserDto;
import com.eventRegistrationSystem.codeAlphProject.repository.PasswordResetTokenRepository;
import com.eventRegistrationSystem.codeAlphProject.repository.RoleRepository;
import com.eventRegistrationSystem.codeAlphProject.repository.UserRepository;
import com.eventRegistrationSystem.codeAlphProject.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
    @Autowired
	private ModelMapper modelMapper;
    @Autowired
	private UserRepository userRepository;
    @Autowired
	private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    private Principal principal;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired 
    private JwtHelper jwtHelper;

	@Override
	public String signUp(UserDto userDto) throws DefaultRoleDoesNotFound {
		String email = userDto.getEmail();
		Optional<User> user = this.userRepository.findByEmail(email);
		if(user.isPresent()) {
			throw new EmailAllreadyAssociatedToAnotherAccountException(email+"already associated to another account");
		}
		Role role = roleRepository.findByRoleName("ROLE_USER");
		if(role==null) {
			throw new DefaultRoleDoesNotFound("Default role does not exist");
		}
	   User newUser = this.modelMapper.map(userDto, User.class);
	   newUser.setRole(role);
	   newUser.setCreatedAt(LocalDateTime.now());
	   newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
	   this.userRepository.save(newUser);
		return "Sign Up successfully !! login to acces your authorities";
	}

	@Override
	public String updateUser(UserDto userDto) {
		String username = principal.getName();
		User user = this.userRepository.findByEmail(username).get();
		Role role = this.roleRepository.findByRoleName("ROLE_ADMIN");
		user.setRole(role);
		return "Role updated successfully";
	}

	@Override
	public String deleteUser() {
		String username = this.principal.getName();
		User user = this.userRepository.findByEmail(username).get();
		this.userRepository.delete(user);
		return "Account deleted successfully !!";
	}

	@Override
	public List<UserDto> getAllUser(Integer pageNumber, Integer pageSize, String sortDir, String sortBy) {
	    Sort sort = null;
	    if (sortDir.equalsIgnoreCase("asc")) {
	        sort = Sort.by(sortBy).ascending();
	    } else {
	        sort = Sort.by(sortBy).descending();
	    }
	    Pageable p = PageRequest.of(pageNumber, pageSize, sort);
	    Page<User> pages = this.userRepository.findAll(p);
	    List<User> dtos = pages.getContent();
	    List<UserDto> userDto = dtos.stream()
	            .map(user -> this.modelMapper.map(user, UserDto.class))
	            .collect(Collectors.toList());

	    return userDto; // Return the list of mapped DTOs here
	}


	@Override
	public String forgotPassword(String email) {
		 try {
		        User user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User not found with "+ email));
		        

		        Random rand = new Random();
		        int otp = 1000 + rand.nextInt(9000);
		        String token = UUID.randomUUID().toString();
		        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);
		        PasswordResetToken resetToken = new PasswordResetToken();
		        resetToken.setOtp(otp);
		        resetToken.setToken(token);
		        resetToken.setUser(user);
		        resetToken.setExpirationTime(expirationTime);
		        passwordResetTokenRepository.save(resetToken);

		       
		        SimpleMailMessage message = new SimpleMailMessage();
		        message.setTo(email);
		        message.setSubject("Your OTP for Password Reset");
		        message.setText("Dear " + user.getName() + ",\r\n" +
		                "\r\n" +
		                "We have received a request to reset your password. " +
		                "Use the following OTP: " + otp + " and this token: " + token + 
		                ".\r\n" +
		                "The OTP is valid for 10 minutes.\r\n");
		        javaMailSender.send(message);

		        return "OTP has been sent to your email.";
		        
		    } catch (UserNotFoundException e) {
		        throw e;
		    } catch (OptimisticLockingFailureException e) {
		        throw new OptimisticLockingFailureException("An error occurred while processing your request. Please try again later.", e);
		    } catch (Exception e) {
		        throw new RuntimeException("An unexpected error occurred. Please try again later.", e);
		    }
	}

	@Override
	public String resetPassword(String token, Integer otp, String newPassword) throws InvalidTokenException, OtpExpiredException, com.eventRegistrationSystem.codeAlphProject.globalExceptions.InvalidOtpException {
		PasswordResetToken passwordResetToken = this.passwordResetTokenRepository.findByToken(token);
		if(passwordResetToken==null) {
			  throw new InvalidTokenException("Invalid token. Please request a new OTP.");
		}
		if(passwordResetToken.getExpirationTime().isBefore(LocalDateTime.now())) {
			 throw new OtpExpiredException("OTP has expired. Please request a new one.");
		}
		 if (!passwordResetToken.getOtp().equals(otp)) {
		        throw new InvalidOtpException("Invalid OTP. Please try again.");
		    }
		 try {
			 User user = passwordResetToken.getUser();
			 user.setPassword(passwordEncoder.encode(newPassword));
			 this.userRepository.save(user);
			 this.passwordResetTokenRepository.delete(passwordResetToken);
		 }catch (OptimisticLockingFailureException e) {
		        throw new RuntimeException("Failed to update password due to a conflict. Please try again.", e);
		    }

		    return "Password has been successfully updated.";
	}

	@Override
	public String updateForNormalUser(UserDto userDto) {
		String username = this.principal.getName();
	    User user = this.userRepository.findByEmail(username).orElseThrow(()-> new UserNotFoundException("User not found with " + username));
	    user.setEmail(userDto.getEmail());
	    user.setName(userDto.getName());
	    user.setPhone(userDto.getPhone());
		return "Information updated Successfully !!.";
	}

	@Override
	public String login(LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		User user = this.userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found with email " + username));
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.
				getUsername(), loginRequest.getPassword()));
		if(authentication.isAuthenticated()) {
			GrantedAuthority authorities = new SimpleGrantedAuthority(user.getRole().getRoleName());
			CustomUserDetailService customUserDetailService = new CustomUserDetailService(user.getId(), user.getName() , user.getEmail(), user.getPassword(), List.of(authorities));
			return jwtHelper.generateToken(customUserDetailService);
		}else {
            throw new BadCredentialsException("Invalid username or password!");
        }
		
	}
	
	

}
