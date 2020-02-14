package com.aroha.HRMSProject.payload;

import com.aroha.HRMSProject.model.Role;
import com.aroha.HRMSProject.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpRequest {
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private User addUser;
	private String status;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Role role;


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
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	
	
}
