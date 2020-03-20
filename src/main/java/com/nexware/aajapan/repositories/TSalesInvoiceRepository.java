package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TSalesInvoice;
import com.nexware.aajapan.repositories.custom.TSalesInvoiceRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TSalesInvoiceRepository extends MongoRepository<TSalesInvoice, String>, TSalesInvoiceRepositoryCustom {

	TSalesInvoice findOneByInvoiceNoAndStockNo(String invoiceNo, String stockNo);

	TSalesInvoice findOneById(String id);

	List<TSalesInvoice> findAllByInvoiceNo(String invoiceNo);

	List<TSalesInvoice> findAllByStockNo(String stockNo);

}
