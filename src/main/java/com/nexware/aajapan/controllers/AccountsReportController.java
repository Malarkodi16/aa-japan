package com.nexware.aajapan.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.BalanceAndPlDto;
import com.nexware.aajapan.dto.MCOABalanceStatementDto;
import com.nexware.aajapan.dto.MCOAProfitLossDto;
import com.nexware.aajapan.dto.SalesSummaryDto;
import com.nexware.aajapan.dto.TBankTransactionDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.AccountsTransactionRepository;
import com.nexware.aajapan.repositories.MCOARepository;
import com.nexware.aajapan.repositories.MGeneralLedgerRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TBankTransactionRepository;
import com.nexware.aajapan.repositories.TInventoryCostRepository;
import com.nexware.aajapan.repositories.TReAuctionRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.repositories.TSupplierTransactionRepository;
import com.nexware.aajapan.services.AccountReportService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TAccountsTransactionService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/accounts/report")
public class AccountsReportController {
	@Autowired
	private TSalesInvoiceRepository salesInvoiceRepository;
	@Autowired
	private AccountsTransactionRepository accountsTransactionRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TAccountsTransactionService tAccountsTransactionService;
	@Autowired
	private TBankTransactionRepository tBankTransactionRepository;
	@Autowired
	private MCOARepository mCoaRepository;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private TSupplierTransactionRepository supplierTransactionRepository;
	@Autowired
	private TInventoryCostRepository inventoryCostRepository;
	@Autowired
	private MGeneralLedgerRepository generalLedgerRepository;
	@Autowired
	private TReAuctionRepository tReAuctionRepository;
	@Autowired
	private AccountReportService accountReportService;

	@GetMapping("/stockSales")
	public ModelAndView stockSales() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.reports.stockSales");
		return modelAndView;
	}

	@GetMapping("/income-by-customer-report")
	public ModelAndView incomeByCustomer() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.reports.income-by-customer");
		return modelAndView;
	}

	@GetMapping("/journalEntry")
	public ModelAndView journalEntry() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.reports.journalEntry");
		return modelAndView;
	}

	@GetMapping("/bank-statement")
	public ModelAndView viewBankStatemnt() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.reports.bankStatement");
		return modelAndView;
	}

	@GetMapping("/sales-summary")
	public ModelAndView viewSalesSummary() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.reports.salesSummary");
		return modelAndView;
	}

	@GetMapping("/stockSales/data-source")
	public ResponseEntity<DatatableResponse> stockSalesDataSource(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
			@RequestParam(value = "maker", required = false) String maker,
			@RequestParam(value = "model", required = false) String model) {
		if (AppUtil.isObjectEmpty(fromDate) || AppUtil.isObjectEmpty(toDate)) {
			return new ResponseEntity<>(new DatatableResponse(new ArrayList<>()), HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new DatatableResponse(this.salesInvoiceRepository.getStockSalesReport(fromDate, toDate, maker, model)),
				HttpStatus.OK);
	}

	@GetMapping("/trailBalanceTransaction")
	public ModelAndView trailBalanceTransaction(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.reports.gl-transaction");
		return modelAndView;
	}

	@GetMapping("/trailBalanceTransaction/list/datasource")
	@ResponseBody
	public DatatableResponse trailBalanceTransactionList(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
			@RequestParam(value = "subAccount", required = false) Long subAccount) {
		return new DatatableResponse(this.accountsTransactionRepository.getTrailBalanceAccountsTransactionList(fromDate,
				toDate, subAccount));
	}

	@GetMapping("/bankTransaction/list/datasource")
	@ResponseBody
	public DatatableResponse bankTransactionList(@RequestParam("bank") String bank,
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate) {
		List<TBankTransactionDto> list = this.tBankTransactionRepository.getBankBalanceAccountsTransactionList(bank,
				fromDate, toDate);
		return new DatatableResponse(!list.isEmpty() ? list : new ArrayList<>());
	}

	@GetMapping("/accountTransaction")
	public ModelAndView accountTransaction(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.reports.accountTransaction");
		return modelAndView;
	}

	@GetMapping("/accountTransaction/list/datasource")
	@ResponseBody
	public DatatableResponse accountTransactionList() {
		return new DatatableResponse(this.mCoaRepository.findAll());
	}

	@GetMapping("/trailBalance")
	public ModelAndView trailBalance(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.reports.trailBalance");
		return modelAndView;
	}

	@GetMapping("/trailBalance/list/datasource")
	@ResponseBody
	public DatatableResponse getTrailBalanceList(
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate) {
		if (AppUtil.isObjectEmpty(toDate)) {
			return new DatatableResponse(new ArrayList<>());

		}
		return new DatatableResponse(this.accountsTransactionRepository.getTrailBalance(toDate));
	}

	@PostMapping("/journalEntry/save")
	public ResponseEntity<Response> journalEntrySave(
			@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date transactionDate,
			@RequestBody List<TAccountsTransaction> tAccountsTransaction) {

		String journalEntryNo;
		journalEntryNo = this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_JOURNAL_ENRTY);
		Double sumCredit = tAccountsTransaction.stream().filter(o -> o.getType() == 1)
				.mapToDouble(TAccountsTransaction::getAmount).sum();
		Double sumDebit = tAccountsTransaction.stream().filter(o -> o.getType() == 0)
				.mapToDouble(TAccountsTransaction::getAmount).sum();
		if (!(sumCredit.equals(sumDebit))) {
			throw new AAJRuntimeException("Please Enter Valid Amount Details!");
		}
		tAccountsTransaction.forEach(entry -> {
			TAccountsTransaction transaction = new TAccountsTransaction(journalEntryNo, entry.getRemitTo(),
					entry.getCode(), entry.getCurrency(), entry.getType(), entry.getAmount(), Constants.TYPE_MANUAL,
					entry.getDescription(), transactionDate);
			if (!AppUtil.isObjectEmpty(transactionDate)) {
				transaction.setTransactionDate(transactionDate);
			} else {
				transaction.setTransactionDate(new Date());
			}
			this.tAccountsTransactionService.accountTransactionEntry(transaction);
		});

		return new ResponseEntity<>(new Response("success", journalEntryNo), HttpStatus.OK);
	}

	@GetMapping("/balanceStatement")
	public ModelAndView balanceStatement(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.reports.balanceStatement");
		return modelAndView;
	}

	@GetMapping("/balanceStatement/list/datasource")
	@ResponseBody
	public DatatableResponse balanceStatementList(
			@RequestParam("toDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate) {
		List<MCOABalanceStatementDto> result = null;
		if (AppUtil.isObjectEmpty(toDate)) {
			result = new ArrayList<>();
		} else {
			result = this.mCoaRepository.getBalanceStatementList(toDate);
		}
		return new DatatableResponse(result);
	}

	@GetMapping("/balanceStatement/search-data")
	@ResponseBody
	public DatatableResponse balanceStatementsearchData(
			@RequestParam("toDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate) {
		List<BalanceAndPlDto> result = null;
		if (AppUtil.isObjectEmpty(toDate)) {
			result = new ArrayList<>();
		} else {
			result = this.mCoaRepository.balanceStatementsearchData(toDate);
		}
		return new DatatableResponse(result);
	}

	@GetMapping("/profitAndLoss")
	public ModelAndView profitAndLoss(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.reports.profitAndLoss");
		return modelAndView;
	}

	@GetMapping("/profitAndLoss/list/datasource")
	@ResponseBody
	public DatatableResponse profitAndLossList(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
			@RequestParam(value = "flag", required = false) Integer flag) {
		List<MCOAProfitLossDto> result = new ArrayList<>();
		if (AppUtil.isObjectEmpty(flag)) {
			result = new ArrayList<>();
		} else if (flag.equals(Constants.BETWEEN_DATES)) {
			result = this.mCoaRepository.getProfitLossList(fromDate, toDate);
		} else if (flag.equals(Constants.LAST_FINANCIAL_YEAR)) {
			Date[] dates = AppUtil.getLastFinancialYearDates(new Date());
			result = this.mCoaRepository.getProfitLossList(dates[0], dates[1]);
		} else if (flag.equals(Constants.CURRENT_FINANCIAL_YEAR)) {
			Date[] dates = AppUtil.getCurrentFinancialYearDates(new Date());
			result = this.mCoaRepository.getProfitLossList(dates[0], dates[1]);
		} else if (flag.equals(Constants.LAST_6_MONTHS)) {
			Date startDate = AppUtil.addMonths(-6);
			result = this.mCoaRepository.getProfitLossList(AppUtil.atStartOfDay(startDate), new Date());
		} else if (flag.equals(Constants.LAST_3_MONTHS)) {
			Date startDate = AppUtil.addMonths(-3);
			result = this.mCoaRepository.getProfitLossList(AppUtil.atStartOfDay(startDate), new Date());
		}
		return new DatatableResponse(result);
	}

	@GetMapping("/profitAndLoss/search-data")
	@ResponseBody
	public DatatableResponse profitAndLossSearchData(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
			@RequestParam(value = "flag", required = false) Integer flag) {
		List<BalanceAndPlDto> result = new ArrayList<>();
		if (AppUtil.isObjectEmpty(flag)) {
			result = new ArrayList<>();
		} else if (flag.equals(Constants.BETWEEN_DATES)) {
			result = this.mCoaRepository.profitAndLossSearchData(fromDate, toDate);
		} else if (flag.equals(Constants.LAST_FINANCIAL_YEAR)) {
			Date[] dates = AppUtil.getLastFinancialYearDates(new Date());
			result = this.mCoaRepository.profitAndLossSearchData(dates[0], dates[1]);
		} else if (flag.equals(Constants.CURRENT_FINANCIAL_YEAR)) {
			Date[] dates = AppUtil.getCurrentFinancialYearDates(new Date());
			result = this.mCoaRepository.profitAndLossSearchData(dates[0], dates[1]);
		} else if (flag.equals(Constants.LAST_6_MONTHS)) {
			Date startDate = AppUtil.addMonths(-6);
			result = this.mCoaRepository.profitAndLossSearchData(AppUtil.atStartOfDay(startDate), new Date());
		} else if (flag.equals(Constants.LAST_3_MONTHS)) {
			Date startDate = AppUtil.addMonths(-3);
			result = this.mCoaRepository.profitAndLossSearchData(AppUtil.atStartOfDay(startDate), new Date());
		}
		return new DatatableResponse(result);
	}

	@GetMapping("/sales-summary-list")
	@ResponseBody
	public DatatableResponse salesSummaryList() {
		List<SalesSummaryDto> salesInvoiceDto = this.salesInvoiceRepository.getSalesSummaryReport();
		return new DatatableResponse(salesInvoiceDto);
	}

	@GetMapping("/inventoryValueReport")
	public ModelAndView inventoryValueReport(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.reports.inventoryValue");
		return modelAndView;
	}

	@GetMapping("/inventoryValueReport/list")
	@ResponseBody
	public DatatableResponse inventoryValueReportList() {
		return new DatatableResponse(this.stockRepository.getInventoryValueReport());
	}

	@GetMapping("/supplierStatement")
	public ModelAndView supplierStatement(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.reports.supplierStatement");
		return modelAndView;
	}

	@GetMapping("/supplierStatement/datasource")
	@ResponseBody
	public DatatableResponse supplierStatementData(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
			@RequestParam(value = "supplier", required = false) String supplier) {
		if (AppUtil.isObjectEmpty(fromDate) || AppUtil.isObjectEmpty(toDate) || AppUtil.isObjectEmpty(supplier)) {
			return new DatatableResponse(new ArrayList<>());
		}
		return new DatatableResponse(
				this.supplierTransactionRepository.findTransactionsBySupplierAndDate(supplier, fromDate, toDate));
	}

	@GetMapping("/inventoryInvoiceCosts/list")
	@ResponseBody
	public DatatableResponse inventoryInvoiceCostsList(@RequestParam("stockNo") String stockNo) {
		return new DatatableResponse(this.inventoryCostRepository.getInventoryAmountsForStock(stockNo));
	}

	@GetMapping("/ar-aging-summary")
	public ModelAndView arAgingSummary() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.reports.arAgingSummary");
		return modelAndView;
	}

	@GetMapping("/ar-aging-summary-list")
	@ResponseBody
	public DatatableResponse arAgingSummaryList() throws ParseException {
		return new DatatableResponse(this.salesInvoiceRepository.getAgingSummary());
	}

	@GetMapping(path = "/income-by-customer-list")
	@ResponseBody
	public DatatableResponse listDataSource() {
		return new DatatableResponse(this.salesInvoiceRepository.getIncomeByCustomerList());
	}

	@GetMapping("/gl-report")
	public ModelAndView glReport() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.reports.gl.report");
		return modelAndView;
	}

	@GetMapping(path = "/glReportDataSource")
	public ResponseEntity<DatatableResponse> glReportDataSource(
			@RequestParam("fromDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<Date> fromDate,
			@RequestParam("toDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<Date> toDate) {
		DatatableResponse response = null;
		if (!fromDate.isPresent() || !toDate.isPresent()) {
			response = new DatatableResponse(this.generalLedgerRepository.getTransactions());
		} else {
			response = new DatatableResponse(
					this.generalLedgerRepository.getTransactions(fromDate.get(), toDate.get()));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// /accounts/report/bs/report
	@GetMapping(path = "/bs/report")
	public void balanceSheetReport(@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date,
			HttpServletResponse response) throws IOException {
		this.accountReportService.exportBalanceSheetReport(date, response);
	}

	@GetMapping("/pl/report")
	public void profitandlossreport(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
			@RequestParam(value = "flag", required = false) Integer flag, HttpServletResponse response) {
		this.accountReportService.exportProfitAndLossReport(flag, fromDate, toDate, response);
	}

	@GetMapping("/gl/report")
	public void glreport(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
			HttpServletResponse response) {
		System.out.println(fromDate);
		this.accountReportService.exportGlReport(fromDate, toDate, response);
	}

	@GetMapping(path = "/payment/tracking/report")
	public void paymentTrackingReport(@RequestParam("type") Integer type,
			@RequestParam(value = "supplier", required = false) String supplier,
			@RequestParam(value = "invoiceDateFrom", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date invoiceDateFrom,
			@RequestParam(value = "invoiceDateTo", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date invoiceDateTo,
			HttpServletResponse response) throws IOException {
		if (type == 3) {
			this.accountReportService.exportPaymentTrackingFreightANdShippingExcel(type, supplier, invoiceDateFrom,
					invoiceDateTo, response);
		} else {
			this.accountReportService.exportPaymentTrackingExcel(type, supplier, invoiceDateFrom, invoiceDateTo,
					response);
		} 

	}

	@GetMapping("/profit/loss")
	public ModelAndView inventory(ModelAndView modelAndView) {

		modelAndView.setViewName("accounts.profit.loss");
		return modelAndView;

	}

	@GetMapping("/profit-loss-data")
	@ResponseBody
	public DatatableResponse getProfitLossData() {

		return new DatatableResponse(this.tReAuctionRepository.getProfitLosslist());
	}

}
