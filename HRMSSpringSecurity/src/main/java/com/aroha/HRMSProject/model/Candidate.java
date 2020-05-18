package com.aroha.HRMSProject.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Candidate implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long candId;
	private String candName;
	private String mobNumber;
	private String candEmail;
	private String fileUrl;
	private String createdAt;
	private String setStatus;
	private String interviewerName;
	private String interviewerEmail;
	private String interviewerMobNumber;
	private String scheduledTime;	
	private String statusAfterInterview;
	@Lob
	private String reason;	
	@Lob
	private String feedback;


	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "candidate1",
	joinColumns = @JoinColumn(name = "candId"),
	inverseJoinColumns = @JoinColumn(name = "joblistId"))
	private Set<JobListing> joblisting=new HashSet<>();

	public Set<JobListing> getJoblisting() {
		return joblisting;
	}

	public void setJoblisting(Set<JobListing> joblisting) {
		this.joblisting = joblisting;
	}

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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getInterviewerEmail() {
		return interviewerEmail;
	}

	public void setInterviewerEmail(String interviewerEmail) {
		this.interviewerEmail = interviewerEmail;
	}

	public String getInterviewerMobNumber() {
		return interviewerMobNumber;
	}

	public void setInterviewerMobNumber(String interviewerMobNumber) {
		this.interviewerMobNumber = interviewerMobNumber;
	}

	public String getStatusAfterInterview() {
		return statusAfterInterview;
	}

	public void setStatusAfterInterview(String statusAfterInterview) {
		this.statusAfterInterview = statusAfterInterview;
	}
	
	

}
