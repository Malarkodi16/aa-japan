package com.nexware.aajapan.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.PayableAmountDto;
import com.nexware.aajapan.dto.PaymentTrackingDto;
import com.nexware.aajapan.dto.TForwarderInvoiceCsvDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.models.MGeneralSupplier;
import com.nexware.aajapan.models.MInspectionCompany;
import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.MTransporter;
import com.nexware.aajapan.models.TAdvancePayment;
import com.nexware.aajapan.models.TExchangeRate;
import com.nexware.aajapan.models.TFreightShippingInvoice;
import com.nexware.aajapan.models.TFwdrInvoice;
import com.nexware.aajapan.models.TInspectionOrderRequest;
import com.nexware.aajapan.models.TInvoice;
import com.nexware.aajapan.models.TInvoicePaymentTransaction;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TTransportInvoice;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.InvoicePaymentTransactionRepository;
import com.nexware.aajapan.repositories.MForwarderRepository;
import com.nexware.aajapan.repositories.MGeneralSupplierRepository;
import com.nexware.aajapan.repositories.MInspectionCompanyRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.MasterSupplierRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TAdvancePaymentRepository;
import com.nexware.aajapan.repositories.TExchageRateRepository;
import com.nexware.aajapan.repositories.TFreightShippingInvoiceRepository;
import com.nexware.aajapan.repositories.TFwdrInvoiceRepository;
import com.nexware.aajapan.repositories.TInspectionInvoiceRepository;
import com.nexware.aajapan.repositories.TInspectionOrderRequestRepository;
import com.nexware.aajapan.repositories.TInvoiceRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.repositories.TTransportInvoiceRepository;
import com.nexware.aajapan.repositories.TransportersRepository;
import com.nexware.aajapan.services.FileStorageService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TAdvancePaymentService;
import com.nexware.aajapan.services.TForwarderInvoiceService;
import com.nexware.aajapan.services.TInvoiceService;
import com.nexware.aajapan.services.TPurchaseInvoiceService;
import com.nexware.aajapan.services.TTransportInvoiceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/accounts/payment")
public class AccountsPaymentController {
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TFwdrInvoiceRepository tFwdrInvoiceRepository;
	@Autowired
	private MForwarderRepository mForwarderRepository;
	@Autowired
	private TInvoiceRepository tInvoiceRepository;
	@Autowired
	private TTransportInvoiceRepository tTransportInvoiceRepository;
	@Autowired
	private MasterBankRepository mBank;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private TPurchaseInvoiceRepository purchaseInvoiceRepository;
	@Autowired
	private TAdvancePaymentRepository advancePaymentRepository;
	@Autowired
	private TransportersRepository transportersRepository;
	@Autowired
	private MasterSupplierRepository masterSupplierRepository;
	@Autowired
	private TAdvancePaymentService advancePaymentService;
	@Autowired
	private TSalesInvoiceRepository salesInvoiceRepository;
	@Autowired
	private TPurchaseInvoiceService purchaseInvoiceService;
	@Autowired
	private InvoicePaymentTransactionRepository invoicePaymentTransactionRepository;
	@Autowired
	private TFreightShippingInvoiceRepository freightShippingInvoiceRepository;
	@Autowired
	private TForwarderInvoiceService forwarderInvoiceService;
	@Autowired
	private TInvoiceService invoiceService;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private TTransportInvoiceService transportInvoiceService;
	@Autowired
	private TInspectionOrderRequestRepository tInspectionOrderRequestRepository;
	@Autowired
	private TInspectionInvoiceRepository inspectionInvoiceRepository;
	@Autowired
	private MInspectionCompanyRepository mInspectionCompanyRepository;
	@Autowired
	private MGeneralSupplierRepository mGeneralSupplierRepository;
	@Autowired
	private TExchageRateRepository tExchageRateRepository;

	// Advance and Prepayments
	@GetMapping("/advance")
	public ModelAndView viewAccountAdv() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", mBank.findAll());
		modelAndView.setViewName("accounts.payments.payment-advance");
		return modelAndView;
	}

	@GetMapping("/completed")
	public ModelAndView completed() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payments.completed");
		return modelAndView;
	}

	@Transactional
	@PostMapping("/createStorageAndPhotos")
	public ResponseEntity<Response> createStorageAndPhotos(@RequestBody List<TFwdrInvoice> tFwdrInvoice,
			@RequestParam("invoiceNo") String preInvoiceNo) {

		if (!AppUtil.isObjectEmpty(preInvoiceNo)) {

			this.tFwdrInvoiceRepository.deleteAllByInvoiceNo(preInvoiceNo);
		}
		final String invoiceNo = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_TFINV);

		tFwdrInvoice.stream().forEach(invoice -> {
			if (AppUtil.isObjectEmpty(preInvoiceNo)) {
				invoice.setInvoiceNo(invoiceNo);
			} else {
				invoice.setInvoiceNo(preInvoiceNo);
			}
			invoice.setInvoiceUpload(Constants.INVOICE_NOT_UPLOADED);
			TExchangeRate exchange = tExchageRateRepository.findTopOneByOrderByCreatedDate(new Date(),
					invoice.getCurrency());
			if (invoice.getCurrency() != 1) {
				invoice.setExchangeRateValue((invoice.getExchangeRate() == 1) ? exchange.getExchangeRate()
						: (invoice.getExchangeRate() == 2) ? exchange.getSalesExchangeRate()
								: (invoice.getExchangeRate() == 3) ? exchange.getSpecialExchangeRate()
										: invoice.getExchangeRateValue());
			} else {
				invoice.setExchangeRateValue(1.0);
			}
			if (invoice.getInvoiceType() == 0) {
				MSupplier mSupplier = masterSupplierRepository.findOneBySupplierCode(invoice.getRemitter());
				invoice.setForwarderName(mSupplier.getCompany());
			} else if (invoice.getInvoiceType() == 1) {
				MTransporter mTransporter = transportersRepository.findOneByCode(invoice.getRemitter());
				invoice.setForwarderName(mTransporter.getName());
			} else if (invoice.getInvoiceType() == 2 || invoice.getInvoiceType() == 3) {
				MForwarder mForwarder = mForwarderRepository.findOneByCode(invoice.getRemitter());
				invoice.setForwarderName(mForwarder.getName());
			} else if (invoice.getInvoiceType() == 4) {
				MGeneralSupplier mGeneralSupplier = mGeneralSupplierRepository.findOneByCode(invoice.getRemitter());
				invoice.setForwarderName(mGeneralSupplier.getName());
			} else if (invoice.getInvoiceType() == 5) {
				MInspectionCompany mInspectionCompany = mInspectionCompanyRepository
						.findOneByCode(invoice.getRemitter());
				invoice.setForwarderName(mInspectionCompany.getName());
			} else {
			}

		});

		tFwdrInvoiceRepository.saveAll(tFwdrInvoice);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/createStorageAndPhotos/csv")
	public ResponseEntity<Response> createStorageAndPhotosUsingCsv(@RequestParam("csvFile") final MultipartFile file,
			HttpServletRequest request) throws ParseException {
		final String invoiceNo = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_TFINV);
		final CellProcessor[] processors = new CellProcessor[] { new NotNull(), // chassisNo
				new ParseDouble(), // amount
				new NotNull(), // remark
				// new ParseDate("MM/dd/yyyy")
		};
		try (ICsvBeanReader beanReader = new CsvBeanReader(new InputStreamReader(file.getInputStream()),
				CsvPreference.STANDARD_PREFERENCE)) {
			final String[] header = beanReader.getHeader(true);
			TForwarderInvoiceCsvDto csvData = null;
			while ((csvData = beanReader.read(TForwarderInvoiceCsvDto.class, header, processors)) != null) {
				final TStock stock = stockRepository.findByChassisNoAndStatus(csvData.getChassisNo(),
						Constants.STOCK_STATUS_SOLD);
				if (!AppUtil.isObjectEmpty(stock)) {
					final List<TFwdrInvoice> invoices = tFwdrInvoiceRepository.findAllByStockNo(stock.getStockNo());
					if (AppUtil.isObjectEmpty(invoices)) {
						forwarderInvoiceService.saveCsvData(invoiceNo, request, csvData, stock.getStockNo());
					} else {
						final String paymentFor = request.getParameter("paymentFor");
						final boolean isValid = forwarderInvoiceService.isEntryValid(invoices, paymentFor);
						if (isValid) {
							forwarderInvoiceService.saveCsvData(invoiceNo, request, csvData, stock.getStockNo());
						} else {
							throw new AAJRuntimeException("Amount updated");
						}
					}
				} else {
					return new ResponseEntity<>(new Response("failure", "Chassis No Not Found"), HttpStatus.OK);
				}

			}
		} catch (final FileNotFoundException ex) {
			throw new AAJRuntimeException("Could not find the CSV file: " + ex);
		} catch (final IOException ex) {
			throw new AAJRuntimeException("Error reading the CSV file: " + ex);
		} catch (final AAJRuntimeException e) {
			return new ResponseEntity<>(new Response("failure", e.getMessage()), HttpStatus.OK);
		}
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/storage-and-photos-datasource")
	@ResponseBody
	public DatatableResponse storageAndPhotosDataSource() {
		return new DatatableResponse(tFwdrInvoiceRepository.getListOnInitiatedStatus());
	}

	@Transactional
	@PostMapping("/save-other-payments")
	public ResponseEntity<Response> savePaymentInvoice(@RequestBody List<TInvoice> tInvoice) {
		final String invoiceNo = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_TINV);

		// final String remitter = tInvoice.get(0).getRemitter();
		// final String supplierOthers = tInvoice.get(0).getRemitterOthers();
		// // create new remitter
		// final String code =
		// sequenceService.getNextSequence(Constants.SEQUENCE_KEY_GENERAL_SUPPLIER);
		// if (remitter.equalsIgnoreCase("others")) {
		// masterGeneralSupplierRepository.save(new MGeneralSupplier(code,
		// supplierOthers, Constants.DELETE_FLAG_0));
		// }

		tInvoice.stream().forEach(invoice -> {
			invoice.setInvoiceNo(invoiceNo);
			invoice.setInvoiceUpload(Constants.INVOICE_NOT_UPLOADED);
			invoice.setDeleteFlag(Constants.DELETE_FLAG_0);
		});

		tInvoiceRepository.saveAll(tInvoice);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/save-other-payments-edit")
	public ResponseEntity<Response> savePaymentInvoiceEdit(@RequestBody List<TInvoice> tInvoice) {

		String invoiceNo = tInvoice.get(0).getInvoiceNo();
		List<TInvoice> existingInvoice = tInvoiceRepository.findAllByInvoiceNo(invoiceNo);

		List<String> ids = tInvoice.stream().map(TInvoice::getId).collect(Collectors.toList());

		List<TInvoice> toBeDeleted = existingInvoice.stream().filter(item -> !ids.contains(item.getId()))
				.collect(Collectors.toList());

		toBeDeleted.stream().forEach(del -> {
			del.setDeleteFlag(Constants.DELETE_FLAG_1);
		});

		tInvoiceRepository.saveAll(toBeDeleted);

		invoiceService.editGeneralExpenses(tInvoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/delete/general-expense/booking")
	public ResponseEntity<Response> savePaymentInvoiceEdit(@RequestParam String invoiceNo) {
		List<TInvoice> tInvoice = tInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		invoiceService.deleteGeneralExpenses(tInvoice);
		return new ResponseEntity<>(
				new Response("success", tInvoiceRepository.findOneOtherPaymentByInvoiceNo(invoiceNo)), HttpStatus.OK);
	}

	@GetMapping("/payment-others-datasource")
	@ResponseBody
	public DatatableResponse paymentOthersDataSource() {
		return new DatatableResponse(tInvoiceRepository.findAllOtherPaymentNotApproved());
	}

	@GetMapping("/transport-invoice-datasource")
	@ResponseBody
	public DatatableResponse transportInvoiceDataSource() {
		return new DatatableResponse(tTransportInvoiceRepository.findAllTranporterInvoice());
	}

	@GetMapping("/transport-mismatch-invoice-datasource")
	@ResponseBody
	public DatatableResponse transportMismatchInvoiceDataSource() {
		return new DatatableResponse(tTransportInvoiceRepository.findAllTranporterMismatchInvoice());
	}

	@GetMapping("/transport-completed-datasource")
	@ResponseBody
	public DatatableResponse transportCompletedDataSource() {
		return new DatatableResponse(tTransportInvoiceRepository.findAllTransportCompletedInvoice());
	}

	@Transactional
	@PostMapping(path = "/transport/invoice/create")
	public ResponseEntity<Response> createTransportInvoiceApproval(@RequestParam("transporter") String transporter,
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dueDate,
			@RequestParam("invoiceDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date invoiceDate,
			@RequestParam("refNo") String refNo, @RequestBody List<Document> requestBody) {
		final String invoiceRefNo = new StringBuilder(transporter).append("-")
				.append(new SimpleDateFormat("ddMMyyyy").format(dueDate)).toString();
		requestBody.forEach(data -> {
			String id = data.get("invoiceId").toString();
			TTransportInvoice transportInvoice = this.tTransportInvoiceRepository.findOneById(id);
			Double amount = Double.parseDouble(data.get("amount").toString());
			Integer status = amount > transportInvoice.getAmount() ? Constants.TRANSPORT_INVOICE_MISMATCH_AMOUNT
					: Constants.TRANSPORT_INVOICE_BOOKED;
			transportInvoice.setRefNo(refNo);
			transportInvoice.setDueDate(dueDate);
			transportInvoice.setInvoiceDate(invoiceDate);
			transportInvoice.setInvoiceRefNo(invoiceRefNo);
			transportInvoice.setAmount(amount);
			transportInvoice.setTax(Double.parseDouble(data.get("tax").toString()));
			transportInvoice.setTaxAmount(Double.parseDouble(data.get("taxAmount").toString()));
			transportInvoice.setTotalTaxIncluded(Double.parseDouble(data.get("totalTaxIncluded").toString()));
			transportInvoice.setStatus(status);
			transportInvoice.setInvoiceUpload(Constants.INVOICE_NOT_UPLOADED);
			tTransportInvoiceRepository.save(transportInvoice);

		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	// Others Completed

	@GetMapping(value = "/others-data/completed")
	@ResponseBody
	public DatatableResponse getauctionPaymentDataCompleted() {
		return new DatatableResponse(purchaseInvoiceRepository.findAllByPaymentOthersCompleted());
	}

	@GetMapping("/invoice/transport/mismatch")
	public ModelAndView approveTransportMismatch() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payments.transport.mismatch.approve");
		return modelAndView;
	}

	@Transactional
	@PostMapping(value = "/cancel-others-payment")
	public ResponseEntity<Response> cancelOthersPayment(@RequestBody Map<String, Object> data) throws ParseException {

		final String cancelledRemarks = (String) data.get("cancelledRemarks");

		@SuppressWarnings("unchecked")
		final List<String> invoiceNos = (List<String>) data.get("invoiceNo");

		final List<TInvoice> invoices = tInvoiceRepository.findAllByInvoiceNoInAndPaymentApprove(invoiceNos,
				Constants.PAYMENT_COMPLETED);
		for (final TInvoice tInvoice : invoices) {
			tInvoice.setCancelledRemarks(cancelledRemarks);
			tInvoice.setPaymentApprove(Constants.PAYMENT_CANCELED);

		}
		tInvoiceRepository.saveAll(invoices);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@Transactional
	@PostMapping(value = "/freeze-others-payment")
	public ResponseEntity<Response> freezeOtherPayment(@RequestBody Map<String, Object> data) throws ParseException {
		final Integer paymentApprove = (Integer) data.get("paymentApprove");
		@SuppressWarnings("unchecked")
		final List<String> invoiceNos = (List<String>) data.get("invoiceNo");

		List<TInvoice> invoices;
		if (paymentApprove == 2) {
			invoices = tInvoiceRepository.findAllByInvoiceNoInAndPaymentApprove(invoiceNos,
					Constants.PAYMENT_COMPLETED);
		} else if (paymentApprove.equals(Constants.PAYMENT_CANCELED)) {
			invoices = tInvoiceRepository.findAllByInvoiceNoInAndPaymentApprove(invoiceNos, Constants.PAYMENT_CANCELED);
		} else {
			return new ResponseEntity<>(new Response("failed"), HttpStatus.OK);
		}

		for (final TInvoice tInvoice : invoices) {
			tInvoice.setPaymentApprove(Constants.PAYMENT_FREEZE);
		}
		// Save Freeze
		tInvoiceRepository.saveAll(invoices);
		TInvoice invoice = invoices.get(0);
		invoiceService.completePayment(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	// Transport Completed
	@GetMapping("/transport-payment-completed-datasource")
	@ResponseBody
	public DatatableResponse transportPaymentCompletedDataSource() {
		return new DatatableResponse(tTransportInvoiceRepository.findAllTransportPaymentCompletedInvoice());
	}

	@Transactional
	@PostMapping(value = "/cancel-transport-payment")
	public ResponseEntity<Response> cancelTransportPayment(@RequestBody Map<String, Object> data)
			throws ParseException {

		final String cancelledRemarks = (String) data.get("cancelledRemarks");

		@SuppressWarnings("unchecked")
		final List<String> invoiceNos = (List<String>) data.get("invoiceNo");

		final List<TTransportInvoice> invoices = tTransportInvoiceRepository
				.findAllByInvoiceRefNoInAndPaymentApprove(invoiceNos, Constants.PAYMENT_COMPLETED);
		for (final TTransportInvoice tTransportInvoice : invoices) {
			tTransportInvoice.setCancelledRemarks(cancelledRemarks);
			tTransportInvoice.setPaymentApprove(Constants.PAYMENT_CANCELED);

		}
		tTransportInvoiceRepository.saveAll(invoices);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@Transactional
	@PostMapping(value = "/freeze-transport-payment")
	public ResponseEntity<Response> freezeTransportPayment(@RequestBody Map<String, Object> data)
			throws ParseException {
		final Integer paymentApprove = (Integer) data.get("paymentApprove");
		final List<String> invoiceNos = (List<String>) data.get("invoiceNo");

		List<TTransportInvoice> invoices;
		if (paymentApprove == 2) {
			invoices = tTransportInvoiceRepository.findAllByInvoiceRefNoInAndPaymentApprove(invoiceNos,
					Constants.PAYMENT_COMPLETED);
		} else if (paymentApprove.equals(Constants.PAYMENT_CANCELED)) {
			invoices = tTransportInvoiceRepository.findAllByInvoiceRefNoInAndPaymentApprove(invoiceNos,
					Constants.PAYMENT_CANCELED);
		} else {
			return new ResponseEntity<>(new Response("failed"), HttpStatus.OK);
		}

		for (final TTransportInvoice tTransportInvoice : invoices) {
			tTransportInvoice.setPaymentApprove(Constants.PAYMENT_FREEZE);
		}
		// Save Freeze
		tTransportInvoiceRepository.saveAll(invoices);
		TTransportInvoice invoice = invoices.get(0);
		transportInvoiceService.completePayment(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	// Storage And Photos Completed
	@GetMapping("/payment-storage-and-photos-completed")
	@ResponseBody
	public DatatableResponse storagePaymentCompletedDataSource() {
		return new DatatableResponse(tFwdrInvoiceRepository.findAllByPaymentStorageCompleted());
	}

	@Transactional
	@PostMapping(value = "/cancel-storage-payment")
	public ResponseEntity<Response> cancelStoragePayment(@RequestBody Map<String, Object> data) throws ParseException {

		final String cancelledRemarks = (String) data.get("cancelledRemarks");

		final List<String> invoiceNos = (List<String>) data.get("invoiceNo");

		final List<TFwdrInvoice> invoices = tFwdrInvoiceRepository.findAllByInvoiceNoInAndPaymentApprove(invoiceNos,
				Constants.PAYMENT_COMPLETED);
		for (final TFwdrInvoice tFwdrInvoice : invoices) {
			tFwdrInvoice.setCancelledRemarks(cancelledRemarks);
			tFwdrInvoice.setPaymentApprove(Constants.PAYMENT_CANCELED);

		}
		tFwdrInvoiceRepository.saveAll(invoices);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@Transactional
	@PostMapping(value = "/freeze-storage-payment")
	public ResponseEntity<Response> freezeStoragePayment(@RequestBody Map<String, Object> data) throws ParseException {
		final Integer paymentApprove = (Integer) data.get("paymentApprove");
		final List<String> invoiceNos = (List<String>) data.get("invoiceNo");

		List<TFwdrInvoice> invoices;
		if (paymentApprove == 2) {
			invoices = tFwdrInvoiceRepository.findAllByInvoiceNoInAndPaymentApprove(invoiceNos,
					Constants.PAYMENT_COMPLETED);
		} else {
			invoices = tFwdrInvoiceRepository.findAllByInvoiceNoInAndPaymentApprove(invoiceNos,
					Constants.PAYMENT_CANCELED);
		}

		for (final TFwdrInvoice tFwdrInvoice : invoices) {
			tFwdrInvoice.setPaymentApprove(Constants.PAYMENT_FREEZE);
		}
		// Save Freeze
		tFwdrInvoiceRepository.saveAll(invoices);
		TFwdrInvoice invoice = invoices.get(0);
		forwarderInvoiceService.completePayment(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/auction/upload/bankStatement")
	public ResponseEntity<Response> a(@RequestParam("invoiceFile") MultipartFile file,
			@RequestParam("paymentVoucherNo") String paymentVoucherNo) {
		if (AppUtil.isObjectEmpty(paymentVoucherNo)) {
			return new ResponseEntity<>(new Response("failed"), HttpStatus.OK);
		}
		// upload file
		final String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file,
				Constants.ATTACHMENT_DIRECTORY_AUCTION_BANK_STATEMENT, "upload");
		final List<TInvoicePaymentTransaction> invoices = invoicePaymentTransactionRepository
				.findByPaymentVoucherNo(paymentVoucherNo);
		for (TInvoicePaymentTransaction invoice : invoices) {
			invoice.setBankStatementAttachmentFilename(fileName);
			invoice.setBankStatementAttachmentDiskFilename(diskFileName);
		}
		invoicePaymentTransactionRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/storageandphotos/bankStatement/upload")
	public ResponseEntity<Response> forwarderInvoiceUpload(@RequestParam("invoiceFile") MultipartFile file,
			@RequestParam("code") String code) {
		final String bankStatementAttachmentFilename = org.springframework.util.StringUtils
				.cleanPath(file.getOriginalFilename());
		final String bankStatementAttachmentDiskFilename = fileStorageService.storeFile(file,
				Constants.ATTACHMENT_DIRECTORY_FORWARDER_BANK_STATEMENT, "upload");
		final TInvoicePaymentTransaction invoice = invoicePaymentTransactionRepository.findOneByCode(code);

		invoice.setBankStatementAttachmentFilename(bankStatementAttachmentFilename);
		invoice.setBankStatementAttachmentDiskFilename(bankStatementAttachmentDiskFilename);

		invoicePaymentTransactionRepository.save(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/payable-amount")
	public ModelAndView payableAmount() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payments.payable");
		return modelAndView;
	}

	@GetMapping(value = "/payable-amount/data-source")
	@ResponseBody
	public DatatableResponse getApproveDayBook(@RequestParam("flag") Integer flag) {
		List<PayableAmountDto> result = new ArrayList<>();
		if (flag == null) {
			result = new ArrayList<>();
		} else if (flag == 0) {
			final List<Integer> invoiceStatus = Arrays.asList(Constants.INV_STATUS_VERIFIED,
					Constants.INV_CANCEL_CHARGE_UPDATED);
			result = purchaseInvoiceRepository.getPayableAmountsForRemitters(invoiceStatus);
		} else if (flag == 1) {
			result = tTransportInvoiceRepository.getPayableAmountsForRemitters();
		} else if (flag == 3) {
			result = tInvoiceRepository.getPayableAmountsForRemitters();
		} else if (flag == 4) {
			result = tFwdrInvoiceRepository.getPayableAmountsForRemitters();
		}
		return new DatatableResponse(result);
	}

	@PostMapping("/auction-cancellation-charge")
	public ResponseEntity<Response> addAuctionCancellationCharge(@RequestBody Map<String, Object> data) {
		purchaseInvoiceService.addAuctionCancellationCharge(data);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@PostMapping("/createAdvanceAndPrePayment")
	public ResponseEntity<Response> createAdvancePrePayment(@RequestBody TAdvancePayment payment) {
		if (payment.getRemitterType().equalsIgnoreCase(Constants.PAYMENT_BOOKING_PRE_ADVANCE_REMITTER_TYPE_SUPPLIER)) {
			final MSupplier supplier = masterSupplierRepository.findOneBySupplierCode(payment.getRemitterId());
			payment.setRemitTo(supplier.getCompany());
		} else if (payment.getRemitterType()
				.equalsIgnoreCase(Constants.PAYMENT_BOOKING_PRE_ADVANCE_REMITTER_TYPE_FORWARDER)) {
			final MForwarder forwarder = mForwarderRepository.findOneByCode(payment.getRemitterId());
			payment.setRemitTo(forwarder.getName());
		} else if (payment.getRemitterType()
				.equalsIgnoreCase(Constants.PAYMENT_BOOKING_PRE_ADVANCE_REMITTER_TYPE_TRANSPORTER)) {
			final MTransporter transporter = transportersRepository.findOneByCode(payment.getRemitterId());
			payment.setRemitTo(transporter.getName());
		}
		payment.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
		advancePaymentRepository.save(payment);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/createAdvanceAndPrePayment/data-source")
	@ResponseBody
	public DatatableResponse createAdvancePrePaymentDataSource(@RequestParam("flag") Integer flag) {
		List<TAdvancePayment> result = new ArrayList<>();
		if (flag == null) {
			result = new ArrayList<>();
		} else if (flag == 0) {
			result = advancePaymentRepository.findAllByPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
		} else if (flag == 1) {
			result = advancePaymentRepository.findAllByPaymentApprove(Constants.PAYMENT_APPROVED);
		} else if (flag == 2) {
			result = advancePaymentRepository.findAllByPaymentApprove(Constants.PAYMENT_COMPLETED);
		}
		return new DatatableResponse(result);
	}

	@PostMapping("/approve/advanceAndPrePayment")
	public ResponseEntity<Response> approveAuctionPayment(@RequestParam("id") String id) {
		final TAdvancePayment payment = advancePaymentRepository.findOneById(id);
		payment.setPaymentApprove(Constants.PAYMENT_APPROVED);
		advancePaymentRepository.save(payment);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/approve/advance/pre-payment")
	public ResponseEntity<Response> approveAuction(@RequestParam("id") String id, @RequestBody Map<String, String> data)
			throws ParseException {
		final String bank = data.get("bank");
		final String sApprovedDate = data.get("approvedDate");
		final String remarks = data.get("remarks");
		Date approvedDate = null;
		if (!AppUtil.isObjectEmpty(sApprovedDate)) {
			approvedDate = new SimpleDateFormat("dd-MM-yyyy").parse(sApprovedDate);
		}
		final TAdvancePayment payment = advancePaymentRepository.findOneById(id);
		payment.setBank(bank);
		payment.setRemarks(remarks);
		payment.setApprovedDate(approvedDate);
		payment.setPaymentApprove(Constants.PAYMENT_COMPLETED);
		advancePaymentService.advancePaymentCompleteTransactions(payment);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/receivable-amount")
	public ModelAndView receivableAmount() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payments.receivable");
		return modelAndView;
	}

	@GetMapping("/receivableAmount/data-source")
	@ResponseBody
	public DatatableResponse receivableAmountDataList() {
		return new DatatableResponse(salesInvoiceRepository.getReceivableAmountForCustomer());
	}

	@Transactional
	@PostMapping("/update/attachmentView/shipping/invoice")
	public ResponseEntity<Response> attachmentViewUpdateShippingInvocie(@RequestParam("id") String invoiceNo) {
		final List<TFreightShippingInvoice> invoices = freightShippingInvoiceRepository.findAllByinvoiceNo(invoiceNo);
		for (final TFreightShippingInvoice invoice : invoices) {
			invoice.setAttachementViewed(Constants.ATTACHMENT_VIEWED);
		}
		freightShippingInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/update/attachmentView/transport")
	public ResponseEntity<Response> attachmentViewUpdateTransport(
			@RequestParam("invoiceRefNo") final String invoiceRefNo, @RequestParam("refNo") final String refNo)
			throws ParseException {
		final List<TTransportInvoice> invoices = tTransportInvoiceRepository.findAllByInvoiceRefNoAndRefNo(invoiceRefNo,
				refNo);
		for (final TTransportInvoice tTransportInvoice : invoices) {
			tTransportInvoice.setAttachementViewed(Constants.ATTACHMENT_VIEWED);
		}
		tTransportInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/update/attachmentView/others")
	public ResponseEntity<Response> attachmentViewUpdateOthers(@RequestParam("id") String invoiceNo)
			throws ParseException {

		final List<TInvoice> invoices = tInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TInvoice invoice : invoices) {
			invoice.setAttachementViewed(Constants.ATTACHMENT_VIEWED);
		}
		tInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/update/attachmentView/storagePhotos")
	public ResponseEntity<Response> attachmentViewUpdateStoragePhotos(@RequestParam("id") String invoiceNo)
			throws ParseException {

		final List<TFwdrInvoice> fwdrinvoices = tFwdrInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TFwdrInvoice invoice : fwdrinvoices) {
			invoice.setAttachementViewed(Constants.ATTACHMENT_VIEWED);
		}
		tFwdrInvoiceRepository.saveAll(fwdrinvoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/invoicePayments/data-source")
	@ResponseBody
	public DatatableResponse invoicePaymentsDataList(@RequestParam("code") String invoiceNo) {
		return new DatatableResponse(
				invoicePaymentTransactionRepository.findAllTransactionDetailsByInvoiceNoAndStatus(invoiceNo));
	}

	@PutMapping("/cancel/auction/invoice")
	public ResponseEntity<Response> cancelAuctionInvoice(@RequestBody Map<String, Object> data) {
		final String cancelledRemarks = (String) data.get("cancellationRemarks");
		final String invoiceNo = (String) data.get("invoiceId");
		purchaseInvoiceService.revertPurchaseConfirm(invoiceNo, cancelledRemarks);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/cancel/transport/invoice")
	public ResponseEntity<Response> cancelTransportInvoice(@RequestBody Map<String, Object> data) {
		final String cancelledRemarks = (String) data.get("cancellationRemarks");
		final String invoiceRefNo = (String) data.get("invoiceRefNo");
		transportInvoiceService.revertTransportInvoice(invoiceRefNo, cancelledRemarks);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/tracking")
	public ModelAndView paymentTracking() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payments.tracking");
		return modelAndView;
	}

	@GetMapping(value = "/tracking/data-list")
	@ResponseBody
	public DatatableResponse getApproveDayBook(@RequestParam("type") final Integer flag,
			@RequestParam(value = "remitter", required = false) String remitter,
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate) {
		List<PaymentTrackingDto> result = new ArrayList<>();
		final List<Integer> invoiceStatus = Arrays.asList(Constants.INV_STATUS_VERIFIED,
				Constants.INV_CANCEL_CHARGE_UPDATED);
		if (flag == null) {
			result = new ArrayList<>();
		} else if (flag == 0) {

			result = purchaseInvoiceRepository.purchasepaymentTracking(invoiceStatus, remitter, fromDate, toDate);
		} else if (flag == 1) {
			result = tTransportInvoiceRepository.purchasepaymentTracking(invoiceStatus, remitter, fromDate, toDate);
		} else if (flag == 4) {
			result = tInvoiceRepository.purchasepaymentTracking(invoiceStatus, remitter, fromDate, toDate);
		} else if (flag == 3) {
			result = freightShippingInvoiceRepository.purchasepaymentTracking(remitter, fromDate, toDate);
		} else if (flag == 2) {
			result = tFwdrInvoiceRepository.purchasepaymentTracking(remitter, fromDate, toDate);
		} else if (flag == 5) {
			result = inspectionInvoiceRepository.purchasepaymentTracking(remitter, fromDate, toDate);
		}
		return new DatatableResponse(result);
	}

	@PutMapping("/edit/DueDate/invoiceApprove")
	@Transactional
	public ResponseEntity<Response> editDueDateInPaymentApprove(@RequestParam("invoiceNo") String invoiceNo,
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dueDate) {
		final List<TPurchaseInvoice> invoices = purchaseInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TPurchaseInvoice tPurchaseInvoice : invoices) {
			tPurchaseInvoice.setDueDate(dueDate);
		}
		purchaseInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success", invoices), HttpStatus.OK);
	}

	@PutMapping("/edit/auction-invoice")
	@Transactional
	public ResponseEntity<Response> editAuctionInvoice(@RequestParam("invoiceNo") String invoiceNo,
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dueDate,
			@RequestParam("auctionRefNo") String auctionRefNo, @RequestParam("remarks") String remarks) {
		final List<TPurchaseInvoice> invoices = purchaseInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TPurchaseInvoice tPurchaseInvoice : invoices) {
			tPurchaseInvoice.setDueDate(dueDate);
			tPurchaseInvoice.setAuctionRefNo(auctionRefNo);
			tPurchaseInvoice.setRemarks(remarks);
		}
		purchaseInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success", invoices), HttpStatus.OK);
	}

	@PutMapping("/edit/transport-invoice")
	@Transactional
	public ResponseEntity<Response> editTransportInvoice(@RequestParam("invoiceRefNo") String invoiceRefNo,
			@RequestParam("preRefNo") String preRefNo,
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dueDate,
			@RequestParam("refNo") String refNo) {
		final List<TTransportInvoice> invoices = tTransportInvoiceRepository.findAllByInvoiceRefNoAndRefNo(invoiceRefNo,
				preRefNo);
		for (final TTransportInvoice tTransportInvoice : invoices) {
			tTransportInvoice.setDueDate(dueDate);
			tTransportInvoice.setRefNo(refNo);
		}
		tTransportInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success", invoices), HttpStatus.OK);
	}

	@PutMapping("/edit/freight-shipping-invoice")
	@Transactional
	public ResponseEntity<Response> editFreightShippingInvoice(@RequestParam("invoiceNo") String invoiceNo,
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dueDate) {
		final List<TFreightShippingInvoice> invoices = freightShippingInvoiceRepository.findAllByinvoiceNo(invoiceNo);
		for (final TFreightShippingInvoice tFreightShippingInvoice : invoices) {
			tFreightShippingInvoice.setDueDate(dueDate);
		}
		freightShippingInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success", invoices), HttpStatus.OK);
	}

	@PutMapping("/edit/storage-photos")
	@Transactional
	public ResponseEntity<Response> editStoragePhotos(@RequestParam("invoiceNo") String invoiceNo,
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dueDate,
			@RequestParam("refNo") String refNo, @RequestParam("remarks") String remarks) {
		final List<TFwdrInvoice> invoices = tFwdrInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TFwdrInvoice tFwdrInvoice : invoices) {
			tFwdrInvoice.setDueDate(dueDate);
			tFwdrInvoice.setRefNo(refNo);
			tFwdrInvoice.setRemarks(remarks);
		}
		tFwdrInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success", invoices), HttpStatus.OK);
	}

	@GetMapping("/invoice/reauction")
	public ModelAndView reAuction(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.invoiceBooking.reauction");
		return modelAndView;
	}
}
