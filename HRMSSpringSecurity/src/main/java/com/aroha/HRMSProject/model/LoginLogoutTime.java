package com.aroha.HRMSProject.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LoginLogoutTime {
	
	@Id
	private int userId;
	private String userName;
	private String loggedInTime;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getLoggedInTime() {
		return loggedInTime;
	}
	public void setLoggedInTime(String loggedInTime) {
		this.loggedInTime = loggedInTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}

