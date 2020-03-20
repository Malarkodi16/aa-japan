package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MUser;
import com.nexware.aajapan.repositories.custom.UserRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface UserRepository extends MongoRepository<MUser, String>, UserRepositoryCustom {
	MUser findOneByid(String id);

	MUser findOneByCode(String code);

}
