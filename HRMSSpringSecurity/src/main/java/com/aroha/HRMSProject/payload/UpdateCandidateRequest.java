package com.aroha.HRMSProject.payload;

import java.util.HashSet;
import java.util.Set;

import com.aroha.HRMSProject.model.JobListing;

public class UpdateCandidateRequest {

	private long candId;
	private String candName;
	private String mobNumber;
	private String candEmail;
	private String fileUrl;
	private String createdAt;
	private String setStatus;
	private String interviewerName;
	private String scheduledTime;
	private Set<JobListing> joblisting=new HashSet<>();
	public long getCandId() {
		return candId;
	}
	public void setCandId(long candId) {
		this.candId = candId;
	}
	public String getCandName() {
		return candName;
	}
	public void setCandName(String candName) {
		this.candName = candName;
	}
	public String getMobNumber() {
		return mobNumber;
	}
	public void setMobNumber(String mobNumber) {
		this.mobNumber = mobNumber;
	}
	public String getCandEmail() {
		return candEmail;
	}
	public void setCandEmail(String candEmail) {
		this.candEmail = candEmail;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getSetStatus() {
		return setStatus;
	}
	public void setSetStatus(String setStatus) {
		this.setStatus = setStatus;
	}
	public String getInterviewerName() {
		return interviewerName;
	}
	public void setInterviewerName(String interviewerName) {
		this.interviewerName = interviewerName;
	}
	public String getScheduledTime() {
		return scheduledTime;
	}
	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
	public Set<JobListing> getJoblisting() {
		return joblisting;
	}
	public void setJoblisting(Set<JobListing> joblisting) {
		this.joblisting = joblisting;
	}	
	
	
}
