package com.nexware.aajapan.services;

import java.util.List;

import com.nexware.aajapan.dto.CustomerEmailDto;

public interface CustomerService {
	List<CustomerEmailDto> getCustomerEmailList();

	List<CustomerEmailDto> getCustomerEmailListBySearchTerm(String search);
}
