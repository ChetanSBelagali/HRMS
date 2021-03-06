package com.aroha.HRMSProject.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GeneratorType;

import com.aroha.HRMSProject.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class JobListing{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long joblistId;
	private String clientName;
	private String postedDate;
	private String jobTitle;
	private String status;
	
	@Lob 
	@Column(name="jobDesc", length=1024)
	private String jobDesc;


	//@ManyToMany(fetch = FetchType.LAZY)
	//@JoinTable(name = "job_desc",
	//joinColumns = @JoinColumn(name = "joblist_id"),
	//inverseJoinColumns = @JoinColumn(name = "cand_id"))
	
	@ManyToMany(mappedBy = "joblisting")
	private Set<Candidate> candidate=new HashSet<>();

	
	public long getJoblistId() {
		return joblistId;
	}
	public void setJoblistId(long joblistId) {
		this.joblistId = joblistId;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	@JsonIgnore
	public Set<Candidate> getCandidate() {
		return candidate;
	}
	public void setCandidate(Set<Candidate> candidate) {
		this.candidate = candidate;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(String postedDate) {
		this.postedDate = postedDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
