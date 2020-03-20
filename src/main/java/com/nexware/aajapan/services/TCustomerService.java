package com.nexware.aajapan.services;

import com.nexware.aajapan.models.TCustomer;

public interface TCustomerService {
	boolean checkReserveAmountIsValid(TCustomer customer, Double amount);

}
