package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MPaymentCategory;

@Repository
@JaversSpringDataAuditable
public interface MasterPaymentRepository extends MongoRepository<MPaymentCategory, String> {
	MPaymentCategory findOneByCategoryCode(String categoryCode);
}
