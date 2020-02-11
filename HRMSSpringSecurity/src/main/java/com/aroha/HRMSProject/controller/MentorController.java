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
import com.aroha.HRMSProject.payload.AddCandidateRequest;
import com.aroha.HRMSProject.payload.AddCandidateResponse;
import com.aroha.HRMSProject.security.CurrentUser;
import com.aroha.HRMSProject.security.UserPrincipal;
import com.aroha.HRMSProject.service.MentorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class MentorController {

	@Autowired
	MentorService mentorService;

	@Value("${app.path}")
	private String filePath;

	@Autowired
	Candidate candidate;

	//Create New Profile Uploader or Create New Candidate
	@PostMapping("/createNewFileUploader")
	public ResponseEntity<?> createNewFileUploader(@RequestParam("model") String model, @RequestPart(name="file") MultipartFile file){
		ObjectMapper mapper=new ObjectMapper();
		try {
			AddCandidateRequest addCandReq=mapper.readValue(model, AddCandidateRequest.class);
			long jobListId=addCandReq.getJoblistId();
			Candidate addCandObj=addCandReq.getAddCandidate();
			byte[] data=file.getBytes();
			int index=file.getOriginalFilename().indexOf(".");
			String fileName=file.getOriginalFilename().substring(0,index);
			OutputStream output=new FileOutputStream(new File(filePath+"/"+addCandObj.getCandEmail()+"-"+fileName));
			output.write(data);
			Path path=Paths.get(filePath+"/"+addCandObj.getCandEmail()+"-"+fileName);
			addCandObj.setFileUrl(path.toString());
			String dateTime=Calendar.getInstance().getTime().toString().replaceAll("Z", " ");
			addCandObj.setCreatedAt(dateTime);
			//addCandReq.setStatus(mentorService.createNewFileUploader(jobListId, addCandObj));
			AddCandidateResponse addCandResponse=mentorService.createNewFileUploader(jobListId, addCandObj);
			return ResponseEntity.ok(addCandResponse);	
		}
		catch(Exception e) {
			return ResponseEntity.ok(e.getMessage());
		}
	}

	//Get FileUploader By ID
	@GetMapping("/getFileUpCandById")
	public ResponseEntity<?> getFileUploaderCandById(@RequestBody Candidate candidate){
		System.out.println("Id is: "+candidate.getCandId());
		Candidate getFileUpDetailsObj=mentorService.getFileUploaderCandById(candidate.getCandId());
		return ResponseEntity.ok(getFileUpDetailsObj);		
	}

	//Get All File Uploaders
	@GetMapping("/getAllFileUploaders")
	public ResponseEntity<?> getAllFileUploaders(){
		return ResponseEntity.ok(mentorService.getAllFileUploaders());	
	}
}
