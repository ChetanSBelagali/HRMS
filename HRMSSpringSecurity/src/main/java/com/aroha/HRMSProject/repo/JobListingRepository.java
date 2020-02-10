package com.aroha.HRMSProject.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aroha.HRMSProject.model.JobListing;

public interface JobListingRepository extends JpaRepository<JobListing, Long>{
	
	Optional<JobListing> findByjoblistId(long joblistId);

	Optional<JobListing> deleteByJoblistId(long jobListId);

}
