package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TCustomReport;
import com.nexware.aajapan.repositories.custom.TCustomReportRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TCustomReportRepository extends MongoRepository<TCustomReport, String>, TCustomReportRepositoryCustom {
	TCustomReport findOneById(String id);

	TCustomReport findOneByCode(String code);

	List<TCustomReport> findAllByCreateBy(String createBy);
}
