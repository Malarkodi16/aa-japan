package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TShippingInstruction;
import com.nexware.aajapan.repositories.custom.TShippingInstructionRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TShippingInstructionRepository
		extends MongoRepository<TShippingInstruction, String>, TShippingInstructionRepositoryCustom {

	TShippingInstruction findOneById(String id);

	TShippingInstruction findOneByShippingInstructionId(String shippingInstructionId);
	
	TShippingInstruction findOneByStockNo(String stockNo);

	void deleteByShippingInstructionId(String shippingInstructionId);

}
