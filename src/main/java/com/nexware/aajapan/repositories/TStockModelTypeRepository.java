package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TStockModelType;
import com.nexware.aajapan.repositories.custom.TStockModelTypeRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TStockModelTypeRepository
		extends MongoRepository<TStockModelType, String>, TStockModelTypeRepositoryCustom {

	TStockModelType findOneByCode(String code);

	TStockModelType findOneByModelType(String modelType);

	void deleteOneByCode(String code);

}
