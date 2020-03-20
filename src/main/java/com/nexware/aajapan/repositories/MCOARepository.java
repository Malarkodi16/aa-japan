package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MCOA;
import com.nexware.aajapan.repositories.custom.MCOARepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MCOARepository extends MongoRepository<MCOA, String>, MCOARepositoryCustom {

	MCOA findByCode(Long code);

}
