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
	private long joblistid;
	private String jobdesc;


	//@ManyToMany(fetch = FetchType.LAZY)
	//@JoinTable(name = "job_desc",
	//joinColumns = @JoinColumn(name = "joblist_id"),
	//inverseJoinColumns = @JoinColumn(name = "cand_id"))
	
	@ManyToMany(mappedBy = "joblisting")
	private Set<Candidate> candidate=new HashSet<>();

	public long getJoblistid() {
		return joblistid;
	}
	public void setJoblistid(long joblistid) {
		this.joblistid = joblistid;
	}
	public String getJobdesc() {
		return jobdesc;
	}
	public void setJobdesc(String jobdesc) {
		this.jobdesc = jobdesc;
	}
	
	@JsonIgnore
	public Set<Candidate> getCandidate() {
		return candidate;
	}
	public void setCandidate(Set<Candidate> candidate) {
		this.candidate = candidate;
	}


}
