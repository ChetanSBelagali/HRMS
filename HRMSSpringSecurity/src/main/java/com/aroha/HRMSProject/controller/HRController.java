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
import com.aroha.HRMSProject.service.HRService;

@RestController
@RequestMapping("/api")
public class HRController {

	@Autowired
	HRService hrService;

	//Create Job List
	@PostMapping("/CreateJobList")
	public ResponseEntity<?> createJobListing(@RequestBody JobListing jobListing){
		JobListing jobList=hrService.createJobListing(jobListing);
		return ResponseEntity.ok("Job List is Added Successfully");	
	}

	//Get Job List By id
	@GetMapping("/getJobListById")
	public ResponseEntity<?> getJobListById(@RequestBody JobListing jobListing){
		JobListing getJobListObj=hrService.getJobListById(jobListing.getJoblistid());
		return ResponseEntity.ok(getJobListObj);	
	}

	//Update Job List
	@PostMapping("/UpdateJobList")
	public ResponseEntity<?> updateJobListing(@RequestBody JobListing jobListing){
		JobListing getUpdatedJobListObj=hrService.updateJobList(jobListing);
		return ResponseEntity.ok("Job Description Updated Successfully");		
	}

	//Get All Job Lists
	@GetMapping("/getAllJobList")
	public ResponseEntity<?> getAllJobListing(){
		List<JobListing> list=hrService.getAllJobListing();
		return ResponseEntity.ok(list);		
	}

	//Delete Job List By ID
	@DeleteMapping("/deleteJobListingById")
	public ResponseEntity<?> deleteJobListById(@RequestBody JobListing jobListing){
		String result=hrService.deleteJobListById(jobListing);
		return ResponseEntity.ok(result);		

	}

	//Get Uploaded Profiles For Particular Job
	@GetMapping("/getUploadedProfilesForPerticularJob")
	public ResponseEntity<?> getUploadedProfilesForPerticularJob(@RequestBody JobListing joblisting){
		long joblistId=joblisting.getJoblistid();
		if(hrService.getUploadedProfilesForPerticularJob(joblistId).isEmpty()) {
			return ResponseEntity.ok("No candicate found for that job");
		}
		return ResponseEntity.ok(hrService.getUploadedProfilesForPerticularJob(joblistId));			
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
	
	@PostMapping("/updateFileUploader")
	public ResponseEntity<?> updateFileUploader(@RequestBody Candidate candidate){
		Candidate candObj=hrService.updateFileUploader(candidate);
		return ResponseEntity.ok(candObj);		
	}
	
	@GetMapping("/scheduleInterview")
	public ResponseEntity<?> scheduleInterview(@RequestBody Candidate candidate){
		Long objId=candidate.getCandid();
		Candidate candObj=hrService.scheduleInterview(candidate);
		return ResponseEntity.ok(candObj);		
	}
	
	@PostMapping("/sendEmail")
	public ResponseEntity<?> sendEmail(@RequestParam("UserEmail") String userEmail){
		System.out.println("Email is: "+userEmail);
		hrService.sendEmail(userEmail);
		return ResponseEntity.ok("SUCCESS");		
	}
	
	@GetMapping("/viewAllScheduledInterviews")
	public ResponseEntity<?> viewAllScheduledInterviews(){
		List<Candidate> listObj=hrService.viewAllScheduledInterviews();
		return ResponseEntity.ok(listObj);		
	}
}
