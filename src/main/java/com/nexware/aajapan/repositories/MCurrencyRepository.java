package com.nexware.aajapan.repositories;

import java.util.Date;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MCurrency;

@Repository
@JaversSpringDataAuditable
public interface MCurrencyRepository extends MongoRepository<MCurrency, String> {
	boolean existsByLastModifiedDateBetween(Date start, Date end);

	MCurrency findOneByCurrencySeq(int currencyType);
}
