package com.aroha.HRMSProject.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GeneratorType;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class JobListing implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long joblistId;
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


}
