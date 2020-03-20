package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MCustomListFields;

@Repository
@JaversSpringDataAuditable
public interface MCustomListFieldsRepository extends MongoRepository<MCustomListFields, String> {
	List<MCustomListFields> findByFieldIdIn(List<Integer> fieldIds);
}
