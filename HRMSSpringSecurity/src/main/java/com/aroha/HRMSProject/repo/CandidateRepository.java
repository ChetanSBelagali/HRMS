package com.aroha.HRMSProject.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aroha.HRMSProject.model.Candidate;
import com.aroha.HRMSProject.payload.AddCandidateRequest;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>{

	Optional<Candidate> findBycandid(long candid);


//	List<Candidate> findAllByjoblistid(long joblistId);/
	
	@Query(value="select * from candidate1 where joblistid=?1",nativeQuery = true)
	List<Long> getByJobListId(long joblistId);


	Boolean existsBycandid(long candid);


}
