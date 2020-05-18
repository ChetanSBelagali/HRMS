package com.aroha.HRMSProject.controller;

import java.awt.PageAttributes.MediaType;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.aroha.HRMSProject.config.FileConfig;
import com.aroha.HRMSProject.model.Candidate;
import com.aroha.HRMSProject.model.JobListing;
import com.aroha.HRMSProject.payload.AcceptorRejectProfileResponse;
import com.aroha.HRMSProject.payload.CreateJobListRequest;
import com.aroha.HRMSProject.payload.CreateJobListResponse;
import com.aroha.HRMSProject.payload.DeleteJobListResponse;
import com.aroha.HRMSProject.payload.InterviewFeedback;
import com.aroha.HRMSProject.payload.SendEmailResponse;
import com.aroha.HRMSProject.payload.UpdateJobListResponse;
import com.aroha.HRMSProject.repo.CandidateRepository;
import com.aroha.HRMSProject.service.HRService;

@RestController
@RequestMapping("/api")
public class HRController {
	
	private final Logger logger = LoggerFactory.getLogger(HRController.class);

	@Autowired
	HRService hrService;

	@Autowired
	FileDownloadController fileDownloadController;

	@Autowired
	CandidateRepository candidateRepo;

	@Autowired
	HttpServletRequest request;

	//=====================================================================================
	//Create Job List
	@PostMapping("/CreateJobList")
	public ResponseEntity<?> createJobListing(@RequestBody JobListing jobListing){
		CreateJobListResponse createJoblistRes=hrService.createJobListing(jobListing);
		return ResponseEntity.ok(createJoblistRes);	
	}
	//=====================================================================================
	//Get Job List By id
	@GetMapping("/getJobListById")
	public ResponseEntity<?> getJobListById(@RequestBody JobListing jobListing){
		JobListing getJobListObj=hrService.getJobListById(jobListing.getJoblistId());
		return ResponseEntity.ok(getJobListObj);	
	}

	//=====================================================================================
	//Update Job List
	@PutMapping("/UpdateJobList")
	public ResponseEntity<?> updateJobListing(@RequestBody JobListing jobListing){
		UpdateJobListResponse updateJobListRes=hrService.updateJobList(jobListing);
		return ResponseEntity.ok(updateJobListRes);		
	}

	//=====================================================================================
	//Get All Job Lists
	@GetMapping("/getAllJobList")
	public ResponseEntity<?> getAllJobListing(){
		List<JobListing> list=hrService.getAllJobListing();
		return ResponseEntity.ok(list);		
	}

	//==========================================================================================
	//Delete Job List By ID
	@DeleteMapping("/deleteJobListingById/{id}")
	public ResponseEntity<?> deleteJobListById(@PathVariable long id){
		DeleteJobListResponse deleteJobListRes=hrService.deleteJobListById(id);
		return ResponseEntity.ok(deleteJobListRes);		

	}

	//============================================================================================
	//Get Uploaded Profiles For Particular Job
	@GetMapping("/getCandidateProfileForPerticularJob")
	public ResponseEntity<?> getCandidateProfileForPerticularJob(@RequestBody JobListing joblisting){
		long joblistId=joblisting.getJoblistId();
		if(hrService.getCandidateProfileForPerticularJob(joblistId).isEmpty()) {
			logger.error("No candicate found for that job");
			return ResponseEntity.ok("No candicate found for that job");
		}
		return ResponseEntity.ok(hrService.getCandidateProfileForPerticularJob(joblistId));			
	}

	//=============================================================================================
	//Get Profile URL
	@GetMapping("/getProfileURLToDownloadById")
	public ResponseEntity<?> getProfileURLToDownloadById(@RequestBody Candidate candidate){
		String profileURL=hrService.getProfileURLToDownloadById(candidate);
		return ResponseEntity.ok(profileURL);		
	}
	
	//==================================================================================================
//	@PostMapping("/download")
//	public ResponseEntity<?> download(@RequestBody Candidate candidate){
//		Optional<Candidate> candid=candidateRepo.findBycandId(candidate.getCandId());
//		if(candid.isPresent()) {
//			Candidate candObj=candid.get();
//			String URL=candObj.getFileUrl();
//			System.out.println("URL is: "+URL);
//			RestTemplate restTemplate = new RestTemplate();
//		    String result = restTemplate.getForObject(URL, String.class);
//		    System.out.println("Result is: "+result);
//			fileDownloadController.downloadFile(result, request);
//		}
//		return ResponseEntity.ok("Downloaded Successfully");
//	}

	//===================================================================================================
	@PostMapping("/AcceptorRejectProfile")
	public ResponseEntity<?> AcceptorRejectProfile(@RequestBody Candidate candidate){
		AcceptorRejectProfileResponse accOrRejProResponse=hrService.AcceptorRejectProfile(candidate);
		return ResponseEntity.ok(accOrRejProResponse);		
	}
	
	//===================================================================================================
	@PostMapping("/interviewFeedback")
	public ResponseEntity<?> interviewFeedback(@RequestBody Candidate candidate){
		InterviewFeedback interviewFeedback=hrService.interviewFeedback(candidate);
		return ResponseEntity.ok(interviewFeedback);
	}

	//===================================================================================================
	@GetMapping("/scheduleInterview")
	public ResponseEntity<?> scheduleInterview(@RequestBody Candidate candidate){
		//Long objId=candidate.getCandid();
		Candidate candObj=hrService.scheduleInterview(candidate);
		return ResponseEntity.ok(candObj);		
	}

	//===================================================================================================
	@PostMapping("/sendEmail")
	public ResponseEntity<?> sendEmail(@RequestBody Candidate candidate) throws MessagingException{
		SendEmailResponse sendEmailRes=hrService.sendEmail(candidate);
		return ResponseEntity.ok(sendEmailRes);		
	}

	//===================================================================================================
	@GetMapping("/viewAllScheduledInterviews")
	public ResponseEntity<?> viewAllScheduledInterviews(){
		List<Candidate> listObj=hrService.viewAllScheduledInterviews();
		return ResponseEntity.ok(listObj);		
	}
}
