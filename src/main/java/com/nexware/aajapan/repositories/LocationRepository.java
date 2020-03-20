package com.nexware.aajapan.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.repositories.custom.MLocationRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface LocationRepository extends MongoRepository<MLocation, String>, MLocationRepositoryCustom {
	List<MLocation> findAllByCodeIn(List<String> codes);

	List<MLocation> findAllByDeleteFlag(Integer deleteFlag);

	List<MLocation> findAllByAuctionHouseIdIn(List<ObjectId> auctionHouseIds);

	MLocation findOneByAuctionHouseId(ObjectId auctionHouseId);

	MLocation findOneById(String id);

	MLocation findOneByCode(String code);

	boolean existsByIdAndDisplayName(String id, String displayName);

	boolean existsByDisplayName(String displayName);

	MLocation findOneBySupplierCodeAndAuctionHouseId(String supplierCode, ObjectId auctionHouseId);

}
