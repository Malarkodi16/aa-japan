package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TInsurance;

@Repository
@JaversSpringDataAuditable
public interface TInsuranceRepository extends MongoRepository<TInsurance, String> {

	TInsurance findOneByChassisNo(String chassisNo);

}
