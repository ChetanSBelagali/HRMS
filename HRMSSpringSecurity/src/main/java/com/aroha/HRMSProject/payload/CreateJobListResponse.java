package com.aroha.HRMSProject.payload;

import com.aroha.HRMSProject.model.JobListing;

public class CreateJobListResponse {
	
	private boolean status;
	private String result;
	private JobListing data;
	
	
	public JobListing getData() {
		return data;
	}
	public void setData(JobListing data) {
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
