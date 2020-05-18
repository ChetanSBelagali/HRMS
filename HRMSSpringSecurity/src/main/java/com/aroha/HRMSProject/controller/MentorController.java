  
package com.aroha.HRMSProject.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.aroha.HRMSProject.payload.UpdateCandidateRequest;
import com.aroha.HRMSProject.payload.UpdateCandidateResponse;
import com.aroha.HRMSProject.security.CurrentUser;
import com.aroha.HRMSProject.security.UserPrincipal;
import com.aroha.HRMSProject.service.MentorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class MentorController {
	
	private final Logger logger = LoggerFactory.getLogger(MentorController.class);

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
			//String dateTime=Calendar.getInstance().getTime().toString().replaceAll("Z", " ");
			//addCandReq.setStatus(mentorService.createNewFileUploader(jobListId, addCandObj));
			createCandResponse=mentorService.createCandidate(createCandReq);
			return ResponseEntity.ok(createCandResponse);
		}
		catch(Exception e) {
			return ResponseEntity.ok(e.getMessage());
		}		
	}
	
	@PostMapping("/updateCandidate")
	public ResponseEntity<?> updateCandidate(@RequestParam("updateModel") String updateModel, @RequestPart(name="updateFile") MultipartFile updateFile){
		ObjectMapper mapper=new ObjectMapper();
		UpdateCandidateRequest updateCandReq=new UpdateCandidateRequest();
		UpdateCandidateResponse updateCandResponse=new UpdateCandidateResponse();
		try {
			updateCandReq=mapper.readValue(updateModel, UpdateCandidateRequest.class);
			FileUploadResponse fileUploadResponse=fileUploadController.updateUploadFile(updateFile, updateCandReq);
			updateCandReq.setFileUrl(fileUploadResponse.getFileDownloadUri());
			//String dateTime=Calendar.getInstance().getTime().toString().replaceAll("Z", " ");
			updateCandResponse=mentorService.updateCandidate(updateCandReq);
			return ResponseEntity.ok(updateCandResponse);
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

	@DeleteMapping("/deleteCandidate/{id}")
	public ResponseEntity<?> deleteCandidate(@PathVariable long id){
		DeleteCandidateResponse deleteCandR=mentorService.deleteCandidate(id);
		return ResponseEntity.ok(deleteCandR);		
	}
}
