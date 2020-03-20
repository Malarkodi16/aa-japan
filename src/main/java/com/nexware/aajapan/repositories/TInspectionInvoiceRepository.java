package com.nexware.aajapan.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nexware.aajapan.models.TInspectionInvoice;
import com.nexware.aajapan.repositories.custom.TInspectionInvoiceRepositoryCustom;

public interface TInspectionInvoiceRepository
		extends MongoRepository<TInspectionInvoice, String>, TInspectionInvoiceRepositoryCustom {

	List<TInspectionInvoice> findAllByInvoiceNo(String invoiceNo);

	List<TInspectionInvoice> findAllByInvoiceNoAndPaymentStatusIn(String invoiceNo,
			List<Integer> paymentApprovedStatus);

	List<TInspectionInvoice> findAllByInvoiceNoAndPaymentStatusIn(String invoiceNo, Integer paymentApprovedStatus);

}
