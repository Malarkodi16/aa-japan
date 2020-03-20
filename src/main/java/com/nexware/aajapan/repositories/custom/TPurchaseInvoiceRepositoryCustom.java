package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.dto.ApprovePaymentsDto;
import com.nexware.aajapan.dto.CancelledStockDto;
import com.nexware.aajapan.dto.PayableAmountDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.PaymentTrackingReportDto;
import com.nexware.aajapan.dto.TInvoiceDto;
import com.nexware.aajapan.dto.TPurchaseInvoiceCarTaxDto;
import com.nexware.aajapan.dto.TPurchaseInvoiceDocumentationRecylceDto;
import com.nexware.aajapan.dto.TPurchaseInvoiceRecylceDto;
import com.nexware.aajapan.dto.TPurchaseInvoiceTaxDto;
import com.nexware.aajapan.models.TPurchaseInvoice;

public interface TPurchaseInvoiceRepositoryCustom {

	List<ApprovePaymentsDto> findAllByPaymentApprove(List<Integer> invoiceStatus, List<Integer> paymentStatus);

	List<ApprovePaymentsDto> findAllByPaymentApproveCompleted();

	List<TInvoiceDto> findAllByPaymentOthersCompleted();

	List<ApprovePaymentsDto> findAllByPaymentApproveFreezed(Integer paymentApprove);

	void updatePurchaseStatusByStockNo(Integer statusList, String stockNo);

	List<CancelledStockDto> findAllCancelledInvoice();

	CancelledStockDto findCancelledInvoiceById(String id);

	UpdateResult updateById(String id, Update update);

	List<ApprovePaymentsDto> getAllPaymentStatusDayBook();

	List<ApprovePaymentsDto> getAllDayBookTransportStatusAccountData();

	List<ApprovePaymentsDto> getAllDayBookTinvStatusAccountData();

	List<ApprovePaymentsDto> getAllDayBookForwarderStatusAccountData();

	List<ApprovePaymentsDto> getAllDayBookFreightShippingStatusAccountData();

	List<TPurchaseInvoiceRecylceDto> getAllNotClaimedRecycleClaimStatus();

	TPurchaseInvoiceRecylceDto getNotClaimedRecycleClaimById(String id);

	List<TPurchaseInvoiceCarTaxDto> getAllNotClaimedCarTaxClaimStatus();

	TPurchaseInvoiceCarTaxDto getNotClaimedCarTaxClaimById(String id);

	boolean isExistsByInvoiceNo(String invoiceNo);

	String findInvoiceNoByPurchaseDateAndSupplier(Date purchaseDate, String supplierCode);

	List<TPurchaseInvoiceDocumentationRecylceDto> getRecycleClaimByRecycleClaimStatus(Integer status);

	Long getCountAuctionData(Integer paymentApprove);

	public Long getAllPurchasedDataCountShipping();
	
	public Long getAllPurchasedDataCountAccounts();

	List<PayableAmountDto> getPayableAmountsForRemitters(List<Integer> invoiceStatus);

	List<Document> findBySearchDto(String search);

	List<TPurchaseInvoiceTaxDto> getAllCarTaxOnStatus(Integer commissionStatus, Integer purchaseStatus);

	List<TPurchaseInvoiceTaxDto> getAllCarTaxReceivable(Integer commissionStatus, Integer purchaseStatus);

	// Double getByTotalTaxIncluded();

	List<PaymentTrackingDto> purchasepaymentTracking(List<Integer> invoiceStatus, String remitter, Date fromDate,
			Date toDate);

	List<PaymentTrackingReportDto> purchasepaymentTrackingReport(List<Integer> invoiceStatus, String remitter,
			Date fromDate, Date toDate);

	Long getApprovalCountAuctionData(List<Integer> invoiceStatus, List<Integer> paymentStatus);

	Long getPaymentApprovalCountAuctionData();

	Long getPaymentApprovalCountOthersData();

	Long getFreezedCountAuctionData();

	Long countByRecycleClaimStatus(Integer recycleClaimStatus);

	Long countByPurchaseTaxClaimStatus(Integer purchaseTaxStatus);

	Long countByCommisionTaxClaimStatus(Integer commisionTaxStatus);

	Long countByCarTaxClaimStatus(Integer carTaxStatus);

	TPurchaseInvoice findOnePurchaseInvoice(String stockNo, String type);
}
