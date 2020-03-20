package com.nexware.aajapan.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface HSCodeService {
	
	void uploadExcelFile(MultipartFile file) throws IOException;

}
