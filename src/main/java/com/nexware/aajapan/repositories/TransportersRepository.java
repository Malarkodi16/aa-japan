package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MTransporter;
import com.nexware.aajapan.repositories.custom.MTransporterRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TransportersRepository extends MongoRepository<MTransporter, String>, MTransporterRepositoryCustom {

	MTransporter findOneByCode(String code);

	List<MTransporter> findAllByCodeNotIn(List<String> list);

}
