package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TLcInvoice;
import com.nexware.aajapan.repositories.custom.TLcInvoiceRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TLcInvoiceRepository extends MongoRepository<TLcInvoice, String>, TLcInvoiceRepositoryCustom {
	List<TLcInvoice> findAllByLcInvoiceNo(String lcInvoiceNo);

	TLcInvoice findOneByLcDtlIdAndStockNoAndStatusNot(String lcDtlId, String stockNo,Integer status);

	TLcInvoice findOneById(String id);

	TLcInvoice findOneByStockNo(String stockId);
}
