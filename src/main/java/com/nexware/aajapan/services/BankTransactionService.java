package com.nexware.aajapan.services;

import com.nexware.aajapan.models.MBank;

public interface BankTransactionService {

	MBank modifyAndGetCurrentBalanceOfBank(String bankId, Double amount, int currencyType, int transactionType);

	Double modifyAndGetClearingBalanceOfBank(String bankId, Double amount, int currencyType, int transactionType);

	String bankTransactionEntry(String bankId, Double amount, Integer currency, Integer transactionType,
			String description, String refNo);

}
