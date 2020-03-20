package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TDocumentConversion;
import com.nexware.aajapan.repositories.custom.TDocumentConversionRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TDocumentConversionRepository
		extends MongoRepository<TDocumentConversion, String>, TDocumentConversionRepositoryCustom {
	TDocumentConversion findOneByid(String id);

	Long countByDocConvertTo(Integer docConvertTo);

	Long countByExportCertificateStatus(Integer exportCertificateStatus);

	List<TDocumentConversion> findAllByIdIn(List<String> ids);

	List<TDocumentConversion> findAllByStockNo(String stockNo);

	TDocumentConversion findOneByStockNo(String stockNo);
}
