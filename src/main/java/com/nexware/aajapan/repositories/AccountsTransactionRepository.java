package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.repositories.custom.TAccountsTransactionRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface AccountsTransactionRepository
		extends MongoRepository<TAccountsTransaction, String>, TAccountsTransactionRepositoryCustom {

}
