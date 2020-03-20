package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MShippingCompany;
import com.nexware.aajapan.repositories.custom.MShippingCompanyRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MShippingCompanyRepository
		extends MongoRepository<MShippingCompany, String>, MShippingCompanyRepositoryCustom {

	MShippingCompany findOneByNameAndShipCompAddrAndShipCompMailAndMobileNo(String name, String shipCompAddr,
			String shipCompMail, String mobileNo);

	MShippingCompany findOneByShippingCompanyNo(String shippingCompanyNo);

	List<MShippingCompany> findAllByDeleteFlag(Integer flag);

}
