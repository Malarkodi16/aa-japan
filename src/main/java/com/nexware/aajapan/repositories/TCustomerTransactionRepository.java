package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TCustomerTransaction;
import com.nexware.aajapan.repositories.custom.TCustomerTransactionRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TCustomerTransactionRepository
		extends MongoRepository<TCustomerTransaction, String>, TCustomerTransactionRepositoryCustom {

}
