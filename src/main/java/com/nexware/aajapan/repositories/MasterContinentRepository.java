package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MContinent;
import com.nexware.aajapan.repositories.custom.MasterContinentRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MasterContinentRepository
		extends MongoRepository<MContinent, String>, MasterContinentRepositoryCustom {

}
