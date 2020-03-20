package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.repositories.custom.MasterSupplierRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MasterSupplierRepository extends MongoRepository<MSupplier, String>, MasterSupplierRepositoryCustom {

	MSupplier findOneByid(String id);

	MSupplier findOneBySupplierCode(String supplierCode);

	MSupplier findOneByCompany(String company);

}
