package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.AuctionPaymentType;

@Repository
@JaversSpringDataAuditable
public interface AuctionPaymentTypeRepository extends MongoRepository<AuctionPaymentType, String> {

	AuctionPaymentType findOneByCode(String code);

	AuctionPaymentType findOneByType(String type);

}
