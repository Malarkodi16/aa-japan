package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.ApprovePaymentsDto;
import com.nexware.aajapan.dto.GenaralExpensesDto;
import com.nexware.aajapan.dto.PayableAmountDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TInvoiceDto;

public interface TInvoiceRepositoryCustom {
	List<ApprovePaymentsDto> findAllByPaymentApprove(List<Integer> paymentApprove);

	List<TInvoiceDto> findAllByPaymentApproveFreezed(Integer paymentApprove);

	List<TInvoiceDto> findAllOtherPaymentNotApproved();

	List<GenaralExpensesDto> findAllGenaralExpensesNotApproved();

	TInvoiceDto findOneOtherPaymentByInvoiceNo(String invoiceNo);

	Long getCountOthersData(List<Integer> paymentApprove);

	List<PayableAmountDto> getPayableAmountsForRemitters();

	List<PaymentTrackingDto> purchasepaymentTracking(List<Integer> invoiceStatus, String remitter, Date fromDate,
			Date toDate);

	List<PaymentTrackingReportDto> purchasepaymentTrackingReport(List<Integer> invoiceStatus, String remitter,
			Date fromDate, Date toDate);

	boolean isSameRefNoExistsInGeneralSupplier(String refNo);

	boolean isSameRefNoExistsWithInvoiceNo(String invoiceNo, String refNo);
}
