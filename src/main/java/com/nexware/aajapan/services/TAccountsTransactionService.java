package com.nexware.aajapan.services;

import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TDayBook;

public interface TAccountsTransactionService {

	void accountTransactionEntry(TAccountsTransaction accountsTransaction);

	boolean isClearingBalance(TAccountsTransaction accountsTransaction);

}
