package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TBlTransaction;
import com.nexware.aajapan.repositories.custom.TBlTransactionRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TBlTransactionRepository
		extends MongoRepository<TBlTransaction, String>, TBlTransactionRepositoryCustom {
	
	TBlTransaction findOneByShippingInstructionId(String shippingInstructionId);

}
