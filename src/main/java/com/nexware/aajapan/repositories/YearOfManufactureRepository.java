package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TYearOfManufacture;

@Repository
@JaversSpringDataAuditable
public interface YearOfManufactureRepository extends MongoRepository<TYearOfManufacture, String> {

	TYearOfManufacture findOneByModelNoAndFrameAndSerialNoFromLessThanEqualAndSerialNoToGreaterThanEqual(String modelNo,
			String frame, Long serialNoFrom, Long serialNoTo);
	List<TYearOfManufacture> findAllByDeleteFlag(Integer deleteFlag);
	TYearOfManufacture findOneById(String id);
	TYearOfManufacture findOneByCode(String code);
	TYearOfManufacture findByCode(String code);
	


}
