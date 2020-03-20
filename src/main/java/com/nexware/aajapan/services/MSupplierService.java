package com.nexware.aajapan.services;

import java.util.List;

import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.TSupplierTransaction;

public interface MSupplierService {
	void supplierTransactionEntry(TSupplierTransaction supplierTransaction);

	void createSupplier(MSupplier supplier);

	void editSupplier(String supplierCode, MSupplier supplier);

	void supplierTransactionEntries(List<TSupplierTransaction> supplierTransactions);

	Double modifyAndGetCurrentBalance(String supplierCode, Double amount, int transactionType);
}
