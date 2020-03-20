package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MReportingCategoryType;

@Repository
@JaversSpringDataAuditable
public interface MReportingCategoryTypeRep extends MongoRepository<MReportingCategoryType, String> {

}
