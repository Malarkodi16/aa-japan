package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TCancelledInvoices;
import com.nexware.aajapan.repositories.custom.TCancelledInvoicesRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TCancelledInvoicesRepository
		extends MongoRepository<TCancelledInvoices, String>, TCancelledInvoicesRepositoryCustom {

	List<TCancelledInvoices> findAll();
}
