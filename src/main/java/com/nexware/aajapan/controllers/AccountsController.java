package com.nexware.aajapan.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.datatable.DataTableResults;
import com.nexware.aajapan.dto.AddPaymentDto;
import com.nexware.aajapan.dto.ApprovePaymentsDto;
import com.nexware.aajapan.dto.DayBookEntryDto;
import com.nexware.aajapan.dto.ExchangeRateDto;
import com.nexware.aajapan.dto.InventoryCostDto;
import com.nexware.aajapan.dto.LoanSearchDto;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.dto.MUACDto;
import com.nexware.aajapan.dto.ROROApprovePaymentsDto;
import com.nexware.aajapan.dto.SalesSumaryResultDto;
import com.nexware.aajapan.dto.SpecialUserDto;
import com.nexware.aajapan.dto.SpecialUserStockSearchDto;
import com.nexware.aajapan.dto.StockFilter;
import com.nexware.aajapan.dto.TLoanCreateDto;
import com.nexware.aajapan.dto.TLoanDetailsDto;
import com.nexware.aajapan.dto.TLoanRepaymentDto;
import com.nexware.aajapan.dto.TStoragePhotosApprovalDto;
import com.nexware.aajapan.dto.TTAllocationExchangeRateDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.form.StockForm;
import com.nexware.aajapan.models.ApprovePaymentDetails;
import com.nexware.aajapan.models.MCOA;
import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.models.MLogin;
import com.nexware.aajapan.models.MPaymentCategory;
import com.nexware.aajapan.models.MShippingCharge;
import com.nexware.aajapan.models.OtherDirectExpense;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TDayBook;
import com.nexware.aajapan.models.TExchangeRate;
import com.nexware.aajapan.models.TFreightShippingInvoice;
import com.nexware.aajapan.models.TFwdrInvoice;
import com.nexware.aajapan.models.TInvoice;
import com.nexware.aajapan.models.TInvoicePaymentTransaction;
import com.nexware.aajapan.models.TLcDetails;
import com.nexware.aajapan.models.TLoan;
import com.nexware.aajapan.models.TLoanDetails;
import com.nexware.aajapan.models.TLoanRepayment;
import com.nexware.aajapan.models.TProformaInvoice;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TShippingRequest;
import com.nexware.aajapan.models.TSupplierTransaction;
import com.nexware.aajapan.models.TTransportInvoice;
import com.nexware.aajapan.models.TransportPaymentCategory;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.BillOfExchangeRepo;
import com.nexware.aajapan.repositories.InvoicePaymentTransactionRepository;
import com.nexware.aajapan.repositories.LoginRepository;
import com.nexware.aajapan.repositories.MCOARepository;
import com.nexware.aajapan.repositories.MCoaTypeRepo;
import com.nexware.aajapan.repositories.MCurrencyRepository;
import com.nexware.aajapan.repositories.MRemitTypeRep;
import com.nexware.aajapan.repositories.MShipChargeRepository;
import com.nexware.aajapan.repositories.MUACRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.MasterPaymentRepository;
import com.nexware.aajapan.repositories.OtherDirectExpenseRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.repositories.TDayBookRepository;
import com.nexware.aajapan.repositories.TDayBookTransactionRepository;
import com.nexware.aajapan.repositories.TExchageRateRepository;
import com.nexware.aajapan.repositories.TFreightShippingInvoiceRepository;
import com.nexware.aajapan.repositories.TFwdrInvoiceRepository;
import com.nexware.aajapan.repositories.TInspectionInvoiceRepository;
import com.nexware.aajapan.repositories.TInventoryCostRepository;
import com.nexware.aajapan.repositories.TInvoiceRepository;
import com.nexware.aajapan.repositories.TLcDetailsRepository;
import com.nexware.aajapan.repositories.TLoanDetailsRepository;
import com.nexware.aajapan.repositories.TLoanRepository;
import com.nexware.aajapan.repositories.TProformaInvoiceRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.repositories.TShippingRequestRepository;
import com.nexware.aajapan.repositories.TTransportInvoiceRepository;
import com.nexware.aajapan.repositories.TransportPaymentRepository;
import com.nexware.aajapan.services.AccountPaymentService;
import com.nexware.aajapan.services.AccountReportService;
import com.nexware.aajapan.services.BankTransactionService;
import com.nexware.aajapan.services.FileStorageService;
import com.nexware.aajapan.services.InvoicePaymentTransactionService;
import com.nexware.aajapan.services.MSupplierService;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.StockInfoService;
import com.nexware.aajapan.services.TDayBookService;
import com.nexware.aajapan.services.TExchangeRateService;
import com.nexware.aajapan.services.TForwarderInvoiceService;
import com.nexware.aajapan.services.TFreightInvoiceService;
import com.nexware.aajapan.services.TInvoiceService;
import com.nexware.aajapan.services.TLoanRepaymentService;
import com.nexware.aajapan.services.TLoanService;
import com.nexware.aajapan.services.TPurchaseInvoiceService;
import com.nexware.aajapan.services.TTransportInvoiceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("accounts")
public class AccountsController {
	@Autowired
	private TPurchaseInvoiceRepository purchaseInvoiceRepository;
	@Autowired
	private TInventoryCostRepository tInventoryCostRepository;
	@Autowired
	private MUACRepository uacRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	TCustomerRepository customerRepository;
	@Autowired
	LoginRepository loginRepository;
	@Autowired
	private TInvoiceService invoiceService;
	@Autowired
	private TTransportInvoiceRepository transportInvoiceRepository;
	@Autowired
	private MasterBankRepository masterBankRepository;
	@Autowired
	private MasterPaymentRepository masterPaymentRepository;
	@Autowired
	private OtherDirectExpenseRepository otherDirectExpenseRepository;
	@Autowired
	private TransportPaymentRepository transportPaymentRepository;
	@Autowired
	private MCurrencyRepository currencyRepository;
	@Autowired
	private MRemitTypeRep remitTypeRep;
	@Autowired
	private TLoanRepository loanRepository;
	@Autowired
	private BillOfExchangeRepo billOfExchange;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private MCOARepository mCoaRepo;
	@Autowired
	private MCoaTypeRepo mCoaTypeRepo;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private AccountPaymentService accPaymentService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TSalesInvoiceRepository tSalesInvoiceRepository;
	@Autowired
	private TInvoiceRepository tInvoiceRepository;
	@Autowired
	private TFreightShippingInvoiceRepository tFreightShippingInvoiceRepository;
	@Autowired
	private TFwdrInvoiceRepository tFwdrInvoiceRepository;
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private TPurchaseInvoiceService purchaseInvoiceService;
	@Autowired
	private TTransportInvoiceService transportInvoiceService;
	@Autowired
	private TExchangeRateService exchangeRateService;
	@Autowired
	private TForwarderInvoiceService tForwarderInvoiceService;
	@Autowired
	private TDayBookService tDayBookService;
	@Autowired
	private TFreightInvoiceService tFreightInvoiceService;
	@Autowired
	private InvoicePaymentTransactionService invoicePaymentTransactionService;
	@Autowired
	private TDayBookTransactionRepository tdayBookTransactionRepository;
	@Autowired
	private TLcDetailsRepository lcDetailsRepository;
	@Autowired
	private TProformaInvoiceRepository proformaInvoiceRepository;
	@Autowired
	private TLoanService loanService;
	@Autowired
	private InvoicePaymentTransactionRepository invoicePaymentTransactionRepository;
	@Autowired
	private TLoanDetailsRepository loanDetailsRepository;
	@Autowired
	private TLoanRepaymentService loanRepaymentService;
	@Autowired
	private TDayBookRepository dayBookRepository;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private MShipChargeRepository shipChargerepo;
	@Autowired
	private MSupplierService supplierService;
	@Autowired
	private BankTransactionService bankTransactionService;
	@Autowired
	private TPurchaseInvoiceRepository tPurchaseInvoiceRepository;
	@Autowired
	private AccountReportService accountReportService;
	@Autowired
	private StockInfoService stockInfoService;
	@Autowired
	private TShippingRequestRepository shippingRequestRepository;
	@Autowired
	private TInspectionInvoiceRepository inspectionInvoiceRepository;
	@Autowired
	private TExchageRateRepository tExchageRateRepository;

	@GetMapping("/dashboard")
	public ModelAndView dashBoard(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.setViewName("accounts.dashboard");
		return modelAndView;
	}

	@GetMapping("/customer/approve/list")
	public ModelAndView accountsCustomerList(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.approve.customerlist");
		return modelAndView;
	}

	@GetMapping("/stockInfo")
	public ModelAndView accountsStockInfo(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.stockinfo");
		return modelAndView;
	}

	@GetMapping("/specialuser/stock/data")
	public ResponseEntity<DataTableResults<SpecialUserDto>> getAccountsStockData(final HttpServletRequest request)
			throws ParseException {
		final StockFilter filter = new StockFilter(request);
//		final DataTableRequest<SpecialUserDto> dataTableInRQ = new DataTableRequest<>(request);
		final SpecialUserStockSearchDto result = stockRepository.getSpecialUserStockList(filter);
		final List<SpecialUserDto> stocks = AppUtil.isObjectEmpty(result) ? new ArrayList<>()
				: result.getListOfDataObjects();
		final int total = AppUtil.isObjectEmpty(result) ? 0 : result.getRecordsTotal();
		final DataTableResults<SpecialUserDto> dataTableResult = new DataTableResults<>();
//		dataTableResult.setDraw(dataTableInRQ.getDraw());
		dataTableResult.setListOfDataObjects(stocks);
		dataTableResult.setRecordsTotal(total);
//		if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
//			dataTableResult.setRecordsFiltered(total);
//		} else {
//			dataTableResult.setRecordsFiltered(stocks.size());
//		}
		return new ResponseEntity<>(dataTableResult, HttpStatus.OK);

	}

	@PostMapping("/save/exchage")
	public ResponseEntity<Response> exchangeRate(@RequestBody final List<ExchangeRateDto> exchangeRate) {
		exchangeRateService.updateExchangeRate(exchangeRate);
		return new ResponseEntity<>(new Response("success", currencyRepository.findAll()), HttpStatus.OK);
	}

	@GetMapping("/invoice/booking/auction")
	public ModelAndView stockPurchased(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.invoiceBooking.auction");
		modelAndView.addObject("screenNameFlag", "accounts");
		return modelAndView;
	}

	@GetMapping("/invoice/approval/auction")
	public ModelAndView auctionPayment(final HttpServletRequest request) {
		final String orderStatus = StringUtils.isEmpty(request.getParameter("status"))
				? Constants.ACCOUNTS_AUCTION_PAYMENT
				: request.getParameter("status");
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("payments", accPaymentService.getPaymentsCount());
		modelAndView.addObject("invoiceType", orderStatus);
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.setViewName("accounts.invoiceApproval.auction");

		return modelAndView;
	}

	@GetMapping("/invoice/booking/transport")
	public ModelAndView transport() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.invoiceBooking.transport");
		return modelAndView;
	}

	@GetMapping("/invoice/approval/transport")
	public ModelAndView transportApproval() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.setViewName("accounts.invoiceApproval.transport");
		return modelAndView;
	}

	@GetMapping("/invoice/booking/shipping/roro")
	public ModelAndView getShippingRoRo(ModelAndView modelAndView, HttpServletRequest request) {
		modelAndView.setViewName("accounts.invoiceBooking.freight.roro");

		return modelAndView;
	}

	@GetMapping("/invoice/booking/shipping/container")
	public ModelAndView getShippingCotainer(ModelAndView modelAndView, HttpServletRequest request) {
		modelAndView.setViewName("accounts.invoiceBooking.freight.container");
		return modelAndView;
	}

	@GetMapping("/invoice/approval/shipping/roro")
	public ModelAndView getshippingInvoice(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.invoiceApproval.freight.roro");
		return modelAndView;
	}

	@GetMapping("/invoice/approval/shipping/container")
	public ModelAndView getshippingInvoiceContainer(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.invoiceApproval.freight.container");
		return modelAndView;
	}

	@GetMapping("/invoice/booking/genaralExpenses")
	public ModelAndView bookingGenaralExpenses() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.setViewName("accounts.invoiceBooking.genaralExpenses");
		return modelAndView;
	}

	@GetMapping("/invoice/approval/genaralExpenses")
	public ModelAndView approveGenaralExpenses() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.setViewName("accounts.invoiceApproval.genaralExpenses");
		return modelAndView;
	}

	@GetMapping("/invoice/booking/storageAndPhotos")
	public ModelAndView storageAndPhotos() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.setViewName("accounts.invoiceBooking.storageAndPhotos");
		return modelAndView;
	}

	@GetMapping("/invoice/booking/otherDirectExpense")
	public ModelAndView otherDirectExpense() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.setViewName("accounts.invoiceBooking.otherDirectExpense");
		return modelAndView;
	}

	@GetMapping("/invoice/approval/otherDirectExpense")
	public ModelAndView otherDirectExpenseApproval() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.setViewName("accounts.invoiceApproval.otherDirectExpense");
		return modelAndView;
	}

	@GetMapping("/invoice/approval/storageAndPhotos")
	public ModelAndView storageAndPhotosList() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.setViewName("accounts.invoiceApproval.storageAndPhotos");
		return modelAndView;
	}

	@GetMapping("/storageAndPhotos/info/{code}")
	public ResponseEntity<Response> activeCompanyInfo(@PathVariable("code") String code) {
		return new ResponseEntity<Response>(
				new Response("success", this.tFwdrInvoiceRepository.findStorageAndPhotosDateByInvoiceNo(code)),
				HttpStatus.OK);
	}

	@GetMapping("/storageAndPhotos/edit/{code}")
	public ModelAndView storageAndPhotosEdit(@PathVariable("code") String code, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("invoiceNo", code);
		modelAndView.setViewName("redirect:/accounts/invoice/booking/otherDirectExpense");
		return modelAndView;
	}

	@GetMapping("/salesOrder")
	public ModelAndView salesOrderInvoice() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales-order");
		modelAndView.addObject("screenNameFlag", "accounts");
		return modelAndView;
	}

	@GetMapping(value = "/auction-data")
	@ResponseBody
	public DatatableResponse getauctionPaymentData() {
		final List<Integer> invoiceStatus = Arrays.asList(Constants.INV_STATUS_VERIFIED);
		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.PAYMENT_NOT_APPROVED);

		return new DatatableResponse(
				purchaseInvoiceRepository.findAllByPaymentApprove(invoiceStatus, paymentApprovedStatus));
	}

	@GetMapping(value = "/auction-data/completed")
	@ResponseBody
	public DatatableResponse getauctionPaymentDataCompleted() {
		return new DatatableResponse(purchaseInvoiceRepository.findAllByPaymentApproveCompleted());
	}

	@GetMapping(value = "/auction-data/freezed")
	@ResponseBody
	public DatatableResponse getauctionPaymentDataFreezed() {
		return new DatatableResponse(
				purchaseInvoiceRepository.findAllByPaymentApproveFreezed(Constants.PAYMENT_FREEZE));
	}

	@GetMapping(value = "/others-payment/freezed")
	@ResponseBody
	public DatatableResponse getOthersPaymentDataFreezed() {
		return new DatatableResponse(tInvoiceRepository.findAllByPaymentApproveFreezed(Constants.PAYMENT_FREEZE));
	}

	@GetMapping(value = "/freight-shipping/freezed")
	@ResponseBody
	public DatatableResponse getFreightShippingPaymentDataFreezed() {
		return new DatatableResponse(
				tFreightShippingInvoiceRepository.findAllByPaymentApproveFreezed(Constants.PAYMENT_FREEZE));
	}

	@GetMapping(value = "/storagePhotos-payment/freezed")
	@ResponseBody
	public DatatableResponse getStoragePhotosPaymentDataFreezed() {
		return new DatatableResponse(tFwdrInvoiceRepository.findAllByPaymentApproveFreezed(Constants.PAYMENT_FREEZE));
	}

	@GetMapping(value = "/transport-payment/freezed")
	@ResponseBody
	public DatatableResponse getTransportPaymentDataFreezed() {
		return new DatatableResponse(
				transportInvoiceRepository.findAllByPaymentApproveFreezed(Constants.PAYMENT_FREEZE));
	}

	@GetMapping(value = { "/payment-completed" })
	public ModelAndView paymentCompleted(final HttpServletRequest request) {
		final String orderStatus = StringUtils.isEmpty(request.getParameter("status"))
				? Constants.ACCOUNTS_AUCTION_PAYMENT
				: request.getParameter("status");
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("payments", accPaymentService.getPaymentsCount());
		modelAndView.addObject("invoiceType", orderStatus);
		modelAndView.setViewName("accounts.payment-completed");

		return modelAndView;
	}

	@GetMapping(value = "/payment-donedata")
	@ResponseBody
	public DatatableResponse getCompletedPaymentData(@RequestParam("status") final String status) {

		return new DatatableResponse(purchaseInvoiceRepository.findAllByInvoiceTypeAndPaymentStatus(status, "done"));
	}

	@GetMapping(value = { "/approve-payment" })
	public ModelAndView approvePayment(final HttpServletRequest request) {
		final String orderStatus = StringUtils.isEmpty(request.getParameter("status"))
				? Constants.ACCOUNTS_AUCTION_PAYMENT
				: request.getParameter("status");
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", masterBankRepository.findAllByAccountType(Constants.ACCOUNT_TYPE_BANK));
		modelAndView.addObject("payments", accPaymentService.getPaymentsCount());
		modelAndView.addObject("invoiceType", orderStatus);
		modelAndView.setViewName("accounts.approve-payment");

		return modelAndView;
	}

	@GetMapping(value = "/payment-daybook")
	@ResponseBody
	public DatatableResponse getApprovePaymentData() {
		final List<ApprovePaymentsDto> result = new ArrayList<>();
		List<ApprovePaymentsDto> result1;
		List<ApprovePaymentsDto> result2;
		List<ApprovePaymentsDto> result3;
		List<ApprovePaymentsDto> result4;
		List<ApprovePaymentsDto> result5;

		result1 = purchaseInvoiceRepository.getAllPaymentStatusDayBook();

		result2 = purchaseInvoiceRepository.getAllDayBookTransportStatusAccountData();

		result3 = purchaseInvoiceRepository.getAllDayBookTinvStatusAccountData();

		result4 = purchaseInvoiceRepository.getAllDayBookFreightShippingStatusAccountData();

		result5 = purchaseInvoiceRepository.getAllDayBookForwarderStatusAccountData();
		result.addAll(result1);
		result.addAll(result2);
		result.addAll(result3);
		result.addAll(result4);
		result.addAll(result5);

		return new DatatableResponse(result);

	}

	@GetMapping(value = "/payment-data")
	@ResponseBody
	public DatatableResponse getApproveDayBook(@RequestParam("flag") final Integer flag) {
		List<ApprovePaymentsDto> result = new ArrayList<>();
		List<TStoragePhotosApprovalDto> storageResult = new ArrayList<>();
		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.PAYMENT_APPROVED,
				Constants.PAYMENT_PARTIAL);
		if (flag == null) {
			result = new ArrayList<>();
		} else if (flag == 0) {
			final List<Integer> invoiceStatus = Arrays.asList(Constants.INV_STATUS_VERIFIED,
					Constants.INV_CANCEL_CHARGE_UPDATED);
			result = purchaseInvoiceRepository.findAllByPaymentApprove(invoiceStatus, paymentApprovedStatus);
		} else if (flag == 1) {
			result = transportInvoiceRepository.findAllByPaymentApprove(paymentApprovedStatus);
		} else if (flag == 2) {
			storageResult = tFwdrInvoiceRepository.findAllByPaymentApprove(paymentApprovedStatus);
		} else if (flag == 3) {
			result = tFreightShippingInvoiceRepository.findAllByPaymentApprove(paymentApprovedStatus);
		} else if (flag == 4) {
			result = tInvoiceRepository.findAllByPaymentApprove(paymentApprovedStatus);
		} else if (flag == 5) {
			result = inspectionInvoiceRepository
					.findAllByPaymentStatus(Arrays.asList(Constants.INSPECTION_PAYMENT_INVOICE_PROCESSING,
							Constants.INSPECTION_PAYMENT_INVOICE_PROCESSING_PARTIAL));
		}
		return new DatatableResponse(!AppUtil.isObjectEmpty(result) ? result : storageResult);
	}

	@Transactional
	@PostMapping("/approve/payment/auction/upload/invoice")
	public ResponseEntity<Response> a(@RequestParam("invoiceFile") final MultipartFile file,
			@RequestParam("id") final String invoiceNo) throws ParseException {
		// upload file
		final String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file, Constants.ATTACHMENT_DIRECTORY_AUCTION_INVOICE,
				"upload");
		final List<TPurchaseInvoice> invoices = purchaseInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TPurchaseInvoice tPurchaseInvoice : invoices) {
			tPurchaseInvoice.setInvoiceAttachmentFilename(fileName);
			tPurchaseInvoice.setInvoiceAttachmentDiskFilename(diskFileName);
			tPurchaseInvoice.setInvoiceUpload(Constants.INVOICE_UPLOADED);
			tPurchaseInvoice.setAttachementViewed(Constants.ATTACHMENT_NOT_VIEWED);
		}
		purchaseInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/approve/payment/shipping/invoice/upload")
	public ResponseEntity<Response> shippingInvoiceUpload(@RequestParam("invoiceFile") final MultipartFile file,
			@RequestParam("id") final String invoiceNo) throws ParseException {
		// upload file
		final String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file, Constants.ATTACHMENT_DIRECTORY_SHIPPING_INVOICE,
				"upload");
		final List<TFreightShippingInvoice> invoices = tFreightShippingInvoiceRepository.findAllByinvoiceNo(invoiceNo);
		for (final TFreightShippingInvoice invoice : invoices) {
			invoice.setInvoiceAttachmentFilename(fileName);
			invoice.setInvoiceAttachmentDiskFilename(diskFileName);
			invoice.setInvoiceUpload(Constants.INVOICE_UPLOADED);
			invoice.setAttachementViewed(Constants.ATTACHMENT_NOT_VIEWED);
		}
		tFreightShippingInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/approve/payment/auction")
	public ResponseEntity<Response> approveAuctionPayment(
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") final Date dueDate,
			@RequestParam("id[]") final List<String> invoiceNos) {
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		final List<Integer> status = Arrays.asList(Constants.INV_STATUS_VERIFIED, Constants.INV_CANCEL_CHARGE_UPDATED);
		final List<TPurchaseInvoice> invoices = purchaseInvoiceRepository
				.findAllByInvoiceNoInAndPaymentApproveAndStatusIn(invoiceNos, Constants.PAYMENT_NOT_APPROVED, status);
		Double totalSupplierCreditAmount = 0.0;
		for (final TPurchaseInvoice tPurchaseInvoice : invoices) {

			final ApprovePaymentDetails approvePaymentDetails = new ApprovePaymentDetails();
			tPurchaseInvoice.setDueDate(dueDate);
			tPurchaseInvoice.setPaymentApprove(Constants.PAYMENT_APPROVED);
			tPurchaseInvoice.setApprovedBy(loggedInUser.getUserId());
			tPurchaseInvoice.setApprovedDate(new Date());
			approvePaymentDetails.setApprovedDate(new Date());
			totalSupplierCreditAmount += tPurchaseInvoice.getTotalTaxIncluded();
		}
		final TPurchaseInvoice aucInvoice = invoices.get(0);

		final TSupplierTransaction transaction = new TSupplierTransaction();
//		transaction.setStockNo(aucInvoice.getStockNo());
		transaction.setInvoiceNo(aucInvoice.getInvoiceNo());
		transaction.setSupplierCode(aucInvoice.getInvoiceName());
		transaction.setTransactionType(Constants.TRANSACTION_CREDIT);
		transaction.setAmount(totalSupplierCreditAmount);

		supplierService.supplierTransactionEntry(transaction);

		purchaseInvoiceService.paymentApproveTransactions(invoices);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/approve/auction")
	@Transactional
	public ResponseEntity<Response> approveAuction(@RequestParam("id[]") final List<String> invoiceNos,
			@RequestBody final Map<String, String> data) throws ParseException {
		List<String> invoiceNunms = new ArrayList<>(invoiceNos);
		final String bank = data.get("bank");
		final String sApprovedDate = data.get("approvedDate");
		final String remarks = data.get("remarks");
		Date approvedDate = null;
		if (!AppUtil.isObjectEmpty(sApprovedDate)) {
			approvedDate = new SimpleDateFormat("dd-MM-yyyy").parse(sApprovedDate);
		}
		// fetch records
		final List<Integer> invoiceStatus = Arrays.asList(Constants.INV_STATUS_VERIFIED,
				Constants.INV_CANCEL_CHARGE_UPDATED);
		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.PAYMENT_APPROVED,
				Constants.PAYMENT_PARTIAL);
		final List<TInvoicePaymentTransaction> invoiceTransaction = new ArrayList<>();
		Double bankTransactionTotalAmount = 0.0;
		for (final String invoice : invoiceNos) {
			final List<TPurchaseInvoice> invoices = purchaseInvoiceRepository
					.findAllByInvoiceNoAndPaymentApproveInAndStatusIn(invoice, paymentApprovedStatus, invoiceStatus);
			// calculate invoice total
			final Double invoiceTotal = invoices.stream().mapToDouble(TPurchaseInvoice::getTotalTaxIncluded).sum();

			// get total paid amount
			final Double totalPaidAmount = invoicePaymentTransactionService
					.getTotalTransactionAmountForAuction(invoice);
			final Double balanceToPay = invoiceTotal - totalPaidAmount;
			// set amount
			final Double amount = invoiceNos.size() > 1 ? balanceToPay : Double.parseDouble(data.get("amount"));
			if (amount > Math.round(balanceToPay)) {
				throw new AAJRuntimeException("Amount is greater than balance.");
			}
			final Double amountAfterTransaction = totalPaidAmount + amount;
			Integer paymentStatus;
			if (amountAfterTransaction >= invoiceTotal) {
				paymentStatus = Constants.PAYMENT_COMPLETED;
			} else {
				paymentStatus = Constants.PAYMENT_PARTIAL;
			}
			for (final TPurchaseInvoice tPurchaseInvoice : invoices) {
				tPurchaseInvoice.setPaymentApprove(paymentStatus);
				tPurchaseInvoice.setInvoiceAmountReceived(amountAfterTransaction);
			}
			// invoice payment transaction
			final TInvoicePaymentTransaction invoicePaymentTransaction = new TInvoicePaymentTransaction(
					Constants.INVOICE_TYPE_PURCHASE, invoice, bank, amount, approvedDate, remarks,
					Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED);

			purchaseInvoiceRepository.saveAll(invoices);
			invoiceTransaction.add(invoicePaymentTransaction);
			bankTransactionTotalAmount += amount;

		}
		final String invoiceNosString = String.join(" | ", invoiceNos);
		// bank transaction
		final String paymentVoucherNo = bankTransactionService.bankTransactionEntry(bank, bankTransactionTotalAmount,
				Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT, "purchase invoice payment", invoiceNosString);
		// invoice transaction
		for (final TInvoicePaymentTransaction invoicePaymentTransaction : invoiceTransaction) {
			invoicePaymentTransaction.setPaymentVoucherNo(paymentVoucherNo);
			invoicePaymentTransactionService.saveInvoicePaymentTransaction(invoicePaymentTransaction);
		}

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/approve/others")
	@Transactional
	public ResponseEntity<Response> approveOthers(@RequestParam("id[]") final List<String> invoiceNos,
			@RequestBody final Map<String, String> data) throws ParseException {
		final String bank = data.get("bank");
		final String sApprovedDate = data.get("approvedDate");
		final String remarks = data.get("remarks");

		Date approvedDate = null;
		if (!AppUtil.isObjectEmpty(sApprovedDate)) {
			approvedDate = new SimpleDateFormat("dd-MM-yyyy").parse(sApprovedDate);
		}
		Double amount = Double.parseDouble(data.get("amount"));
		// fetch records
		boolean processingMultipleInvoice = invoiceNos.size() > 1;
		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.PAYMENT_APPROVED,
				Constants.PAYMENT_PARTIAL);
		final List<TInvoicePaymentTransaction> invoiceTransaction = new ArrayList<>();
		Double bankTransactionTotalAmount = 0.0;
		for (String invoiceNo : invoiceNos) {
			final List<TInvoice> invoices = tInvoiceRepository.findAllByInvoiceNoAndPaymentApproveIn(invoiceNo,
					paymentApprovedStatus);
			// calculate invoice total
			final Double invoiceTotal = invoices.stream().mapToDouble(TInvoice::getTaxIncludedAmount).sum();
			// get total paid amount
			final Double totalPaidAmount = invoicePaymentTransactionService
					.getTotalTransactionAmount(Constants.INVOICE_TYPE_OTHERS, invoiceNo);
			final Double balanceToPay = invoiceTotal - totalPaidAmount;
			if (processingMultipleInvoice) {
				amount = balanceToPay;
			}
			if (amount > Math.round(balanceToPay)) {
				throw new AAJRuntimeException("Amount is greater than balance.");
			}
			final Double amountAfterTransaction = totalPaidAmount + amount;
			Integer paymentStatus;
			if (amountAfterTransaction >= invoiceTotal) {
				paymentStatus = Constants.PAYMENT_COMPLETED;
			} else {
				paymentStatus = Constants.PAYMENT_PARTIAL;
			}
			for (final TInvoice tInvoice : invoices) {
				final ApprovePaymentDetails approvePaymentDetails = new ApprovePaymentDetails(bank, approvedDate,
						remarks);
				tInvoice.setApprovePaymentDetails(approvePaymentDetails);
				tInvoice.setPaymentApprove(paymentStatus);
				tInvoice.setInvoiceAmountReceived(amountAfterTransaction);

			}
			this.tInvoiceRepository.saveAll(invoices);
			// invoice payment transaction
			TInvoicePaymentTransaction invoicePaymentTransaction = new TInvoicePaymentTransaction(
					Constants.INVOICE_TYPE_OTHERS, invoiceNo, bank, amount, approvedDate, remarks,
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

	@Transactional
	@PostMapping("/approve/payment/transport/invoice/upload")
	public ResponseEntity<Response> transportInvoiceUpload(@RequestParam("invoiceFile") final MultipartFile file,
			@RequestParam("invoiceRefNo") final String invoiceRefNo, @RequestParam("refNo") final String refNo)
			throws ParseException {
		final String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file, Constants.ATTACHMENT_DIRECTORY_TRANSPORT_INVOICE,
				"upload");
		final List<TTransportInvoice> invoices = transportInvoiceRepository.findAllByInvoiceRefNoAndRefNo(invoiceRefNo,
				refNo);
		for (final TTransportInvoice tTransportInvoice : invoices) {
			tTransportInvoice.setInvoiceAttachmentFilename(fileName);
			tTransportInvoice.setInvoiceAttachmentDiskFilename(diskFileName);
			tTransportInvoice.setInvoiceUpload(Constants.INVOICE_UPLOADED);
			tTransportInvoice.setAttachementViewed(Constants.ATTACHMENT_NOT_VIEWED);
		}
		transportInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/approve/payment/others/invoice/upload")
	public ResponseEntity<Response> othersInvoiceUpload(@RequestParam("invoiceFile") final MultipartFile file,
			@RequestParam("code") final String code) {
		final String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file,
				Constants.ATTACHMENT_DIRECTORY_OTHER_BANK_STATEMENT, "upload");
		final TInvoicePaymentTransaction invoice = invoicePaymentTransactionRepository.findOneByCode(code);

		invoice.setBankStatementAttachmentFilename(fileName);
		invoice.setBankStatementAttachmentDiskFilename(diskFileName);

		invoicePaymentTransactionRepository.save(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/approve/payment/shipping/statement/upload")
	public ResponseEntity<Response> othersStatementUpload(@RequestParam("invoiceFile") final MultipartFile file,
			@RequestParam("code") final String code) {
		final String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file,
				Constants.ATTACHMENT_DIRECTORY_FREIGHT_BANK_STATEMENT, "upload");
		final TInvoicePaymentTransaction invoice = invoicePaymentTransactionRepository.findOneByCode(code);

		invoice.setBankStatementAttachmentFilename(fileName);
		invoice.setBankStatementAttachmentDiskFilename(diskFileName);

		invoicePaymentTransactionRepository.save(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/approve/payment/transport/statement/upload")
	public ResponseEntity<Response> transportBankStatementUpload(@RequestParam("invoiceFile") final MultipartFile file,
			@RequestParam("code") final String code) {
		final String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file,
				Constants.ATTACHMENT_DIRECTORY_TRANSPORT_BANK_STATEMENT, "upload");

		final TInvoicePaymentTransaction invoice = invoicePaymentTransactionRepository.findOneByCode(code);

		invoice.setBankStatementAttachmentFilename(fileName);
		invoice.setBankStatementAttachmentDiskFilename(diskFileName);

		invoicePaymentTransactionRepository.save(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/approve/payment/transport")
	public ResponseEntity<Response> approveTransportPayment(
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") final Date dueDate,
			@RequestParam("invoiceRefNo") final String invoiceRefNo, @RequestParam("refNo") final String refNo) {
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		final List<TTransportInvoice> invoices = transportInvoiceRepository
				.findAllByInvoiceRefNoAndRefNoAndPaymentApprove(invoiceRefNo, refNo, Constants.PAYMENT_NOT_APPROVED);
		for (final TTransportInvoice tTransportInvoice : invoices) {
			tTransportInvoice.setDueDate(dueDate);
			tTransportInvoice.setApprovedBy(loggedInUser.getUserId());
			tTransportInvoice.setApprovedDate(new Date());
			tTransportInvoice.setPaymentApprove(Constants.PAYMENT_APPROVED);
			tTransportInvoice.setStatus(Constants.TRANSPORT_INVOICE_BOOKED);
		}

		// account transaction entry
		transportInvoiceService.transportInvoicePaymentApproveTransactions(invoices);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/approve/payment/transport/mismatch")
	public ResponseEntity<Response> approveTransportPaymentMismatch(
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") final Date dueDate,
			@RequestParam("invoiceRefNo") final String invoiceRefNo, @RequestParam("refNo") final String refNo) {
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		final List<TTransportInvoice> invoices = transportInvoiceRepository
				.findAllByInvoiceRefNoAndRefNoAndPaymentApprove(invoiceRefNo, refNo, Constants.PAYMENT_NOT_APPROVED);
		for (final TTransportInvoice tTransportInvoice : invoices) {
			tTransportInvoice.setDueDate(dueDate);
			tTransportInvoice.setApprovedBy(loggedInUser.getUserId());
			tTransportInvoice.setApprovedDate(new Date());
			tTransportInvoice.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
			tTransportInvoice.setStatus(Constants.TRANSPORT_INVOICE_MISMATCH_AMOUNT_APPROVED);
		}

		// account transaction entry
		transportInvoiceRepository.saveAll(invoices);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/approve/transport")
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

		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.PAYMENT_APPROVED,
				Constants.PAYMENT_PARTIAL);
		final List<TInvoicePaymentTransaction> invoiceTransaction = new ArrayList<>();
		Double bankTransactionTotalAmount = 0.0;
		for (String invoiceNo : invoiceNos) {
			final List<TTransportInvoice> invoices = transportInvoiceRepository
					.findAllByInvoiceRefNoAndPaymentApproveIn(invoiceNo, paymentApprovedStatus);
			// calculate invoice total
			final Double invoiceTotal = invoices.stream().mapToDouble(TTransportInvoice::getInvoiceTotal).sum();
			// get total paid amount
			final Double totalPaidAmount = invoicePaymentTransactionService
					.getTotalTransactionAmount(Constants.INVOICE_TYPE_TRANSPORT, invoiceNo);
			final Double balanceToPay = invoiceTotal - totalPaidAmount;
			if (processingMultipleInvoice) {
				amount = balanceToPay;
			}
			if (amount > Math.round(balanceToPay)) {
				throw new AAJRuntimeException("Amount is greater than balance.");
			}
			final Double amountAfterTransaction = totalPaidAmount + amount;
			Integer paymentStatus;
			if (amountAfterTransaction >= invoiceTotal) {
				paymentStatus = Constants.PAYMENT_COMPLETED;
			} else {
				paymentStatus = Constants.PAYMENT_PARTIAL;
			}

			for (final TTransportInvoice tTransportInvoice : invoices) {
				final ApprovePaymentDetails approvePaymentDetails = new ApprovePaymentDetails(bank, approvedDate,
						remarks);
				tTransportInvoice.setApprovePaymentDetails(approvePaymentDetails);
				tTransportInvoice.setPaymentApprove(paymentStatus);
				tTransportInvoice.setInvoiceAmountReceived(amountAfterTransaction);
			}
			this.transportInvoiceRepository.saveAll(invoices);
			// invoice payment transaction
			TInvoicePaymentTransaction invoicePaymentTransaction = new TInvoicePaymentTransaction(
					Constants.INVOICE_TYPE_TRANSPORT, invoiceNo, bank, amount, approvedDate, remarks,
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

	@Transactional
	@PostMapping("/approve/payment/other/invoice/upload")
	public ResponseEntity<Response> otherInvoiceUpload(@RequestParam("invoiceFile") final MultipartFile file,
			@RequestParam("id") final String invoiceNo) throws ParseException {
		final String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file, Constants.ATTACHMENT_DIRECTORY_OTHER_INVOICE,
				"upload");
		final List<TInvoice> invoices = tInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TInvoice invoice : invoices) {
			invoice.setInvoiceAttachmentFilename(fileName);
			invoice.setInvoiceAttachmentDiskFilename(diskFileName);
			invoice.setInvoiceUpload(Constants.INVOICE_UPLOADED);
			invoice.setAttachementViewed(Constants.ATTACHMENT_NOT_VIEWED);
		}
		tInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/approve/payment/storageandphotos/invoice/upload")
	public ResponseEntity<Response> forwarderInvoiceUpload(@RequestParam("invoiceFile") final MultipartFile file,
			@RequestParam("id") final String invoiceNo) throws ParseException {
		final String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file, Constants.ATTACHMENT_DIRECTORY_FORWARDER_INVOICE,
				"upload");
		final List<TFwdrInvoice> fwdrinvoices = tFwdrInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		for (final TFwdrInvoice invoice : fwdrinvoices) {
			invoice.setInvoiceAttachmentFilename(fileName);
			invoice.setInvoiceAttachmentDiskFilename(diskFileName);
			invoice.setInvoiceUpload(Constants.INVOICE_UPLOADED);
			invoice.setAttachementViewed(Constants.ATTACHMENT_NOT_VIEWED);
		}
		tFwdrInvoiceRepository.saveAll(fwdrinvoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/approve/payment/others")
	public ResponseEntity<Response> approveOtherPayment(@RequestParam("id[]") final List<String> invoiceNos,
			@RequestBody final Map<String, String> data) throws ParseException {
		final String sDueDate = data.get("dueDate");
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		Date dueDate = null;
		if (!AppUtil.isObjectEmpty(sDueDate)) {
			dueDate = new SimpleDateFormat("dd-MM-yyyy").parse(sDueDate);
		}
		final List<TInvoice> invoices = tInvoiceRepository.findAllByInvoiceNoInAndPaymentApprove(invoiceNos,
				Constants.PAYMENT_NOT_APPROVED);

		for (final TInvoice invoice : invoices) {
			final ApprovePaymentDetails approvePaymentDetails = new ApprovePaymentDetails();
			invoice.setDueDate(dueDate);
			invoice.setPaymentApprove(Constants.PAYMENT_APPROVED);
			invoice.setApprovedDate(new Date());
			invoice.setApprovedBy(loggedInUser.getUserId());
			approvePaymentDetails.setApprovedDate(new Date());

		}
		tInvoiceRepository.saveAll(invoices);
		// account transaction
		invoiceService.othersInvoicePaymentApproveTransactions(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/approve/payment/storageAndPhoto")
	public ResponseEntity<Response> approvePaymentStorageAndPhoto(@RequestParam("id[]") final List<String> invoiceNos,
			@RequestBody final Map<String, String> data) throws ParseException {
		final String sApprovedDate = data.get("approvedDate");
		final String bank = data.get("bank");
		final String remarks = data.get("remarks");
		Date approvedDate = null;
		if (!AppUtil.isObjectEmpty(sApprovedDate)) {
			approvedDate = new SimpleDateFormat("dd-MM-yyyy").parse(sApprovedDate);
		}
		Double amount = Double.parseDouble(data.get("amount"));
		Integer currency = 1;
		Double exchangeRate = 1.0;
		// fetch records
		boolean processingMultipleInvoice = invoiceNos.size() > 1;

		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.PAYMENT_APPROVED,
				Constants.PAYMENT_PARTIAL);
		final List<TInvoicePaymentTransaction> invoiceTransaction = new ArrayList<>();
		Double bankTransactionTotalAmount = 0.0;
		for (String invoiceNo : invoiceNos) {
			final List<TFwdrInvoice> invoices = tFwdrInvoiceRepository.findAllByInvoiceNoAndPaymentApproveIn(invoiceNo,
					paymentApprovedStatus);
			// calculate invoice total
			final Double invoiceTotal = invoices.stream().findFirst().get().getTotalAmount();
			currency = invoices.stream().findFirst().get().getCurrency();
			exchangeRate = invoices.stream().findFirst().get().getExchangeRateValue();
			// get total paid amount
			final Double totalPaidAmount = invoicePaymentTransactionService
					.getTotalTransactionAmount(Constants.INVOICE_TYPE_FORWARDER, invoiceNo);
			final Double balanceToPay = invoiceTotal - totalPaidAmount;
			if (processingMultipleInvoice) {
				amount = balanceToPay;
			}
			if (amount > Math.round(balanceToPay)) {
				throw new AAJRuntimeException("Amount is greater than balance.");
			}
			final Double amountAfterTransaction = totalPaidAmount + amount;
			Integer paymentStatus;
			if (amountAfterTransaction >= invoiceTotal) {
				paymentStatus = Constants.PAYMENT_COMPLETED;
			} else {
				paymentStatus = Constants.PAYMENT_PARTIAL;
			}
			for (final TFwdrInvoice fwdrinvoice : invoices) {
				fwdrinvoice.setDueDate(approvedDate);
				fwdrinvoice.setPaymentApprove(paymentStatus);
				fwdrinvoice.setInvoiceAmountReceived(amountAfterTransaction);

			}
			this.tFwdrInvoiceRepository.saveAll(invoices);
			// invoice payment transaction
			TInvoicePaymentTransaction invoicePaymentTransaction = new TInvoicePaymentTransaction(
					Constants.INVOICE_TYPE_FORWARDER, invoiceNo, bank, amount, approvedDate, remarks,
					Constants.INVOICE_PAYMENT_TRANSACTION_NOT_CANCELLED);
			invoiceTransaction.add(invoicePaymentTransaction);
			bankTransactionTotalAmount += (currency != 1) ? (amount * exchangeRate) : amount;

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

	@PostMapping("/approve/storageandphoto")
	public ResponseEntity<Response> approveStorageAndPhoto(@RequestParam("id[]") final List<String> invoiceNos,
			@RequestBody final Map<String, Object> data) throws ParseException {
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		Date dueDate = null;
		final String dueDateString = AppUtil.isObjectEmpty(data.get("dueDate")) ? "" : data.get("dueDate").toString();
		if (!AppUtil.isObjectEmpty(dueDateString)) {
			dueDate = new SimpleDateFormat("dd-MM-yyyy").parse(dueDateString);
		}

		final List<TFwdrInvoice> fwdrinvoices = tFwdrInvoiceRepository.findAllByInvoiceNoInAndPaymentApprove(invoiceNos,
				Constants.PAYMENT_NOT_APPROVED);

		for (final TFwdrInvoice fwdrinvoice : fwdrinvoices) {
			fwdrinvoice.setDueDate(dueDate);
			fwdrinvoice.setPaymentApprove(Constants.PAYMENT_APPROVED);
			fwdrinvoice.setApprovedDate(new Date());
			fwdrinvoice.setApprovedBy(loggedInUser.getUserId());

		}

		// account transaction entry
		tForwarderInvoiceService.forwarderInvoicePaymentApproveTransactions(fwdrinvoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/approve/payment/freightShipping")
	public ResponseEntity<Response> approveFreightShipping(@RequestParam("id[]") final List<String> invoiceNos,
			@RequestBody final Map<String, String> data) throws ParseException {
		final String bank = data.get("bank");
		final String sApprovedDate = data.get("approvedDate");
		final String remarks = data.get("remarks");
		Date approvedDate = null;
		final Double amount = Double.parseDouble(data.get("amount"));
		if (!AppUtil.isObjectEmpty(sApprovedDate)) {
			approvedDate = new SimpleDateFormat("dd-MM-yyyy").parse(sApprovedDate);
		}
		final Integer paymentCurrency = AppUtil.isObjectEmpty(data.get("paymentType")) ? Constants.CURRENCY_YEN
				: Integer.parseInt(data.get("paymentType"));
		// fetch records
		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.PAYMENT_APPROVED,
				Constants.PAYMENT_PARTIAL);

		for (String invoiceNo : invoiceNos) {
			final List<TFreightShippingInvoice> invoices = tFreightShippingInvoiceRepository
					.findAllByInvoiceNoAndPaymentApproveIn(invoiceNo, paymentApprovedStatus);

			if (paymentCurrency != null && paymentCurrency.equals(Constants.RORO_INVOICE_CURRENCY_TYPE_YEN)) {
				tFreightInvoiceService.approveRoroInvoicePayment(invoices, bank, approvedDate, remarks, amount);
			} else {

				tFreightInvoiceService.approveRoroFreightInvoicePayment(invoices, bank, approvedDate, remarks, amount,
						paymentCurrency);
			}
		}

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/approve/payment/freightShipping/roro")
	@Transactional
	public ResponseEntity<Response> approveFreightShippingRoRo(@RequestBody ROROApprovePaymentsDto data) {
		List<String> ids = data.getIds();
		String bank = data.getBank();
		Date approvedDate = data.getApprovedDate();
		String remarks = data.getRemarks();
		Integer paymentType = data.getPaymentType();
		List<Document> items = data.getItems();
		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.PAYMENT_APPROVED,
				Constants.PAYMENT_PARTIAL);
		final List<TFreightShippingInvoice> invoices = tFreightShippingInvoiceRepository
				.findAllByIdInAndPaymentApproveIn(ids, paymentApprovedStatus);

		if (paymentType.equals(1)) {
			final Double freightAmount = invoices.stream()
					.filter(invoice -> invoice.getFreightPaymentStatus()
							.equals(Constants.SHIPPING_RORO_INVOICE_FREIGHT_PAYMENT_NOT_DONE))
					.mapToDouble(TFreightShippingInvoice::getFreightCharge).sum();
			if (freightAmount == 0) {
				throw new AAJRuntimeException("freight amount should be grater than zero.");
			}
			invoices.forEach(invoice -> {
				invoice.setPaymentApprove(Constants.PAYMENT_PARTIAL);
				invoice.setFreightPaymentStatus(Constants.SHIPPING_RORO_INVOICE_FREIGHT_PAYMENT_DONE);
			});
			tFreightInvoiceService.completePayment(invoices, bank, freightAmount, remarks, approvedDate);
		} else if (paymentType.equals(2)) {
			final Double freightAmountUsd = invoices.stream()
					.filter(invoice -> invoice.getFreightPaymentStatus()
							.equals(Constants.SHIPPING_RORO_INVOICE_FREIGHT_PAYMENT_NOT_DONE))
					.mapToDouble(TFreightShippingInvoice::getFreightChargeUsd).sum();
			if (freightAmountUsd == 0) {
				throw new AAJRuntimeException("freight amount should be grater than zero.");
			}
			invoices.forEach(invoice -> {
				invoice.setPaymentApprove(Constants.PAYMENT_PARTIAL);
				invoice.setFreightPaymentStatus(Constants.SHIPPING_RORO_INVOICE_FREIGHT_PAYMENT_DONE);
			});
			tFreightInvoiceService.completePaymentFreightInvoice(invoices, bank, freightAmountUsd, remarks,
					approvedDate, invoices.get(0).getExchangeRate(), Constants.CURRENCY_USD);
		} else if (paymentType.equals(3)) {
			final Double freightAmount = invoices.stream()
					.filter(invoice -> invoice.getFreightPaymentStatus()
							.equals(Constants.SHIPPING_RORO_INVOICE_FREIGHT_PAYMENT_NOT_DONE))
					.mapToDouble(TFreightShippingInvoice::getFreightCharge).sum();
			final Double withoutFreightAmount = invoices.stream()
					.mapToDouble(TFreightShippingInvoice::getTotalWithoutFreight).sum();
			final Double amount = AppUtil.ifNullOrEmpty(freightAmount, 0.0)
					+ AppUtil.ifNullOrEmpty(withoutFreightAmount, 0.0);
			invoices.forEach(invoice -> {
				invoice.setPaymentApprove(Constants.PAYMENT_COMPLETED);
				invoice.setFreightPaymentStatus(Constants.SHIPPING_RORO_INVOICE_FREIGHT_PAYMENT_DONE);
			});
			tFreightInvoiceService.completePayment(invoices, bank, amount, remarks, approvedDate);
		}

		// update bl status
		items.forEach(item -> {
			final String blStatus = item.getString("blStatus");
			if (!AppUtil.isObjectEmpty(blStatus)) {
				TFreightShippingInvoice shippingInvoice = tFreightShippingInvoiceRepository
						.findOneById(item.getString("invoiceId"));
				TShippingRequest shippingRequest = shippingRequestRepository
						.findOneByShipmentRequestId(shippingInvoice.getShipmentRequestId());

				if (blStatus.equalsIgnoreCase("1")) {
					shippingRequest.setRecSurStatus(Constants.SHIPIING_REQUEST_REC_SUR_STATUS_RECEIVE);
				} else if (blStatus.equalsIgnoreCase("2")) {
					shippingRequest.setRecSurStatus(Constants.SHIPIING_REQUEST_REC_SUR_STATUS_SURRENDER);
				} else if (blStatus.equalsIgnoreCase("3")) {
					shippingRequest.setRecSurStatus(Constants.SHIPIING_REQUEST_REC_SUR_STATUS_HOLD);
				}
				shippingRequestRepository.save(shippingRequest);
			}

		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	// Add New Payment
	@GetMapping(value = { "/add-payment" })
	public ModelAndView addNewPaymnet(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject("mCurrency", currencyRepository.findAll());
		modelAndView.addObject("mRemitType", remitTypeRep.findAll());
		modelAndView.addObject("mCoa", mCoaRepo.findAll());
		modelAndView.setViewName("accounts.add-payment");
		return modelAndView;
	}

	// Payment Completed
	@GetMapping("/auction-payment-done")
	public ModelAndView auctionPaymentDone() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.auction-payment-done");
		return modelAndView;
	}

	// Payment Freezed
	@GetMapping("/auction-payment-freezed")
	public ModelAndView auctionPaymentFreezed() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.auction-payment-freeze");
		return modelAndView;
	}

	// others Freezed
	@GetMapping("/payment-others-freezed")
	public ModelAndView othersPaymentFreezed() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payment-others-freeze");
		return modelAndView;
	}

	// Freight Shipping Freezed
	@GetMapping("/payment-freight-freezed")
	public ModelAndView freightPaymentFreezed() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payment-freight-freeze");
		return modelAndView;
	}

	// transport Freezed
	@GetMapping("/payment-transport-freezed")
	public ModelAndView transportPaymentFreezed() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payment-transport-freeze");
		return modelAndView;
	}

	// others Freezed
	@GetMapping("/payment-storagePhotos-freezed")
	public ModelAndView storagePhotosPaymentFreezed() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payment-storagePhotos-freeze");
		return modelAndView;
	}

	@GetMapping("/payment-transport-done")
	public ModelAndView paymentTransportDone() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payment-transport-done");
		return modelAndView;
	}

	@GetMapping("/payment-freight-done")
	public ModelAndView paymentFreightDone() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payment-freight-done");
		return modelAndView;
	}

	@GetMapping("/payment-others-done")
	public ModelAndView paymentOthersDone() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payment-others-done");
		return modelAndView;
	}

	@GetMapping("/payment-storage-photos-done")
	public ModelAndView paymentStorageDone() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.payment-storage-photos-done");
		return modelAndView;
	}

	@GetMapping(value = "/settled")
	@ResponseBody
	public DatatableResponse getSettledPayment() {
		return new DatatableResponse(purchaseInvoiceRepository.findAllByPaymentStatus("done"));
	}

	@GetMapping(value = { "/daybook-entry" })
	public ModelAndView dayBookEtry(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject("mbank", masterBankRepository.findAll());
		// modelAndView.addObject("mCurrency", currencyRepository.findAll());
		modelAndView.addObject("mRemitType", remitTypeRep.findAll());
		modelAndView.addObject("mCoa", mCoaRepo.findAll());
		modelAndView.setViewName("accounts.daybook-entry");

		return modelAndView;
	}

	@GetMapping(value = { "/daybook-list" })
	public ModelAndView dayBookList(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.addObject("mCurrency", currencyRepository.findAll());
		modelAndView.addObject("mRemitType", remitTypeRep.findAll());
		modelAndView.addObject("boe", billOfExchange.findAll());
		modelAndView.addObject("mCoa", mCoaRepo.findAll());
		modelAndView.setViewName("accounts.daybook-list");

		return modelAndView;
	}

	@GetMapping("/daybook-data")
	@ResponseBody
	public DatatableResponse getDayBookData() {
		return new DatatableResponse(tdayBookTransactionRepository.listDayBookEntry());
	}

	@PostMapping("/delete/daybook")
	public ResponseEntity<Response> deleteDaybook(@RequestBody final Map<String, Object> data) {

		final String id = (String) data.get("id");
		TDayBook dayBook = dayBookRepository.findOneByid(id);
		dayBook.setStatus(Constants.DAYBOOK_ENTRY_REMOVED);
		dayBookRepository.save(dayBook);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@PostMapping("/update/daybook")
	@ResponseBody
	public ResponseEntity<Response> updateDaybook(@RequestBody final Map<String, Object> data) throws ParseException {
		final String id = AppUtil.ifNull(data.get("id"), "").toString();
		Date remitDate = null;
		System.out.println(Integer.parseInt(data.get("remitType").toString()));
		final String remitDateString = AppUtil.isObjectEmpty(data.get("remitDate")) ? ""
				: data.get("remitDate").toString();

		if (!AppUtil.isObjectEmpty(remitDateString)) {
			remitDate = new SimpleDateFormat("dd-MM-yyyy").parse(remitDateString);
			remitDate = AppUtil.appendTime(remitDate);
		}
		final TDayBook dayBook = dayBookRepository.findOneByid(id);
		dayBook.setClearingAccount(
				Integer.parseInt(data.get("clearingAccount").toString()) == 1 ? Constants.DAY_BOOK_CLEARING_ACCOUNT
						: Constants.DAY_BOOK_NOT_CLEARED_ACCOUNT);
		dayBook.setIsCustomerBankCharge(
				Integer.parseInt(data.get("customerBankCharge").toString()) == 1 ? Constants.IS_AAJ_PAYABLE_YES
						: Constants.IS_AAJ_PAYABLE_N0);
		dayBook.setAmount(Double.parseDouble(data.get("amount").toString()));
		dayBook.setAmountWithOutBankCharge(Double.parseDouble(data.get("amountWithOutBankCharge").toString()));
		dayBook.setRemitDate(remitDate);
		dayBook.setBankCharges(Double.parseDouble(data.get("bankCharges").toString()));
		dayBook.setRemitter(data.get("remitter").toString());
		dayBook.setRemitType(Integer.parseInt(data.get("remitType").toString()));
		dayBook.setBank(data.get("bank").toString());
		if (Integer.parseInt(data.get("remitType").toString()) == 4) {
			dayBook.setBillOfExchange(data.get("billOfExchange").toString());
			dayBook.setLcNo(data.get("lcNo").toString());
			dayBook.setSalesPerson(data.get("staff").toString());
			dayBook.setCustomer(data.get("customerId").toString());
		} else {
			dayBook.setBillOfExchange("");
			dayBook.setLcNo("");
			dayBook.setSalesPerson("");
			dayBook.setCustomer("");
		}
		dayBook.setCurrency(Integer.parseInt(data.get("currency").toString()));
		dayBook.setRemarks(data.get("remarks").toString());
		dayBookRepository.save(dayBook);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping("/coa-list")
	public ModelAndView coaList(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mCoa", mCoaRepo.findAll());
		modelAndView.addObject("mCoaType", mCoaTypeRepo.findAll());
		modelAndView.setViewName("accounts.coa-list");

		return modelAndView;
	}

	@GetMapping("/coa-data")
	@ResponseBody
	public DatatableResponse getCoaData() {
		return new DatatableResponse(mCoaRepo.findAllByReportFlag());
	}

	@PostMapping(path = "/create/coa")
	public ResponseEntity<Response> saveCoa(@RequestBody final Map<String, Object> data) throws IOException {
		final MCOA coa = mapper.readValue(mapper.writeValueAsString(data.get("coa")), new TypeReference<MCOA>() {
		});
		coa.setBalance(0.0);
		coa.setStatus(Constants.COA_STATUS_TYPE_NEW);
		mCoaRepo.save(coa);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping(path = "/search/subAccount")
	@ResponseBody
	public List<MCOA> searchSubAccount(@RequestParam("reportingCategory") final String reportingCategory) {
		return mCoaRepo.findSubAccountTypes(reportingCategory);
	}

	@Transactional
	@PostMapping(path = "/create/category")
	public ResponseEntity<Response> saveCategory(@RequestBody final MPaymentCategory data) {
		final String categoryCode = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_CATEGORYTYPE);
		final Long coaCode = data.getCoaCode();
		final String category = data.getCategory();
		final MCOA subAccount = mCoaRepo.findByCode(coaCode);
//		subAccount.setStatus(Constants.COA_STATUS_TYPE_OLD);
//		mCoaRepo.save(subAccount);
		masterPaymentRepository.save(new MPaymentCategory(categoryCode, coaCode, category, subAccount.getTkcCode(),
				subAccount.getTkcDescription()));
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@Transactional
	@PostMapping(path = "/create/newExpense")
	public ResponseEntity<Response> saveCategory(@RequestBody final OtherDirectExpense data) {
		final String categoryCode = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_EXPTYPE);
		final Long coaCode = data.getCoaCode();
		final String category = data.getType();
		otherDirectExpenseRepository.save(new OtherDirectExpense(categoryCode, coaCode, category));
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@Transactional
	@PostMapping(path = "/create/transport/payment/category")
	public ResponseEntity<Response> saveCategory(@RequestBody final TransportPaymentCategory data) {
		final String categoryCode = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_TRANSPORTCATEGORYTYPE);
		data.setCategoryCode(categoryCode);
		transportPaymentRepository.save(data);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping(value = { "/exchange" })
	public ModelAndView exchange(final ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.exchange");
		return modelAndView;
	}

	// Save Add Payment
	@Transactional
	@PostMapping("/save/addpayment")
	public ModelAndView savePayment(@ModelAttribute final AddPaymentDto addPaymentDto, final ModelAndView modelAndView,
			final RedirectAttributes redirectAttributes) throws IOException {
		final List<TPurchaseInvoice> paymentInvoices = new ArrayList<>();

		addPaymentDto.getItems().stream().forEach(p -> {
			final TPurchaseInvoice invoice = new TPurchaseInvoice();
			invoice.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_PRCHSINVC));
			invoice.setInvoiceNo(addPaymentDto.getInvoiceNo());
			invoice.setPaymentType(p.getPaymentType());
			invoice.setInvoiceType(addPaymentDto.getInvoiceType());
			invoice.setRemitTo(addPaymentDto.getRemitTo());
			invoice.setInvoiceDate(addPaymentDto.getInvoiceDate());
			invoice.setDueDate(addPaymentDto.getDueDate());
			invoice.setDescription(p.getDescription());
			invoice.setPurchaseCost(AppUtil.ifNull(p.getPurchaseCost(), 0.0));
			invoice.setStockNo(p.getStockNo());
			invoice.setRemarks(p.getRemarks());
			paymentInvoices.add(invoice);

		});

		purchaseInvoiceRepository.saveAll(paymentInvoices);
		redirectAttributes.addFlashAttribute("message", "Payment added successfully.");
		modelAndView.setViewName("redirect:/accounts/add-payment");
		return modelAndView;
	}

	@PostMapping(path = "/daybookcreate")
	public ModelAndView save(@ModelAttribute final DayBookEntryDto dayBookEntryDto, final ModelAndView modelAndView,
			final RedirectAttributes redirectAttributes) {
		final String bank = dayBookEntryDto.getBank();
		Integer currencyValue = dayBookEntryDto.getCurrency();
		final MCurrency currency = currencyRepository.findOneByCurrencySeq(currencyValue);
		List<TExchangeRate> exchange = tExchageRateRepository
				.findTop3ByOrderByCreatedDate(dayBookEntryDto.getRemitDate());
		final List<TDayBook> dayBookentryList = new ArrayList<>();
		dayBookEntryDto.getItems().stream().forEach(d -> {
			final String daybookId = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_DYBK);
			DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
			String fileName = bank + "_" + daybookId + "_" + dateFormat.format(dayBookEntryDto.getRemitDate()) + "."
					+ FilenameUtils.getExtension(d.getAttachment().getOriginalFilename());
			final String diskFileName = fileStorageService.storeDaybookSlip(d.getAttachment(), fileName,
					Constants.ATTACHMENT_DIRECTORY_DAYBOOK, "upload");
			final TDayBook daybook = new TDayBook();
			daybook.setDaybookId(daybookId);
			// get exchange rate

			if (dayBookEntryDto.getClearingAccount() == null) {
				daybook.setClearingAccount(Constants.DAY_BOOK_NOT_CLEARED_ACCOUNT);
			} else if (dayBookEntryDto.getClearingAccount() == 1) {
				daybook.setClearingAccount(Constants.DAY_BOOK_CLEARING_ACCOUNT);
			}
			if (d.getRemitType() == Constants.DAYBOOK_TRANSACTION_TYPE_CAR_TAX_CLAIMED) {
				daybook.setCartaxClaimedStatus(Constants.TPURCHASEINVOICE_CARTAX_NOT_CLAIMED);
			}
			daybook.setAttachmentFilename(fileName);
			daybook.setAttachmentDiskFilename(diskFileName);
			daybook.setSlipUpload(Constants.SLIP_NOT_UPLOADED);
			daybook.setAttachementViewed(Constants.ATTACHMENT_NOT_VIEWED);
			daybook.setRemitType(d.getRemitType());
			daybook.setCoaNo((Long) AppUtil.ifNull(d.getCoaNo(), 0.0));
			daybook.setExchangeRate(currency.getExchangeRate());
			daybook.setSalesExchangeRate(currency.getSalesExchangeRate());
			daybook.setSpecialExchangeRate(currency.getSpecialExchangeRate());
			if (!AppUtil.isObjectEmpty(exchange)) {
				daybook.setPoundRate(exchange.get(0).getId());
				daybook.setAusDollarRate(exchange.get(1).getId());
				daybook.setDollarRate(exchange.get(2).getId());
			}
			daybook.setBankCharges(AppUtil.ifNull(d.getBankCharges(), 0.0));
			daybook.setRemarks(d.getRemarks());
			daybook.setRemitDate(dayBookEntryDto.getRemitDate());
			daybook.setBank(bank);
			daybook.setCurrency(currencyValue);
			daybook.setRemitter(d.getRemitter());
			daybook.setAmountWithOutBankCharge(AppUtil.ifNull(d.getAmountWithOutBankCharge(), 0.0));
			daybook.setAmount(AppUtil.ifNull(d.getAmount(), 0.0));
			daybook.setOwnedAmount(0.0);
			if (!AppUtil.isObjectEmpty(d.getCustomerBankCharge())
					&& d.getCustomerBankCharge().equals(Constants.IS_AAJ_PAYABLE_YES)) {
				daybook.setIsCustomerBankCharge(Constants.IS_AAJ_PAYABLE_YES);
			} else {
				daybook.setIsCustomerBankCharge(Constants.IS_AAJ_PAYABLE_N0);
			}
			if (d.getRemitType().equals(Constants.DAYBOOK_TRANSACTION_TYPE_LC_REMIT)) {
				if (AppUtil.isObjectEmpty(d.getBillOfExchange())) {
					throw new AAJRuntimeException("BillofExchange number not fount!");
				}
				System.out.println(d.getBillOfExchange());
				final TLcDetails lcDetails = lcDetailsRepository.findOneByBillOfExchangeNo(d.getBillOfExchange());
				final TProformaInvoice proformaInvoice = proformaInvoiceRepository
						.findOneByInvoiceNo(lcDetails.getProformaInvoiceId());
//				daybook.setCurrency(Constants.CURRENCY_YEN);
//				daybook.setRemitter(lcDetails.getCustomerName());
//				daybook.setAmount(lcDetails.getAmount());
				daybook.setBillOfExchange(lcDetails.getBillOfExchangeNo());
				daybook.setLcNo(lcDetails.getLcNo());
				daybook.setCustomer(proformaInvoice.getCustomerId());
				daybook.setSalesPerson(proformaInvoice.getSalesPerson());
			}

			daybook.setStatus(Constants.DAYBOOK_ENTRY_CREATED);
			dayBookentryList.add(daybook);
		});
		tDayBookService.createDayBookEntry(dayBookentryList);

		redirectAttributes.addFlashAttribute("message", "Day Book Entry Added successfully.");
		modelAndView.setViewName("redirect:/accounts/daybook-entry");
		return modelAndView;

	}

	@GetMapping("/create-loan")
	public ModelAndView createloan(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mCurrency", currencyRepository.findAll());
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.setViewName("accounts.createloan");
		return modelAndView;

	}

	@GetMapping("/view-loan")
	public ModelAndView viewloan(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.viewloan");
		return modelAndView;

	}

	@GetMapping(path = "/get/loan/calc")
	public ResponseEntity<Response> loanCalc(@RequestParam("loanAmount") double loanAmount,
			@RequestParam("rateOfInterest") double rateOfInterest, @RequestParam("loanTerm") int loanTerm) {
		loanService.calcEmi(loanAmount, rateOfInterest, loanTerm);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@PostMapping(path = "/createLoan")
	public ResponseEntity<Response> createloansave(@RequestBody TLoanCreateDto loanDto, final ModelAndView modelAndView,
			final RedirectAttributes redirectAttributes) {
		System.out.println(loanDto.getLoanAmount());
		final TLoan loan = new TLoan(loanDto.getLoanType(), loanDto.getBank(), loanDto.getRateOfInterest(),
				loanDto.getLoanTerm(), loanDto.getLoanAmount());
		final String loanId = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_CREATE_LOAN);
		System.out.println(loanId);
//		double totalPayable = loanDto.getMonthlyEmi() * loanDto.getLoanTerm();
		loan.setLoanId(loanId);
		loan.setDate(loanDto.getDate());
		loan.setReference(loanDto.getReference());
		loan.setDueDate(loanDto.getDueDate());
		loan.setLeaveDay(loanDto.getLeaveDay());
		loan.setSavingAccount(loanDto.getSavingAccount());
		if (loan.getSavingAccount() == 1) {
			loan.setSavingsAccountAmount(loanDto.getSavingsAccountAmount());
			loan.setSavingsBankAccount(loanDto.getSavingsBankAccount());
		}
		loan.setPrincipalPaymentFrequency(loanDto.getPrincipalPaymentFrequency());
		loan.setInterestPaymentFrequency(loanDto.getInterestPaymentFrequency());
		loan.setFirstPaymentDate(loanDto.getFirstPaymentDate());
		loan.setRateOfInterest(loanDto.getRateOfInterest());
		loan.setLoanTerm(loanDto.getLoanTerm());
		loan.setDescription(loanDto.getDescription());
		loan.setLoanAmount(loanDto.getLoanAmount());
		loan.setTotalInterest(loanDto.getTotalInterest());
		loan.setInstallmentAmount(loanDto.getMonthlyEmi());
		loan.setTotalPayable(loanDto.getLoanAmount());
		loan.setClosingBalance(loanDto.getLoanAmount());
		// create loan
//		loanService.createLoan(loan);
		loanRepository.insert(loan);
		loanDto.getLoanDetails().forEach(loanDetail -> {
			final TLoanDetails loanDetails = new TLoanDetails(loanId, loanDetail.getDueDate(),
					loanDetail.getPrincipalAmt(), loanDetail.getInterestAmount(), loanDetail.getAmount(),
					loanDetail.getOpeningBalance(), loanDetail.getClosingBalance(), Constants.LOAN_REPAYMENT_NOT_PAID);
			loanDetails.setLoanDtlId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_CREATE_LNDTL));
			loanDetailsRepository.insert(loanDetails);
		});

//		redirectAttributes.addFlashAttribute("message", "success");
//		modelAndView.setViewName("redirect:/accounts/loan-details");
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@PostMapping(path = "/editLoan")
	public ResponseEntity<Response> editLoan(@RequestBody TLoanCreateDto loanDto, final ModelAndView modelAndView,
			final RedirectAttributes redirectAttributes) {
		System.out.println(loanDto.getLoanAmount());

		TLoan loan = loanRepository.findOneByLoanId(loanDto.getLoanId());
		loan.setLoanId(loanDto.getLoanId());
		loan.setLoanType(loanDto.getLoanType());
		loan.setBank(loanDto.getBank());
		loan.setRateOfInterest(loanDto.getRateOfInterest());
		loan.setLoanTerm(loanDto.getLoanTerm());
		loan.setLoanAmount(loanDto.getLoanAmount());
		loan.setDate(loanDto.getDate());
		loan.setReference(loanDto.getReference());
		loan.setDueDate(loanDto.getDueDate());
		loan.setLeaveDay(loanDto.getLeaveDay());
		loan.setSavingAccount(loanDto.getSavingAccount());
		if (loan.getSavingAccount() == 1) {
			loan.setSavingsAccountAmount(loanDto.getSavingsAccountAmount());
			loan.setSavingsBankAccount(loanDto.getSavingsBankAccount());
		}
		loan.setPrincipalPaymentFrequency(loanDto.getPrincipalPaymentFrequency());
		loan.setInterestPaymentFrequency(loanDto.getInterestPaymentFrequency());
		loan.setFirstPaymentDate(loanDto.getFirstPaymentDate());
		loan.setRateOfInterest(loanDto.getRateOfInterest());
		loan.setLoanTerm(loanDto.getLoanTerm());
		loan.setDescription(loanDto.getDescription());
		loan.setLoanAmount(loanDto.getLoanAmount());
		loan.setTotalInterest(loanDto.getTotalInterest());
		loan.setInstallmentAmount(loanDto.getMonthlyEmi());
		loan.setTotalPayable(loanDto.getLoanAmount());
		loan.setClosingBalance(loanDto.getLoanAmount());

		loanRepository.save(loan);

		List<TLoanDetailsDto> conforming = loanDto.getLoanDetails().stream()
				.filter(loandetai -> AppUtil.isObjectEmpty(loandetai.getLoanDtlId())).collect(Collectors.toList());
		if (!AppUtil.isObjectEmpty(conforming)) {
			loanDetailsRepository.deleteByLoanId(loanDto.getLoanId());
		}
		loanDto.getLoanDetails().forEach(loanDetail -> {
			TLoanDetails loanDetails = loanDetailsRepository.findOneByLoanDtlId(loanDetail.getLoanDtlId());
			if (!AppUtil.isObjectEmpty(loanDetails)) {
				loanDetails.setLoanId(loanDto.getLoanId());
				loanDetails.setAmount(loanDetail.getAmount());
				loanDetails.setInterestAmount(loanDetail.getInterestAmount());
				loanDetails.setDate(loanDetail.getDueDate());
				loanDetails.setPrincipalAmt(loanDetail.getPrincipalAmt());
				loanDetails.setClosingBalance(loanDetail.getClosingBalance());
				loanDetails.setOpeningBalance(loanDetail.getOpeningBalance());
				loanDetails.setStatus(loanDetail.getStatus());
				loanDetailsRepository.save(loanDetails);
			} else {

				TLoanDetails newLoanDetails = new TLoanDetails(loanDto.getLoanId(), loanDetail.getDueDate(),
						loanDetail.getPrincipalAmt(), loanDetail.getInterestAmount(), loanDetail.getAmount(),
						loanDetail.getOpeningBalance(), loanDetail.getClosingBalance(),
						Constants.LOAN_REPAYMENT_NOT_PAID);
				newLoanDetails.setLoanDtlId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_CREATE_LNDTL));
				loanDetailsRepository.insert(newLoanDetails);
			}

		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping("/re-payment")
	public ModelAndView rePayLoan() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.repayment");
		return modelAndView;
	}

	@GetMapping("/loan-search")
	public ResponseEntity<Response> searchLoanId(@RequestParam("search") String search) {
		final List<LoanSearchDto> loan = loanRepository.findBySearchDto(search);
		return new ResponseEntity<>(new Response("success", loan), HttpStatus.OK);
	}

	@GetMapping("findOne/loan-search")
	public ResponseEntity<Response> findOneSearchLoan(@RequestParam("loanId") String loanId,
			@RequestParam("flag") int flag) {
		TLoanRepaymentDto rePayment;
		if (flag == 0) {
			rePayment = loanDetailsRepository.findLoanWithMinDateAndNotPaid(loanId);
		} else {
			rePayment = loanDetailsRepository.preCloseLoan(loanId);
		}

		return new ResponseEntity<>(new Response("success", rePayment), HttpStatus.OK);

	}

	@GetMapping("/sales-summary-list/{stockNo}")
	public ResponseEntity<Response> findOneSearchLoan(@PathVariable String stockNo) {

		List<SalesSumaryResultDto> inventoryCost = tInventoryCostRepository.getInventoryByStockAndType(stockNo);
		InventoryCostDto invCost = new InventoryCostDto();
		for (SalesSumaryResultDto salesSumaryResultDto : inventoryCost) {

			// Purchase Cost
			if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(), Constants.COST_OF_GOODS_TYPE_PURCHASE))
				invCost.setPurchaseCost(salesSumaryResultDto.getAmount());

			// Purchase Cost tax
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_PURCHASE_TAX))
				invCost.setPurchaseCostTax(salesSumaryResultDto.getAmount());

			// Commission
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_PURCHASE_COMM))
				invCost.setCommision(salesSumaryResultDto.getAmount());

			// Commission Tax
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_PURCHASE_COMM_TAX))
				invCost.setCommisionTax(salesSumaryResultDto.getAmount());

			// Recycle
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_PURCHASE_RECYCLE))
				invCost.setRecycle(salesSumaryResultDto.getAmount());

//			Road Tax
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_PURCHASE_ROADTAX))
				invCost.setRoadTax(salesSumaryResultDto.getAmount());

			// Other Charges
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_PURCHASE_OTHERS))
				invCost.setOtherCharges(salesSumaryResultDto.getAmount());

			// Others Tax
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_PURCHASE_OTHERS_TAX))
				invCost.setOtherChargesTax(salesSumaryResultDto.getAmount());

			// Freight
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(), Constants.COST_OF_GOODS_TYPE_FREIGHT))
				invCost.setFreight(salesSumaryResultDto.getAmount());

			// Shipping
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_SHIPPING))
				invCost.setShipping(salesSumaryResultDto.getAmount());

			// Radiation
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_RADIATION))
				invCost.setRadiation(salesSumaryResultDto.getAmount());

			// Transport
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_TRANSPORT))
				invCost.setTransport(salesSumaryResultDto.getAmount());

			// Transport Tax
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_TRANSPORT_TAX))
				invCost.setTransportTax(salesSumaryResultDto.getAmount());

			// Storage
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(), Constants.COST_OF_GOODS_TYPE_STORAGE))
				invCost.setStorage(salesSumaryResultDto.getAmount());

			// Inspection
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_INSPECTION))
				invCost.setInspection(salesSumaryResultDto.getAmount());

			// inspection Tax
			else if (StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(),
					Constants.COST_OF_GOODS_TYPE_INSPECTION_TAX))
				invCost.setInspectionTax(salesSumaryResultDto.getAmount());

//			//Storage
//			if(StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(), Constants.COST_OF_GOODS_TYPE_STORAGE))
//				invCost.setStorage(salesSumaryResultDto.getAmount());
//			else
//				invCost.setPurchaseCost(0.0);
//			
//			//Storage
//			if(StringUtils.equalsIgnoreCase(salesSumaryResultDto.getType(), Constants.COST_OF_GOODS_TYPE_STORAGE))
//				invCost.setStorage(salesSumaryResultDto.getAmount());
//			else
//				invCost.setPurchaseCost(0.0);

		}
		return new ResponseEntity<>(new Response("success", invCost), HttpStatus.OK);

	}

	@PostMapping("/create/re-payment")
	public ModelAndView createRepayment(final TLoanRepayment loanDto, final ModelAndView modelAndView) {

		loanRepaymentService.rePayLoan(loanDto);
		modelAndView.addObject("message", "Repayment done succesfully");
		modelAndView.setViewName("redirect:/accounts/loan-details");
		return modelAndView;

	}

	@GetMapping("/viewloanlist-data")
	@ResponseBody
	public DatatableResponse getViewLoanData() {

		return new DatatableResponse(loanRepository.findAll());
	}

	@GetMapping("/loan-details")
	public ModelAndView loanDetails(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.view-loan-details");
		return modelAndView;
	}

	@GetMapping("/viewloan/details/datasource")
	@ResponseBody
	public DatatableResponse getViewLoanDetails() {
		return new DatatableResponse(loanRepository.getAllLoanDetails());
	}

	@GetMapping("/loan/edit/{loanId}")
	public ModelAndView supplierCreate(@PathVariable("loanId") String loanId, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("loanId", loanId);
		modelAndView.setViewName("redirect:/accounts/create-loan");
		return modelAndView;
	}

	@GetMapping("/loan/info/{loanId}")
	public ResponseEntity<Response> loanInfo(@PathVariable("loanId") String loanId) {
		return new ResponseEntity<Response>(new Response("success", this.loanRepository.findOneByLoanId(loanId)),
				HttpStatus.OK);
	}

	@GetMapping("/customer-accounts")
	public ModelAndView customerAccounts() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.customer.accounts");
		return modelAndView;
	}

	@GetMapping("/customer-accounts/list/datasource")
	@ResponseBody
	public DatatableResponse getCustomerAccountsList() {
		return new DatatableResponse(tSalesInvoiceRepository.findAllCustomerTransactions());
	}

	@Transactional
	@PostMapping("/cancel-auction-payment")
	public ResponseEntity<Response> cancelAuctionPayment(@RequestBody final Map<String, Object> data)
			throws ParseException {

		final String cancelledRemarks = (String) data.get("cancelledRemarks");

		@SuppressWarnings("unchecked")
		final List<String> invoiceNos = (List<String>) data.get("invoiceNo");
		final List<Integer> status = Arrays.asList(Constants.INV_STATUS_VERIFIED, Constants.INV_CANCEL_CHARGE_UPDATED);
		final List<TPurchaseInvoice> invoices = purchaseInvoiceRepository
				.findAllByInvoiceNoInAndPaymentApproveAndStatusIn(invoiceNos, Constants.PAYMENT_COMPLETED, status);
		for (final TPurchaseInvoice tPurchaseInvoice : invoices) {
			tPurchaseInvoice.setCancelledRemarks(cancelledRemarks);
			tPurchaseInvoice.setPaymentApprove(Constants.PAYMENT_CANCELED);

		}
		purchaseInvoiceRepository.saveAll(invoices);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@Transactional
	@PostMapping(value = "/freeze-auction-payment")
	public ResponseEntity<Response> freezeAuctionPayment(@RequestBody final Map<String, Object> data)
			throws ParseException {

		final Integer paymentApprove = (Integer) data.get("paymentApprove");
		@SuppressWarnings("unchecked")
		final List<String> invoiceNos = (List<String>) data.get("invoiceNo");

		List<TPurchaseInvoice> invoices;
		final List<Integer> status = Arrays.asList(Constants.INV_STATUS_VERIFIED, Constants.INV_CANCEL_CHARGE_UPDATED);
		if (paymentApprove == 2) {
			invoices = purchaseInvoiceRepository.findAllByInvoiceNoInAndPaymentApproveAndStatusIn(invoiceNos,
					Constants.PAYMENT_COMPLETED, status);
			purchaseInvoiceService.purchaseInvoicePaymentCompleteTransactions(invoices);

		} else {
			invoices = purchaseInvoiceRepository.findAllByInvoiceNoInAndPaymentApproveAndStatusIn(invoiceNos,
					Constants.PAYMENT_CANCELED, status);
		}

		for (final TPurchaseInvoice tPurchaseInvoice : invoices) {
			tPurchaseInvoice.setPaymentApprove(Constants.PAYMENT_FREEZE);
		}
		// Save Freeze
		purchaseInvoiceRepository.saveAll(invoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/customerlist-data")
	@ResponseBody
	public DatatableResponse getCustomerlistData() {
		return new DatatableResponse(
				customerRepository.findAllByApproveCustomerflag(Constants.CUSTOMER_NOT_APPROVED_FLAG));

	}

	@PostMapping(value = "/approve-customer")
	public ResponseEntity<Response> approveCustomer(@RequestParam("customerCode") final String customerCode) {
		final TCustomer customer = customerRepository.findOneByCode(customerCode);
		customer.setApproveCustomerflag(Constants.CUSTOMER_APPROVED_FLAG);
		customerRepository.save(customer);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping("create/branch-salesOrder")
	public ModelAndView createBranchSalesOrder() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("screenNameFlag", "accounts");
		modelAndView.setViewName("accounts.branch.salesOrder.create");
		return modelAndView;
	}

	@GetMapping("create/branch-salesOrder/datasource")
	@ResponseBody
	public DatatableResponse createBranchSalesOrderList() {
		return new DatatableResponse(stockRepository.getShippedStockWithoutSalesOrder());

	}

	@GetMapping("/branch-salesOrder/list")
	public ModelAndView branchSalesOrdered() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.branch.salesOrder.list");
		return modelAndView;
	}

	@GetMapping("/branch-salesOrdered/datasource")
	@ResponseBody
	public DatatableResponse branchSalesOrderedList() {
		return new DatatableResponse(tSalesInvoiceRepository.branchSalesOrderList());

	}

	@GetMapping("/user/management")
	public ModelAndView manageUser(Model model, ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.user.management");
		return modelAndView;
	}

	@GetMapping("/master/shipping/list")
	public ModelAndView shipChargeList() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.shippingcharge.list");

		return modelAndView;
	}

	@GetMapping("/create/master/shipping")
	public ModelAndView shipChargeCreate(HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.shippingcharge.create");

		return modelAndView;
	}

	@PostMapping("/ship/charge/save")
	public ModelAndView saveShipCharge(@RequestBody MShippingCharge mShippingCharge,
			RedirectAttributes redirectAttributes, ModelAndView modelAndView) {

		shipChargerepo.save(mShippingCharge);
		redirectAttributes.addFlashAttribute("message", "Shipping Charge Created Successfully");
		modelAndView.setViewName("redirect:/accounts/master/shipping/list");
		return modelAndView;
	}

	@DeleteMapping("/delete/invoice/payment/transaction")
	public ResponseEntity<String> deleteInvoicePaymentTransaction(@RequestParam String paymentVoucherNo) {
		invoicePaymentTransactionService.deleteTransaction(paymentVoucherNo);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	@GetMapping(value = { "/stock-entry" })
	public ModelAndView stockEntry(@ModelAttribute("stockForm") StockForm stockForm, ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.shipping.stock.entry");
		return modelAndView;
	}

	@GetMapping("/stock-entry/{stockNo}")
	public ModelAndView stockSearch(@PathVariable String stockNo, ModelAndView modelAndView,
			HttpServletRequest request) {
		final String editFlag = request.getParameter("editFlag");
		final String returnPath = request.getParameter("return");
		final StockForm stockForm = new StockForm();
		final List<TPurchaseInvoice> invoice = tPurchaseInvoiceRepository.findAllByStockNoAndType(stockNo,
				Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);
		final TPurchaseInvoice tPurInv = new TPurchaseInvoice();
		for (final TPurchaseInvoice tPurchaseInvoice : invoice) {
			tPurInv.setChassisNo(tPurchaseInvoice.getChassisNo());
			tPurInv.setInvoiceNo(tPurchaseInvoice.getInvoiceNo());
			if (tPurchaseInvoice.getPurchaseCost() > 0)
				tPurInv.setPurchaseCost(tPurchaseInvoice.getPurchaseCost());
			if (tPurchaseInvoice.getCommision() > 0)
				tPurInv.setCommision(tPurchaseInvoice.getCommision());
			if (!AppUtil.isObjectEmpty(tPurchaseInvoice.getRecycle()) && tPurchaseInvoice.getRecycle() >= 0)
				tPurInv.setRecycle(tPurchaseInvoice.getRecycle());
			if (!AppUtil.isObjectEmpty(tPurchaseInvoice.getRoadTax()) && tPurchaseInvoice.getRoadTax() >= 0)
				tPurInv.setRoadTax(tPurchaseInvoice.getRoadTax());

		}
		stockForm.setStock(stockRepository.findOneByStockNo(stockNo));
		stockForm.setPurchaseInvoice(tPurInv);
		modelAndView.addObject("stockForm", stockForm);
		modelAndView.addObject("editFlag", editFlag);
		modelAndView.addObject("returnPath", returnPath);
		modelAndView.setViewName("accounts.shipping.stock.entry");
		return modelAndView;

	}

	@GetMapping("/loan-view-before-create")
	@ResponseBody
	public DatatableResponse loanBeforeCreate(@RequestParam("loanId") String loanId,
			@RequestParam("loanAmount") double loanAmount, @RequestParam("rateOfInterest") double rateOfInterest,
			@RequestParam("loanTerm") int loanTerm, @RequestParam("loanType") int loanType,
			@RequestParam("monthlyEmi") double monthlyEmi,
			@RequestParam("paymentDate") @DateTimeFormat(pattern = "dd-MM-yyyy") final Date paymentDate) {
		System.out.println("loanAmount" + loanAmount + "rateOfInterest" + rateOfInterest + "loanTerm" + loanTerm
				+ "loanType" + loanType + "monthlyEmi" + monthlyEmi + "paymentDate" + paymentDate);
		if (AppUtil.isObjectEmpty(loanId)) {
			return new DatatableResponse(this.loanService.getConformLoanDetails(loanAmount, rateOfInterest, loanTerm,
					monthlyEmi, paymentDate, loanType));
		} else {
			TLoan tLoan = loanRepository.findOneByLoanId(loanId);
			boolean paidStatus = this.loanDetailsRepository.existsByStatus(loanId);
			if (paidStatus) {
				return new DatatableResponse(this.loanDetailsRepository.getEditLoanDetail(loanId));

			} else if (tLoan.getLoanAmount() != loanAmount || tLoan.getFirstPaymentDate().compareTo(paymentDate) != 0
					|| tLoan.getRateOfInterest() != rateOfInterest || tLoan.getInstallmentAmount() != monthlyEmi) {
				return new DatatableResponse(this.loanService.getConformLoanDetails(loanAmount, rateOfInterest,
						loanTerm, monthlyEmi, paymentDate, loanType));
			} else {
				return new DatatableResponse(this.loanDetailsRepository.getEditLoanDetail(loanId));
			}

		}

	}

	@GetMapping("/uac")
	public ModelAndView userAccessControl(final HttpServletRequest request, ModelAndView modelAndView) {

		modelAndView.setViewName("accounts.userAccessControl");
		return modelAndView;
	}

	@GetMapping("/uac/data/{userId}")
	public ResponseEntity<Map<String, Object>> userAccessControlData(@PathVariable("userId") String userId) {
		MLogin login = loginRepository.findOneByUserId(userId);
		Integer access = AppUtil.getDeptIdByDept(login.getDepartment());
		List<MUACDto> list;
		if (!access.equals(Constants.ROLE_ID_ADMIN)) {
			list = uacRepository.findAllByDepartment(access);
		} else {
			list = uacRepository.findAllUAC();
		}

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("data", createTree(list));
		response.put("access", login.getAccess());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/uac/update/access")
	public ResponseEntity<Response> updateAccess(@RequestBody Document data) {
		@SuppressWarnings("unchecked")
		ArrayList<Integer> access = (ArrayList<Integer>) data.get("access", ArrayList.class);
		String userId = data.get("userId", String.class);
		MLogin login = loginRepository.findOneByUserId(userId);
		login.setAccess(access);
		loginRepository.save(login);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	private static List<MUACDto> createTree(List<MUACDto> nodes) {
		Map<Integer, MUACDto> mapTmp = new HashMap<>();
		// Save all nodes to a map
		for (MUACDto current : nodes) {
			mapTmp.put(current.getId(), current);
		}

		// loop and assign parent/child relationships
		for (

		MUACDto current : nodes) {
			Integer parentId = current.getParentId();

			if (parentId != null) {
				MUACDto parent = mapTmp.get(parentId);
				if (parent != null) {
					current.setParent(parent);
					parent.addChild(current);
					mapTmp.put(parentId, parent);
					mapTmp.put(current.getId(), current);
				}
			}

		}

		// get the root
		List<MUACDto> roots = new ArrayList<>();
		for (MUACDto node : mapTmp.values()) {
			if (node.getParent() == null) {
				roots.add(node);
			}
		}
		return roots;
	}

	@GetMapping("/invoice/booking/genaralExpenses/export")
	public void exportExcelNew(HttpServletResponse response) throws IOException {
		this.accountReportService.exportGenaralExpensesReport(response);
	}

	@GetMapping("/stock/info/{stockNo}")
	public ResponseEntity<Response> getStockInfo(@PathVariable("stockNo") String stockNo) {
		return new ResponseEntity<>(new Response("success", stockInfoService.fetchStockData(stockNo)), HttpStatus.OK);
	}

	@GetMapping(value = { "custom/report" })
	public ModelAndView stockTracker(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.custom.report");
		return modelAndView;
	}

	@GetMapping("/exchangerate/checking")
	public ResponseEntity<Response> getTTAllocationExchangeRate(@RequestParam(value = "daybookId") String daybookId,
			@RequestParam(value = "currency") Integer currency,
			@RequestParam(value = "cstmrCurrency") Integer cstmrCurrency,
			@RequestParam(value = "sourceAmt") Double sourceAmt,
			@RequestParam(value = "exchangeValue", required = false) Integer exchangeValue) {
		TTAllocationExchangeRateDto exchangeRateDto = new TTAllocationExchangeRateDto();
		TDayBook daybook = dayBookRepository.findOneByDaybookId(daybookId);
		MCurrency mCurrency = currencyRepository.findOneByCurrencySeq(cstmrCurrency);
		if (cstmrCurrency == 1 && currency != 1) {
			String id = (currency == 2) ? daybook.getDollarRate()
					: (currency == 3) ? daybook.getAusDollarRate() : daybook.getPoundRate();
			Optional<TExchangeRate> exchangeRate = tExchageRateRepository.findById(id);
			if (!AppUtil.isObjectEmpty(exchangeRate)) {
				exchangeRateDto
						.setActualAmount((exchangeValue == 1) ? (exchangeRate.get().getExchangeRate() * sourceAmt)
								: (exchangeValue == 2) ? (exchangeRate.get().getSalesExchangeRate() * sourceAmt)
										: (exchangeRate.get().getSpecialExchangeRate() * sourceAmt));
				exchangeRateDto.setExchangeRate2((exchangeValue == 1) ? (exchangeRate.get().getExchangeRate())
						: (exchangeValue == 2) ? (exchangeRate.get().getSalesExchangeRate())
								: (exchangeRate.get().getSpecialExchangeRate()));
				exchangeRateDto.setExchangeRate1(1.0);
				exchangeRateDto.setCurrencySymbol(mCurrency.getSymbol());
			}

		} else if (cstmrCurrency != 1 && currency == 1) {
			String id = (cstmrCurrency == 2) ? daybook.getDollarRate()
					: (cstmrCurrency == 3) ? daybook.getAusDollarRate() : daybook.getPoundRate();
			Optional<TExchangeRate> exchangeRate = tExchageRateRepository.findById(id);
			if (!AppUtil.isObjectEmpty(exchangeRate)) {
				exchangeRateDto
						.setActualAmount((exchangeValue == 1) ? (sourceAmt / exchangeRate.get().getExchangeRate())
								: (exchangeValue == 2) ? (sourceAmt / exchangeRate.get().getSalesExchangeRate())
										: (sourceAmt / exchangeRate.get().getSpecialExchangeRate()));
				exchangeRateDto.setExchangeRate2((exchangeValue == 1) ? (exchangeRate.get().getExchangeRate())
						: (exchangeValue == 2) ? (exchangeRate.get().getSalesExchangeRate())
								: (exchangeRate.get().getSpecialExchangeRate()));
				exchangeRateDto.setExchangeRate1(1.0);
				exchangeRateDto.setCurrencySymbol(mCurrency.getSymbol());
			}
		} else if (cstmrCurrency != currency) {
			sourceAmt = (exchangeValue == 1) ? (sourceAmt * daybook.getExchangeRate())
					: (exchangeValue == 2) ? (sourceAmt * daybook.getSalesExchangeRate())
							: (sourceAmt * daybook.getSpecialExchangeRate());
			String id = (cstmrCurrency == 2) ? daybook.getDollarRate()
					: (cstmrCurrency == 3) ? daybook.getAusDollarRate() : daybook.getPoundRate();
			Optional<TExchangeRate> exchangeRate = tExchageRateRepository.findById(id);

			if (!AppUtil.isObjectEmpty(exchangeRate)) {
				exchangeRateDto
						.setActualAmount((exchangeValue == 1) ? (sourceAmt / exchangeRate.get().getExchangeRate())
								: (exchangeValue == 2) ? (sourceAmt / exchangeRate.get().getSalesExchangeRate())
										: (sourceAmt / exchangeRate.get().getSpecialExchangeRate()));
				exchangeRateDto.setExchangeRate2((exchangeValue == 1) ? (exchangeRate.get().getExchangeRate())
						: (exchangeValue == 2) ? (exchangeRate.get().getSalesExchangeRate())
								: (exchangeRate.get().getSpecialExchangeRate()));
				exchangeRateDto.setExchangeRate1((exchangeValue == 1) ? daybook.getExchangeRate()
						: (exchangeValue == 2) ? daybook.getSalesExchangeRate() : daybook.getSpecialExchangeRate());
				exchangeRateDto.setCurrencySymbol(mCurrency.getSymbol());
			}
		} else {
			exchangeRateDto.setActualAmount(sourceAmt);
			exchangeRateDto.setCurrencySymbol(mCurrency.getSymbol());
			exchangeRateDto.setExchangeRate1(1.0);
			exchangeRateDto.setExchangeRate2(1.0);
		}

		return new ResponseEntity<>(new Response("success", exchangeRateDto), HttpStatus.OK);
	}
}
