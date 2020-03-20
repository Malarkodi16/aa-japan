package com.nexware.aajapan.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.models.MGeneralLedger;
import com.nexware.aajapan.repositories.MGeneralLedgerRepository;
import com.nexware.aajapan.services.MGeneralLedgerService;

@Service
@Transactional
public class MGeneralLedgerServiceImpl implements MGeneralLedgerService {
	@Autowired
	private MGeneralLedgerRepository generalLedgerRepository;

	@Override
	public String getSequenceCodeByLedgerId(int id) {
		MGeneralLedger generalLedger = this.generalLedgerRepository.getNextSequenceById(id);
		return generalLedger.getPrefix() + generalLedger.getSequence();
	}
}
