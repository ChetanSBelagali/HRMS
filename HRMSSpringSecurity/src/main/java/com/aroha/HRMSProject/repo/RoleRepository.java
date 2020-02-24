package com.aroha.HRMSProject.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.aroha.HRMSProject.model.Role;
import com.aroha.HRMSProject.model.RoleName;
import com.aroha.HRMSProject.model.User;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Boolean existsByroleName(String roleName);

	Optional<Role> findByRoleName(RoleName roleName);
	
}
