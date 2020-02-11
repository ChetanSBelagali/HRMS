package com.aroha.HRMSProject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aroha.HRMSProject.exception.FileNotFoundException;
import com.aroha.HRMSProject.exception.RecordNotFoundException;
import com.aroha.HRMSProject.exception.ResourceNotFoundException;
import com.aroha.HRMSProject.model.Candidate;
import com.aroha.HRMSProject.model.JobListing;
import com.aroha.HRMSProject.model.Role;
import com.aroha.HRMSProject.model.User;
import com.aroha.HRMSProject.repo.CandidateRepository;
import com.aroha.HRMSProject.repo.JobListingRepository;
import com.sun.mail.util.MailSSLSocketFactory;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class HRService {

	@Autowired
	JobListingRepository jobListingRepo;

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	public HRService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public JobListing createJobListing(JobListing jobListing) {
		return jobListingRepo.save(jobListing);
	}

	public JobListing getJobListById(long joblistId) {
		Optional<JobListing> JobListObj=jobListingRepo.findByjoblistId(joblistId);
		if(JobListObj.isPresent()) {
			return JobListObj.get();
		}
		else
			throw new RecordNotFoundException("Not Found");
	}

	public JobListing updateJobList(JobListing jobListing) {
		Optional<JobListing> jobListId=jobListingRepo.findByjoblistId(jobListing.getJoblistId());
		System.out.println("Here Job List Id is: "+jobListId);
		if(jobListId.isPresent()) {
			JobListing joblistingObj=jobListId.get();
			joblistingObj.setJobDesc(jobListing.getJobDesc());
			return jobListingRepo.save(joblistingObj);
		}
		else {
			// TODO Auto-generated method stub
			return jobListingRepo.save(jobListing);
		}
	}

	public List<JobListing> getAllJobListing() {
		List<JobListing> jobList=jobListingRepo.findAll();
		if(jobList.size()>0) {
			return jobList;
		}
		return new ArrayList<JobListing>();
		// TODO Auto-generated method stub		
	}

	public String deleteJobListById(JobListing jobListing) {
		Optional<JobListing> joblistid=jobListingRepo.findByjoblistId(jobListing.getJoblistId());
		// TODO Auto-generated method stub
		long id=jobListing.getJoblistId();
		if(joblistid.isPresent()) {
			jobListingRepo.deleteById(id);
			return "JobListing Deleted Successfully";
		}
		else {
			return "No Record Found";
		}

	}

	public Optional<JobListing> getJobListByJobListId(long joblistid){
		return jobListingRepo.findById(joblistid);
	}

	public ArrayList<Candidate> getUploadedProfilesForPerticularJob(long joblistId) {
		List<Long> candObj=candidateRepository.getByJobListId(joblistId);
		ArrayList<Candidate> arrayList=new ArrayList<>();
		// TODO Auto-generated method stub
		for(Long i:candObj) {
			//Optional<Candidate> candaData=candidateRepository.findById(i);
			Optional<Candidate> candaData=candidateRepository.findById(i);
			Candidate cObj=candaData.get();
			arrayList.add(cObj);			
		}		
		return arrayList;		
	}

	public String getProfileURLToDownloadById(Candidate candidate) {
		// TODO Auto-generated method stub
		Optional<Candidate> candid=candidateRepository.findBycandId(candidate.getCandId());
		if(candid.isPresent()) {
			Candidate candObj=candid.get();
			String URL=candObj.getFileUrl();
			return URL;
		}
		else {
			return "No Id Found";
		}
	}

	public void sendEmail(Candidate candidate) {
		// TODO Auto-generated method stub
		SimpleMailMessage mail = new SimpleMailMessage();
		String subjectLine="Invitation to an interview - Aroha Technologies for the position Software Engineer";
		String message="Hello " +candidate.getCandName()+ ",\n" + 
				"\n" + 
				"Congrats! Your profile has been shortlisted for Software Engineer Position & F2F Interview has been scheduled for "+
				     candidate.getScheduledTime()+"\n"+
				"\n" + 
				"Venue and Contact person details:"+
				"\n"+
				"Aroha Technologies "+
				"\n"+
				"5th block, Jayanagar Bangalore-560041" + 
				"\n" + 
				"Contact Person: "+candidate.getInterviewerName()+"-"+candidate.getMobNumber()+"\n"+
				"\n" +
				"Note," + 
				"\n" + 
				"Take a printout of this mail as call letter & same profile, Any of your original ID Proof.";
		mail.setTo(candidate.getCandEmail());
		mail.setSubject(subjectLine);
		mail.setText(message);
		System.out.println(mail.getText());

		/*
		 * This send() contains an Object of SimpleMailMessage as an Parameter
		 */
		try {
			javaMailSender.send(mail);
			System.out.println("Mail sent successfully");
		}catch(Exception ex) {System.out.println(ex.getMessage());}
	}

	public Candidate updateFileUploader(Candidate candidate) {
		Optional<Candidate> candId=candidateRepository.findBycandId(candidate.getCandId());
		if(candId.isPresent()) {
			Candidate candObj=candId.get();
			candObj.setSetStatus(candidate.getSetStatus());
			candObj.setInterviewerName(candidate.getInterviewerName());
			candObj.setScheduledTime(candidate.getScheduledTime());
			return candidateRepository.save(candObj);
		}
		return candidateRepository.save(candidate);		
	}

	public Candidate scheduleInterview(Candidate candidate) {
		Optional<Candidate> candObj=candidateRepository.findBycandId(candidate.getCandId());
		if(candObj.isPresent()) {
			return candObj.get();
		}
		throw new RecordNotFoundException("Not Found");		
	}

	public List<Candidate> viewAllScheduledInterviews() {
		List<Candidate> allInterviews=candidateRepository.getAllScheduledInterviews();
		if(allInterviews.size()>0) {
			return allInterviews;
		}
		throw new FileNotFoundException("Not Found");		
	}	
}


