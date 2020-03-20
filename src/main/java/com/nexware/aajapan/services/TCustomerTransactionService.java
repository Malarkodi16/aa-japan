package com.nexware.aajapan.services;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TCustomerTransaction;

public interface TCustomerTransactionService {
	void customerTransactionEntry(TCustomerTransaction transaction);

	TCustomer updateCustomerBalance(String customerId, Integer transactionType, Double amount);
	
	TCustomer updateCustomerAdvanceAmount(String customerId, Integer transactionType, Double amount);

	TCustomer updateCustomerDepositAmount(String customerId, Integer transactionType, Double amount);

	Double getAmountReceivedForStockByCustomer(String customerId, String stockNo);

	void exportAllCustomerTransactions(String customerId, Date minDate, Date maxDate, HttpServletResponse response)
			throws IOException;
}
