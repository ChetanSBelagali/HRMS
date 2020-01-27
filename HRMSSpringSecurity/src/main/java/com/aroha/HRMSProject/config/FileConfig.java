package com.aroha.HRMSProject.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.aroha.HRMSProject.model.Candidate;

@Configuration
public class FileConfig {
	
	@Bean
	public Candidate getFile() {
		return new Candidate();
	}

}
