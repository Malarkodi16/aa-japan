package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MPort;

@Repository
@JaversSpringDataAuditable
public interface MPortRepository extends MongoRepository<MPort, String> {

	MPort findOneByPortName(String portName);

}
