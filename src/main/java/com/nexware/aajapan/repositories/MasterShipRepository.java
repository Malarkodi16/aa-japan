package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MShip;
import com.nexware.aajapan.repositories.custom.MasterShipRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MasterShipRepository extends MongoRepository<MShip, String>, MasterShipRepositoryCustom {
	MShip findOneByShipIdAndName(String shipId, String name);

	MShip findOneByShipId(String shipId);

	List<MShip> findAllByDeleteFlag(Integer flag);
}
