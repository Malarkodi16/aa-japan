package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MAuctionGradesExterior;
import com.nexware.aajapan.repositories.custom.MAuctionGradesExteriorRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MAuctionGradesExteriorRepository
		extends MongoRepository<MAuctionGradesExterior, String>, MAuctionGradesExteriorRepositoryCustom {

	MAuctionGradesExterior findOneById(String id);

	MAuctionGradesExterior findOneByCode(String code);

}
