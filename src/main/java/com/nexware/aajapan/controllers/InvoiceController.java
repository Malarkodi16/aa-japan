package com.nexware.aajapan.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.dto.SalesInvoiceDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.models.MLogin;
import com.nexware.aajapan.models.ProformaInvoiceItem;
import com.nexware.aajapan.models.SalesInvoiceDetail;
import com.nexware.aajapan.models.ShippingInstructionInfo;
import com.nexware.aajapan.models.TBlTransaction;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TNotification;
import com.nexware.aajapan.models.TProformaInvoice;
import com.nexware.aajapan.models.TSalesInvoice;
import com.nexware.aajapan.models.TShippingInstruction;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.LoginRepository;
import com.nexware.aajapan.repositories.MCurrencyRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TBankTransactionRepository;
import com.nexware.aajapan.repositories.TBlTransactionRepository;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.repositories.TProformaInvoiceRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.repositories.TShippingInstructionRepository;
import com.nexware.aajapan.services.SalesOrderService;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TBlTransactionService;
import com.nexware.aajapan.services.TNotificationService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("invoice")
public class InvoiceController {

	@Autowired
	private TProformaInvoiceRepository proformaInvoiceOrderRepository;
	@Autowired
	private TShippingInstructionRepository shippingInstructionRepository;
	@Autowired
	private TBlTransactionRepository tBlTransactionRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private SalesOrderService salesOrderService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private TNotificationService notificationService;
	@Autowired
	private TBlTransactionService blTransactionService;
	@Autowired
	private TSalesInvoiceRepository salesInvoiceRepository;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private LoginRepository loginRepository;
	@Autowired
	private TCustomerRepository customerRepository;

	@Autowired
	private MCurrencyRepository mCurrencyRepository;

	// Proforma Invoice Save
	@PostMapping(path = "/proforma/order/save")
	@ResponseBody
	public ResponseEntity<Response> proformaInvoiceSave(@RequestBody TProformaInvoice proformaInvoice) {
		final Map<String, Object> response = new HashMap<>();
		final MLoginDto loginDto = securityService.findLoggedInUser();
		final MCurrency mCurrency = mCurrencyRepository.findOneByCurrencySeq(proformaInvoice.getCurrencyType());
		proformaInvoice.setInvoiceNo(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_PINV));
		proformaInvoice.setExchangeRate(mCurrency.getSalesExchangeRate());
		proformaInvoice.setDate(new Date());
		proformaInvoice.setSalesPerson(loginDto.getUserId());
		proformaInvoiceOrderRepository.save(proformaInvoice);
		response.put("orderNo", proformaInvoice.getInvoiceNo());
		return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);
	}

	@PostMapping(path = "/proforma/invoice/edit")
	@ResponseBody
	public ResponseEntity<Response> proformaInvoiceSave(@RequestParam("invoiceId") String invoiceId,
			@RequestBody TProformaInvoice proformaInvoice) {

		final MCurrency mCurrency = mCurrencyRepository.findOneByCurrencySeq(proformaInvoice.getCurrencyType());
		final TProformaInvoice invoiceToEdit = proformaInvoiceOrderRepository.findOneByInvoiceNo(invoiceId);
		invoiceToEdit.setCustomerId(proformaInvoice.getCustomerId());
		invoiceToEdit.setConsigneeId(proformaInvoice.getConsigneeId());
		if (!AppUtil.isObjectEmpty(proformaInvoice.getNotifypartyId())) {
			invoiceToEdit.setNotifypartyId(proformaInvoice.getNotifypartyId());
		}
		invoiceToEdit.setPaymentType(proformaInvoice.getPaymentType());
		invoiceToEdit.setCurrencyType(proformaInvoice.getCurrencyType());
		invoiceToEdit.setItems(proformaInvoice.getItems());
		invoiceToEdit.setExchangeRate(mCurrency.getSalesExchangeRate());
		proformaInvoiceOrderRepository.save(invoiceToEdit);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	// Sales Order save
	@PostMapping(path = "/sales/order/save")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> salesInvoiceSave(@RequestBody SalesInvoiceDto salesInvoiceDto) {
		final TSalesInvoice salesinvoice = salesInvoiceDto.getInvoiceDtl();
		final List<SalesInvoiceDetail> salesInvoiceDetail = salesInvoiceDto.getInvoiceItems();
		final MLoginDto loginDto = securityService.findLoggedInUser();
		final List<TSalesInvoice> invoices = new ArrayList<>();
		final String invoiceNo = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SINV);
		final MCurrency mCurrency = mCurrencyRepository.findOneByCurrencySeq(salesinvoice.getCurrencyType());
		final MCurrency usd = mCurrencyRepository.findOneByCurrencySeq(Constants.CURRENCY_USD);
		TCustomer customer=customerRepository.findOneByCode(salesinvoice.getCustomerId());
		List<String> salesInvoiceStock=salesInvoiceDetail.stream().map(item->item.getStockNo()).collect(Collectors.toList());
		HashSet<String> proformaInvoiceCreatedStockId=new HashSet<>();
		if(customer.isLcCustomer()) {
			List<TProformaInvoice> invoice=proformaInvoiceOrderRepository.findByCustomerId(salesinvoice.getCustomerId());
			for (TProformaInvoice tProformaInvoice : invoice) {
				List<ProformaInvoiceItem> proformaInvoiceItems=tProformaInvoice.getItems();
				for (ProformaInvoiceItem item : proformaInvoiceItems) {
					proformaInvoiceCreatedStockId.add(item.getStockNo());
				}
			}
			if(!proformaInvoiceCreatedStockId.containsAll(salesInvoiceStock)) {
				return new ResponseEntity<>(new Response("failed", "Proforma invoice not created",""), HttpStatus.OK);
			}
		}
		salesInvoiceDetail.forEach(item -> {
			final TSalesInvoice invoice = new TSalesInvoice(salesinvoice.getCustomerId(),
					Constants.CUSTOMER_FLAG_CUSTOMER, salesinvoice.getConsigneeId(), salesinvoice.getNotifypartyId(),
					salesinvoice.getCurrencyType(), salesinvoice.getPaymentType(), item.getStockNo());
			invoice.setInvoiceNo(invoiceNo);
			invoice.setDollarRate(usd.getExchangeRate());
			invoice.setStatus(Constants.SALES_INV_PAYMENT_NOT_RECEIVED);
			invoice.setAmountReceived(0.0);
			invoice.setAmountAllocatted(0.0);
			invoice.setExchangeRate(mCurrency.getSalesExchangeRate());
			invoice.setSalesPerson(loginDto.getUserId());
			invoice.setTotal(item.getTotal());
			invoices.add(invoice);
		});

		salesOrderService.createSalesOrder(invoices);
		return new ResponseEntity<>(new Response("success", invoiceNo), HttpStatus.OK);
	}

	@PostMapping(path = "/sales/order/edit")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> salesInvoiceEdit(@RequestBody SalesInvoiceDto salesInvoiceDto) {
		final TSalesInvoice salesinvoice = salesInvoiceDto.getInvoiceDtl();
		final List<SalesInvoiceDetail> salesInvoiceDetail = salesInvoiceDto.getInvoiceItems();
		final List<TSalesInvoice> invoices = new ArrayList<>();

		String invoiceNo = salesinvoice.getInvoiceNo();

		salesInvoiceDetail.forEach(item -> {
			final TSalesInvoice invoice = this.salesInvoiceRepository.findOneByInvoiceNoAndStockNo(invoiceNo,
					item.getStockNo());
			invoice.setCustomerId(salesinvoice.getCustomerId());
			invoice.setConsigneeId(salesinvoice.getConsigneeId());
			invoice.setNotifypartyId(salesinvoice.getNotifypartyId());
			invoice.setPaymentType(salesinvoice.getPaymentType());
			invoice.setCurrencyType(salesinvoice.getCurrencyType());
			invoice.setTotal(item.getTotal());
			invoices.add(invoice);
		});

		salesOrderService.editSalesOrderTransaction(invoices);
		return new ResponseEntity<>(new Response("success", invoiceNo), HttpStatus.OK);
	}

	@PutMapping(path = "/sales/order/cancel/{invoiceNo}")
	public ResponseEntity<Response> salesInvoiceSave(@PathVariable("invoiceNo") String invoiceNo) {
		try {
			salesOrderService.cancelSalesOrder(invoiceNo);
		} catch (final AAJRuntimeException e) {
			return new ResponseEntity<>(new Response("failed", e.getMessage(), null), HttpStatus.OK);
		}

		List<TSalesInvoice> listInvoice = this.salesInvoiceRepository.findAllByInvoiceNo(invoiceNo);
		TSalesInvoice invoice = listInvoice.stream().findFirst().get();
		// notification for Shipping Instruction to Shipping Persons entry /./.start
		final List<TNotification> notifications = notificationService.cancelSalesOrderNotification(invoiceNo,
				invoice.getSalesPerson());
		MLogin login = this.loginRepository.findOneByUserId(invoice.getSalesPerson());
		notificationService.notify(notifications, login.getUsername());
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	// Shipping Instruction save
	@PostMapping(path = "/shipping/order/save")
	@Transactional
	public ResponseEntity<Response> shippingInvoiceSave(@RequestBody List<TShippingInstruction> shippingInstruction) {

		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		// create shipping instruction
		shippingInstruction.forEach(shippingInstructions -> {
			shippingInstructions
					.setShippingInstructionId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SPNGINSTN));
			shippingInstructions.setStatus(Constants.SHIPPING_INSTRUCTION_GIVEN);
			shippingInstructions.setSalesPersonId(loggedInUser.getUserId());
			shippingInstructions.setDeleteStatus(Constants.DELETE_FLAG_0);
		});
		shippingInstructionRepository.saveAll(shippingInstruction);
		shippingInstruction.forEach(shippingInstructions -> {
			// update shipping instruction status
			final ShippingInstructionInfo shippingInstructionInfo = new ShippingInstructionInfo(
					shippingInstructions.getCustomerId(), loggedInUser.getUserId(),
					shippingInstructions.getShippingInstructionId());
			TStock tStock = this.stockRepository.findOneByStockNo(shippingInstructions.getStockNo());
			tStock.setShippingInstructionStatus(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_ARRANGED);
			tStock.setShippingInstructionInfo(shippingInstructionInfo);
			tStock.setInspectionFlag(shippingInstructions.getInspectionFlag());
			this.stockRepository.save(tStock);

		});

		// save bl transaction value
		blTransactionService.saveBlTransaction(shippingInstruction);

		// notification for Shipping Instruction to Shipping Persons entry /./.start
		final List<TNotification> notifications = notificationService
				.notifyAboutShippingInstruction(shippingInstruction);
		notificationService.notify(notifications, "shipping@aaj.com");
		// notification for Shipping Instruction to Shipping Persons entry /./.end

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	// Sales Order save
	@PostMapping(path = "branch/sales/order/save")
	@ResponseBody
	public ResponseEntity<Response> branchSalesInvoiceSave(@RequestBody SalesInvoiceDto salesInvoiceDto) {
		final TSalesInvoice salesinvoice = salesInvoiceDto.getInvoiceDtl();
		final List<SalesInvoiceDetail> salesInvoiceDetail = salesInvoiceDto.getInvoiceItems();
		final MLoginDto loginDto = securityService.findLoggedInUser();
		final List<TSalesInvoice> invoices = new ArrayList<>();
		final String invoiceNo = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SINV);
		final MCurrency mCurrency = mCurrencyRepository.findOneByCurrencySeq(salesinvoice.getCurrencyType());
		final MCurrency usd = mCurrencyRepository.findOneByCurrencySeq(Constants.CURRENCY_USD);
		salesInvoiceDetail.forEach(item -> {
			final TSalesInvoice invoice = new TSalesInvoice(salesinvoice.getCustomerId(),
					Constants.CUSTOMER_FLAG_BRANCH, salesinvoice.getConsigneeId(), salesinvoice.getNotifypartyId(),
					salesinvoice.getCurrencyType(), salesinvoice.getPaymentType(), item.getStockNo());
			invoice.setInvoiceNo(invoiceNo);
			invoice.setDollarRate(usd.getSalesExchangeRate());
			invoice.setStatus(Constants.SALES_INV_PAYMENT_NOT_RECEIVED);
			invoice.setAmountReceived(0.0);
			invoice.setAmountAllocatted(0.0);
			invoice.setExchangeRate(mCurrency.getExchangeRate());
			invoice.setSalesPerson(loginDto.getUserId());
			invoice.setTotal(item.getTotal());
			invoices.add(invoice);
		});

		salesOrderService.createSalesOrder(invoices);
		return new ResponseEntity<>(new Response("success", invoiceNo), HttpStatus.OK);
	}

	@PutMapping(path = "/shipping/order/edit")
	public ResponseEntity<Response> shippingInstructionEdit(@RequestBody TShippingInstruction shippingInstruction) {
		TShippingInstruction tShippingInstruction = this.shippingInstructionRepository
				.findOneByShippingInstructionId(shippingInstruction.getShippingInstructionId());
		tShippingInstruction.setConsigneeId(shippingInstruction.getConsigneeId());
		tShippingInstruction.setNotifypartyId(shippingInstruction.getNotifypartyId());
		tShippingInstruction.setDestCountry(shippingInstruction.getDestCountry());
		tShippingInstruction.setDestPort(shippingInstruction.getDestPort());
		tShippingInstruction.setPaymentType(shippingInstruction.getPaymentType());
		tShippingInstruction.setInspectionFlag(shippingInstruction.getInspectionFlag());
		tShippingInstruction.setScheduleType(shippingInstruction.getScheduleType());
		tShippingInstruction.setYard(shippingInstruction.getYard());
		tShippingInstruction.setInspectionFlag(shippingInstruction.getInspectionFlag());
		tShippingInstruction.setEstimatedDeparture(shippingInstruction.getEstimatedDeparture());
		tShippingInstruction.setEstimatedArrival(shippingInstruction.getEstimatedArrival());
		this.shippingInstructionRepository.save(tShippingInstruction);

		TStock tStock = this.stockRepository.findOneByStockNo(tShippingInstruction.getStockNo());
		tStock.setShippingInstructionStatus(tShippingInstruction.getStatus());
		tStock.setInspectionFlag(shippingInstruction.getInspectionFlag());
		this.stockRepository.save(tStock);

		TBlTransaction tBlTransaction = this.tBlTransactionRepository
				.findOneByShippingInstructionId(shippingInstruction.getShippingInstructionId());
		tBlTransaction.setConsigneeId(shippingInstruction.getConsigneeId());
		this.tBlTransactionRepository.save(tBlTransaction);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
}