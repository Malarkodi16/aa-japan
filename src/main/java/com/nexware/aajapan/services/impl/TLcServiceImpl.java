package com.nexware.aajapan.services.impl;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.repositories.TLcDetailsRepository;
import com.nexware.aajapan.services.TLcService;

@Service
@Transactional
public class TLcServiceImpl implements TLcService {
	@Autowired
	TLcDetailsRepository lcDetailsRepository;

	@Override
	public List<Document> getBillofExchangeDetailsByStatus() {
		return this.lcDetailsRepository.updateFields();
	}

}