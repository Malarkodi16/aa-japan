package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MShippingCharge;
import com.nexware.aajapan.repositories.custom.MasterShipRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MShipChargeRepository extends MongoRepository<MShippingCharge, String>, MasterShipRepositoryCustom {

}
