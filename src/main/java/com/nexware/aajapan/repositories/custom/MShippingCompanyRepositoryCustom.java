package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.MAuctionGradesExterior;
import com.nexware.aajapan.models.MShippingCompany;

public interface MShippingCompanyRepositoryCustom {
	
	boolean existByNameAndShippingCompanyNo(String name, String shippingCompanyNo);
	
	boolean existsByName(String name);

	List<MShippingCompany> getAllUnDeletedShippingCompany();
}
