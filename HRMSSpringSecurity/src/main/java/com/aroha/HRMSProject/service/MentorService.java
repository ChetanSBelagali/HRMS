package com.aroha.HRMSProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aroha.HRMSProject.model.Candidate;
import com.aroha.HRMSProject.model.JobListing;
import com.aroha.HRMSProject.payload.AddCandidateRequest;
import com.aroha.HRMSProject.repo.CandidateRepository;

@Service
public class MentorService {

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	HRService hrService;

	public String createNewFileUploader(long jobListId, Candidate addCandObj) {
		// TODO Auto-generated method stub
		Optional<JobListing> jobListObj=hrService.getJobListByJobListId(jobListId);
		System.out.println("Job List id is: "+jobListId);
		if(jobListObj.isPresent()) {
			JobListing candObj=jobListObj.get();
			System.out.println("Cand Details: "+candObj.getJobdesc());
			addCandObj.getJoblisting().add(candObj);
			candidateRepository.save(addCandObj);
			return "Candidate is saved";
		}
		return "Job List is not there";
	}

	public List<Candidate> getAllFileUploaders(){
		return candidateRepository.findAll();
	}

	public Candidate getFileUploaderCandById(long candid) {
		Optional<Candidate> fileUpObj=candidateRepository.findBycandid(candid);
		if(fileUpObj.isPresent()) {
			return fileUpObj.get();
		}

		return null;
	}


}
