package com.aroha.HRMSProject.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aroha.HRMSProject.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByuserEmail(String userEmail);
	
	Optional<User> findByuserId(Long userId);
	
	Boolean existsByuserEmail(String userEmail);

}
