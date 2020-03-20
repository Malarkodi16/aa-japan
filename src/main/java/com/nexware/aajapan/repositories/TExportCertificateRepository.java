package com.nexware.aajapan.repositories;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TExportCertificate;

@Repository
@JaversSpringDataAuditable
public interface TExportCertificateRepository extends MongoRepository<TExportCertificate, String> {
	TExportCertificate findOneByStockNo(String stockNo);
}
