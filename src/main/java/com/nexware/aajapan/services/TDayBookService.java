package com.nexware.aajapan.services;

import java.util.List;

import com.nexware.aajapan.models.TDayBook;
import com.nexware.aajapan.models.TDayBookTransaction;

public interface TDayBookService {

	void dayBookPaymentApproveTransactions(List<TDayBook> tDayBook);

	void dayBookPaymentBankTransactions(Long coaCode, List<TDayBook> tDayBook);

	void dayBookPaymentBankTransactions(Long coaCode, TDayBook tDayBook);

	void approveDaybookEntry(String id);

	void dayBookApproveTTOwned(String id);

	long getClearingAccountCoaCode(TDayBook tDayBook);

	void createDayBookEntry(List<TDayBook> dayBookentry);

	void createDayBookTransaction(TDayBookTransaction transaction);
	
	void deleteDayBookTransaction(TDayBookTransaction transaction);

	void accountTransactionEntry(TDayBookTransaction transaction);

	void dayBookApproveReAllocate(String id, Integer type, String invoiceNo, String stockNo, Double soExchRate,
			Double ownedAmt);

}
