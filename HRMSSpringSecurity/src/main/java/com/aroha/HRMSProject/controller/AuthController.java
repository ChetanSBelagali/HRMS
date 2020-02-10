package com.aroha.HRMSProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aroha.HRMSProject.model.User;
import com.aroha.HRMSProject.payload.ForgetPassword;
import com.aroha.HRMSProject.payload.JwtAuthenticationResponse;
import com.aroha.HRMSProject.payload.LoginRequest;
import com.aroha.HRMSProject.payload.SignUpRequest;
import com.aroha.HRMSProject.security.CurrentUser;
import com.aroha.HRMSProject.security.JwtTokenProvider;
import com.aroha.HRMSProject.security.UserPrincipal;
import com.aroha.HRMSProject.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

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

	//Add Users
	@PostMapping("/addUsers")
	public ResponseEntity<?> addUsersInRoles(@RequestBody SignUpRequest signUp,@CurrentUser UserPrincipal user){
		Long roleId=signUp.getRoleId();
		User getUser=signUp.getAddUser();
		getUser.setUserPassword(passwordEncoder.encode(signUp.getAddUser().getUserPassword()));
		signUp.setStatus(userService.addUser(roleId, getUser));
		return ResponseEntity.ok(signUp);
	}

	//Get All Users
	@GetMapping("/getAllUsers")
	public ResponseEntity<?> getAllUsers(){
		return ResponseEntity.ok(userService.getAllUser());	
	}

	//Update Users
	@PostMapping("/updateUserInRoles")
	public ResponseEntity<?> updateUserInRoles(@RequestBody SignUpRequest signUp,@CurrentUser UserPrincipal user){
		Long userId=signUp.getAddUser().getUserId();
		User getUser=signUp.getAddUser();
		System.out.println("Id is: "+userId);
		getUser.setUserPassword(passwordEncoder.encode(signUp.getAddUser().getUserPassword()));
		signUp.setStatus(userService.updateUser(userId, getUser));
		return ResponseEntity.ok(signUp);		
	}

	//Delete Particular User based on ID
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUserInRoles(@PathVariable("id") long id){
		String result=userService.deleteUserInRoles(id);
		return ResponseEntity.ok(result);		
	}

	@PostMapping("/ForgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestBody LoginRequest loginRequest){
		boolean isExists=userService.checkUserEmail(loginRequest.getUsernameOrEmail());
		if(!isExists){
			return ResponseEntity.ok(loginRequest.getUsernameOrEmail()+" does not exists");
		}
		else {
			boolean isTrue=userService.forgotPassword(loginRequest.getUsernameOrEmail());
			if(isTrue) {
				return ResponseEntity.ok("OTP sent to registered emailId");
			}
		}
		return ResponseEntity.ok("Failed to send the email");		
	}

	@PostMapping("/UpdatePassword")
	public ResponseEntity<?> updatePassword(@RequestBody ForgetPassword password){
		return ResponseEntity.ok(userService.updatePassword(password));	
	}
}
