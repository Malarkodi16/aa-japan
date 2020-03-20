package com.nexware.aajapan.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.mongodb.bulk.BulkWriteResult;

public interface OpeningStockService {
	public int openingStockUpload(MultipartFile file) throws IOException;
}
