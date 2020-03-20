package com.nexware.aajapan.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.TFreightShippingInvoice;
import com.nexware.aajapan.models.TInspectionInvoice;
import com.nexware.aajapan.repositories.TInspectionInvoiceRepository;
import com.nexware.aajapan.services.InvoicePaymentTransactionService;
import com.nexware.aajapan.services.TInspectionInvoiceService;

@Service
@Transactional
public class TInspectionInvoiceServiceImpl implements TInspectionInvoiceService {

	@Autowired
	private TInspectionInvoiceRepository inspectionInvoiceRepository;
	@Autowired
	private InvoicePaymentTransactionService invoicePaymentTransactionService;

	@Override
	public void revertPaymentTransaction(String invoiceNo, Double amount) {
		final List<TInspectionInvoice> invoices = inspectionInvoiceRepository.findAllByInvoiceNo(invoiceNo);

		// calculate invoice total
		final Double invoiceTotal = invoices.stream().mapToDouble(TInspectionInvoice::getTotalTaxIncluded).sum();

		// get total paid amount
		final Double totalPaidAmount = invoicePaymentTransactionService
				.getTotalTransactionAmount(Constants.INVOICE_TYPE_INSPECTION, invoiceNo);

		final Double amountAfterTransaction = totalPaidAmount - amount;
		Integer paymentStatus;
		if (amountAfterTransaction >= invoiceTotal) {
			paymentStatus = Constants.INSPECTION_PAYMENT_INVOICE_APPROVAL;
		} else if (amountAfterTransaction > 0) {
			paymentStatus = Constants.INSPECTION_PAYMENT_INVOICE_PROCESSING_PARTIAL;
		} else {
			paymentStatus = Constants.INSPECTION_PAYMENT_INVOICE_BOOKING_APPROVED;
		}
		for (final TInspectionInvoice invoice : invoices) {
			invoice.setPaymentStatus(paymentStatus);
			invoice.setInvoiceAmountReceived(amountAfterTransaction);
		}
		inspectionInvoiceRepository.saveAll(invoices);

	}

}
