package com.aroha.HRMSProject.payload;

import com.aroha.HRMSProject.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpRequest {
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private User addUser;
	private String status;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private long roleId;


	public User getAddUser() {
		return addUser;
	}
	public void setAddUser(User addUser) {
		this.addUser = addUser;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
	
}
