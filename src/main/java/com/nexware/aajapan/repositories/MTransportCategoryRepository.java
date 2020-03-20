package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MTransportCategory;

@Repository
@JaversSpringDataAuditable
public interface MTransportCategoryRepository extends MongoRepository<MTransportCategory, String> {

}
