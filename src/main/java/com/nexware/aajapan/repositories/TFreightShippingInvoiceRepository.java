package com.nexware.aajapan.repositories;

import java.util.Date;
import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TFreightShippingInvoice;
import com.nexware.aajapan.repositories.custom.TFreightShippingInvoiceRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TFreightShippingInvoiceRepository
		extends MongoRepository<TFreightShippingInvoice, String>, TFreightShippingInvoiceRepositoryCustom {
	TFreightShippingInvoice findOneById(String id);

	List<TFreightShippingInvoice> findAllByinvoiceNo(String invoiceNo);

	List<TFreightShippingInvoice> findAllByInvoiceNoInAndPaymentApproveIn(List<String> invoiceNo,
			List<Integer> approveStatus);

	List<TFreightShippingInvoice> findAllByInvoiceNoAndPaymentApproveIn(String invoiceNo, List<Integer> approveStatus);

	List<TFreightShippingInvoice> findAllByIdInAndPaymentApproveIn(List<String> invoiceNo, List<Integer> approveStatus);

	List<TFreightShippingInvoice> findAllByInvoiceNoInAndPaymentApprove(List<String> invoiceNo, Integer approveStatus);

	@Query("{'dueDate' : { $gte: ?0, $lte: ?1 } }]}")
	List<TFreightShippingInvoice> findAllByDueDate(Date startDate, Date endDate);
}
