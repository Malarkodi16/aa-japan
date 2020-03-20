package com.nexware.aajapan.repositories;

import java.util.Date;
import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TFwdrInvoice;
import com.nexware.aajapan.repositories.custom.TFwdrInvoiceRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TFwdrInvoiceRepository extends MongoRepository<TFwdrInvoice, String>, TFwdrInvoiceRepositoryCustom {
	List<TFwdrInvoice> findAllByInvoiceNoInAndPaymentApproveIn(List<String> invoiceNo, List<Integer> approveStatus);

	List<TFwdrInvoice> findAllByInvoiceNoAndPaymentApproveIn(String invoiceNo, List<Integer> approveStatus);

	List<TFwdrInvoice> findAllByInvoiceNoInAndPaymentApprove(List<String> invoiceNo, Integer approveStatus);

	List<TFwdrInvoice> findAllByInvoiceNo(String invoiceNo);

	@Query("{'dueDate' : { $gte: ?0, $lte: ?1 } }]}")
	List<TFwdrInvoice> findAllByDueDate(Date startDate, Date endDate);

	List<TFwdrInvoice> findAllByStockNo(String stockNo);
	
	void deleteAllByInvoiceNo(String invoiceNo);
}
