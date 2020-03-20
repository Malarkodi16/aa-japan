package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.repositories.custom.TCustomerRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TCustomerRepository extends MongoRepository<TCustomer, String>, TCustomerRepositoryCustom {
	TCustomer findOneByid(String id);

	List<TCustomer> findByMobileNoAndCity(String mobileNo, String city);

	List<TCustomer> findAllBySalesPersonIn(List<String> salesPersonIds);

	List<TCustomer> findAllByCreatedBy(String createdBy);

	List<TCustomer> findAllByApproveCustomerflag(Integer approveCustomerflag);

	@Override
	List<TCustomer> findAll();

	TCustomer findOneByCode(String code);

	boolean existsByIdAndEmail(String id, String email);

	boolean existsByEmail(String email);

}
