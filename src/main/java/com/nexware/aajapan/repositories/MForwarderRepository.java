package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.repositories.custom.MForwarderRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MForwarderRepository extends MongoRepository<MForwarder, String>, MForwarderRepositoryCustom {

	MForwarder findOneByid(String id);

	MForwarder findOneByCode(String code);

}
