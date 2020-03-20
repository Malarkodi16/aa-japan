package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.nexware.aajapan.dto.GlReportDetails;
import com.nexware.aajapan.models.MGeneralLedger;

public interface MGeneralLedgerRepositoryCustom {
	MGeneralLedger getNextSequenceById(int id);

	List<GlReportDetails> getTransactions();

	List<GlReportDetails> getTransactions(Date from, Date to);
}
