package com.aroha.HRMSProject.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aroha.HRMSProject.model.Role;
import com.aroha.HRMSProject.model.User;
import com.aroha.HRMSProject.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;


	@Autowired
	private RoleService roleService;

	public Optional<User> getuser(String userNameOrEmail){
		return userRepo.findByuseremail(userNameOrEmail);
	}

	public String addUser(long roleId,User user) {
		Boolean isExists=userRepo.existsByuseremail(user.getUseremail());
		System.out.println("IsExists :"+isExists);
		if(isExists) {
			return "User with "+user.getUseremail()+" already exists";
		}else {
			//Optional<User> userObj=userRepo.findByuseremail(user.getUseremail());
			//User userData=userObj.get();
			Optional<Role> role=roleService.findRole(roleId);
			System.out.println("RoleId: "+roleId);
			if(role.isPresent()) {
				Role roleObj=role.get();
				user.getRole().add(roleObj);
				userRepo.save(user);
				return "User is Saved";
			}
			else {
				return "Role is not Present";
			}
		}
	}


	public List<User> getAllUser(){
		return userRepo.findAll();

	}

	public String updateUser(Long userid, User user) {
		Optional<User> userId=userRepo.findByuserid(userid);
		if(userId.isPresent()) {
			User useObj=userId.get();
			useObj.setUsername(user.getUsername());
			useObj.setUseremail(user.getUseremail());
			useObj.setUserpassword(user.getUserpassword());
			useObj.setPhoneNumber(user.getPhoneNumber());
			useObj.setAddress(user.getAddress());
			useObj.setRole(user.getRole());
			System.out.println("Address is: "+useObj.getAddress());
			System.out.println("Password is: "+useObj.getUserpassword());
			userRepo.save(useObj);
			return "User Profile is Updated Successfully";
		}
		else {
			return "Something Went Wrong";
		}	
	}

	public String deleteUserInRoles(Long userId) {
		Optional<User> userIdObj=userRepo.findByuserid(userId);
		if(userIdObj.isPresent()) {
			userRepo.deleteById(userId);
			return "User Deleted Successfully";
		}
		else {
			return "User ID not Present";
		}
	}

}
