package com.aroha.HRMSProject.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aroha.HRMSProject.model.JobListing;

public interface JobListingRepository extends JpaRepository<JobListing, Long>{
	
	Optional<JobListing> findByjoblistId(long joblistId);

	Optional<JobListing> deleteByJoblistId(long jobListId);

	Optional<JobListing> findByjobTitle(String jobTitle);
	
	
	@Query(value="select * from job_listing ORDER BY posted_date DESC",nativeQuery = true)
	List<JobListing> findAll();

}
