package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TInspectionOrderRequest;
import com.nexware.aajapan.repositories.custom.TInspectionOrderRequestRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TInspectionOrderRequestRepository
		extends MongoRepository<TInspectionOrderRequest, String>, TInspectionOrderRequestRepositoryCustom {
	TInspectionOrderRequest findOneByid(String id);

	TInspectionOrderRequest findOneByCode(String code);
	
	void deleteByCode(String code);
	
	void deleteByStockNo(String stockNo);
}
