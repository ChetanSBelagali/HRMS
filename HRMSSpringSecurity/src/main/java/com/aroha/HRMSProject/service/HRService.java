package com.aroha.HRMSProject.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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
import com.aroha.HRMSProject.payload.CreateJobListRequest;
import com.aroha.HRMSProject.payload.CreateJobListResponse;
import com.aroha.HRMSProject.payload.DeleteJobListResponse;
import com.aroha.HRMSProject.payload.InterviewFeedback;
import com.aroha.HRMSProject.payload.SendEmailResponse;
import com.aroha.HRMSProject.payload.UpdateJobListResponse;
import com.aroha.HRMSProject.repo.CandidateRepository;
import com.aroha.HRMSProject.repo.JobListingRepository;
import com.sun.mail.util.MailSSLSocketFactory;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

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
		Optional<JobListing> jobTitle=jobListingRepo.findByjobTitle(jobListing.getJobTitle());
		boolean status=false;
		CreateJobListResponse createJoblistRes=new CreateJobListResponse();

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateTime = formatter.format(date);

		if(jobTitle.isPresent()) {
			createJoblistRes.setStatus(status);
			createJoblistRes.setResult("Same Job List is Already Present");
			return createJoblistRes;
		}
		else {
			jobListing.setPostedDate(dateTime);
			jobListingRepo.save(jobListing);
			status=true;
			createJoblistRes.setStatus(status);
			createJoblistRes.setResult("Job List is Created Successfully");
			createJoblistRes.setData(jobListing);
			return createJoblistRes;
		}
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

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateTime = formatter.format(date);

		boolean status=false;
		if(jobListId.isPresent()) {
			JobListing joblistingObj=jobListId.get();
			joblistingObj.setJobTitle(jobListing.getJobTitle());
			joblistingObj.setClientName(jobListing.getClientName());
			joblistingObj.setPostedDate(dateTime);
			joblistingObj.setJobDesc(jobListing.getJobDesc());		
			joblistingObj.setStatus(jobListing.getStatus());
			jobListingRepo.save(joblistingObj);
			status=true;
			updateJobListRes.setStatus(status);
			updateJobListRes.setResult("Job List Updated Successfully");
			updateJobListRes.setData(joblistingObj);
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

	public DeleteJobListResponse deleteJobListById(long id) {
		Optional<JobListing> joblistid=jobListingRepo.findByjoblistId(id);
		// TODO Auto-generated method stub
		//long id=jobListing.getJoblistId();
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

	public SendEmailResponse sendEmail(Candidate candidate) throws MessagingException {
		// TODO Auto-generated method stub
		Optional<Candidate> id=candidateRepository.findBycandId(candidate.getCandId());
		SendEmailResponse sendEmailRes=new SendEmailResponse();
		boolean status=false;
		String jobName="";
		if(id.isPresent()) {
			Candidate candObj=id.get();
			//==================================================================================================================//
			Set<JobListing> jobListDesc=candObj.getJoblisting();
			Iterator<JobListing> itr=jobListDesc.iterator();
			while(itr.hasNext()) {
				JobListing robj=itr.next();
				jobName=robj.getJobTitle();
				//getRoleName=robj.getRoleName();
			}
			//==================================================================================================================//
			SimpleMailMessage mail = new SimpleMailMessage();
			SimpleMailMessage mail1 = new SimpleMailMessage();
			//=================================================================================================================================//
			if(candObj.getSetStatus().equalsIgnoreCase("ACCEPT")) {
				String subjectLine="Invitation to an interview - Aroha Technologies for the position"+" "+jobName;
				String subjectLine1="Interview to be Conducted on "+candObj.getScheduledTime();
				String message="Hello " +candObj.getCandName()+ ",\n" + 
						"\n" + 
						"Congrats! Your profile has been shortlisted for "+jobName+" Position & F2F Interview has been scheduled On "+candObj.getScheduledTime()+
						"\n"+
						"\n" + 
						"Venue and Contact person details:"+
						"\n"+
						"Aroha Technologies "+
						"\n"+
						"5th block, Jayanagar Bangalore-560041" + 
						"\n" + 
						"Contact Person: "+candObj.getInterviewerName()+"-"+candObj.getInterviewerMobNumber()+"\n"+
						"\n" +
						"Note," + 
						"\n" + 
						"Take a printout of this mail as call letter & same profile, Any of your original ID Proof.";

				String message1="Hello "+candObj.getInterviewerName()+ ",\n" +
						"\n"+
						"Candidate Name: "+candObj.getCandName() +
						"\n"+
						"Candidate Phone.no: "+candObj.getMobNumber() +
						"\n"+
						"Candidate Email: "+candObj.getCandEmail() +
						"\n"+
						"Interview Date & Time: "+candObj.getScheduledTime() +
						"\n" + 
						"\n" +
						"Best Wishes,"+
						"\n" +
						"Aroha Technologies";
				System.out.println(subjectLine);
				System.out.println(message);
				System.out.println("============================================================");
				System.out.println(subjectLine1);
				System.out.println(message1);
				//================================================================================//
				mail.setTo(candObj.getCandEmail());
				mail.setSubject(subjectLine);
				mail.setText(message);
				//================================================================================//
				mail1.setTo(candObj.getInterviewerEmail());
				mail1.setSubject(subjectLine1);
				mail1.setText(message1);
				//================================================================================//
				try {
					javaMailSender.send(mail);
					javaMailSender.send(mail1);
					status=true;
					sendEmailRes.setStatus(status);
					sendEmailRes.setResult("Mail Sent Successfully");
					return sendEmailRes;
				}catch(Exception ex) {System.out.println(ex.getMessage());
				}
			}
			//==========================================================================================================================//
			else {
				String subjectLine="Status of the Job Interview";
				String message="Hello " +candObj.getCandName()+ ",\n" + 
						"\n" + 
						"We appreciate your interest in our company and the time you’ve invested in applying for the software engineer opening."+
						"\n"+
						"We ended up moving forward with another candidate, but we’d like to thank you for talking to our team and giving us the"+
						"opportunity to learn about your skills and accomplishments."+
						"\n"+
						"We will be advertising more positions in the coming months. We hope you’ll keep us in mind and we encourage you to"+
						"apply again."+
						"\n"+
						"We wish you good luck with your job search and professional future endeavors." + 
						"\n" + 
						"Best Wishes,"+
						"\n" +
						"Aroha Technologies";
				mail.setTo(candObj.getCandEmail());
				mail.setSubject(subjectLine);
				mail.setText(message);
				try {
					//System.out.println("I am here unable to send email");
					//javaMailSender.send(mail);
					javaMailSender.send(mail);
					status=true;
					sendEmailRes.setStatus(status);
					sendEmailRes.setResult("Mail Sent Successfully");
					return sendEmailRes;
				}catch(Exception ex) {System.out.println(ex.getMessage());
				}

			}
			//==================================================================================================//
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
			candObj.setInterviewerEmail(candidate.getInterviewerEmail());
			candObj.setInterviewerMobNumber(candidate.getInterviewerMobNumber());
			candObj.setScheduledTime(candidate.getScheduledTime());
			candObj.setReason(candidate.getReason());
			candObj.setFeedback(candidate.getFeedback());
			candidateRepository.save(candObj);
			status=true;
			accOrRejProResponse.setStatus(status);
			accOrRejProResponse.setResult("Profile Status is Updated Successfully");
			accOrRejProResponse.setData(candObj);
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

	public InterviewFeedback interviewFeedback(Candidate candidate) {
		// TODO Auto-generated method stub
		InterviewFeedback interviewFeedback=new InterviewFeedback();
		boolean status=true;
		Optional<Candidate> candObj=candidateRepository.findBycandId(candidate.getCandId());
		if(candObj.isPresent()) {
			Candidate obj=candObj.get();
			obj.setStatusAfterInterview(candidate.getStatusAfterInterview());
			obj.setReason(candidate.getReason());
			obj.setFeedback(candidate.getFeedback());
			candidateRepository.save(obj);
			interviewFeedback.setStatus(status);
			interviewFeedback.setResult("Feedback Saved Successfully");
			interviewFeedback.setData(obj);
			return interviewFeedback;
		}	
		status=false;
		interviewFeedback.setStatus(status);
		interviewFeedback.setResult("Candidate ID Not Found");
		return interviewFeedback;

	}
}


