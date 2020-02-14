package com.aroha.HRMSProject.payload;

public class SignUpResponse {
	
	//private String result;
	private boolean status;
	private Data data;
	
//	public String getResult() {
//		return result;
//	}
//	public void setResult(String result) {
//		this.result = result;
//	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}	
}
