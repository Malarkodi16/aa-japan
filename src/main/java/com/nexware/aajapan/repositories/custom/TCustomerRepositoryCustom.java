package com.nexware.aajapan.repositories.custom;

import java.util.List;

import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.models.TCustomer;

public interface TCustomerRepositoryCustom {
	List<TCustomer> findBySearchTermsAndSalesPerson(String search, Integer flag, List<String> createdBy);

	List<TCustomer> findByAdminSearchTermsAndSalesPerson(String search, List<String> createdBy);

	List<TCustomer> findBySearchTermsAndSalesPersonName(String search, String salesName);

	List<TCustomer> findBySearchTerms(String search);

	List<TCustomer> findBySearchTermsAndFlag(String search, Integer flag);

	List<TCustomer> findLcCustomerBySearchTerms(String search);

	UpdateResult updateById(String id, Update update);

	// boolean isExistsByMobileNoAndCity(String mobileNo, String city);

	TCustomer updateBalance(String customerId, Integer transactionType, Double amount);

	TCustomer updateDepositeAmount(String customerId, Integer transactionType, Double amount);
	
	TCustomer updateAdvanceAmount(String customerId, Integer transactionType, Double amount);
}
