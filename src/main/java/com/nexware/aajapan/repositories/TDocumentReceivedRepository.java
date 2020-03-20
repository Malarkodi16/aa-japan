package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TDocumentReceived;
import com.nexware.aajapan.repositories.custom.TDocumentReceivedRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TDocumentReceivedRepository
		extends MongoRepository<TDocumentReceived, String>, TDocumentReceivedRepositoryCustom {

	TDocumentReceived findOneByStockNo(String stockNo);

	List<TDocumentReceived> findAllByStockNoIn(List<String> stockNos);

}
