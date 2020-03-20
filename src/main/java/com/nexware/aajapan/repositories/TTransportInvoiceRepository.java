package com.nexware.aajapan.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TTransportInvoice;
import com.nexware.aajapan.repositories.custom.TTransportInvoiceRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TTransportInvoiceRepository
		extends MongoRepository<TTransportInvoice, String>, TTransportInvoiceRepositoryCustom {

	TTransportInvoice findOneById(String id);

	Optional<TTransportInvoice> findOneByInvoiceNo(String invoiceNo);

	TTransportInvoice findOneByOrderIdAndStockNo(String orderId, String stockNo);

	List<TTransportInvoice> findAllByInvoiceRefNoInAndPaymentApproveIn(List<String> invoiceNo,
			List<Integer> approveStatus);

	List<TTransportInvoice> findAllByInvoiceRefNoAndPaymentApproveIn(String invoiceNo, List<Integer> approveStatus);

	List<TTransportInvoice> findAllByInvoiceRefNo(String invoiceNo);

	List<TTransportInvoice> findAllByInvoiceRefNoInAndPaymentApprove(List<String> invoiceNo, Integer approveStatus);

	List<TTransportInvoice> findAllByInvoiceRefNoAndRefNoAndPaymentApprove(String invoiceRefNo, String refNo,
			Integer approveStatus);

	List<TTransportInvoice> findAllByInvoiceRefNoAndRefNo(String invoiceRefNo, String refNo);

	List<TTransportInvoice> findAllByInvoiceNoInAndPaymentApprove(List<String> invoiceNo, Integer approveStatus);

	List<TTransportInvoice> findAllByStockNoAndPaymentApprove(String stockNo, Integer paymentApprove);

	List<TTransportInvoice> findAllByStockNo(String stockNo);

	@Query("{'dueDate' : { $gte: ?0, $lte: ?1 } }]}")
	List<TTransportInvoice> findAllByDueDate(Date startDate, Date endDate);

}
