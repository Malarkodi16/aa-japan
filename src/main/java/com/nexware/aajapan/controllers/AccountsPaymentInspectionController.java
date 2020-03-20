package com.nexware.aajapan.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.ApprovePaymentDetails;
import com.nexware.aajapan.models.TInspectionInvoice;
import com.nexware.aajapan.models.TInspectionOrderRequest;
import com.nexware.aajapan.models.TInvoicePaymentTransaction;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.InvoicePaymentTransactionRepository;
import com.nexware.aajapan.repositories.TInspectionInvoiceRepository;
import com.nexware.aajapan.repositories.TInspectionOrderRequestRepository;
import com.nexware.aajapan.services.BankTransactionService;
import com.nexware.aajapan.services.FileStorageService;
import com.nexware.aajapan.services.InvoicePaymentTransactionService;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/accounts/payment")
public class AccountsPaymentInspectionController {
	@Autowired
	private TInspectionOrderRequestRepository tInspectionOrderRequestRepository;
	@Autowired
	private TInspectionInvoiceRepository inspectionInvoiceRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private InvoicePaymentTransactionService invoicePaymentTransactionService;
	@Autowired
	private BankTransactionService bankTransactionService;
	@Autowired
	private InvoicePaymentTransactionRepository invoicePaymentTransactionRepository;

	@GetMapping("/inspection/invoice/booking")
	public ModelAndView inspectionBookingPage() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.invoiceBooking.inspection");
		return modelAndView;
	}

	@GetMapping("/inspection-booking-datasource")
	@ResponseBody
	public DatatableResponse inspectionBookingDataSource() {
		return new DatatableResponse(tInspectionOrderRequestRepository.getAllInspectionBookingData());
	}

	@Transactional
	@PostMapping(path = "/inspection/invoice/create")
	public ResponseEntity<Response> createInspectionInvoiceBooking(
			@RequestParam("inspectionCompanyId") String inspectionCompanyId,
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dueDate,
			@RequestParam("refNo") String refNo, @RequestParam("inspectionCompany") String inspectionCompany,
			@RequestBody List<Document> requestBody) {
//		final String invoiceRefNo = new StringBuilder(inspectionCompany).append("-")
//				.append(new SimpleDateFormat("ddMMyyyy").format(dueDate)).toString();

		String invoiceNo = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_INS_INVOICE);

		requestBody.forEach(data -> {
			String code = data.get("code").toString();
			TInspectionInvoice invoice = new TInspectionInvoice();
			invoice.setRefNo(refNo);
			invoice.setInspectionCompanyId(inspectionCompanyId);
			invoice.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_INS_INVOICE_UNIQUE_CODE));
			invoice.setInspectionOrderCode(code);
			invoice.setDueDate(dueDate);
			invoice.setStockNo(data.get("stockNo").toString());
			invoice.setInvoiceNo(invoiceNo);
			invoice.setInvoiceType(AppUtil.isObjectEmpty(code) ? Constants.INSPECTION_INVOICE_TYPE_REINSPECTION
					: Constants.INSPECTION_INVOICE_TYPE_INSPECTION);
			invoice.setAmount(Double.parseDouble(data.get("amount").toString()));
			invoice.setTax(Double.parseDouble(data.get("tax").toString()));
			invoice.setTaxAmount(Double.parseDouble(data.get("taxAmount").toString()));
			invoice.setTotalTaxIncluded(Double.parseDouble(data.get("totalTaxIncluded").toString()));
			invoice.setPaymentStatus(Constants.INSPECTION_PAYMENT_INVOICE_BOOKING_APPROVED);
			invoice.setInvoiceUpload(Constants.INVOICE_NOT_UPLOADED);
			invoice.setDeleteFlag(Constants.DELETE_FLAG_0);
			inspectionInvoiceRepository.save(invoice);

			if (!AppUtil.isObjectEmpty(code)) {
				TInspectionOrderRequest inspectionOrder = tInspectionOrderRequestRepository.findOneByCode(code);
				inspectionOrder.setBookingStatus(Constants.INVOICE_FOR_INSPECTION_ORDER_BOOKED);
				tInspectionOrderRequestRepository.save(inspectionOrder);
			}
		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/inspection/invoice/approve")
	public ModelAndView inspectionApprovalPage() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.invoiceApproval.inspection");
		return modelAndView;
	}

	@GetMapping("/inspection-approval-datasource")
	@ResponseBody
	public DatatableResponse inspectionApprovalDataSource() {
		return new DatatableResponse(tInspectionOrderRequestRepository.getAllInspectionApprovalData());
	}

	@PutMapping("/inspection/edit-invoice")
	@Transactional
	public ResponseEntity<Response> editInspectionInvoice(@RequestParam("invoiceNo") String invoiceNo,
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dueDate,
			@RequestParam("refNo") String refNo) {
		final List<TInspectionInvoice> invoices = inspectionInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TInspectionInvoice tInspectionOrderRequest : invoices) {
			tInspectionOrderRequest.setDueDate(dueDate);
			tInspectionOrderRequest.setRefNo(refNo);
		}
		inspectionInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success", invoices), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("inspection/invoice/upload")
	public ResponseEntity<Response> transportInvoiceUpload(@RequestParam("invoiceFile") final MultipartFile file,
			@RequestParam("invoiceNo") final String invoiceNo) throws ParseException {
		final String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file,
				Constants.ATTACHMENT_DIRECTORY_INSPECTION_INVOICE, "upload");
		final List<TInspectionInvoice> invoices = inspectionInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TInspectionInvoice invoice : invoices) {
			invoice.setInvoiceAttachmentFilename(fileName);
			invoice.setInvoiceAttachmentDiskFilename(diskFileName);
			invoice.setInvoiceUpload(Constants.INVOICE_UPLOADED);
			invoice.setAttachmentViewed(Constants.ATTACHMENT_NOT_VIEWED);
		}
		inspectionInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/inspection/update/attachmentView")
	public ResponseEntity<Response> attachmentViewUpdate(@RequestParam("invoiceNo") final String invoiceNo)
			throws ParseException {
		final List<TInspectionInvoice> invoices = inspectionInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TInspectionInvoice tTransportInvoice : invoices) {
			tTransportInvoice.setAttachmentViewed(Constants.ATTACHMENT_VIEWED);
		}
		inspectionInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/inspection/booked/approve")
	@Transactional
	public ResponseEntity<Response> approvePayment(
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") final Date dueDate,
			@RequestParam("invoiceNo") final String invoiceNo) {
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		final List<TInspectionInvoice> invoices = inspectionInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TInspectionInvoice invoice : invoices) {
			invoice.setDueDate(dueDate);
			invoice.setApprovedBy(loggedInUser.getUserId());
			invoice.setApprovedDate(new Date());
			invoice.setPaymentStatus(Constants.INSPECTION_PAYMENT_INVOICE_PROCESSING);
		}
		inspectionInvoiceRepository.saveAll(invoices);
		// account transaction entry
		// transportInvoiceService.transportInvoicePaymentApproveTransactions(invoices);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/approve/inspection")
	public ResponseEntity<Response> approveTransport(@RequestParam("id[]") final List<String> invoiceNos,
			@RequestBody final Map<String, String> data) throws ParseException {
		final String bank = data.get("bank");
		final String sApprovedDate = data.get("approvedDate");
		final String remarks = data.get("remarks");
		Double amount = Double.valueOf(data.get("amount"));
		Date approvedDate = null;
		if (!AppUtil.isObjectEmpty(sApprovedDate)) {
			approvedDate = new SimpleDateFormat("dd-MM-yyyy").parse(sApprovedDate);
		}
		// fetch records
		boolean processingMultipleInvoice = invoiceNos.size() > 1;

		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.INSPECTION_PAYMENT_INVOICE_PROCESSING,
				Constants.INSPECTION_PAYMENT_INVOICE_PROCESSING_PARTIAL);
		final List<TInvoicePaymentTransaction> invoiceTransaction = new ArrayList<>();
		Double bankTransactionTotalAmount = 0.0;
		for (String invoiceNo : invoiceNos) {
			final List<TInspectionInvoice> invoices = inspectionInvoiceRepository
					.findAllByInvoiceNoAndPaymentStatusIn(invoiceNo, paymentApprovedStatus);
			// calculate invoice total
			final Double invoiceTotal = invoices.stream().mapToDouble(TInspectionInvoice::getInvoiceTotal).sum();
			// get total paid amount
			final Double totalPaidAmount = invoicePaymentTransactionService
					.getTotalTransactionAmount(Constants.INVOICE_TYPE_INSPECTION, invoiceNo);
			final Double balanceToPay = invoiceTotal - totalPaidAmount;
			if (processingMultipleInvoice) {
				amount = balanceToPay;
			}
			if (Math.round(amount) > Math.round(balanceToPay)) {
				throw new AAJRuntimeException("Amount is greater than balance.");
			}
			final Double amountAfterTransaction = totalPaidAmount + amount;
			Integer paymentStatus;
			if (amountAfterTransaction >= invoiceTotal) {
				paymentStatus = Constants.INSPECTION_PAYMENT_INVOICE_APPROVAL;
			} else {
				paymentStatus = Constants.INSPECTION_PAYMENT_INVOICE_PROCESSING_PARTIAL;
			}

			for (final TInspectionInvoice invoice : invoices) {
				final ApprovePaymentDetails approvePaymentDetails = new ApprovePaymentDetails(bank, approvedDate,
						remarks);
				invoice.setApprovePaymentDetails(approvePaymentDetails);
				invoice.setPaymentStatus(paymentStatus);
				invoice.setInvoiceAmountReceived(amountAfterTransaction);
			}
			this.inspectionInvoiceRepository.saveAll(invoices);
			// invoice payment transaction
			TInvoicePaymentTransaction invoicePaymentTransaction = new TInvoicePaymentTransaction(
					Constants.INVOICE_TYPE_INSPECTION, invoiceNo, bank, amount, approvedDate, remarks,
					Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED);
			invoiceTransaction.add(invoicePaymentTransaction);
			bankTransactionTotalAmount += amount;

		}
		final String invoiceNosString = String.join(" | ", invoiceNos);
		// bank transaction
		final String paymentVoucherNo = bankTransactionService.bankTransactionEntry(bank, bankTransactionTotalAmount,
				Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, "invoice payment", invoiceNosString);
		// invoice transaction
		for (final TInvoicePaymentTransaction invoicePaymentTransaction : invoiceTransaction) {
			invoicePaymentTransaction.setPaymentVoucherNo(paymentVoucherNo);
			invoicePaymentTransactionService.saveInvoicePaymentTransaction(invoicePaymentTransaction);
		}
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("inspection-payment-approval")
	public ModelAndView inspectionPaymentApprovalPage() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.paymentApproval.inspection");
		return modelAndView;
	}

	// Transport Completed
	@GetMapping("/inspection-payment-approval-datasource")
	@ResponseBody
	public DatatableResponse findAllInspectionPaymentApprovalInvoice() {
		return new DatatableResponse(inspectionInvoiceRepository.findAllInspectionPaymentApprovalInvoice());
	}

	@Transactional
	@PostMapping(value = "/freeze-inspection-payment")
	public ResponseEntity<Response> freezeTransportPayment(@RequestBody Map<String, Object> data)
			throws ParseException {
		final Integer paymentStatus = (Integer) data.get("paymentStatus");
		final String invoiceNo = data.get("invoiceNo").toString();

		List<TInspectionInvoice> invoices;
		if (paymentStatus == 4) {
			invoices = inspectionInvoiceRepository.findAllByInvoiceNoAndPaymentStatusIn(invoiceNo,
					Constants.INSPECTION_PAYMENT_INVOICE_APPROVAL);
		} else if (paymentStatus.equals(Constants.INSPECTION_PAYMENT_INVOICE_PAYMENT_CANCELLED)) {
			invoices = inspectionInvoiceRepository.findAllByInvoiceNoAndPaymentStatusIn(invoiceNo,
					Constants.INSPECTION_PAYMENT_INVOICE_PAYMENT_CANCELLED);
		} else {
			return new ResponseEntity<>(new Response("failed"), HttpStatus.OK);
		}

		for (final TInspectionInvoice invoice : invoices) {
			invoice.setPaymentStatus(Constants.INSPECTION_PAYMENT_INVOICE_PAYMENT_COMPLETED);
		}
		// Save Freeze
		inspectionInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping(value = "/cancel-inspection-payment")
	public ResponseEntity<Response> cancelTransportPayment(@RequestBody Map<String, Object> data)
			throws ParseException {

		final String cancelledRemarks = (String) data.get("cancelledRemarks");
		final String invoiceNo = data.get("invoiceNo").toString();

		final List<TInspectionInvoice> invoices = inspectionInvoiceRepository
				.findAllByInvoiceNoAndPaymentStatusIn(invoiceNo, Constants.INSPECTION_PAYMENT_INVOICE_APPROVAL);

		for (final TInspectionInvoice invoice : invoices) {
			invoice.setCancelledRemarks(cancelledRemarks);
			invoice.setPaymentStatus(Constants.INSPECTION_PAYMENT_INVOICE_PAYMENT_CANCELLED);

		}
		inspectionInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/inspection/statement/upload")
	public ResponseEntity<Response> inspectionBankStatementUpload(@RequestParam("invoiceFile") final MultipartFile file,
			@RequestParam("code") final String code) {
		final String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file,
				Constants.ATTACHMENT_DIRECTORY_INSPECTION_BANK_STATEMENT, "upload");

		final TInvoicePaymentTransaction invoice = invoicePaymentTransactionRepository.findOneByCode(code);

		invoice.setBankStatementAttachmentFilename(fileName);
		invoice.setBankStatementAttachmentDiskFilename(diskFileName);

		invoicePaymentTransactionRepository.save(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("inspection-payment-completed")
	public ModelAndView inspectionPaymentCompletedPage() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.paymentCompleted.inspection");
		return modelAndView;
	}

	// Transport Completed
	@GetMapping("/inspection-payment-completed-datasource")
	@ResponseBody
	public DatatableResponse findAllInspectionPaymentCompletedInvoice() {
		return new DatatableResponse(inspectionInvoiceRepository.findAllInspectionPaymentCompletedInvoice());
	}

}
