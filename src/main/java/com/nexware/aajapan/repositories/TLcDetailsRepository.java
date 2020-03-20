package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TLcDetails;
import com.nexware.aajapan.repositories.custom.TLcDetailsRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TLcDetailsRepository extends MongoRepository<TLcDetails, String>, TLcDetailsRepositoryCustom {
	TLcDetails findOneByBillOfExchangeNo(String billOfExchangeNo);

	TLcDetails findOneById(String id);

	TLcDetails findOneByLcInvoiceNo(String invoiceNo);
}
