package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MInvoiceType;

@Repository
@JaversSpringDataAuditable
public interface MInvoiceTypeRepository extends MongoRepository<MInvoiceType, String> {
	MInvoiceType findOneByid(String id);
}
