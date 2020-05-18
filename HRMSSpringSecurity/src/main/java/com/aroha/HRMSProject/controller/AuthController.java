package com.aroha.HRMSProject.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aroha.HRMSProject.model.Role;
import com.aroha.HRMSProject.model.User;
import com.aroha.HRMSProject.payload.AddUserRequest;
import com.aroha.HRMSProject.payload.AddUserResponse;
import com.aroha.HRMSProject.payload.DeleteUserResponse;
import com.aroha.HRMSProject.payload.ForgetPassword;
import com.aroha.HRMSProject.payload.ForgotPasswordResponse;
import com.aroha.HRMSProject.payload.JwtAuthenticationResponse;
import com.aroha.HRMSProject.payload.LoginRequest;
import com.aroha.HRMSProject.payload.UpdateAddUserRequest;
import com.aroha.HRMSProject.payload.UpdateAddUserResponse;
import com.aroha.HRMSProject.security.CurrentUser;
import com.aroha.HRMSProject.security.JwtTokenProvider;
import com.aroha.HRMSProject.security.UserPrincipal;
import com.aroha.HRMSProject.service.UserService;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
	
	private final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	UserService userService;

	@Autowired
	JwtAuthenticationResponse jwtObj;

	@PostMapping("/signin")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsernameOrEmail(),
						loginRequest.getPassword()
						)
				);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		User user=userService.getuser(loginRequest.getUsernameOrEmail()).get();
		jwtObj.setTokenType("Bearer");
		jwtObj.setAccessToken(jwt);
		jwtObj.setId(user.getUserId());
		jwtObj.setName(user.getUserName());
		jwtObj.setRoles(authentication.getAuthorities());
		jwtObj.setUsername(user.getUserEmail());
		//return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
		return ResponseEntity.ok(jwtObj);
	}
	
	@PostMapping("/addUsers")
	public ResponseEntity<?> newUser(@RequestBody AddUserRequest addUserReq, @CurrentUser UserPrincipal currentUser){
		AddUserResponse response=userService.newUser(addUserReq, currentUser);
		return ResponseEntity.ok(response);	
	}

	//Get All Users
	@GetMapping("/getAllUsers")
	public ResponseEntity<?> getAllUsers(){
		return ResponseEntity.ok(userService.getAllUser());	
	}

	@PutMapping("/updateNewUser")
	public ResponseEntity<?> updateNewUser(@RequestBody UpdateAddUserRequest updateAddUserReq, @CurrentUser UserPrincipal currentUser){
		System.out.println("Id is: "+updateAddUserReq.getUserId());
		UpdateAddUserResponse response=userService.updateNewUser(updateAddUserReq, currentUser);
		return ResponseEntity.ok(response);		
	}

	//Delete Particular User based on ID
	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<?> deleteUserInRoles(@PathVariable long id){
		DeleteUserResponse deleteUserResponse=userService.deleteUserInRoles(id);
		return ResponseEntity.ok(deleteUserResponse);		
	}

	@PostMapping("/ForgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestBody LoginRequest loginRequest){
		ForgotPasswordResponse forgotPassRes=new ForgotPasswordResponse();
		boolean status=false;
		boolean isExists=userService.checkUserEmail(loginRequest.getUsernameOrEmail());
		if(!isExists){
			forgotPassRes.setStatus(status);
			logger.error(loginRequest.getUsernameOrEmail()+" does not exists");
			forgotPassRes.setResult(loginRequest.getUsernameOrEmail()+" does not exists");
			return ResponseEntity.ok(forgotPassRes);
		}
		else {
			boolean isTrue=userService.forgotPassword(loginRequest.getUsernameOrEmail());
			if(isTrue) {
				status=true;
				forgotPassRes.setStatus(status);
				logger.info("OTP sent to registered emailId");
				forgotPassRes.setResult("OTP sent to registered emailId");
				return ResponseEntity.ok(forgotPassRes);
			}
		}
		status=false;
		forgotPassRes.setStatus(status);
		logger.error("Failed to send the email");
		forgotPassRes.setResult("Failed to send the email");
		return ResponseEntity.ok(forgotPassRes);		
	}

	@PostMapping("/UpdatePassword")
	public ResponseEntity<?> updatePassword(@RequestBody ForgetPassword password) throws Exception{
		return ResponseEntity.ok(userService.updatePassword(password));	
	}
}
