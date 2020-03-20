package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MAuctionGradesInterior;
import com.nexware.aajapan.repositories.custom.MAuctionGradesInteriorRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MAuctionGradesInteriorRepository
		extends MongoRepository<MAuctionGradesInterior, String>, MAuctionGradesInteriorRepositoryCustom {

	MAuctionGradesInterior findOneById(String id);

	MAuctionGradesInterior findOneByCode(String code);

}
