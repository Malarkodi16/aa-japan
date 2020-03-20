package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TReAuction;
import com.nexware.aajapan.repositories.custom.TReAuctionRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TReAuctionRepository extends MongoRepository<TReAuction, String>, TReAuctionRepositoryCustom {

	TReAuction findOneById(String id);

	TReAuction findOneByStockNo(String stockNo);

	Long countByStatus(Integer status);

}
