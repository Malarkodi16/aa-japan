package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TInvoicePaymentTransactionDto;

public interface InvoicePaymentTransactionRepositoryCustom {
	Double findTotalByInvoiceNoAndStatus(String invoiceNo);

	Double findTotalByInvoiceTypeAndInvoiceNoAndStatus(String invoiceType, String invoiceNo);

	Double findTotalUsdByInvoiceTypeAndInvoiceNoAndStatus(String invoiceType, String invoiceNo);

	List<TInvoicePaymentTransactionDto> findAllTransactionDetailsByInvoiceNoAndStatus(String invoiceNo);
}
