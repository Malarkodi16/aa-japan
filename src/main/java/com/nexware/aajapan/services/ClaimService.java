package com.nexware.aajapan.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.bulk.BulkWriteResult;

public interface ClaimService {
	BulkWriteResult applyRecycleClaimByExcel(MultipartFile file) throws IOException;

	BulkWriteResult receiveRecycleClaimByExcel(MultipartFile file) throws IOException;

	BulkWriteResult receiveRecycleClaim(List<Document> data) throws IOException;

	String applyInsuranceByExcel(MultipartFile file, Date insuranceApplyDate) throws IOException;
}
