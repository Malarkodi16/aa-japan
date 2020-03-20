package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.MForeignBank;
import com.nexware.aajapan.repositories.custom.MForeignBankRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface MForeignBankRepository extends MongoRepository<MForeignBank, String>, MForeignBankRepositoryCustom {
	List<MForeignBank> findAll();

	MForeignBank findOneByBankId(String bankId);

	MForeignBank findOneById(String Id);
}
