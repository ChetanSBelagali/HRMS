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

	@PostMapping("/createNewFileUploader")
	public ResponseEntity<?> createNewFileUploader(@RequestParam("model") String model, @RequestPart(name="file") MultipartFile file){
		ObjectMapper mapper=new ObjectMapper();
		try {
			AddCandidateRequest addCandReq=mapper.readValue(model, AddCandidateRequest.class);
			long jobListId=addCandReq.getJoblistid();
			Candidate addCandObj=addCandReq.getAddCandidate();
			byte[] data=file.getBytes();
			int index=file.getOriginalFilename().indexOf(".");
			String fileName=file.getOriginalFilename().substring(0,index);
			OutputStream output=new FileOutputStream(new File(filePath+"/"+addCandObj.getCandemail()+"-"+fileName));
			output.write(data);
			Path path=Paths.get(filePath+"/"+addCandObj.getCandemail()+"-"+fileName);
			addCandObj.setFileurl(path.toString());
			addCandObj.setCreatedat(Calendar.getInstance().getTime().toInstant());
			addCandReq.setStatus(mentorService.createNewFileUploader(jobListId, addCandObj));
			System.out.println("jhfhjfhjfhjhjh is: "+addCandReq.getAddCandidate().getJoblisting());
			return ResponseEntity.ok(addCandReq);	
		}
		catch(Exception e) {
			return ResponseEntity.ok(e.getMessage());
		}
	}
	
	@GetMapping("/getFileUpCandById")
	public ResponseEntity<?> getFileUploaderCandById(@RequestBody Candidate candidate){
		System.out.println("Id is: "+candidate.getCandid());
		Candidate getFileUpDetailsObj=mentorService.getFileUploaderCandById(candidate.getCandid());
		return ResponseEntity.ok(getFileUpDetailsObj);		
	}

	@GetMapping("/getAllFileUploaders")
	public ResponseEntity<?> getAllFileUploaders(){
		return ResponseEntity.ok(mentorService.getAllFileUploaders());	
	}
}
