package com.aroha.HRMSProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aroha.HRMSProject.exception.RecordNotFoundException;
import com.aroha.HRMSProject.exception.ResourceNotFoundException;
import com.aroha.HRMSProject.model.Candidate;
import com.aroha.HRMSProject.model.JobListing;
import com.aroha.HRMSProject.payload.AddCandidateRequest;
import com.aroha.HRMSProject.payload.AddCandidateResponse;
import com.aroha.HRMSProject.repo.CandidateRepository;

@Service
public class MentorService {

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	HRService hrService;

	public AddCandidateResponse createNewFileUploader(long jobListId, Candidate addCandObj) {
		// TODO Auto-generated method stub
		Optional<JobListing> jobListObj=hrService.getJobListByJobListId(jobListId);
		AddCandidateResponse addCandResponse=new AddCandidateResponse();
		System.out.println("Job List id is: "+jobListId);
		if(jobListObj.isPresent()) {
			JobListing candObj=jobListObj.get();
			System.out.println("Cand Details: "+candObj.getJobDesc());
			addCandObj.getJoblisting().add(candObj);
			candidateRepository.save(addCandObj);
			addCandResponse.setResult("Candidate is Saved");
			addCandResponse.setStatus("Success");
			return addCandResponse;
		}else {
			addCandResponse.setResult("Job List is Not Present");
			addCandResponse.setStatus("Fail");
			return addCandResponse;
		}
	}

	public List<Candidate> getAllFileUploaders(){
		return candidateRepository.findAll();
	}

	public Candidate getFileUploaderCandById(long candid) {
		Optional<Candidate> fileUpObj=candidateRepository.findBycandId(candid);
		if(fileUpObj.isPresent()) {
			return fileUpObj.get();
		}

		throw new RecordNotFoundException("Not Found");
	}


}
