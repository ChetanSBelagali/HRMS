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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aroha.HRMSProject.config.FileConfig;
import com.aroha.HRMSProject.model.Candidate;
import com.aroha.HRMSProject.model.JobListing;
import com.aroha.HRMSProject.payload.AcceptorRejectProfileResponse;
import com.aroha.HRMSProject.payload.CreateJobListResponse;
import com.aroha.HRMSProject.payload.DeleteJobListResponse;
import com.aroha.HRMSProject.payload.SendEmailResponse;
import com.aroha.HRMSProject.payload.UpdateJobListResponse;
import com.aroha.HRMSProject.service.HRService;

@RestController
@RequestMapping("/api")
public class HRController {

	@Autowired
	HRService hrService;

	//Create Job List
	@PostMapping("/CreateJobList")
	public ResponseEntity<?> createJobListing(@RequestBody JobListing jobListing){
		CreateJobListResponse createJoblistRes=hrService.createJobListing(jobListing);
		return ResponseEntity.ok(createJoblistRes);	
	}

	//Get Job List By id
	@GetMapping("/getJobListById")
	public ResponseEntity<?> getJobListById(@RequestBody JobListing jobListing){
		JobListing getJobListObj=hrService.getJobListById(jobListing.getJoblistId());
		return ResponseEntity.ok(getJobListObj);	
	}

	//Update Job List
	@PostMapping("/UpdateJobList")
	public ResponseEntity<?> updateJobListing(@RequestBody JobListing jobListing){
		UpdateJobListResponse updateJobListRes=hrService.updateJobList(jobListing);
		return ResponseEntity.ok(updateJobListRes);		
	}

	//Get All Job Lists
	@GetMapping("/getAllJobList")
	public ResponseEntity<?> getAllJobListing(){
		List<JobListing> list=hrService.getAllJobListing();
		return ResponseEntity.ok(list);		
	}

	//Delete Job List By ID
	@PostMapping("/deleteJobListingById")
	public ResponseEntity<?> deleteJobListById(@RequestBody JobListing jobListing){
		DeleteJobListResponse deleteJobListRes=hrService.deleteJobListById(jobListing);
		return ResponseEntity.ok(deleteJobListRes);		

	}

	//Get Uploaded Profiles For Particular Job
	@GetMapping("/getCandidateProfileForPerticularJob")
	public ResponseEntity<?> getCandidateProfileForPerticularJob(@RequestBody JobListing joblisting){
		long joblistId=joblisting.getJoblistId();
		if(hrService.getCandidateProfileForPerticularJob(joblistId).isEmpty()) {
			return ResponseEntity.ok("No candicate found for that job");
		}
		return ResponseEntity.ok(hrService.getCandidateProfileForPerticularJob(joblistId));			
	}

	//Get Profile URL
	@GetMapping("/getProfileURLToDownloadById")
	public ResponseEntity<?> getProfileURLToDownloadById(@RequestBody Candidate candidate){
		String profileURL=hrService.getProfileURLToDownloadById(candidate);
		return ResponseEntity.ok(profileURL);		
	}

	@PostMapping("/fileDownload")
	public ResponseEntity<?> fileDownload(@RequestBody Candidate candidate, HttpServletRequest request, HttpServletResponse response) throws IOException{
		String profileURL=hrService.getProfileURLToDownloadById(candidate);
		//File newFile=new File(profileURL);
		Path file=Paths.get(profileURL);

		//Tika tika = new Tika();
		File newfile =new File(profileURL);
		System.out.println("new File is: "+newfile.getCanonicalPath());

		InputStream is = new BufferedInputStream(new FileInputStream(profileURL));
		if(Files.exists(file)) {
			//String mimeType = Files.probeContentType(file);
			//String mimeType = tika.detect(newfile);
			String mimeType = URLConnection.guessContentTypeFromStream(is);
			System.out.println("File is :"+file);
			System.out.println("file mimetype:"+mimeType);
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			response.setContentType(mimeType);
			//			response.addHeader("Content-Disposition", "attachment; filename="+file);
			response.addHeader("Content-Disposition", "attachment; filename="+profileURL);
			try
			{
				Files.copy(file, response.getOutputStream());
				response.getOutputStream().flush();
			} 
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return ResponseEntity.ok("Downloaded Successfully");
	}

	@PostMapping("/AcceptorRejectProfile")
	public ResponseEntity<?> AcceptorRejectProfile(@RequestBody Candidate candidate){
		AcceptorRejectProfileResponse accOrRejProResponse=hrService.AcceptorRejectProfile(candidate);
		return ResponseEntity.ok(accOrRejProResponse);		
	}

	@GetMapping("/scheduleInterview")
	public ResponseEntity<?> scheduleInterview(@RequestBody Candidate candidate){
		//Long objId=candidate.getCandid();
		Candidate candObj=hrService.scheduleInterview(candidate);
		return ResponseEntity.ok(candObj);		
	}

	@PostMapping("/sendEmail")
	public ResponseEntity<?> sendEmail(@RequestBody Candidate candidate){
		SendEmailResponse sendEmailRes=hrService.sendEmail(candidate);
		return ResponseEntity.ok(sendEmailRes);		
	}

	@GetMapping("/viewAllScheduledInterviews")
	public ResponseEntity<?> viewAllScheduledInterviews(){
		List<Candidate> listObj=hrService.viewAllScheduledInterviews();
		return ResponseEntity.ok(listObj);		
	}
}
