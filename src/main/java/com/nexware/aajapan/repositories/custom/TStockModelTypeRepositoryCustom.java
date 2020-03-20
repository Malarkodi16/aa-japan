package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.TStockModelType;

public interface TStockModelTypeRepositoryCustom {
	
	List<TStockModelType> getAllUnDeletedModel();
	
	boolean existsByCodeAndModelType(String code, String modelType);

	boolean existsByModelType(String modelType);

}
