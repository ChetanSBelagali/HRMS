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
import javax.persistence.ManyToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Candidate implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long candid;
	private String candname;
	private String mobnumber;
	private String candemail;
	private String fileurl;
	private Instant createdat;

	@ManyToMany(mappedBy = "candidate")
	private Set<JobListing> joblisting=new HashSet<>();

	public Set<JobListing> getJoblisting() {
		return joblisting;
	}

	public void setJoblisting(Set<JobListing> joblisting) {
		this.joblisting = joblisting;
	}

	public long getCandid() {
		return candid;
	}

	public void setCandid(long candid) {
		this.candid = candid;
	}

	public String getCandname() {
		return candname;
	}

	public void setCandname(String candname) {
		this.candname = candname;
	}

	public String getMobnumber() {
		return mobnumber;
	}

	public void setMobnumber(String mobnumber) {
		this.mobnumber = mobnumber;
	}

	public String getCandemail() {
		return candemail;
	}

	public void setCandemail(String candemail) {
		this.candemail = candemail;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public Instant getCreatedat() {
		return createdat;
	}

	public void setCreatedat(Instant createdat) {
		this.createdat = createdat;
	}


}
