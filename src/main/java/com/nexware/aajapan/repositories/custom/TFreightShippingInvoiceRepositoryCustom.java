package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.nexware.aajapan.dto.ApprovePaymentsDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TFreightShippingContainerInvoiceDto;
import com.nexware.aajapan.dto.TFreightShippingInvoiceDto;
import com.nexware.aajapan.dto.TFreightShippingRadiationDto;

public interface TFreightShippingInvoiceRepositoryCustom {
	void updateFreightExchange(String invoiceNo, Date invoiceDate, String exchangeRate, List<String> invoiceId);

	List<ApprovePaymentsDto> findAllByPaymentApprove(List<Integer> paymentApprove);

	List<ApprovePaymentsDto> findAllRoroInvoicesGroup();

	List<TFreightShippingInvoiceDto> findAllRoroPaymentCompleted();

	TFreightShippingInvoiceDto findOneRoroPaymentCompleted(String invoiceNo);

	List<ApprovePaymentsDto> findAllByPaymentApproveFreezed(Integer paymentApprove);

	Long getCountStorageData(List<Integer> paymentApprove);

	List<TFreightShippingRadiationDto> getAllNotClaimedRadiationStatus();

	List<PaymentTrackingDto> purchasepaymentTracking(String remitter, Date fromDate, Date toDate);

	List<PaymentTrackingReportDto> purchasepaymentTrackingReport(String remitter, Date fromDate, Date toDate);

	List<TFreightShippingContainerInvoiceDto> findAllShippingContainerInvoice();

	List<Document> findAllStockByinvoiceNo(String invoiceNo);
}
