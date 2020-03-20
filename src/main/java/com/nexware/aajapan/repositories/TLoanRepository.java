package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TLoan;
import com.nexware.aajapan.repositories.custom.TLoanRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TLoanRepository extends MongoRepository<TLoan, String>, TLoanRepositoryCustom {

	TLoan findOneByLoanId(String loanId);

	TLoan findOneByid(String id);

	@Override
	List<TLoan> findAll();
}
