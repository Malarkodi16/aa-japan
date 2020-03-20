package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TSupplierTransaction;
import com.nexware.aajapan.repositories.custom.TSupplierTransactionRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TSupplierTransactionRepository
		extends MongoRepository<TSupplierTransaction, String>, TSupplierTransactionRepositoryCustom {

}
