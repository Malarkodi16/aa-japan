package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MForwarderDetail;
import com.nexware.aajapan.repositories.custom.MForwarderDetailRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MForwarderDetailRepository
		extends MongoRepository<MForwarderDetail, String>, MForwarderDetailRepositoryCustom {

	MForwarderDetail findOneByForwarderIdAndOrginPortAndDestPort(String forwarderId, String orginPort, String destPort);

}
