package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TInspectionInstruction;
import com.nexware.aajapan.repositories.custom.TInspectionInstructionRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TInspectionInstructionRepository
		extends MongoRepository<TInspectionInstruction, String>, TInspectionInstructionRepositoryCustom {

	TInspectionInstruction findOneByCode(String code);
	
	TInspectionInstruction findOneByStockNo(String stockNo);

}
