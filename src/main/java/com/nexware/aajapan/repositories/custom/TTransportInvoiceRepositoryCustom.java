package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.ApprovePaymentsDto;
import com.nexware.aajapan.dto.PayableAmountDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TTransportInvoiceDto;
import com.nexware.aajapan.dto.TTransportInvoiceStockDto;

public interface TTransportInvoiceRepositoryCustom {

	List<TTransportInvoiceDto> findAllTranporterInvoice();

	List<TTransportInvoiceDto> findAllTranporterMismatchInvoice();

	List<TTransportInvoiceStockDto> findAllTransportCompletedInvoice();

	List<ApprovePaymentsDto> findAllByPaymentApprove(List<Integer> paymentApprove);

	List<ApprovePaymentsDto> findAllByPaymentApproveFreezed(Integer paymentApprove);

	Long getCountTransportData(Integer paymentApprove);

	List<TTransportInvoiceDto> findAllTransportPaymentCompletedInvoice();

	List<PayableAmountDto> getPayableAmountsForRemitters();

	List<PaymentTrackingDto> purchasepaymentTracking(List<Integer> invoiceStatus, String remitter, Date fromDate,
			Date toDate);
	
	List<PaymentTrackingReportDto> purchasepaymentTrackingReport(List<Integer> invoiceStatus, String remitter, Date fromDate,
			Date toDate);

	Long getApprovalCountTransportData();

	Long getPaymentApprovalCountTransportData();

	Long getFreezedCountTransportData(List<Integer> paymentApprove);
}
