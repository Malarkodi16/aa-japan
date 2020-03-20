package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MUAC;
import com.nexware.aajapan.repositories.custom.MUACRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MUACRepository extends MongoRepository<MUAC, String>, MUACRepositoryCustom {

}
