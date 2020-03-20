package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.nexware.aajapan.dto.TAccountsTransactionDto;

public interface TAccountsTransactionRepositoryCustom {

	List<TAccountsTransactionDto> getTrailBalanceAccountsTransactionList(Date fromDate, Date toDate, Long subAccount);

	List<TAccountsTransactionDto> findAllByCode(Long code, Date fromDate, Date toDate);

	List<Document> getTrailBalance(Date toDate);

}
