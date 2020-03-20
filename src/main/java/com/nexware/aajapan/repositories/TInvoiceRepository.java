package com.nexware.aajapan.repositories;

import java.util.Date;
import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TInvoice;
import com.nexware.aajapan.repositories.custom.TInvoiceRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TInvoiceRepository extends MongoRepository<TInvoice, String>, TInvoiceRepositoryCustom {
	List<TInvoice> findAllByInvoiceNoInAndPaymentApproveIn(List<String> invoiceNo, List<Integer> approveStatus);

	List<TInvoice> findAllByInvoiceNoAndPaymentApproveIn(String invoiceNo, List<Integer> approveStatus);

	List<TInvoice> findAllByInvoiceNoInAndPaymentApprove(List<String> invoiceNo, Integer paymentApprove);

	List<TInvoice> findAllByInvoiceNo(String invoiceNo);

	TInvoice findOneById(String id);

	@Query("{'dueDate' : { $gte: ?0, $lte: ?1 } }]}")
	List<TInvoice> findAllByDueDate(Date startDate, Date endDate);
}
