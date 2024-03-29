package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TDayBookTransaction;
import com.nexware.aajapan.repositories.custom.TDayBookTransactionCustom;

@Repository
@JaversSpringDataAuditable
public interface TDayBookTransactionRepository
		extends MongoRepository<TDayBookTransaction, String>, TDayBookTransactionCustom {

	TDayBookTransaction findOneById(String id);
	
	void deleteById(String id);

}
