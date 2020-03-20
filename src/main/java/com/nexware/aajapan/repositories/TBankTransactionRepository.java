package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TBankTransaction;
import com.nexware.aajapan.repositories.custom.TBankTransactionRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TBankTransactionRepository
		extends MongoRepository<TBankTransaction, String>, TBankTransactionRepositoryCustom {
	TBankTransaction findOneByPaymentVoucherNo(String paymentVoucherNo);
}
