package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MCoaType;

@Repository
@JaversSpringDataAuditable
public interface MCoaTypeRepo extends MongoRepository<MCoaType, String> {

}
