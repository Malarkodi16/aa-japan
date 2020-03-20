package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.CustomerTransactionInMonthDto;

public interface TCustomerTransactionRepositoryCustom {
	Double getAmountReceivedForStockByCustomer(String customerId, String stockNo);

	Double getBroughtForwardAmountForCustomer(String customerId, Date date);

	Double getpaymentAmountForCustomer(String customerId, Date date);

	List<CustomerTransactionInMonthDto> getMonthlyTransaction(String customerId, Date date);
}
