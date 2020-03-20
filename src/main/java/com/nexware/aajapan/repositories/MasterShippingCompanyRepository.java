package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MShippingCompany;
import com.nexware.aajapan.repositories.custom.MShippingCompanyRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MasterShippingCompanyRepository
		extends MongoRepository<MShippingCompany, String>, MShippingCompanyRepositoryCustom {
	MShippingCompany findOneByshippingCompanyNo(String shippingCompanyNo);

	MShippingCompany findOneByNameAndShipCompAddrAndShipCompMailAndMobileNo(String name, String shipCompAddr,
			String shipCompMail, String mobileNo);

	List<MShippingCompany> findAllByDeleteFlag(Integer flag);
}
