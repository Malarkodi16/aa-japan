package com.nexware.aajapan.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.repositories.MCurrencyRepository;
import com.nexware.aajapan.services.TCustomerService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TCustomerServiceImpl implements TCustomerService {

	@Autowired
	private MCurrencyRepository currencyRepository;

	@Override
	public boolean checkReserveAmountIsValid(TCustomer customer, Double amount) {

		if (customer.getCheckCreditLimit() == Constants.NOT_CHECK_CREDIT_LIMIT) {
			return true;
		}
		Double exchangeRate = 1.0;
		if (customer.getCurrencyType() != Constants.CURRENCY_YEN) {
			MCurrency currency = this.currencyRepository.findOneByCurrencySeq(customer.getCurrencyType());
			exchangeRate = currency.getExchangeRate();
		}
		Double creditLimit = AppUtil.ifNull(customer.getCreditBalance(), 0.0);
		Double balance = AppUtil.ifNull(customer.getBalance(), 0.0);
		if (balance == 0.0) {
			balance = customer.getCreditBalance();
		}
		Double amountAfterTransaction = balance - (amount * exchangeRate);
		return (creditLimit * -1) <= amountAfterTransaction;
	}

}
