package com.nexware.aajapan.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.CancelledStockDto;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.dto.StockDetailsDto;
import com.nexware.aajapan.dto.StockReservedDto;
import com.nexware.aajapan.dto.TPurchaseInvoiceAddPaymentDto;
import com.nexware.aajapan.dto.TReAuctionDto;
import com.nexware.aajapan.dto.TStockEditHistoryDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.form.StockForm;
import com.nexware.aajapan.models.AuctionInfo;
import com.nexware.aajapan.models.AuctionPaymentType;
import com.nexware.aajapan.models.PurchaseInfo;
import com.nexware.aajapan.models.ReservedInfo;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TCustomerTransaction;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TReAuction;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TSupplierTransaction;
import com.nexware.aajapan.models.TransportInfo;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.AuctionPaymentTypeRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.repositories.TReAuctionRepository;
import com.nexware.aajapan.services.MSupplierService;
import com.nexware.aajapan.services.S3Factory;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TCustomerService;
import com.nexware.aajapan.services.TCustomerTransactionService;
import com.nexware.aajapan.services.TStockService;
import com.nexware.aajapan.utils.AppUtil;

import constants.FbFeedConstants;

@Controller
@RequestMapping("stock")
public class StockController {
	@Autowired
	StockRepository stockRepository;
	@Autowired
	ObjectMapper mapper;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private SecurityService securityService;

	@Autowired
	private TReAuctionRepository reAuctionRepository;
	@Autowired
	private AuctionPaymentTypeRepository auctionPaymentTypeRepository;
	@Autowired
	private TPurchaseInvoiceRepository tPurchaseInvoiceRepository;
	@Autowired
	private TCustomerRepository customerRepository;
	@Autowired
	private TCustomerService customerService;
	@Autowired
	private TCustomerTransactionService customerTransactionService;
	@Autowired
	private TStockService stockService;
	@Autowired
	private MSupplierService supplierService;
	@Autowired
	private S3Factory s3FactoryService;

	public static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;

	@GetMapping(value = { "/stock-entry" })
	public ModelAndView stockEntry(@ModelAttribute("stockForm") StockForm stockForm, ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.stock.entry");
		return modelAndView;
	}

	@GetMapping("/stock-entry/{stockNo}")
	public ModelAndView stockSearch(@PathVariable String stockNo, ModelAndView modelAndView,
			HttpServletRequest request) {
		final String editFlag = request.getParameter("editFlag");
		final String returnPath = request.getParameter("return");
		final StockForm stockForm = new StockForm();
		final TPurchaseInvoice invoice = tPurchaseInvoiceRepository.findOnePurchaseInvoice(stockNo,
				Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);
		Double total = invoice.getPurchaseCost() + invoice.getCommision() + invoice.getRoadTax() + invoice.getRecycle()
				+ invoice.getOtherCharges();
		Double totalTax = invoice.getPurchaseCostTaxAmount() + invoice.getCommisionTaxAmount()
				+ invoice.getOthersCostTaxAmount();
		Double totalTaxIncluded = total + totalTax;
		invoice.setTotal(total);
		invoice.setTotalTax(totalTax);
		invoice.setTotalTaxIncluded(totalTaxIncluded);
		stockForm.setStock(stockRepository.findOneByStockNo(stockNo));
		stockForm.setPurchaseInvoice(invoice);
		modelAndView.addObject("stockForm", stockForm);
		modelAndView.addObject("editFlag", editFlag);
		modelAndView.addObject("returnPath", returnPath);
		modelAndView.setViewName("shipping.stock.entry");
		return modelAndView;

	}

	// Save Stock and edit
	@PostMapping("/stock-entry")
	public ModelAndView stockEntry(@ModelAttribute("stockForm") StockForm stockForm, @RequestParam String action,
			@RequestParam("attaachment_directory") String tempDirectory, @RequestParam("returnPath") String returnPath,
			ModelAndView modelAndView, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		try {
			final TStock stock = stockForm.getStock();
			final boolean isNewEntry = stock.getId().trim().isEmpty();
			if (isNewEntry) {
				stockService.createStock(stockForm, tempDirectory);
			} else {
				stockService.editStock(stockForm);
			}
			if (action.equalsIgnoreCase("save")) {
				if (returnPath.indexOf(request.getContextPath()) != -1) {
					returnPath = returnPath.substring(
							returnPath.indexOf(request.getContextPath()) + request.getContextPath().length());
				} else {
					returnPath = "/stock/purchased";
				}
				modelAndView.setViewName(String.format("redirect:%s", returnPath));
			} else {
				final StockForm purchaseInfo = new StockForm();
				final TStock tStock = new TStock();
				final PurchaseInfo info = stock.getPurchaseInfo();
				final AuctionInfo auctionInfo = info.getAuctionInfo();
				TransportInfo transportInfo = stock.getTransportInfo();
				tStock.setTransportInfo(transportInfo);
				auctionInfo.setLotNo(null);
				info.setAuctionInfo(auctionInfo);
				tStock.setPurchaseInfo(info);
				purchaseInfo.setStock(tStock);
				redirectAttributes.addFlashAttribute("stockForm", purchaseInfo);
				redirectAttributes.addFlashAttribute("message", "Stock saved successfully!");
				modelAndView.setViewName("redirect:/stock/stock-entry");
			}
		} catch (final Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("stockForm", stockForm);
			modelAndView.setViewName("redirect:/stock/stock-entry");
		}
		return modelAndView;

	}

	@PostMapping(path = "/purchased/invoice/addPayment")
	@Transactional
	public ResponseEntity<Response> addPayment(@RequestBody TPurchaseInvoiceAddPaymentDto paymentDto) {
		final TPurchaseInvoice invoice = new TPurchaseInvoice();
		invoice.setSupplierId(paymentDto.getSupplier());
		invoice.setPurchaseCost(0.0);
		invoice.setPurchaseCostTax(0.0);
		invoice.setCommision(0.0);
		invoice.setCommisionTax(0.0);
		invoice.setRoadTax(0.0);
		invoice.setRecycle(0.0);
		if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(1))) {
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_PENALTY_CHARGE);
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOtherCharges(paymentDto.getAmount());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount());
		} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(2))) {
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_DOCUMENT_PENALTY);
			invoice.setOtherCharges(paymentDto.getAmount() * -1);
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount() * -1);
		} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(3))) {
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_LATE_PENALTY);
			invoice.setOtherCharges(paymentDto.getAmount() * -1);
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount() * -1);
		} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(4))) {
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_ROADTAX_CLAIMED);
			invoice.setOtherCharges(paymentDto.getAmount() * -1);
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount() * -1);
		} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(5))
				|| paymentDto.getInvoiceItemType().equals(Integer.valueOf(10))) {
			if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(5))) {
				invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_CHARGES);
			} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(10))) {
				invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_PENALTY_CHARGES);
			}

			invoice.setOtherCharges(paymentDto.getAmount());
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount());
			final TStock stock = stockRepository.findOneByStockNo(paymentDto.getStockNo());
			// update invoice status
//			final TPurchaseInvoice purchaseInvoice = tPurchaseInvoiceRepository
//					.findOneByCode(stock.getPurchaseInvoiceCode());
//			purchaseInvoice.setStatus(Constants.INV_CANCEL_CHARGE_UPDATED);
//			tPurchaseInvoiceRepository.save(purchaseInvoice);
			// set stock details
			invoice.setStockNo(stock.getStockNo());
			invoice.setChassisNo(stock.getChassisNo());

			// supplier transaction
			final TSupplierTransaction transaction = new TSupplierTransaction();
			transaction.setStockNo(invoice.getStockNo());
			transaction.setInvoiceNo(invoice.getInvoiceNo());
			transaction.setSupplierCode(invoice.getSupplierId());
			transaction.setTransactionType(Constants.TRANSACTION_CREDIT);
			transaction.setAmount(invoice.getTotalTaxIncluded());

			supplierService.supplierTransactionEntry(transaction);

		} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(6))) {
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_CASH_BACK);
			invoice.setOtherCharges(paymentDto.getAmount());
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount());
		} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(7))) {
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_RIKUSO_PAYMENT);
			invoice.setOtherCharges(paymentDto.getAmount());
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount());
			if (!AppUtil.isObjectEmpty(paymentDto.getStockNo())) {
				final TStock stock = stockRepository.findOneByStockNo(paymentDto.getStockNo());
				// set stock details
				invoice.setStockNo(stock.getStockNo());
				invoice.setChassisNo(stock.getChassisNo());
			}
		} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(8))) {
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_MEMBERSHIP);
			invoice.setOtherCharges(paymentDto.getAmount());
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount());
		} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(9))) {
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_UNSOLD_AUCTION_CHARGE);
			invoice.setOtherCharges(paymentDto.getAmount());
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount());

			final TStock stock = stockRepository.findOneByStockNo(paymentDto.getStockNo());
			invoice.setStockNo(stock.getStockNo());
			invoice.setChassisNo(stock.getChassisNo());
		} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(11))) {
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_RECYCLE_CLAIMED);
			invoice.setOtherCharges(paymentDto.getAmount() * -1);
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount() * -1);
		} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(12))) {
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_RECYCLE_PAID);
			invoice.setOtherCharges(paymentDto.getAmount());
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount());
			if (!AppUtil.isObjectEmpty(paymentDto.getStockNo())) {
				final TStock stock = stockRepository.findOneByStockNo(paymentDto.getStockNo());
				invoice.setStockNo(stock.getStockNo());
				invoice.setChassisNo(stock.getChassisNo());
			}
		} else if (paymentDto.getInvoiceItemType().equals(Integer.valueOf(13))) {
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_ROADTAX_PAID);
			invoice.setOtherCharges(paymentDto.getAmount());
			invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
			invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount());
			if (!AppUtil.isObjectEmpty(paymentDto.getStockNo())) {
				final TStock stock = stockRepository.findOneByStockNo(paymentDto.getStockNo());
				invoice.setStockNo(stock.getStockNo());
				invoice.setChassisNo(stock.getChassisNo());
			}
		} else if (paymentDto.getInvoiceItemType() > 14) {
			AuctionPaymentType auctionPaymentType = auctionPaymentTypeRepository
					.findOneByCode(paymentDto.getInvoiceItemType().toString());
			invoice.setType("OTHERS - " + auctionPaymentType.getType());
			if (paymentDto.getClaimed().equals(Integer.valueOf(1))) {
				invoice.setOtherCharges(paymentDto.getAmount() * -1);
				invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
				invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount() * -1);
			} else {
				invoice.setOtherCharges(paymentDto.getAmount());
				invoice.setOtherChargesTax(paymentDto.getOthersTaxValue());
				invoice.setOthersCostTaxAmount(paymentDto.getOthersCostTaxAmount());
			}

			if (!AppUtil.isObjectEmpty(paymentDto.getStockNo())) {
				final TStock stock = stockRepository.findOneByStockNo(paymentDto.getStockNo());
				invoice.setStockNo(stock.getStockNo());
				invoice.setChassisNo(stock.getChassisNo());
			}
		}
		invoice.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_PRCHSINVC));
		invoice.setStatus(Constants.INV_STATUS_NEW);
		invoice.setInvoiceDate(paymentDto.getInvoiceDate());
		invoice.setAuctionHouseId(new ObjectId(paymentDto.getPurchasedAuctionHouse()));
		invoice.setCarTaxClaimStatus(Constants.TPURCHASEINVOICE_CARTAX_NOT_CLAIMED);
		invoice.setRecycleClaimStatus(Constants.TPURCHASEINVOICE_RECYCLE_NOT_CLAIMED);
		invoice.setPurchaseTaxClaimStatus(Constants.TPURCHASEINVOICE_PURCHASETAX_NOT_CLAIMED);
		invoice.setCommisionTaxClaimStatus(Constants.TPURCHASEINVOICE_COMMISSIONTAX_NOT_CLAIMED);
		invoice.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
		invoice.setPurchaseType(Constants.ACCOUNTS_AUCTION_PAYMENT);

		tPurchaseInvoiceRepository.insert(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/purchased/invoice/addPayment/takeOutStocks")
	@Transactional
	public ResponseEntity<Response> addPaymentTakeOtStock(@RequestBody TPurchaseInvoiceAddPaymentDto paymentDto) {
		final List<TPurchaseInvoice> listInvoice = new ArrayList<TPurchaseInvoice>();
		paymentDto.getStockNos().forEach(stk -> {
			TPurchaseInvoice invoice = new TPurchaseInvoice();
			invoice.setSupplierId(paymentDto.getSupplier());
			invoice.setPurchaseCost(0.0);
			invoice.setPurchaseCostTax(0.0);
			invoice.setCommision(0.0);
			invoice.setCommisionTax(0.0);
			invoice.setRoadTax(0.0);
			invoice.setRecycle(0.0);
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_TAKE_OUT_STOCK);
			invoice.setOtherCharges(paymentDto.getAmount());
			// if (!AppUtil.isObjectEmpty(stk)) {
			final TStock stock = stockRepository.findOneByStockNo(stk);
			invoice.setStockNo(stock.getStockNo());
			invoice.setChassisNo(stock.getChassisNo());
			// }
			invoice.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_PRCHSINVC));
			invoice.setStatus(Constants.INV_STATUS_NEW);
			invoice.setInvoiceDate(paymentDto.getInvoiceDate());
			invoice.setAuctionHouseId(new ObjectId(paymentDto.getPurchasedAuctionHouse()));
			invoice.setCarTaxClaimStatus(Constants.TPURCHASEINVOICE_CARTAX_NOT_CLAIMED);
			invoice.setRecycleClaimStatus(Constants.TPURCHASEINVOICE_RECYCLE_NOT_CLAIMED);
			invoice.setPurchaseTaxClaimStatus(Constants.TPURCHASEINVOICE_PURCHASETAX_NOT_CLAIMED);
			invoice.setCommisionTaxClaimStatus(Constants.TPURCHASEINVOICE_COMMISSIONTAX_NOT_CLAIMED);
			invoice.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
			invoice.setPurchaseType(Constants.ACCOUNTS_AUCTION_PAYMENT);
			listInvoice.add(invoice);
		});

		tPurchaseInvoiceRepository.saveAll(listInvoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/purchased/save")
	@Transactional
	public ResponseEntity<Response> purchasedSave(@RequestBody List<Document> data) {
		data.stream().forEach(p -> {
			final TPurchaseInvoice auctionInvoice = tPurchaseInvoiceRepository.findOneByCode(p.getString("id"));
			if (auctionInvoice != null) {
				auctionInvoice
						.setPurchaseCost(Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("purchaseCost"), "0.0")));

				auctionInvoice.setCommision(Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("commision"), "0.0")));

				auctionInvoice.setRecycle(Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("recycle"), "0.0")));

				auctionInvoice
						.setOtherCharges(Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("otherCharges"), "0.0")));
				auctionInvoice.setRoadTax(Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("roadTax"), "0.0")));
				auctionInvoice.setPurchaseCostTax(
						Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("purchaseCostTax"), "0.0")));

				auctionInvoice
						.setCommisionTax(Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("commisionTax"), "0.0")));
				auctionInvoice.setOtherChargesTax(
						Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("otherChargesTax"), "0.0")));
				auctionInvoice.setPurchaseCostTaxAmount(
						Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("purchaseCostTaxAmount"), "0.0")));

				auctionInvoice.setCommisionTaxAmount(
						Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("commisionTaxAmount"), "0.0")));
				auctionInvoice.setOthersCostTaxAmount(
						Double.valueOf(AppUtil.ifNullOrEmpty(p.getString("othersCostTaxAmount"), "0.0")));

				tPurchaseInvoiceRepository.save(auctionInvoice);

			} else {
				throw new AAJRuntimeException("Purcahse invoice not found for stock ::" + p.getString("id"));
			}
		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/isfound")
	@ResponseBody
	public boolean isfound(@RequestParam("stockNo") String stockNo) {
		return stockRepository.existsByStockNo(stockNo);
	}

	@GetMapping("/purchased")
	public ModelAndView stockPurchased(ModelAndView modelAndView) {

		modelAndView.setViewName("shipping.dashboard.purchased");
		return modelAndView;
	}

	@GetMapping("/purchased-data")
	@ResponseBody
	public DatatableResponse getPurchasedData(@RequestParam("screen") String screen) {
		return new DatatableResponse(stockRepository.getAllPurchasedData(screen));

	}

	@GetMapping("/purchase-confirmed")
	public ModelAndView stockPurchaseconfirmed(ModelAndView modelAndView) {

		modelAndView.setViewName("shipping.dashboard.purchaseConfirmed");
		return modelAndView;
	}

	@GetMapping("/purchaseconfirmed-data")
	@ResponseBody
	public DatatableResponse getPurchaseConfirmedData() {
		return new DatatableResponse(stockRepository.getAllPurchaseconfirmedData());
	}

	@GetMapping("/purchaseconfirmed-data/month-wise")
	@ResponseBody
	public DatatableResponse getPurchaseMonthWiseConfirmedData(@RequestParam("period") String period,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date to) {
		Date currentDate = new Date();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(currentDate);
		if (period.equalsIgnoreCase("alldata")) {
			return new DatatableResponse(stockRepository.getAllPurchaseconfirmedData());
		} else {
			if (period.equalsIgnoreCase("last3months")) {
				cal.add(Calendar.DATE, -90);
				from = AppUtil.atStartOfDay(cal.getTime());
				to = AppUtil.atEndOfDay(currentDate);
			} else if (period.equalsIgnoreCase("thismonth")) {
				from = AppUtil.startDateOfMonth(currentDate);
				to = AppUtil.atEndOfDay(currentDate);
			} else if (period.equalsIgnoreCase("lastmonth")) {
				from = AppUtil.startDateOfMonth(AppUtil.addMonths(currentDate, -1));
				to = AppUtil.endDateOfMonth(from);
			} else if (period.equalsIgnoreCase("period")) {
				from = AppUtil.atStartOfDay(from);
				to = AppUtil.atEndOfDay(AppUtil.isObjectEmpty(to) ? currentDate : to);
			}

			return new DatatableResponse(stockRepository.getAllMonthWisePurchaseconfirmedData(period, from, to));
		}

	}

	@GetMapping("/stock-search")
	public ModelAndView stockSearch(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.stock.search");
		return modelAndView;
	}

	@PutMapping("/reserve")
	public ResponseEntity<Response> reserve(@RequestBody List<StockReservedDto> reserveInfoList,
			@RequestParam("custId") String custId) {
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		try {
			reserveInfoList.forEach(data -> stockService.reserveStock(data.getStockNo(), custId,
					loggedInUser.getUserId(), data.getPrice()));
		} catch (final AAJRuntimeException e) {
			return new ResponseEntity<>(new Response("failed", e.getMessage(), null), HttpStatus.OK);
		}
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/unReserve")
	public ResponseEntity<Response> unReserve(@RequestBody List<String> stockNo) {
		final List<TStock> stockDetails = stockRepository.findAllByStockNoIn(stockNo);

		for (final TStock stk : stockDetails) {
			final Date date1 = stk.getReservedInfo().getDate();
			final Date date2 = new Date();
			if (securityService.findLoggedInUser().getRole().equalsIgnoreCase(Constants.ROLE_SALES_ADMIN)
					|| securityService.findLoggedInUser().getRole().equalsIgnoreCase(Constants.ROLE_SALES_MANAGER)
					|| securityService.findLoggedInUser().getRole().equalsIgnoreCase(Constants.OVERALL_ADMIN)) {
				stockRepository.stockUnreserve(stockNo);
				final TCustomer customer = customerRepository.findOneByCode(stk.getReservedInfo().getCustomerId());
				customerTransactionService.customerTransactionEntry(new TCustomerTransaction(customer.getCode(),
						stk.getStockNo(), stk.getReservedInfo().getCurrency(), Constants.TRANSACTION_CREDIT,
						stk.getReservedInfo().getPrice()));
			} else {
				if (Math.abs(date1.getTime() - date2.getTime()) > MILLIS_PER_DAY) {
					return new ResponseEntity<>(
							new Response("failure", "Please contact respective Sales Manager.", null), HttpStatus.OK);

				} else {
					final TCustomer customer = customerRepository.findOneByCode(stk.getReservedInfo().getCustomerId());
					customerTransactionService.customerTransactionEntry(new TCustomerTransaction(customer.getCode(),
							stk.getStockNo(), stk.getReservedInfo().getCurrency(), Constants.TRANSACTION_CREDIT,
							stk.getReservedInfo().getPrice()));
					stockRepository.stockUnreserve(stockNo);
				}
			}
		}

		return new ResponseEntity<>(new Response("success", "Stock Unreserved Successfully.", null), HttpStatus.OK);
	}

	@PutMapping("/lock")
	public ResponseEntity<Response> reserve(@RequestBody List<String> stockNos) {
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		stockRepository.lockStocks(stockNos, loggedInUser.getUserId(), loggedInUser.getUsername());
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/unlock")
	public ResponseEntity<Response> unLock(@RequestBody List<String> stockNos) {
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		stockRepository.unLockStocks(stockNos, loggedInUser.getUserId(), loggedInUser.getUsername());
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("search/stockNos")
	public ResponseEntity<Response> searchCustomer(@RequestParam("search") String search) {
		final List<TStock> stock = stockRepository.findBySearchTerms(search);
		return new ResponseEntity<>(new Response("success", stock), HttpStatus.OK);
	}

	@GetMapping("/details/{stockNo}")
	public ModelAndView stockDetails(ModelAndView modelAndView, @PathVariable String stockNo) {
		modelAndView.setViewName("shipping.stockview");
		modelAndView.addObject("stockNo", stockNo);
		return modelAndView;
	}

	@GetMapping("details/data/{stockNo}")
	public ResponseEntity<Response> getStockDetails(@PathVariable("stockNo") String stockNo) {
		StockDetailsDto stockDetailsDto = stockRepository.findOneStockDetailsByStockNo(stockNo);
		List<S3ObjectSummary> summary = s3FactoryService.getNoOfAttachment(stockDetailsDto.getChassisNo(),
				stockDetailsDto.getDestinationCountry(), stockDetailsDto.getSupplierName());

		final String imageBaseURl = FbFeedConstants.IMAGE_URL;
		List<String> urls = new ArrayList<String>();
		summary.stream().forEach(data -> {
			String url = imageBaseURl + data.getKey();
			urls.add(url);
		});
		stockDetailsDto.setNoOfAttachments(summary.size());
		stockDetailsDto.setImageUrls(urls);
		return new ResponseEntity<>(new Response("success", stockDetailsDto), HttpStatus.OK);
	}

	@GetMapping("/availableForShipping/datasource")
	public ResponseEntity<Response> availableForShipping() {
		return new ResponseEntity<>(new Response("success", stockRepository.findAllAvailableStocksForShipping()),
				HttpStatus.OK);
	}

	@PutMapping("/re-auction/create")
	public ResponseEntity<Response> reAuction(@RequestBody List<TReAuction> reauction) {
		stockService.saveReAuctionAndChangeStockStatus(reauction);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/cancel/create")
	public ResponseEntity<Response> cancel(@RequestBody List<TStock> stocks) {
		for (TStock stockToSave : stocks) {
			TStock stockToUpdate = this.stockRepository.findOneByStockNo(stockToSave.getStockNo());
			stockToUpdate.setRemarks(stockToUpdate.getRemarks() + " " + stockToSave.getVehicleRemarks());
			stockToUpdate.setStatus(Constants.STOCK_STATUS_CANCEL);
			this.stockRepository.save(stockToUpdate);
		}

		return new ResponseEntity<>(new Response("success", stocks), HttpStatus.OK);

	}

	@PutMapping("/edit/re-auction")
	public ResponseEntity<Response> editReAuction(@RequestBody TReAuction reauction) {
		TReAuction reAuctionUpdate = reAuctionRepository.findOneById(reauction.getId());
		reAuctionUpdate.setReauctionDate(reauction.getReauctionDate());
		reAuctionUpdate.setAuctionCompany(reauction.getAuctionCompany());
		reAuctionUpdate.setAuctionHouse(reauction.getAuctionHouse());
		reAuctionRepository.save(reAuctionUpdate);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/re-auction/list")
	public ModelAndView reAuctionList() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("stock.re_auction");
		return modelAndView;
	}

	@GetMapping("/re-auction/list/datasource")
	@ResponseBody
	public DatatableResponse reAuctionListDatasource() {
		return new DatatableResponse(reAuctionRepository.getTReAuctionInitiatedList());
	}

	@PutMapping("/purchased/cancel-update")
	public ResponseEntity<Response> cancelUpdate(@RequestParam("stockNo") String stockNo) {
		stockRepository.updateStatusByStockNo(Constants.STOCK_STATUS_CANCEL, stockNo);
		tPurchaseInvoiceRepository.updatePurchaseStatusByStockNo(Constants.INV_STATUS_CANCEL, stockNo);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@DeleteMapping("/purchased/invoice/delete")
	@Transactional
	public ResponseEntity<Response> deleteInvoice(@RequestParam("invoiceNo") String invoiceNo,
			@RequestParam("type") String type) {
		if (!type.equals("PURCHASE")) {
			TPurchaseInvoice invoice = tPurchaseInvoiceRepository.findOneByCode(invoiceNo);
			if (invoice.getType().equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_CHARGES)
					|| invoice.getType().equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_CANCELLATION_PENALTY_CHARGES)) {
				TPurchaseInvoice cancelledInvoice = tPurchaseInvoiceRepository
						.findOneByStockNoAndType(invoice.getStockNo(), Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);
				cancelledInvoice.setStatus(Constants.INV_STATUS_CANCEL);
				tPurchaseInvoiceRepository.save(cancelledInvoice);
			}
			if (invoice.getType().equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_REAUCTION)) {
				TPurchaseInvoice cancelledInvoice = tPurchaseInvoiceRepository
						.findOneByStockNoAndType(invoice.getStockNo(), Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);
				cancelledInvoice.setStatus(Constants.INV_STATUS_CANCEL);
				tPurchaseInvoiceRepository.save(cancelledInvoice);
				TReAuction cancelledReauction = reAuctionRepository.findOneByStockNo(invoice.getStockNo());
				cancelledReauction.setStatus(Constants.REAUCTION_STATUS_INITIATED);
				cancelledReauction.setSoldPrice(0.0);
				cancelledReauction.setShuppinCommission(0.0);
				cancelledReauction.setSoldCommission(0.0);
				reAuctionRepository.save(cancelledReauction);
			}

			tPurchaseInvoiceRepository.delete(invoice);
		}
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping(value = { "/re-auction/cancel/dashboard" })
	public ModelAndView invoiceEntry(ModelAndView modelAndView) {

		modelAndView.setViewName("shipping.re-auction.cancel.dashboard");
		return modelAndView;
	}

	@PutMapping("/re-auction/edit")
	@Transactional
	public ResponseEntity<Response> reAuctionEdit(@RequestBody TReAuction reauction) {
		if (!reauction.getId().isEmpty()) {
			final TReAuction auction = reAuctionRepository.findOneById(reauction.getId());

			auction.setTax(Constants.COMMON_TAX);
			auction.setSoldPrice(reauction.getSoldPrice());
			auction.setShuppinCommission(reauction.getShuppinCommission());
			auction.setShuppinTax(Constants.COMMON_TAX);
			auction.setSoldCommission(reauction.getSoldCommission());
			auction.setSoldCommTax(Constants.COMMON_TAX);
			auction.setInvoiceDate(reauction.getInvoiceDate());
			auction.setRecycleAmount(reauction.getRecycleAmount());
			auction.setStatus(Constants.REAUCTION_STATUS_INVOICE_GENARATED);
			reAuctionRepository.save(auction);
			final Double overallCommision = AppUtil.ifNullOrEmpty(reauction.getSoldCommission(), 0.0)
					+ AppUtil.ifNullOrEmpty(reauction.getShuppinCommission(), 0.0);
			// update old invoice status
//			final TPurchaseInvoice oldInvoice = tPurchaseInvoiceRepository.findOneByStockNoAndType(auction.getStockNo(),
//					Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);
//			oldInvoice.setStatus(Constants.INV_STATUS_REAUCTION);
//			tPurchaseInvoiceRepository.save(oldInvoice);
			// create purchase invoice invoice
			final TPurchaseInvoice invoice = new TPurchaseInvoice();
			invoice.setStockNo(auction.getStockNo());
			invoice.setChassisNo(auction.getChassisNo());
			invoice.setPurchaseCost((reauction.getSoldPrice() * -1));
			invoice.setSupplierId(auction.getAuctionCompany());
			invoice.setAuctionHouseId(auction.getAuctionHouse());
			invoice.setPurchaseType("auction");
			invoice.setCommision(overallCommision);
			invoice.setCommisionTax(Constants.COMMON_TAX);
			invoice.setRecycle(reauction.getRecycleAmount() * -1);
//			invoice.setRoadTax(oldInvoice.getRoadTax() * -1);
			invoice.setPurchaseCostTax(Constants.COMMON_TAX);
			invoice.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_PRCHSINVC));
			invoice.setStatus(Constants.INV_STATUS_NEW);
			invoice.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_REAUCTION);
			invoice.setInvoiceDate(reauction.getInvoiceDate());

			tPurchaseInvoiceRepository.save(invoice);
			final Map<String, TReAuctionDto> response = new HashMap<>();
			response.put("updatedTReAuction", reAuctionRepository.getTReAuctionInitiatedListById(reauction.getId()));
			return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);
		} else {
			return null;
		}

	}

	@PutMapping("/cancel/re-auction")
	@Transactional
	public ResponseEntity<Response> reAuctionCancel(@RequestParam("reauctionId") String reauctionId) {
		if (!reauctionId.isEmpty()) {
			final TReAuction auction = reAuctionRepository.findOneById(reauctionId);
			auction.setStatus(Constants.REAUCTION_STATUS_SOLD_CANCELLED);
			reAuctionRepository.save(auction);

			TStock tStock = stockRepository.findOneByStockNo(auction.getStockNo());
			tStock.setStatus(Constants.STOCK_STATUS_PURCHASED_CONFIRMED);
			stockRepository.save(tStock);
			
			// create purchase invoice invoice
			final TPurchaseInvoice invoice = new TPurchaseInvoice();
			invoice.setStockNo(auction.getStockNo());
			invoice.setChassisNo(auction.getChassisNo());
			invoice.setPurchaseCost((auction.getSoldPrice()));
			invoice.setSupplierId(auction.getAuctionCompany());
			invoice.setAuctionHouseId(auction.getAuctionHouse());
			invoice.setPurchaseType("auction");
			invoice.setRecycle(auction.getRecycleAmount());
			invoice.setPurchaseCostTax(Constants.COMMON_TAX);
			invoice.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_PRCHSINVC));
			invoice.setStatus(Constants.INV_STATUS_NEW);
			invoice.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_REAUCTION_SOLD_AND_CANCELLED);
			invoice.setInvoiceDate(auction.getInvoiceDate());

			tPurchaseInvoiceRepository.save(invoice);
			final Map<String, TReAuctionDto> response = new HashMap<>();
			response.put("updatedTReAuction", reAuctionRepository.getTReAuctionInitiatedListById(reauctionId));
			return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);
		} else {
			return null;
		}

	}

	@PutMapping("/re-auction/inventory-details")
	@Transactional
	public ResponseEntity<Response> reAuctionInventoryDetails(@RequestBody Map<String, Object> data)
			throws ParseException {
		final String sNagareCharge = AppUtil.isObjectEmpty(data.get("nagareCharge").toString()) ? "0.0"
				: data.get("nagareCharge").toString();
		final Double nagareCharge = Double.parseDouble(sNagareCharge);

		final String sAuctionShippingCharge = AppUtil.isObjectEmpty(data.get("auctionShippingCharge").toString())
				? "0.0"
				: data.get("auctionShippingCharge").toString();
		final Double auctionShippingCharge = Double.parseDouble(sAuctionShippingCharge);

		final String invoiceDateString = AppUtil.isObjectEmpty(data.get("invoiceDate")) ? ""
				: data.get("invoiceDate").toString();
		Date invoiceDate = null;
		if (!AppUtil.isObjectEmpty(invoiceDateString)) {
			invoiceDate = new SimpleDateFormat("dd-MM-yyyy").parse(invoiceDateString);
		}

		final String id = AppUtil.isObjectEmpty(data.get("id").toString()) ? "" : data.get("id").toString();

		if (!AppUtil.isObjectEmpty(id)) {
			final TReAuction auction = reAuctionRepository.findOneById(id);
			auction.setNagareCharge(nagareCharge);
			auction.setAuctionShippingCharge(auctionShippingCharge);
			auction.setStatus(Constants.REAUCTION_STATUS_CANCELLED);
			reAuctionRepository.save(auction);

			final TStock stock = stockRepository.findOneByStockNo(auction.getStockNo());
			stock.setReservedInfo(new ReservedInfo());
			stock.setReserve(Constants.NOT_RESERVED);
			stock.setStatus(Constants.STOCK_STATUS_PURCHASED_CONFIRMED);
			stockRepository.save(stock);

//			final TPurchaseInvoice oldInvoice = tPurchaseInvoiceRepository.findOneByStockNoAndType(auction.getStockNo(),
//					Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);

			// create purchase invoice invoice
			final TPurchaseInvoice invoice = new TPurchaseInvoice();
			invoice.setStockNo(auction.getStockNo());
			invoice.setChassisNo(stock.getChassisNo());
			invoice.setSupplierId(auction.getAuctionCompany());
			invoice.setAuctionHouseId(auction.getAuctionHouse());
			invoice.setPurchaseType("auction");
			invoice.setCommision(auction.getAuctionShippingCharge());
			invoice.setCommisionTax(Constants.COMMON_TAX);
			invoice.setOtherCharges(auction.getNagareCharge());
			invoice.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_PRCHSINVC));
			invoice.setStatus(Constants.INV_STATUS_NEW);
			invoice.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
			invoice.setType(Constants.PURCHASE_INVOICE_ITEM_TYPE_REAUCTION_CANCELLED);
			invoice.setInvoiceDate(invoiceDate);

			tPurchaseInvoiceRepository.save(invoice);
			final Map<String, TReAuctionDto> response = new HashMap<>();
			response.put("updatedTReAuction", reAuctionRepository.getTReAuctionInitiatedListById(id));
			return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);
		} else {
			return null;
		}

	}

	@PutMapping("/cancelled/inventory-details")
	@Transactional
	public ResponseEntity<Response> cancelledInventoryDetails(@RequestParam("stockNo") String stockNo) {

		final TPurchaseInvoice tPurchaseInvoice = tPurchaseInvoiceRepository.findOneByStockNoAndType(stockNo,
				Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);
		tPurchaseInvoice.setStatus(Constants.INV_STATUS_NEW);
		tPurchaseInvoiceRepository.save(tPurchaseInvoice);

		final TStock stock = stockRepository.findOneByStockNo(stockNo);
		stock.setReservedInfo(new ReservedInfo());
		stock.setReserve(Constants.NOT_RESERVED);
		stock.setStatus(Constants.STOCK_STATUS_NEW);
		stockRepository.save(stock);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/purchase/cancelled/list")
	public ModelAndView cancelled(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.stock.cancelled");
		return modelAndView;
	}

	@GetMapping("/purchase/cancelled/datasource")
	@ResponseBody
	public DatatableResponse cancelledDatasource() {
		final List<CancelledStockDto> list = tPurchaseInvoiceRepository.findAllCancelledInvoice();
		return new DatatableResponse(list == null ? new ArrayList<>() : list);
	}

	@PostMapping("/purchase/cancelled/details/update")
	public ResponseEntity<Response> cancelledDetailsUpdate(@RequestParam("id") String id,
			@RequestBody Map<String, Object> data) {
		final String sCancellationCharge = AppUtil.isObjectEmpty(data.get("cancellationCharge").toString()) ? "0.0"
				: data.get("cancellationCharge").toString();
		final Double cancellationCharge = Double.parseDouble(sCancellationCharge);
		final Update update = new Update().set("cancellationCharge", cancellationCharge);
		tPurchaseInvoiceRepository.updateById(id, update);
		final CancelledStockDto cancelledStockDtoo = tPurchaseInvoiceRepository.findCancelledInvoiceById(id);
		return new ResponseEntity<>(new Response("success", cancelledStockDtoo), HttpStatus.OK);
	}

	@PostMapping("/chassisNo/purchaseDate/isSame")
	@ResponseBody
	public boolean chassisNoAndPurchaseDateIsSame(@RequestParam(value = "stockNo", required = false) String stockNo,
			@RequestParam("chassisNo") String chassisNo,
			@RequestParam("purchaseDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date purchaseDate) {
		boolean isExist = false;
		final TStock existingStock = stockRepository.findOneByStockNo(stockNo);
		if (!AppUtil.isObjectEmpty(existingStock)) {
			final TStock stock = stockRepository.findByChassisNoAndPurchaseInfoDate(chassisNo,
					AppUtil.atStartOfDay(purchaseDate), AppUtil.atEndOfDay(purchaseDate));
			if (!AppUtil.isObjectEmpty(stock)) {
			}
		} else {
			final TStock stock = stockRepository.findByChassisNoAndPurchaseInfoDate(chassisNo,
					AppUtil.atStartOfDay(purchaseDate), AppUtil.atEndOfDay(purchaseDate));
			if (!AppUtil.isObjectEmpty(stock)) {
				isExist = true;
			}
		}
		return isExist;
	}

	@Transactional
	@PostMapping(path = "/create/auction/payment/category")
	public ResponseEntity<Response> addCategory(@RequestBody final AuctionPaymentType data) {
		final String code = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_AUCTIONCATEGORYTYPE);
		data.setCode(code);
		auctionPaymentTypeRepository.save(data);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping("/edit/history")
	@ResponseBody
	public DatatableResponse getStockInfo(@RequestParam("stockNo") String stockNo) {

		List<TStockEditHistoryDto> stockEditHistoryList = this.stockService.findAllByStockNo(stockNo);
		return new DatatableResponse(stockEditHistoryList);
	}
}
