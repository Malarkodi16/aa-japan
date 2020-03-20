package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TInspectionOrderRequestCancelled;
import com.nexware.aajapan.repositories.custom.TInspectionOrderRequestCancelledRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TInspectionOrderRequestCancelledRepository extends
		MongoRepository<TInspectionOrderRequestCancelled, String>, TInspectionOrderRequestCancelledRepositoryCustom {

	List<TInspectionOrderRequestCancelled> findAllByInspectionCode(String inspectionCode);

	List<TInspectionOrderRequestCancelled> findAllByStockNo(String stockNo);

}
