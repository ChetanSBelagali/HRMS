package com.aroha.HRMSProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aroha.HRMSProject.payload.CreateCandidateRequest;
import com.aroha.HRMSProject.payload.FileUploadResponse;
import com.aroha.HRMSProject.service.FileStorageService;

@RestController
public class FileUploadController {
	
	@Autowired
	FileStorageService fileStorageService;

	public FileUploadResponse uploadFile(MultipartFile file, CreateCandidateRequest createCandReq) {
		String fileName = fileStorageService.storeFile(file, createCandReq);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/downloadFile/")
				.path(fileName)
				.toUriString();

		return new FileUploadResponse(fileName, fileDownloadUri,
				file.getContentType(), file.getSize());
	}

}
