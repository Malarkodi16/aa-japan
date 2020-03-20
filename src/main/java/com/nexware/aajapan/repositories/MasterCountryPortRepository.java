package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MCountryPort;

@Repository
@JaversSpringDataAuditable
public interface MasterCountryPortRepository extends MongoRepository<MCountryPort, String> {

	MCountryPort findOneByCountry(String country);
}
