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
import com.aroha.HRMSProject.payload.AcceptorRejectProfileResponse;
import com.aroha.HRMSProject.payload.CreateJobListResponse;
import com.aroha.HRMSProject.payload.DeleteJobListResponse;
import com.aroha.HRMSProject.payload.SendEmailResponse;
import com.aroha.HRMSProject.payload.UpdateJobListResponse;
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

	public CreateJobListResponse createJobListing(JobListing jobListing) {
		jobListingRepo.save(jobListing);
		CreateJobListResponse createJoblistRes=new CreateJobListResponse();
		boolean status=true;
		createJoblistRes.setStatus(status);
		createJoblistRes.setResult("Job List is Created Successfully");
		return createJoblistRes;
	}

	public JobListing getJobListById(long joblistId) {
		Optional<JobListing> JobListObj=jobListingRepo.findByjoblistId(joblistId);
		if(JobListObj.isPresent()) {
			return JobListObj.get();
		}
		else
			throw new RecordNotFoundException("Not Found");
	}

	public UpdateJobListResponse updateJobList(JobListing jobListing) {
		Optional<JobListing> jobListId=jobListingRepo.findByjoblistId(jobListing.getJoblistId());
		System.out.println("Here Job List Id is: "+jobListId);
		UpdateJobListResponse updateJobListRes=new UpdateJobListResponse();
		boolean status=false;
		if(jobListId.isPresent()) {
			JobListing joblistingObj=jobListId.get();
			joblistingObj.setJobTitle(jobListing.getJobTitle());
			joblistingObj.setJobDesc(jobListing.getJobDesc());		
			jobListingRepo.save(joblistingObj);
			status=true;
			updateJobListRes.setStatus(status);
			updateJobListRes.setResult("Job List Updated Successfully");
			return updateJobListRes;
		}
		else {
			// TODO Auto-generated method stub
			status=false;
			updateJobListRes.setStatus(status);
			updateJobListRes.setResult("Job List Id is not Present");
			return updateJobListRes;
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

	public DeleteJobListResponse deleteJobListById(JobListing jobListing) {
		Optional<JobListing> joblistid=jobListingRepo.findByjoblistId(jobListing.getJoblistId());
		// TODO Auto-generated method stub
		long id=jobListing.getJoblistId();
		DeleteJobListResponse deleteJobListRes=new DeleteJobListResponse();
		boolean status=false;
		if(joblistid.isPresent()) {
			jobListingRepo.deleteById(id);
			status=true;
			deleteJobListRes.setStatus(status);
			deleteJobListRes.setResult("Job List Deleted Successfully");
			return deleteJobListRes;
		}
		else {
			status=false;
			deleteJobListRes.setStatus(status);
			deleteJobListRes.setResult("Job List Id is not Present");
			return deleteJobListRes;
		}

	}

	public Optional<JobListing> getJobListByJobListId(long joblistid){
		return jobListingRepo.findById(joblistid);
	}

	public ArrayList<Candidate> getCandidateProfileForPerticularJob(long joblistId) {
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

	public SendEmailResponse sendEmail(Candidate candidate) {
		// TODO Auto-generated method stub
		Optional<Candidate> id=candidateRepository.findBycandId(candidate.getCandId());
		SendEmailResponse sendEmailRes=new SendEmailResponse();	
		boolean status=false;
		if(id.isPresent()) {
			Candidate candObj=id.get();
			System.out.println("=========================================================");
			System.out.println("Name is: "+candObj.getCandName());
			System.out.println("Time is: "+candObj.getScheduledTime());
			System.out.println("Interviewer Name is: "+candObj.getInterviewerName());
			System.out.println("Mobile Number is: "+candObj.getMobNumber());
			System.out.println("=========================================================");
			SimpleMailMessage mail = new SimpleMailMessage();
			String subjectLine="Invitation to an interview - Aroha Technologies for the position Software Engineer";
			String message="Hello " +candObj.getCandName()+ ",\n" + 
					"\n" + 
					"Congrats! Your profile has been shortlisted for Software Engineer Position & F2F Interview has been scheduled for "+
					candObj.getScheduledTime()+"\n"+
					"\n" + 
					"Venue and Contact person details:"+
					"\n"+
					"Aroha Technologies "+
					"\n"+
					"5th block, Jayanagar Bangalore-560041" + 
					"\n" + 
					"Contact Person: "+candObj.getInterviewerName()+"-"+candObj.getMobNumber()+"\n"+
					"\n" +
					"Note," + 
					"\n" + 
					"Take a printout of this mail as call letter & same profile, Any of your original ID Proof.";
			mail.setTo(candObj.getCandEmail());
			mail.setSubject(subjectLine);
			mail.setText(message);
			try {
				System.out.println("I am here");
				javaMailSender.send(mail);
				status=true;
				sendEmailRes.setStatus(status);
				sendEmailRes.setResult("Mail Sent Successfully");
				return sendEmailRes;
			}catch(Exception ex) {System.out.println(ex.getMessage());
			}

		}
		status=false;
		sendEmailRes.setStatus(status);
		sendEmailRes.setResult("Cannot Send Email, Id is not Present");
		return sendEmailRes;
	}

	public AcceptorRejectProfileResponse AcceptorRejectProfile(Candidate candidate) {
		Optional<Candidate> candId=candidateRepository.findBycandId(candidate.getCandId());
		AcceptorRejectProfileResponse accOrRejProResponse=new AcceptorRejectProfileResponse();
		boolean status=false;
		if(candId.isPresent()) {
			Candidate candObj=candId.get();
			candObj.setSetStatus(candidate.getSetStatus());
			candObj.setInterviewerName(candidate.getInterviewerName());
			candObj.setScheduledTime(candidate.getScheduledTime());
			candidateRepository.save(candObj);
			status=true;
			accOrRejProResponse.setStatus(status);
			accOrRejProResponse.setResult("Profile Status is Updated Successfully");
			return accOrRejProResponse;
		}else {
			status=false;
			accOrRejProResponse.setStatus(status);
			accOrRejProResponse.setResult("Candidate Id is not Present");
			return accOrRejProResponse;	
		}
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


