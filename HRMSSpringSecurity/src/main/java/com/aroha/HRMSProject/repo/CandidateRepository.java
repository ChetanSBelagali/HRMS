package com.aroha.HRMSProject.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aroha.HRMSProject.model.Candidate;
import com.aroha.HRMSProject.payload.AddCandidateRequest;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>{

	Optional<Candidate> findBycandid(long candid);

}
