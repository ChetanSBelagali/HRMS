package com.aroha.HRMSProject.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aroha.HRMSProject.model.Candidate;
import com.aroha.HRMSProject.payload.CreateCandidateRequest;
import com.aroha.HRMSProject.payload.CreateCandidateResponse;
import com.aroha.HRMSProject.payload.DeleteCandidateResponse;
import com.aroha.HRMSProject.payload.FileUploadResponse;
import com.aroha.HRMSProject.security.CurrentUser;
import com.aroha.HRMSProject.security.UserPrincipal;
import com.aroha.HRMSProject.service.MentorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class MentorController {

	@Autowired
	MentorService mentorService;
	
	@Autowired
	FileUploadController fileUploadController;

	@Value("${app.path}")
	private String filePath;


	@Autowired
	Candidate candidate;

	@PostMapping("/createCandidate")
	public ResponseEntity<?> createCandidate(@RequestParam("model") String model, @RequestPart(name="file") MultipartFile file){
		ObjectMapper mapper=new ObjectMapper();
		CreateCandidateRequest createCandReq=null;
		CreateCandidateResponse createCandResponse=null;
		try {
			createCandReq=mapper.readValue(model, CreateCandidateRequest.class);
			FileUploadResponse fileUploadResponse=fileUploadController.uploadFile(file, createCandReq);
			createCandReq.setFileUrl(fileUploadResponse.getFileDownloadUri());
			String dateTime=Calendar.getInstance().getTime().toString().replaceAll("Z", " ");
			createCandReq.setCreatedAt(dateTime);
			//addCandReq.setStatus(mentorService.createNewFileUploader(jobListId, addCandObj));
			createCandResponse=mentorService.createCandidate(createCandReq);
			return ResponseEntity.ok(createCandResponse);
		}
		catch(Exception e) {
			return ResponseEntity.ok(e.getMessage());
		}		
	}

	//Get FileUploader By ID
	@GetMapping("/getCandidateById")
	public ResponseEntity<?> getCandidateById(@RequestBody Candidate candidate){
		System.out.println("Id is: "+candidate.getCandId());
		Candidate getFileUpDetailsObj=mentorService.getCandidateById(candidate.getCandId());
		return ResponseEntity.ok(getFileUpDetailsObj);		
	}

	//Get All File Uploaders
	@GetMapping("/getAllCandidates")
	public ResponseEntity<?> getAllCandidates(){
		return ResponseEntity.ok(mentorService.getAllCandidates());	
	}

	@PostMapping("/deleteCandidate")
	public ResponseEntity<?> deleteCandidate(@RequestBody Candidate candidate){
		DeleteCandidateResponse deleteCandR=mentorService.deleteCandidate(candidate);
		return ResponseEntity.ok(deleteCandR);		
	}
}
