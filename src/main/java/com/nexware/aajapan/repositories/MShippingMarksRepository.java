package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MShippingMarks;
import com.nexware.aajapan.repositories.custom.MShippingMarksRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MShippingMarksRepository
		extends MongoRepository<MShippingMarks, String>, MShippingMarksRepositoryCustom {

	MShippingMarks findOneByMarksIdAndNameAndShippingMarks(String marksId, String name, String shippingMarks);

	MShippingMarks findOneByMarksId(String marksId);

	List<MShippingMarks> findAllByDeleteFlag(Integer flag);
}
