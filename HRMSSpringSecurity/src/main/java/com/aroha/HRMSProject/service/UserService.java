package com.aroha.HRMSProject.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aroha.HRMSProject.model.Role;
import com.aroha.HRMSProject.model.RoleName;
import com.aroha.HRMSProject.model.User;
import com.aroha.HRMSProject.payload.AddUserRequest;
import com.aroha.HRMSProject.payload.AddUserResponse;
import com.aroha.HRMSProject.payload.DeleteUserResponse;
import com.aroha.HRMSProject.payload.ForgetPassword;
import com.aroha.HRMSProject.payload.UpdateAddUserRequest;
import com.aroha.HRMSProject.payload.UpdateAddUserResponse;
import com.aroha.HRMSProject.payload.UpdatePasswordResponse;
import com.aroha.HRMSProject.repo.RoleRepository;
import com.aroha.HRMSProject.repo.UserRepository;
import com.aroha.HRMSProject.security.CurrentUser;
import com.aroha.HRMSProject.security.UserPrincipal;
import com.aroha.HRMSProject.exception.AppException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepo;


	@Autowired
	private RoleService roleService;

	static String unique_password="";
	
	static String strOTP="";

	@Autowired
	private JavaMailSender javaMailSender;


	@Autowired
	PasswordEncoder passwordEncoder;
	
	private static final Logger Logger=LoggerFactory.getLogger(UserService.class);


	public Optional<User> getuser(String userNameOrEmail){
		return userRepo.findByuserEmail(userNameOrEmail);
	}

	public List<User> getAllUser(){
		return userRepo.findAll();		 
	}

	public DeleteUserResponse deleteUserInRoles(Long userId) {
		Optional<User> userIdObj=userRepo.findByuserId(userId);
		boolean status=true;
		DeleteUserResponse deleteUserResponse=new DeleteUserResponse();
		if(userIdObj.isPresent()) {
			userRepo.deleteById(userId);
			deleteUserResponse.setStatus(status);
			deleteUserResponse.setResult("User Deleted Successfully");
			Logger.info("User Deleted Successfully");
			return deleteUserResponse;
		}
		else {
			status=false;
			deleteUserResponse.setStatus(status);
			deleteUserResponse.setResult("User Id is not Present");
			Logger.error("User Id is not Present");
			return deleteUserResponse;
		}
	}

	public boolean checkUserEmail(String usernameOrEmail) {
		Boolean isExists=userRepo.existsByuserEmail(usernameOrEmail);
		// TODO Auto-generated method stub
		return isExists;		
	}

	public boolean forgotPassword(String usernameOrEmail) {
		Optional<User> user=userRepo.findByuserEmail(usernameOrEmail);
		User getUser=user.get();
		Random rand=new Random();
		int uniqueValue=rand.nextInt(1000000);
		strOTP=String.valueOf(uniqueValue);
		System.out.println(uniqueValue);
		boolean istrue=sendEmail(strOTP,usernameOrEmail);
		return istrue;

	}

	public boolean sendEmail(String unique_password,String userOrEmail) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(userOrEmail);
		msg.setSubject("Forget Password");
		msg.setText("Dear User ,\n" +
				"\n" +
				"The OTP generated for your Account with ID "+ userOrEmail +"  is : " + unique_password
				+"\n\n"+
				"\nUse this OTP to change the password\n"+
				"In case of any queries, kindly contact our customer service desk at the details below\n" +
				"\n" +
				"\n" +
				"Warm Regards,\n" +
				"\n" +
				"ArohaTechnologies");
		try {
			javaMailSender.send(msg);
			Logger.info("Email sent to registered email");
			return true;
		}catch(Exception ex) {		
			Logger.error("Failed to send mail "+ex.getMessage());

		}
		return false;

	}

	public Object updatePassword(ForgetPassword object) {
		String getOtpFromUser=object.getOneTimePass();
		UpdatePasswordResponse updatePassRes=new UpdatePasswordResponse();
		boolean status=false;
		if(getOtpFromUser.equals(strOTP)) {
			String email=object.getUsernameOrEmail();
			Optional<User> obj=userRepo.findByuserEmail(email);
			User user=obj.get();
			if(passwordEncoder.matches(object.getPassword(), user.getUserPassword()) ){
				Logger.info("Error You can not give previous password, please enter a new password");
				updatePassRes.setStatus(status);
				updatePassRes.setResult("You can not give previous password, please enter a new password");
				return updatePassRes;
			}else {
				user.setUserPassword((passwordEncoder.encode(object.getPassword())));
				userRepo.save(user);
				status=true;
				updatePassRes.setStatus(status);
				updatePassRes.setResult("Password updated successfully, please login with your new password");
				Logger.info("password changed for :"+user.getUserName());
				return updatePassRes;
			}
		}else {
			Logger.error("OTP didn't matched");
			status=false;
			updatePassRes.setStatus(status);
			updatePassRes.setResult("OTP did not match");
			return updatePassRes;
		}		
	}

	public AddUserResponse newUser(AddUserRequest addUserReq, UserPrincipal currentUser) {

		Boolean isExists=userRepo.existsByuserEmail(addUserReq.getUserEmail());
		AddUserResponse addUserRes=new AddUserResponse();
		System.out.println("IsExists :"+isExists);
		Boolean status=false;
		if(isExists) {
			addUserRes.setStatus(status);
			addUserRes.setResult("User Already Exists");
			Logger.error("User Already Exists");
			return addUserRes;
		}
		else {
			long getRoleId=0;
			//String getRoleName="";
			Set<Role>r=addUserReq.getRole();
			Iterator<Role>itr=r.iterator();
			while(itr.hasNext()) {
				Role robj=itr.next();
				getRoleId=robj.getRoleId();
				//getRoleName=robj.getRoleName();
			}
			Optional<Role> role=roleService.findRole(getRoleId);
			//Optional<Role> role=roleService.findByRoleName(getRoleName);
			//System.out.println("RoleId: "+roleId);
			User user = new User();
			if(role.isPresent()) {
				Role roleObj=role.get();
				user.getRole().add(roleObj);
				user.setUserName(addUserReq.getUserName());
				user.setUserEmail(addUserReq.getUserEmail());
				user.setPhoneNumber(addUserReq.getPhoneNumber());
				user.setAddress(addUserReq.getAddress());
				user.setUserPassword(passwordEncoder.encode(addUserReq.getUserPassword()));
				System.out.println("Role is: "+user.getRole());
				userRepo.save(user);
				Logger.info("User saved Successfully");
				status=true;
				addUserRes.setStatus(status);
				addUserRes.setResult("User Saved Successfully");
				addUserRes.setData(user);
				return addUserRes;
			}
		}
		status=false;
		addUserRes.setStatus(status);
		addUserRes.setResult("Something Went Wrong");
		Logger.error("Something Went Wrong");
		return addUserRes;
	}

	public List<User> findByRole(String roleName) {
		return userRepo.findByRole(roleName);
	}

	public Optional<Role> findByRoleName(RoleName roleName) {
		return roleRepo.findByRoleName(roleName);
	}

	public UpdateAddUserResponse updateNewUser(UpdateAddUserRequest updateAddUserReq, UserPrincipal currentUser) {
		// TODO Auto-generated method stub
		boolean status=false;
		UpdateAddUserResponse updateAddUserResponse=new UpdateAddUserResponse();
		Optional<User> userID=userRepo.findByuserId(updateAddUserReq.getUserId());
		if(userID.isPresent()) {
			User userObj=userID.get();
			userObj.setRole(updateAddUserReq.getRole());
			System.out.println("fghfhf: "+userObj.getRole().toString());
			userObj.setUserName(updateAddUserReq.getUserName());
			userObj.setUserEmail(updateAddUserReq.getUserEmail());
			userObj.setPhoneNumber(updateAddUserReq.getPhoneNumber());
			userObj.setAddress(updateAddUserReq.getAddress());
			userObj.setUserPassword(passwordEncoder.encode(updateAddUserReq.getUserPassword()));
			userRepo.save(userObj);
			status=true;
			updateAddUserResponse.setStatus(status);
			updateAddUserResponse.setResult("User Updated Successfully");
			updateAddUserResponse.setData(userObj);
			Logger.info("User Updated Successfully");
			return updateAddUserResponse;
		}
		status=false;
		updateAddUserResponse.setStatus(status);
		updateAddUserResponse.setResult("User Id is not Present");
		Logger.error("User Id is not Present");
		return updateAddUserResponse;
	}

}
