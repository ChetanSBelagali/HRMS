package com.aroha.HRMSProject.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aroha.HRMSProject.model.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>{

	Optional<Candidate> findBycandId(long candId);


//	List<Candidate> findAllByjoblistid(long joblistId);/
	
	@Query(value="select * from candidate1 where joblistId=?1",nativeQuery = true)
	List<Long> getByJobListId(long joblistId);
	
	@Query(value="select * from candidate where set_Status='Accept'", nativeQuery=true)
	List<Candidate> getAllScheduledInterviews();


	Boolean existsBycandId(long candId);
	
	Boolean existsBycandEmail(String candEmail);


}
