package com.aroha.HRMSProject.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GeneratorType;

@Entity
public class JobListing {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long joblistid;
	private String jobdesc;
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

	
}
