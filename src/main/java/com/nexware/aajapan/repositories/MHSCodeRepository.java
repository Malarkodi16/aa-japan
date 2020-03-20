package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MHSCode;
import com.nexware.aajapan.repositories.custom.MHSCodeRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MHSCodeRepository extends MongoRepository<MHSCode, String>, MHSCodeRepositoryCustom {

	MHSCode findOneByCcAndCategoryAndSubCategoryAndHsCode(String cc, String category, String subCategory, String hsCode);

	MHSCode findOneByCode(String code);

	List<MHSCode> findAllByDeleteFlag(Integer flag);

}
