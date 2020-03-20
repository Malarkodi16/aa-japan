package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MShippingTerms;
import com.nexware.aajapan.repositories.custom.MShippingTermsRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MShippingTermsRepository
		extends MongoRepository<MShippingTerms, String>, MShippingTermsRepositoryCustom {

	MShippingTerms findOneByTermsIdAndNameAndShippingTerms(String termsId, String name, String shippingTerms);

	MShippingTerms findOneByTermsId(String termsId);

	List<MShippingTerms> findAllByDeleteFlag(Integer flag);

}
