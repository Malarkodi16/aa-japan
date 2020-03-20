package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TInventoryCost;
import com.nexware.aajapan.repositories.custom.TInventoryCostRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TInventoryCostRepository
		extends MongoRepository<TInventoryCost, String>, TInventoryCostRepositoryCustom {

}
