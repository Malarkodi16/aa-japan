package com.nexware.aajapan.repositories;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TInvoicePaymentTransaction;
import com.nexware.aajapan.repositories.custom.InvoicePaymentTransactionRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface InvoicePaymentTransactionRepository
		extends MongoRepository<TInvoicePaymentTransaction, String>, InvoicePaymentTransactionRepositoryCustom {

	void deleteOneByCode(String code);

	List<TInvoicePaymentTransaction> findAllByInvoiceNoAndStatus(String invoiceNo, Integer status);

	List<TInvoicePaymentTransaction> findAllByPaymentVoucherNo(String paymentVoucherNo);

	List<TInvoicePaymentTransaction> findByPaymentVoucherNo(String paymentVoucherNo);

	TInvoicePaymentTransaction findOneByCode(String code);
}
