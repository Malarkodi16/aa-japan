package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.repositories.custom.MasterBankRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MasterBankRepository extends MongoRepository<MBank, String>, MasterBankRepositoryCustom {
	MBank findOneByBankSeq(String bankId);

	MBank findOneByCoaCode(Long coaCode);

	List<MBank> findAllByAccountType(Integer accountType);

	List<MBank> findAllByCurrencyType(Integer currencyType);
}
