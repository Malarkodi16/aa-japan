package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.ApprovePaymentsDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TInspectionInvoiceDto;

public interface TInspectionInvoiceRepositoryCustom {

	List<ApprovePaymentsDto> findAllByPaymentStatus(List<Integer> paymentProcessing);

	List<TInspectionInvoiceDto> findAllInspectionPaymentApprovalInvoice();

	List<TInspectionInvoiceDto> findAllInspectionPaymentCompletedInvoice();

	List<PaymentTrackingDto> purchasepaymentTracking(String remitter, Date fromDate, Date toDate);

	List<PaymentTrackingReportDto> purchasepaymentTrackingReport(String remitter, Date fromDate, Date toDate);

}
