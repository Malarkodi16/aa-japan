package com.nexware.aajapan.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.nexware.aajapan.models.Model;

public interface MVechicleMakerService {
	
	Model getModelData(String maker, String modelId);
	
	void uploadExcelFile(MultipartFile file) throws IOException;

}
