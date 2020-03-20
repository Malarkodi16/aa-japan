/*
 * Copyright (c) 2018 - AAJ
 * @Date:
 * @Last Modified by: Rajlakshman(Nexware)
 * @Last Modified time: 2018-11-15
 */
package com.nexware.aajapan.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.AccountTransactionConstants;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.models.TDayBook;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TDayBookRepository;
import com.nexware.aajapan.repositories.TFreightShippingInvoiceRepository;
import com.nexware.aajapan.repositories.TInsuranceRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.services.ClaimService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.services.TPurchaseInvoiceService;

@Controller

@RequestMapping("accounts/claim")
public class ClaimController {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TPurchaseInvoiceRepository tpurchaseInvoiceRepository;
	@Autowired
	private TAccountsTransactionService accountsTransactionService;
	@Autowired
	private TPurchaseInvoiceService purchaseInvoiceService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private TInsuranceRepository insuranceRepository;
	@Autowired
	private TFreightShippingInvoiceRepository freightShippingInvoiceRepository;
	@Autowired
	private TDayBookRepository dayBookRepository;

	@GetMapping("/tax")
	public ModelAndView tax(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.claim.tax");
		return modelAndView;
	}

	@GetMapping("/recycle")
	public ModelAndView recycle(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.claim.recycle");
		return modelAndView;
	}

	@GetMapping("/cartax")
	public ModelAndView cartax(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.claim.cartax");
		return modelAndView;
	}

	@GetMapping("/insurance")
	public ModelAndView insurance(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.claim.insurance");
		return modelAndView;
	}

	@GetMapping("/radiation")
	public ModelAndView radiation(ModelAndView modelAndView) {
		// To set necessary models for view
		modelAndView.setViewName("accounts.claim.radiation");
		return modelAndView;
	}

	@GetMapping("/radiation/list/datasource")
	@ResponseBody
	public DatatableResponse radiationNotClaimedList() {
		return new DatatableResponse(this.freightShippingInvoiceRepository.getAllNotClaimedRadiationStatus());
	}

	@GetMapping("/recycle/list/datasource")
	@ResponseBody
	public DatatableResponse recycleNotClaimedList() {
		return new DatatableResponse(this.tpurchaseInvoiceRepository.getAllNotClaimedRecycleClaimStatus());
	}

	@GetMapping("/tax/list/datasource")
	@ResponseBody
	public DatatableResponse taxNotClaimedList(@RequestParam("show") int show) {
		if (show == 0) {
			return new DatatableResponse(this.tpurchaseInvoiceRepository.getAllCarTaxReceivable(
					Constants.TPURCHASEINVOICE_COMMISSIONTAX_NOT_CLAIMED,
					Constants.TPURCHASEINVOICE_PURCHASETAX_NOT_CLAIMED));
		} else if (show == 2) {
			return new DatatableResponse(this.tpurchaseInvoiceRepository.getAllCarTaxOnStatus(
					Constants.TPURCHASEINVOICE_PURCHASETAX_RECEIVED,
					Constants.TPURCHASEINVOICE_COMMISSIONTAX_RECEIVED));
		}
		return new DatatableResponse(new ArrayList<>());
	}

	@PostMapping("/recycle")
	@Transactional
	public ResponseEntity<Response> recycleClaim(@RequestParam("id") String id) {
		TPurchaseInvoice tPurchaseInvoice = this.tpurchaseInvoiceRepository.findOneByid(id);
		tPurchaseInvoice.setRecycleClaimStatus(Constants.TPURCHASEINVOICE_RECYCLE_RECEIVED);
		this.tpurchaseInvoiceRepository.save(tPurchaseInvoice);
		this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(null, null,
				AccountTransactionConstants.RECYCLE_RECOVERABLE, Constants.CURRENCY_YEN, Constants.TRANSACTION_CREDIT,
				tPurchaseInvoice.getRecycle(), AccountTransactionConstants.ACCOUNT_TRANSACTION_RECYCLE_CLAIM,
				tPurchaseInvoice.getInvoiceDate()));

		return new ResponseEntity<>(
				new Response("success", this.tpurchaseInvoiceRepository.getNotClaimedRecycleClaimById(id)),
				HttpStatus.OK);
	}

	@PostMapping("/recycle/received")
	public ResponseEntity<Response> recycleReceived(@RequestParam("id") String id,
			@RequestBody Map<String, Object> data) {

		return new ResponseEntity<>(
				new Response("success", this.tpurchaseInvoiceRepository.getNotClaimedRecycleClaimById(id)),
				HttpStatus.OK);
	}

	@GetMapping("/cartax/list/datasource")
	@ResponseBody
	public DatatableResponse carTaxNotClaimedList() {
		return new DatatableResponse(this.tpurchaseInvoiceRepository.getAllNotClaimedCarTaxClaimStatus());
	}

	@PostMapping("/carTax")
	public ResponseEntity<Response> carTaxClaim(@RequestParam("id") String id) {
		TPurchaseInvoice tPurchaseInvoice = this.tpurchaseInvoiceRepository.findOneByid(id);
		tPurchaseInvoice.setCarTaxClaimStatus(Constants.TPURCHASEINVOICE_CARTAX_CLAIMED);
		this.tpurchaseInvoiceRepository.save(tPurchaseInvoice);
		return new ResponseEntity<>(
				new Response("success", this.tpurchaseInvoiceRepository.getNotClaimedCarTaxClaimById(id)),
				HttpStatus.OK);
	}

	@PostMapping("/carTax/received")
	public ResponseEntity<Response> carTaxReceived(@RequestParam("dayBookId") String dayBookId,
			@RequestParam("receivedDate") @DateTimeFormat(pattern = "dd-MM-yyyy") final Date receivedDate,
			@RequestBody List<Map<String, Object>> data) {
		double sumValue = data.stream().mapToDouble(a -> Double.parseDouble((String) a.get("amount"))).sum();
		TDayBook dayBookentry = this.dayBookRepository.findOneByDaybookId(dayBookId);
		data.forEach(loop -> {
			String id = loop.get("id").toString();
			String chassisNo = loop.get("chassisNo").toString();
			Double amount = Double.parseDouble(loop.get("amount").toString());
			this.purchaseInvoiceService.carTaxReceived(id, amount, receivedDate, chassisNo);
		});
		this.accountsTransactionService.accountTransactionEntry(new TAccountsTransaction(dayBookentry.getDaybookId(),
				dayBookentry.getRemitter(), AccountTransactionConstants.CAR_TAX_CLEARING_ACCOUNT,
				dayBookentry.getCurrency(), Constants.TRANSACTION_DEBIT, sumValue,
				AccountTransactionConstants.ACCOUNT_TRANSACTION_DAYBOOK, dayBookentry.getRemitDate()));
		dayBookentry.setCartaxClaimedStatus(Constants.TPURCHASEINVOICE_CARTAX_CLAIMED);
		this.dayBookRepository.save(dayBookentry);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/purchase-claim")
	public ResponseEntity<Response> purchaseClaim(@RequestParam("code") String code) {
		TPurchaseInvoice invoice = this.tpurchaseInvoiceRepository.findOneByCode(code);
		invoice.setPurchaseTaxClaimStatus(Constants.TPURCHASEINVOICE_PURCHASETAX_RECEIVED);
		this.tpurchaseInvoiceRepository.save(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/commission-claim")
	public ResponseEntity<Response> commissionClaim(@RequestParam("code") String code) {
		TPurchaseInvoice invoice = this.tpurchaseInvoiceRepository.findOneByCode(code);
		invoice.setCommisionTaxClaimStatus(Constants.TPURCHASEINVOICE_COMMISSIONTAX_RECEIVED);
		this.tpurchaseInvoiceRepository.save(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/insurance/apply/uploadExcelFile")
	public ResponseEntity<Response> claimReceive(@RequestParam("excelFile") final MultipartFile file,
			HttpServletRequest request, RedirectAttributes redirectAttributes,
			@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date insuranceApplyDate) throws IOException {
		String successMessage = claimService.applyInsuranceByExcel(file, insuranceApplyDate);
		return new ResponseEntity<>(new Response("success", successMessage), HttpStatus.OK);
	}

	@GetMapping("/insurance/applied/datasource")
	public ResponseEntity<DatatableResponse> claimReceive() {
		return new ResponseEntity<>(new DatatableResponse(insuranceRepository.findAll()), HttpStatus.OK);

	}
}
