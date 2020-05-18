package com.aroha.HRMSProject.controller;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aroha.HRMSProject.model.Role;
import com.aroha.HRMSProject.payload.CreateRole;
import com.aroha.HRMSProject.payload.RoleResponse;
import com.aroha.HRMSProject.security.CurrentUser;
import com.aroha.HRMSProject.security.UserPrincipal;
import com.aroha.HRMSProject.service.RoleService;


@RestController
@RequestMapping("/api")
public class RoleController {
	
	private final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;

	//Create Role
	@PostMapping("/createRole")
	public ResponseEntity<?> createNewRole(@RequestBody CreateRole createNewRole,@CurrentUser UserPrincipal user){
		if(!user.isAdminRole()) {
			logger.error("Not Authorized to create role");
			return ResponseEntity.ok("Not Authorized to create role");
		}
		Role getROle=createNewRole.getCreateRole();
		createNewRole.setStatus(roleService.saveRole(getROle));
		return ResponseEntity.ok(createNewRole);
	}

	//Find Role
	@GetMapping("/findRoles")
	public ResponseEntity<?> getAllRole(){
		if(roleService.getAllRole().isEmpty()) {
			logger.error("No role found");
			return ResponseEntity.ok("No role found");
		}
//		return ResponseEntity.ok(roleService.getAllRole());
		List<Role> role=roleService.getAllRole();
		ArrayList<RoleResponse>list=new ArrayList<>();
		Iterator<Role>itr=role.iterator();
		while(itr.hasNext()) {
			Role r=itr.next();
			RoleResponse roleRes=new RoleResponse();
			if(r.getRoleName().equals("ROLE_ADMIN")) {
				roleRes.setRoleId(r.getRoleId());
				roleRes.setRoleName("ROLE_ADMIN");

			}if(r.getRoleName().equals("ROLE_MENTOR")) {
				roleRes.setRoleId(r.getRoleId());
				roleRes.setRoleName("ROLE_MENTOR");

			}else if(r.getRoleName().equals("ROLE_HR")) {
				roleRes.setRoleId(r.getRoleId());
				roleRes.setRoleName("ROLE_HUMAN_RESOURCE");

			}
			list.add(roleRes);
		}
		return ResponseEntity.ok(list);
	}

}
