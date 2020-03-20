package com.nexware.aajapan.controllers;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TDayBook;
import com.nexware.aajapan.models.TDayBookTransaction;
import com.nexware.aajapan.models.TTransportInvoice;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.BillOfExchangeRepo;
import com.nexware.aajapan.repositories.MCOARepository;
import com.nexware.aajapan.repositories.MCurrencyRepository;
import com.nexware.aajapan.repositories.MRemitTypeRep;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.repositories.TDayBookTransactionRepository;
import com.nexware.aajapan.services.TDayBookService;
import com.nexware.aajapan.services.TLcService;

@Controller
@RequestMapping("daybook")
public class TDayBookController {
	@Autowired
	private TDayBookTransactionRepository dayBookTransactionRepository;
	@Autowired
	private TDayBookService dayBookService;
	@Autowired
	private TLcService lcService;
	@Autowired
	private MasterBankRepository masterBankRepository;
	@Autowired
	private MCurrencyRepository currencyRepository;
	@Autowired
	private MRemitTypeRep remitTypeRep;
	@Autowired
	private BillOfExchangeRepo billOfExchange;
	@Autowired
	private MCOARepository mCoaRepo;
	@Autowired
	private TDayBookTransactionRepository tdayBookTransactionRepository;
	@Autowired
	private TCustomerRepository customerRepository;
	@Autowired
	private MongoTemplate mongoTemplate;

	@GetMapping("/approve")
	public ModelAndView payableAmount() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.daybook.approve");
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		return modelAndView;
	}

	@GetMapping("/approve/data-source")
	@ResponseBody
	public DatatableResponse dayBookCompletedDataSource() {
		return new DatatableResponse(this.dayBookTransactionRepository.fetchDayBookTransactionToApprove());
	}

	@PostMapping("/approve/tt-allcation")
	public ResponseEntity<Response> approveDayBookPayment(@RequestParam("id") String id) {
		this.dayBookService.dayBookApproveTTOwned(id);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/cancel/tt-allcation")
	public ResponseEntity<Response> cancelDayBookPayment(@RequestParam("id") String id) {
		TDayBookTransaction dayBookTransaction = this.dayBookTransactionRepository.findOneById(id);
		dayBookTransaction.setTransactionType(Constants.TRANSACTION_DEBIT);
		this.dayBookService.deleteDayBookTransaction(dayBookTransaction);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/get/billofexchange/details")
	public ResponseEntity<List<Document>> getBillOfExchangeDetails() {
		return new ResponseEntity<>(this.lcService.getBillofExchangeDetailsByStatus(), HttpStatus.OK);
	}

	@PostMapping("/approve/entry")
	public ResponseEntity<Response> approveDayBookEntry(@RequestParam("id") String id) {
		try {
			this.dayBookService.approveDaybookEntry(id);
		} catch (AAJRuntimeException e) {
			return new ResponseEntity<>(new Response("failed", e.getMessage(), null), HttpStatus.OK);
		}
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping(value = { "/daybook-list/approved" })
	public ModelAndView dayBookListApproved(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mbank", masterBankRepository.findAll());
		modelAndView.addObject("mCurrency", currencyRepository.findAll());
		modelAndView.addObject("mRemitType", remitTypeRep.findAll());
		modelAndView.addObject("boe", billOfExchange.findAll());
		modelAndView.addObject("mCoa", mCoaRepo.findAll());
		modelAndView.setViewName("accounts.daybook-list.approved");
		return modelAndView;
	}

	@GetMapping("/approved/data-list")
	@ResponseBody
	public DatatableResponse getDayBookData(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate) {
		return new DatatableResponse(tdayBookTransactionRepository.listDayBookEntryApproved(fromDate, toDate));
	}

	@GetMapping("/owned/tt-list")
	public ModelAndView approvedTtList() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.daybook.owned.tt-list");
		return modelAndView;
	}

	@GetMapping("/tt-owned/data-source")
	@ResponseBody
	public DatatableResponse dayBookTtOwnedDataSource(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate) {
		return new DatatableResponse(
				this.dayBookTransactionRepository.fetchDayBookTransactionToApproveOwned(fromDate, toDate));
	}

	@GetMapping("/tt-allocation")
	public ModelAndView ttAllocation(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.daybook.tt-allocation");
		return modelAndView;

	}

	@GetMapping("/adv-deposit/page")
	public ModelAndView advDepositAllocation(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.daybook.advcance-deposit-allocation");
		return modelAndView;

	}

	@GetMapping("tt-owned-advance-dataSource")
	@ResponseBody
	public DatatableResponse ttOwnedAdvanceDataSource() {
		return new DatatableResponse(
				this.dayBookTransactionRepository.findAllByAllocationType(Constants.DAYBOOK_ALLOCATION_ADVANCE));
	}

	@GetMapping("tt-owned-deposit-dataSource")
	@ResponseBody
	public DatatableResponse ttOwnedDepositDataSource() {
		return new DatatableResponse(
				this.dayBookTransactionRepository.findAllByAllocationType(Constants.DAYBOOK_ALLOCATION_DEPOSITE));
	}

	@PostMapping("transaction/customer-refund")
	@Transactional
	public ResponseEntity<Response> refundToCustomer(@RequestParam("id") String id,
			@RequestParam("amount") String amount, @RequestParam("daybookId") String daybookId) {

		Double advanceAmount = Double.parseDouble(amount);
		TDayBookTransaction transaction = this.dayBookTransactionRepository.findOneById(id);
//		if(transaction.getAdvanceOwned() > advanceAmount) {
//			transaction.setAmountRefund(Constants.ADVANCE_AMOUNT_REFUNDED_PARTIAL);
//		}else {
		transaction.setAmountRefund(Constants.ADVANCE_AMOUNT_REFUNDED);
//		}
//		transaction.setAdvanceOwned((transaction.getAdvanceOwned() - advanceAmount));
		this.dayBookTransactionRepository.save(transaction);

		TCustomer customer = this.customerRepository.findOneByCode(transaction.getCustomerId());
		customer.setBalance(customer.getBalance() + transaction.getAmount());
		this.customerRepository.save(customer);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/update/attachmentView/slip")
	public ResponseEntity<Response> attachmentViewUpdateTransport(@RequestParam("daybookId") String daybookId)
			throws ParseException {
		final Update update = new Update().set("attachementViewed", Constants.ATTACHMENT_VIEWED);
		mongoTemplate.updateFirst(Query.query(Criteria.where("daybookId").is(daybookId)), update, TDayBook.class);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
}
