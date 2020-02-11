package com.aroha.HRMSProject.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aroha.HRMSProject.model.Role;
import com.aroha.HRMSProject.model.User;
import com.aroha.HRMSProject.payload.ForgetPassword;
import com.aroha.HRMSProject.payload.SignUpResponse;
import com.aroha.HRMSProject.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;


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

	public SignUpResponse addUser(long roleId,User user) {
		Boolean isExists=userRepo.existsByuserEmail(user.getUserEmail());
		SignUpResponse signUpResponse=new SignUpResponse();
		System.out.println("IsExists :"+isExists);
		if(isExists) {
			signUpResponse.setResult("User Already Exists");
			signUpResponse.setStatus("Fail");
			return signUpResponse;
		}else {
			//Optional<User> userObj=userRepo.findByuseremail(user.getUseremail());
			//User userData=userObj.get();
			Optional<Role> role=roleService.findRole(roleId);
			System.out.println("RoleId: "+roleId);
			if(role.isPresent()) {
				Role roleObj=role.get();
				user.getRole().add(roleObj);
				userRepo.save(user);
				signUpResponse.setResult("User is Saved");
				signUpResponse.setStatus("Success");
				
				return signUpResponse;
			}
			else {
				signUpResponse.setResult("Something Went Wrong");
				signUpResponse.setStatus("Fail");
				return signUpResponse;
			}
		}
	}


	public List<User> getAllUser(){
		return userRepo.findAll();

	}

	public String updateUser(Long userid, User user) {
		Optional<User> userId=userRepo.findByuserId(userid);
		if(userId.isPresent()) {
			User useObj=userId.get();
			useObj.setUserName(user.getUserName());
			useObj.setUserEmail(user.getUserEmail());
			useObj.setUserPassword(user.getUserPassword());
			useObj.setPhoneNumber(user.getPhoneNumber());
			useObj.setAddress(user.getAddress());
			useObj.setRole(user.getRole());
			System.out.println("Address is: "+useObj.getAddress());
			System.out.println("Password is: "+useObj.getUserPassword());
			userRepo.save(useObj);
			return "User Profile is Updated Successfully";
		}
		else {
			return "Something Went Wrong";
		}	
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
}
