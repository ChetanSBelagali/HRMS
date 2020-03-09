package com.aroha.HRMSProject.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.aroha.HRMSProject.payload.ForgetPassword;
import com.aroha.HRMSProject.payload.UpdateAddUserRequest;
import com.aroha.HRMSProject.payload.UpdateAddUserResponse;
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

	@Autowired
	private JavaMailSender javaMailSender;


	@Autowired
	PasswordEncoder passwordEncoder;


	public Optional<User> getuser(String userNameOrEmail){
		return userRepo.findByuserEmail(userNameOrEmail);
	}

	public List<User> getAllUser(){
		return userRepo.findAll();		 
	}

	public String deleteUserInRoles(Long userId) {
		Optional<User> userIdObj=userRepo.findByuserId(userId);
		if(userIdObj.isPresent()) {
			userRepo.deleteById(userId);
			return "User Deleted Successfully";
		}
		else {
			return "User ID not Present";
		}
	}

	public boolean checkUserEmail(String usernameOrEmail) {
		Boolean isExists=userRepo.existsByuserEmail(usernameOrEmail);
		// TODO Auto-generated method stub
		return isExists;		
	}

	public boolean forgotPassword(String usernameOrEmail) {
		// TODO Auto-generated method stub
		long code=Code();
		for (long i=code;i!=0;i/=100)//a loop extracting 2 digits from the code  
		{ 
			long digit=i%100;//extracting two digits 
			if (digit<=90) 
				digit=digit+32;  
			//converting those two digits(ascii value) to its character value 
			char ch=(char) digit; 
			// adding 32 so that our least value be a valid character  
			unique_password=ch+unique_password;//adding the character to the string 
		} 
		Optional<User> user=userRepo.findByuserEmail(usernameOrEmail);
		User getUser=user.get();
		boolean istrue=sendEmail(unique_password,usernameOrEmail);
		return istrue;

	}

	public boolean sendEmail(String unique_password,String userOrEmail) {
		//logger.info("OTP Password is "+unique_password);
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
			//logger.info("Email sent to registered email");
			return true;
		}catch(Exception ex) {		
			//logger.error("Failed to send mail "+ex.getMessage());

		}
		return false;

	}

	public static long Code() //this code returns the  unique 16 digit code  
	{  //creating a 16 digit code using Math.random function 
		long code   =(long)((Math.random()*9*Math.pow(10,15))+Math.pow(10,15)); 
		return code; //returning the code 
	}

	public Object updatePassword(ForgetPassword object) {
		String getOtpFromUser=object.getOneTimePass();
		if(getOtpFromUser.equals(unique_password)) {
			String email=object.getUsernameOrEmail();
			Optional<User> obj=userRepo.findByuserEmail(email);
			User user=obj.get();
			if(passwordEncoder.matches(object.getPassword(), user.getUserPassword()) ){
				//logger.info("Error You can not give previous password, please enter a new password");
				return "You can not give previous password, please enter a new password";
			}else {
				user.setUserPassword((passwordEncoder.encode(object.getPassword())));
				userRepo.save(user);
				//logger.info("password changed for :"+user.getName());
				return "Password updated successfully, please login with your new password";
			}
		}else {
			//logger.error("OTP didn't matched");
			return "OTP did not match";
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
				status=true;
				addUserRes.setStatus(status);
				addUserRes.setResult("User Saved Successfully");
				return addUserRes;
			}
		}
		status=false;
		addUserRes.setStatus(status);
		addUserRes.setResult("Something Went Wrong");
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
			userObj.setUserName(updateAddUserReq.getUserName());
			userObj.setUserEmail(updateAddUserReq.getUserEmail());
			userObj.setPhoneNumber(updateAddUserReq.getPhoneNumber());
			userObj.setAddress(updateAddUserReq.getAddress());
			userObj.setUserPassword(passwordEncoder.encode(updateAddUserReq.getUserPassword()));
			userRepo.save(userObj);
			status=true;
			updateAddUserResponse.setStatus(status);
			updateAddUserResponse.setResult("User Updated Successfully");
			return updateAddUserResponse;
		}
		status=false;
		updateAddUserResponse.setStatus(status);
		updateAddUserResponse.setResult("User Id is not Present");
		return updateAddUserResponse;
	}

}
