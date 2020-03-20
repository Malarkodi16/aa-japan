package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MCustomListFields;

@Repository
@JaversSpringDataAuditable
public interface CustomListFieldsRepo extends MongoRepository<MCustomListFields, String> {
	@Query("{access:{$elemMatch:{$eq:?0}}}")
	List<MCustomListFields> findAllByAccess(Integer access);

}
