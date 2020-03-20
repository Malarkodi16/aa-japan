package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MGeneralSupplier;
import com.nexware.aajapan.repositories.custom.MGeneralSupplierRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MGeneralSupplierRepository
		extends MongoRepository<MGeneralSupplier, String>, MGeneralSupplierRepositoryCustom {

	MGeneralSupplier findOneByid(String id);

	MGeneralSupplier findOneByCode(String code);

}
