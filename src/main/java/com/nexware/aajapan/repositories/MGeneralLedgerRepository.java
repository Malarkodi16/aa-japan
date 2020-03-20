package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MGeneralLedger;
import com.nexware.aajapan.repositories.custom.MGeneralLedgerRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MGeneralLedgerRepository
		extends MongoRepository<MGeneralLedger, String>, MGeneralLedgerRepositoryCustom {

}
