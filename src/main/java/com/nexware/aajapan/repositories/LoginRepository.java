package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MLogin;
import com.nexware.aajapan.repositories.custom.LoginRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface LoginRepository extends MongoRepository<MLogin, String>, LoginRepositoryCustom {
	MLogin findByUsername(String username);

	MLogin findOneByUserId(String userId);

	MLogin findOneById(String id);
}
