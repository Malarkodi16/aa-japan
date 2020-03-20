package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.SupplierLocation;

@Repository
@JaversSpringDataAuditable
public interface SupplierLocationRepository extends MongoRepository<SupplierLocation, String> {

	SupplierLocation findOneByid(String id);

	SupplierLocation findOneByCode(String code);

}
