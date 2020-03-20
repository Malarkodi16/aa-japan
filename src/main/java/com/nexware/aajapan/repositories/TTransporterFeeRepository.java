package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TTransporterFee;
import com.nexware.aajapan.repositories.custom.TTransporterFeeRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TTransporterFeeRepository
		extends MongoRepository<TTransporterFee, String>, TTransporterFeeRepositoryCustom {
	TTransporterFee findOneById(String id);

}
