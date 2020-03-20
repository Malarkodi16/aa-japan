package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TLoanDetails;
import com.nexware.aajapan.repositories.custom.TLoanDetailsRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TLoanDetailsRepository extends MongoRepository<TLoanDetails, String>, TLoanDetailsRepositoryCustom {

	TLoanDetails findOneByLoanDtlId(String loanDtlId);

	void deleteByLoanId(String loanId);

}
