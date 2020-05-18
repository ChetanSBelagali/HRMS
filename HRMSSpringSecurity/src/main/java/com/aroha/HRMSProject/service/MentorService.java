package com.aroha.HRMSProject.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aroha.HRMSProject.exception.RecordNotFoundException;
import com.aroha.HRMSProject.exception.ResourceNotFoundException;
import com.aroha.HRMSProject.model.Candidate;
import com.aroha.HRMSProject.model.JobListing;
import com.aroha.HRMSProject.payload.CreateCandidateRequest;
import com.aroha.HRMSProject.payload.CreateCandidateResponse;
import com.aroha.HRMSProject.payload.DeleteCandidateResponse;
import com.aroha.HRMSProject.payload.UpdateCandidateRequest;
import com.aroha.HRMSProject.payload.UpdateCandidateResponse;
import com.aroha.HRMSProject.repo.CandidateRepository;

@Service
public class MentorService {

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	HRService hrService;


	public CreateCandidateResponse createCandidate(CreateCandidateRequest createCandReq) {
		// TODO Auto-generated method stub
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateTime = formatter.format(date);

		
		long jobListId=0;
		Set<JobListing> set=createCandReq.getJoblisting();
		Iterator<JobListing> itr=set.iterator();
		while(itr.hasNext()) {
			JobListing jobObj=itr.next();
			jobListId=jobObj.getJoblistId();
		}
		Optional<JobListing> jobListObj=hrService.getJobListByJobListId(jobListId);
		CreateCandidateResponse createCandRes=new CreateCandidateResponse();
		boolean status=false;
		Candidate candidate=new Candidate();
		if(jobListObj.isPresent()) {
			JobListing joblistObj=jobListObj.get();
			candidate.getJoblisting().add(joblistObj);
			candidate.setCandName(createCandReq.getCandName());
			candidate.setCandEmail(createCandReq.getCandEmail());
			candidate.setMobNumber(createCandReq.getMobNumber());
			candidate.setCreatedAt(createCandReq.getCreatedAt());
			candidate.setFileUrl(createCandReq.getFileUrl());
			candidate.setCreatedAt(dateTime);
			candidateRepository.save(candidate);
			status=true;
			createCandRes.setStatus(status);
			createCandRes.setResult("Candidate Profile Submitted Successfully");
			createCandRes.setData(candidate);
			return createCandRes;
		}
		status=false;
		createCandRes.setStatus(status);
		createCandRes.setResult("Specified Job List is not Present");
		return createCandRes;
	}


	public List<Candidate> getAllCandidates(){
		return candidateRepository.findAll();
	}

	public Candidate getCandidateById(long candid) {
		Optional<Candidate> fileUpObj=candidateRepository.findBycandId(candid);
		if(fileUpObj.isPresent()) {
			return fileUpObj.get();
		}

		throw new RecordNotFoundException("Not Found");
	}


	public DeleteCandidateResponse deleteCandidate(long id) {
		// TODO Auto-generated method stub
		Optional<Candidate> candId=candidateRepository.findBycandId(id);
		DeleteCandidateResponse deleteCandRes=new DeleteCandidateResponse();
		boolean status=false;
		if(candId.isPresent()) {
			candidateRepository.deleteById(id);
			status=true;
			deleteCandRes.setStatus(status);
			deleteCandRes.setResult("Candidate Profile Deleted Successfully");
			return deleteCandRes;
		}
		else {
			status=false;
			deleteCandRes.setStatus(status);
			deleteCandRes.setResult("Candidate Id is not Present");
			return deleteCandRes;
		}
	}


	public UpdateCandidateResponse updateCandidate(UpdateCandidateRequest updateCandReq) {
		// TODO Auto-generated method stub
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateTime = formatter.format(date);
		System.out.println("Current Time is: "+dateTime);
		
		UpdateCandidateResponse updateCandRes=new UpdateCandidateResponse();
		boolean status=true;

		Optional<Candidate> candId=candidateRepository.findBycandId(updateCandReq.getCandId());
		if(candId.isPresent()) {
			long joblistid=0;
			Set<JobListing> set=updateCandReq.getJoblisting();
			Iterator<JobListing> itr=set.iterator();
			while(itr.hasNext()) {
				JobListing jobObj=itr.next();
				joblistid=jobObj.getJoblistId();
			}
			Optional<JobListing> jobListObj=hrService.getJobListByJobListId(joblistid);
			if(jobListObj.isPresent()) {
				Candidate candObj=candId.get();
				candObj.setCandName(updateCandReq.getCandName());
				candObj.setCandEmail(updateCandReq.getCandEmail());
				candObj.setMobNumber(updateCandReq.getMobNumber());
				candObj.setJoblisting(updateCandReq.getJoblisting());
				candObj.setFileUrl(updateCandReq.getFileUrl());
				candObj.setCreatedAt(dateTime);
				//candObj.setCreatedAt(createdAt);
				candidateRepository.save(candObj);
				updateCandRes.setStatus(status);
				updateCandRes.setResult("Candidate Details Updated Successfully");
				updateCandRes.setData(candObj);
				return updateCandRes;
			}
			else {
				status=false;
				updateCandRes.setStatus(status);
				updateCandRes.setResult("Specified Job List is not Present");
				return updateCandRes;
			}
		}
		else {
			status=false;
			updateCandRes.setStatus(status);
			updateCandRes.setResult("Candidate Id is not Present");
			return updateCandRes;
		}
	}

}
