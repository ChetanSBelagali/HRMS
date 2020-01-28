package com.aroha.HRMSProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aroha.HRMSProject.model.User;
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
		jwtObj.setId(user.getUserid());
		jwtObj.setName(user.getUsername());
		jwtObj.setRoles(authentication.getAuthorities());
		jwtObj.setUsername(user.getUseremail());
		//return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
		return ResponseEntity.ok(jwtObj);
	}

	//Add Users
	@PostMapping("/addUsers")
	public ResponseEntity<?> addUsersInRoles(@RequestBody SignUpRequest signUp,@CurrentUser UserPrincipal user){
		Long roleId=signUp.getRoleId();
		User getUser=signUp.getAdduser();
		getUser.setUserpassword(passwordEncoder.encode(signUp.getAdduser().getUserpassword()));
		signUp.setStatus(userService.addUser(roleId, getUser));
		return ResponseEntity.ok(signUp);
	}

	//Get All Users
	@GetMapping("/getAllUsers")
	public ResponseEntity<?> getAllUsers(){
		return ResponseEntity.ok(userService.getAllUser());	
	}
	
	@PostMapping("/updateUserInRoles")
	public ResponseEntity<?> updateUserInRoles(@RequestBody SignUpRequest signUp,@CurrentUser UserPrincipal user){
		Long userId=signUp.getAdduser().getUserid();
		User getUser=signUp.getAdduser();
		getUser.setUserpassword(passwordEncoder.encode(signUp.getAdduser().getUserpassword()));
		signUp.setStatus(userService.updateUser(userId, getUser));
		return ResponseEntity.ok(signUp);		
	}

}
