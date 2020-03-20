package com.nexware.aajapan.services;

import java.util.List;

import org.bson.Document;

public interface TLcService {
	List<Document> getBillofExchangeDetailsByStatus();
}
