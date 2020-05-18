package com.aroha.HRMSProject.payload;

import com.aroha.HRMSProject.model.Candidate;

public class InterviewFeedback {
	
	private boolean status;
	private String result;
	private Candidate data;
	
	
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
	public Candidate getData() {
		return data;
	}
	public void setData(Candidate data) {
		this.data = data;
	}
	
	

}
