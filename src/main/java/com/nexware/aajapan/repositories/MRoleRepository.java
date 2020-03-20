package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MRole;

@Repository
@JaversSpringDataAuditable
public interface MRoleRepository extends MongoRepository<MRole, String> {
	// @Query("{$and:[{'department' : { $eq: ?0 }},{'reportTo':{ $exists: true
	// }}]}")
	List<MRole> findAllByDepartment(String department);

}
