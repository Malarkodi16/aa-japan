package com.nexware.aajapan.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nexware.aajapan.models.TStockEditHistory;
import com.nexware.aajapan.repositories.custom.TStockEditHistoryRepositoryCustom;

public interface TStockEditHistoryRepository
		extends MongoRepository<TStockEditHistory, String>, TStockEditHistoryRepositoryCustom {

//	List<TStockEditHistory> findAllByStockNo(String stockNo);

}
