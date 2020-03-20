package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.PayableAmountDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TStoragePhotosApprovalDto;

public interface TFwdrInvoiceRepositoryCustom {

	List<TStoragePhotosApprovalDto> getListOnInitiatedStatus();

	List<TStoragePhotosApprovalDto> findAllByPaymentApprove(List<Integer> paymentApprove);

	List<TStoragePhotosApprovalDto> findAllByPaymentApproveFreezed(Integer paymentApprove);

	List<TStoragePhotosApprovalDto> findAllByPaymentStorageCompleted();
	
	TStoragePhotosApprovalDto findStorageAndPhotosDateByInvoiceNo(String invoiceNo);

	Long getCountStorageData(List<Integer> paymentApprove);

	public List<PayableAmountDto> getPayableAmountsForRemitters();

	List<PaymentTrackingDto> purchasepaymentTracking(String remitter, Date fromDate, Date toDate);
	
	List<PaymentTrackingReportDto> purchasepaymentTrackingReport(String remitter, Date fromDate, Date toDate);

}
