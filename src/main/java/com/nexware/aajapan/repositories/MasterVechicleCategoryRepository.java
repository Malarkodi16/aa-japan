package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MVechicleCategory;

@Repository
@JaversSpringDataAuditable
public interface MasterVechicleCategoryRepository extends MongoRepository<MVechicleCategory, String> {

	MVechicleCategory findOneByName(String name);

}
