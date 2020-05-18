package com.aroha.HRMSProject.payload;

import com.aroha.HRMSProject.model.User;

public class UpdateAddUserResponse {
	private boolean status;
	private String result;
	private User data;
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
	public User getData() {
		return data;
	}
	public void setData(User data) {
		this.data = data;
	}		
}
