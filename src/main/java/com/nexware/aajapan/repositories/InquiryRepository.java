package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TInquiry;
import com.nexware.aajapan.repositories.custom.InquiryRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface InquiryRepository extends MongoRepository<TInquiry, String>, InquiryRepositoryCustom {
	TInquiry findOneByid(String id);

	@Override
	List<TInquiry> findAll();
}
