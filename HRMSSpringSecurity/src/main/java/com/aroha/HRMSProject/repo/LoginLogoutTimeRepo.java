package com.aroha.HRMSProject.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aroha.HRMSProject.model.LoginLogoutTime;

@Repository
public interface LoginLogoutTimeRepo extends JpaRepository<LoginLogoutTime, Integer>{

}
