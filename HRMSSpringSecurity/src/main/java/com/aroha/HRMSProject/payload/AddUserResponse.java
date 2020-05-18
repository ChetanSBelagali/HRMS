package com.aroha.HRMSProject.payload;

import java.util.List;

import com.aroha.HRMSProject.model.User;

public class AddUserResponse {
	
	private boolean status;
	private String result;
	private User data;
	

	public User getData() {
		return data;
	}
	public void setData(User data) {
		this.data = data;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	

}
