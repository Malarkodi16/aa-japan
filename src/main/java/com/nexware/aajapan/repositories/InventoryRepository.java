package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.repositories.custom.InventoryRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface InventoryRepository extends MongoRepository<TStock, String>, InventoryRepositoryCustom {
	TStock findOneByid(String id);
}
