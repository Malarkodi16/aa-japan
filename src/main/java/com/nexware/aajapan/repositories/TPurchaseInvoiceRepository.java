package com.nexware.aajapan.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.repositories.custom.TPurchaseInvoiceRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface TPurchaseInvoiceRepository
		extends MongoRepository<TPurchaseInvoice, String>, TPurchaseInvoiceRepositoryCustom {
	// TPurchaseInvoice findOneByStockNoAndType(String stockNo)
	void deleteByCode(String code);

	TPurchaseInvoice findOneById(String id);

	Optional<TPurchaseInvoice> findOneByInvoiceNo(String invoiceNo);

	TPurchaseInvoice findOneByid(String id);

	TPurchaseInvoice findOneByStockNoAndType(String stockNo, String type);

	List<TPurchaseInvoice> findByStockNoAndType(String stockNo, String type);

	List<TPurchaseInvoice> findAllByStockNoAndType(String stockNo, String type);

	List<TPurchaseInvoice> findAllByStockNo(String stockNo);

	List<TPurchaseInvoice> findAllByInvoiceType(String invoiceType);

	List<TPurchaseInvoice> findAllByStockNoIn(List<String> stockNos);

	List<TPurchaseInvoice> findAllByPaymentStatus(String paymentStatus);

	List<TPurchaseInvoice> findAllByInvoiceTypeAndPaymentStatus(String invoiceType, String paymentStatus);

	List<TPurchaseInvoice> findAllByInvoiceNoInAndPaymentApproveAndStatusIn(List<String> invoiceNo,
			Integer paymentApprove, List<Integer> status);

	List<TPurchaseInvoice> findAllByInvoiceNoInAndPaymentApproveInAndStatusIn(String invoiceNo,
			List<Integer> paymentStatus, List<Integer> invoiceStatus);

	List<TPurchaseInvoice> findAllByInvoiceNoAndPaymentApproveInAndStatusIn(String invoiceNo,
			List<Integer> paymentStatus, List<Integer> invoiceStatus);

	List<TPurchaseInvoice> findAllByInvoiceNoInAndInvoiceTypeAndStatusAndPaymentApprove(List<String> invoiceNo,
			String invoiceType, Integer status, Integer paymentApprove);

	List<TPurchaseInvoice> findAllByInvoiceNo(String invoiceNo);

	Long countByStatus(Integer status);

	Long countByPaymentApprove(Integer paymentApprove);

//	Long countByRecycleClaimStatus(Integer recycleClaimStatus);

	TPurchaseInvoice findOneByCode(String purchaseInvoiceCode);

	@Query("{'dueDate' : { $gte: ?0, $lte: ?1 } }]}")
	List<TPurchaseInvoice> findAllByDueDate(Date startDate, Date endDate);

//	Long countByPurchaseTaxClaimStatus(Integer purchaseTaxStatus);
//
//	Long countByCommisionTaxClaimStatus(Integer purchaseTaxStatus);
}
