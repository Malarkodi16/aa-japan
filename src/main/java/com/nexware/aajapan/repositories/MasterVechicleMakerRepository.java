package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MVechicleMaker;
import com.nexware.aajapan.repositories.custom.MVechicleMakerRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MasterVechicleMakerRepository
		extends MongoRepository<MVechicleMaker, String>, MVechicleMakerRepositoryCustom {

	MVechicleMaker findOneByCode(String code);

	MVechicleMaker findOneByName(String name);

}
