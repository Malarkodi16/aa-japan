package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.nexware.aajapan.models.MSpecialExchangeRate;
import com.nexware.aajapan.models.TExchangeRate;

@Repository
@JaversSpringDataAuditable
public interface MasterSpecialExchangeRateRepository extends MongoRepository<MSpecialExchangeRate, String>{
	MSpecialExchangeRate findOneById(String userid);
	
	
	MSpecialExchangeRate findOneByOrderByCreatedDateDesc();

}
