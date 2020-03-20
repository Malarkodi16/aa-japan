package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MPlateNoRegistration;

@Repository
@JaversSpringDataAuditable
public interface MPlateNoRegistrationRepository extends MongoRepository<MPlateNoRegistration, String> {
	
	List<MPlateNoRegistration> findAll(Sort sort);

}
