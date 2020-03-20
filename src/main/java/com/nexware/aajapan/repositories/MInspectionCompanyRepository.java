package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MInspectionCompany;
import com.nexware.aajapan.repositories.custom.MInspectionCompanyRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MInspectionCompanyRepository
		extends MongoRepository<MInspectionCompany, String>, MInspectionCompanyRepositoryCustom {

	MInspectionCompany findOneByid(String id);

	MInspectionCompany findOneByCode(String code);

}
