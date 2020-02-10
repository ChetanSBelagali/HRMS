package com.aroha.HRMSProject.payload;

import com.aroha.HRMSProject.model.Candidate;

public class AddCandidateRequest {
	
	private Candidate addCandidate;
	private String status;
	private long joblistId;
	public Candidate getAddCandidate() {
		return addCandidate;
	}
	public void setAddCandidate(Candidate addCandidate) {
		this.addCandidate = addCandidate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getJoblistId() {
		return joblistId;
	}
	public void setJoblistId(long joblistId) {
		this.joblistId = joblistId;
	}


	
}
