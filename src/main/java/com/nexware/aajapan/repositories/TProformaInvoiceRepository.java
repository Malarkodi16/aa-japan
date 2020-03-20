package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TProformaInvoice;
import com.nexware.aajapan.repositories.custom.TProformaInvoiceCustom;

@Repository
@JaversSpringDataAuditable
public interface TProformaInvoiceRepository extends MongoRepository<TProformaInvoice, String>, TProformaInvoiceCustom {
	TProformaInvoice findOneByInvoiceNo(String invoiceNo);
	
	List<TProformaInvoice> findByCustomerId(String customerId);

	void deleteByInvoiceNo(String invoiceNo);
}
