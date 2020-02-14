package com.aroha.HRMSProject.payload;

import com.aroha.HRMSProject.model.Role;

public class RoleResponse {
	private long roleId;
	private String roleName;
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}



	

}
