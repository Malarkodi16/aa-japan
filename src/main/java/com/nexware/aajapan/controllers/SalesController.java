package com.nexware.aajapan.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.datatable.DataTableResults;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.dto.SalesStockSearchDto;
import com.nexware.aajapan.dto.StockFilter;
import com.nexware.aajapan.dto.TSalesStockSearchDto;
import com.nexware.aajapan.dto.TTUnitAllocationDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.ConsigneeNotifyparty;
import com.nexware.aajapan.models.MShip;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TDayBook;
import com.nexware.aajapan.models.TDayBookTransaction;
import com.nexware.aajapan.models.TLcDetails;
import com.nexware.aajapan.models.TShippingInstruction;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MasterCountryPortRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.repositories.TDayBookRepository;
import com.nexware.aajapan.repositories.TDayBookTransactionRepository;
import com.nexware.aajapan.repositories.TLcDetailsRepository;
import com.nexware.aajapan.repositories.TLcInvoiceRepository;
import com.nexware.aajapan.repositories.TProformaInvoiceRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.repositories.TShippingInstructionRepository;
import com.nexware.aajapan.repositories.TTAllocationRepository;
import com.nexware.aajapan.services.MLoginService;
import com.nexware.aajapan.services.SalesOrderService;
import com.nexware.aajapan.services.SalesReportService;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.TStockService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("sales")
public class SalesController {

	@Autowired
	ObjectMapper mapper;
	@Autowired
	TCustomerRepository customerRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	StockRepository stockRepository;
	@Autowired
	private MasterCountryPortRepository masterCountryPortRepository;
	@Autowired
	private TProformaInvoiceRepository proformaInvoiceOrderRepository;
	@Autowired
	private TShippingInstructionRepository shippingInstructionRepository;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private TSalesInvoiceRepository tsalesInvoiceRepository;
	@Autowired
	private TTAllocationRepository tTAllocationRepository;
	@Autowired
	private TDayBookRepository tDayBookRepository;
	@Autowired
	private TLcDetailsRepository tLcDetailsRepository;
	@Autowired
	private TLcInvoiceRepository tLcInvoiceRepository;

	@Autowired
	private TStockService stockService;
	@Autowired
	private TDayBookTransactionRepository tdayBookTransactionRepository;

	@Autowired
	private MLoginService loginService;
	@Autowired
	private SalesOrderService salesOrderService;
	@Autowired
	private SalesReportService salesReportService;

	@GetMapping("/stock-search-data")
	public ResponseEntity<DataTableResults<TSalesStockSearchDto>> getSalesStockSearchData(HttpServletRequest request)
			throws ParseException {
		StockFilter filter = new StockFilter(request);

//		DataTableRequest<TSalesStockSearchDto> dataTableInRQ = new DataTableRequest<>(request);

		SalesStockSearchDto result = this.stockRepository.getSalesStockSearchList(filter);
		List<TSalesStockSearchDto> stocks = AppUtil.isObjectEmpty(result) ? new ArrayList<>()
				: result.getListOfDataObjects();
		int total = AppUtil.isObjectEmpty(result) ? 0 : result.getRecordsTotal();
		DataTableResults<TSalesStockSearchDto> dataTableResult = new DataTableResults<>();

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

	@GetMapping(value = { "/proformainvoice" })
	public ModelAndView performalInvoice(Model model, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales.proformainvoice");
		return modelAndView;
	}

	@GetMapping("/proformainvoicelist-data")
	@ResponseBody
	public DatatableResponse getProformainvoicelistData(@RequestParam("flag") int flag) {

		// get sales person id and sales person under logged in sales person
		List<String> salesPersonIds = new ArrayList<>();
		if (flag == 0) {
			MLoginDto loginDto = this.securityService.findLoggedInUser();
			salesPersonIds.add(loginDto.getUserId());
		} else {
			salesPersonIds = this.loginService.getSalesPersonIdsByHierarchyLevel();
		}
		return new DatatableResponse(
				this.proformaInvoiceOrderRepository.findAllProformaInvoiceBySalesPerson(salesPersonIds));
	}

	@GetMapping("/special-user")
	public ModelAndView accountsSpecialUser(ModelAndView modelAndView) {
		modelAndView.setViewName("sales.specialuser");
		return modelAndView;
	}

	// Reserve Page View
	@GetMapping(value = { "/reserved" })
	// link in menu.jsp//
	public ModelAndView reserved(Model model, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("screenNameFlag", "sales");
		modelAndView.setViewName("sales.reserved");
		return modelAndView;
	}

	// Sales Status Page View
	@GetMapping(value = { "/status" })
	// link in menu.jsp//
	public ModelAndView salesStatus(Model model, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales.salesstatus");
		return modelAndView;
	}

	// Shipping Instruction Page View
	@GetMapping("/shippinginstruction")
	// link in menu.jsp//
	public ModelAndView shippingInstruction(Model model, HttpServletRequest request,
			@RequestParam("id") Optional<String> id) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("shippingInstructionId", id.isPresent() ? id.get() : "");
		modelAndView.setViewName("sales.shippinginstruction");
		return modelAndView;
	}

	@GetMapping(value = { "/customer" })
	public ModelAndView customer(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();
		// list//
		modelAndView.addObject("masterCountry", this.masterCountryPortRepository.findAll());
		modelAndView.addObject("customerForm", new TCustomer());
		// customerform is from Model attribute//
		// new customer is used to load new page//
		modelAndView.setViewName("shipping.customer");
		// definition name in tiles//
		return modelAndView;
	}

	@GetMapping(value = { "/dashboard" })
	public ModelAndView stockEntry(ModelAndView modelAndView) {
		// To set necessary models for view

		modelAndView.setViewName("sales.dashboard");
		return modelAndView;
	}

	@GetMapping("/customerlist-data")
	@ResponseBody
	public DatatableResponse getCustomerlistData(@RequestParam(value = "showAll", required = false) Integer showAll) {
		// get sales person id and sales person under logged in sales person
		List<String> salesPersonIds = this.loginService.getSalesPersonIdsByHierarchyLevel();
		MLoginDto logged = securityService.findLoggedInUser();
		if (logged.getRole().equals(Constants.OVERALL_ADMIN)) {
			return new DatatableResponse(this.customerRepository.findAll());
		}

		else {
			if ((!AppUtil.isObjectEmpty(salesPersonIds)) && (showAll == 1)) {
				return new DatatableResponse(this.customerRepository.findAllBySalesPersonIn(salesPersonIds));
			} else {
				return new DatatableResponse(this.customerRepository.findAllByCreatedBy(logged.getUsername()));
			}
		}

	}

	@GetMapping("/stock/stock-search")
	public ModelAndView salesStockSearch(@RequestParam("category") Optional<String> category,
			@RequestParam("maker") Optional<String> maker, @RequestParam("model") Optional<String> model,
			ModelAndView modelAndView) {

		modelAndView.addObject("category", category.isPresent() ? category.get() : "")
				.addObject("maker", maker.isPresent() ? maker.get() : "")
				.addObject("model", model.isPresent() ? model.get() : "");
		modelAndView.setViewName("sales.stock.search");
		return modelAndView;
	}

	@GetMapping("/reserve-data")
	@ResponseBody
	public DatatableResponse getReserveData(@RequestParam("flag") int flag, @RequestParam("stockType") int stockType) {

		// get sales person id and sales person under logged in sales person
		List<String> salesPersonIds = new ArrayList<>();
		if (flag == 0) {
			MLoginDto loginDto = this.securityService.findLoggedInUser();
			salesPersonIds.add(loginDto.getUserId());
		} else {
			salesPersonIds = this.loginService.getSalesPersonIdsByHierarchyLevel();
		}
		return new DatatableResponse(this.stockRepository.findByReserveStock(salesPersonIds, stockType));

	}

	@GetMapping("/shipping-instruction-data")
	@ResponseBody
	public DatatableResponse getShippingInstructionData(String salesPersonId) {
		// get sales person id and sales person under logged in sales person
		List<String> salesPersonIds = this.loginService.getSalesPersonIdsByHierarchyLevel();

		return new DatatableResponse(this.shippingInstructionRepository.findBySalesPersonShippingData(salesPersonIds));

	}

	@GetMapping("/shipping-status-data")
	@ResponseBody
	public DatatableResponse getSalesShippingStatusData(@RequestParam("flag") int flag) {
		// get sales person id and sales person under logged in sales person
		List<String> salesPersonIds = new ArrayList<>();
		if (flag == 0) {
			MLoginDto loginDto = this.securityService.findLoggedInUser();
			salesPersonIds.add(loginDto.getUserId());
		} else {
			salesPersonIds = this.loginService.getSalesPersonIdsByHierarchyLevel();
		}
		return new DatatableResponse(
				this.shippingInstructionRepository.findBySalesPersonShippingStatus(salesPersonIds));

	}

	@PostMapping("/edit")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> editCustomer(@RequestBody TShippingInstruction shippingInstruction) {
		TShippingInstruction editCustomer = shippingInstructionRepository
				.findOneById(shippingInstruction.getId());
		
			editCustomer.setCustomerId(shippingInstruction.getCustomerId());
			
			System.out.println("ffjfffgffj");
			
			editCustomer.setConsigneeId(shippingInstruction.getConsigneeId());
		
			editCustomer.setNotifypartyId(shippingInstruction.getNotifypartyId());
			editCustomer.setIfEdited(Constants.CUSTOMER_STATUS_IF_EDITED);
			this.shippingInstructionRepository.save(editCustomer);
			return new ResponseEntity<>(new Response("success", editCustomer), HttpStatus.OK);
		
	}

	@GetMapping(value = { "/view-shipment" })
	public ModelAndView shipmentlist(ModelAndView modelAndView) {
		modelAndView.addObject("masterCountries", this.masterCountryPortRepository.findAll());
		modelAndView.setViewName("sales.viewshipment");
		return modelAndView;
	}

	@GetMapping("/sales-order-invoice-list")
	public ModelAndView salesOrderInvoice() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales-order-invoice");
		modelAndView.addObject("screenNameFlag", "sales");
		return modelAndView;
	}

	@GetMapping(path = "/orderInvoice/list/datasource")
	@ResponseBody
	public DatatableResponse listDataSource(@RequestParam("flag") int flag) {
		// get sales person id and sales person under logged in sales person
		List<String> salesPersonIds = new ArrayList<>();
		if (flag == 0) {
			MLoginDto loginDto = this.securityService.findLoggedInUser();
			salesPersonIds.add(loginDto.getUserId());
		} else {
			salesPersonIds = this.loginService.getSalesPersonIdsByHierarchyLevel();
		}

		return new DatatableResponse(this.tsalesInvoiceRepository.getListWithCustomerDetails(salesPersonIds));
	}

	@GetMapping(path = "/orderStock/list/datasource")
	@ResponseBody
	public DatatableResponse stockDataSource(@RequestParam("flag") int flag) {
		// get sales person id and sales person under logged in sales person
		List<String> salesPersonIds = new ArrayList<>();
		if (flag == 0) {
			MLoginDto loginDto = this.securityService.findLoggedInUser();
			salesPersonIds.add(loginDto.getUserId());
		} else {
			salesPersonIds = this.loginService.getSalesPersonIdsByHierarchyLevel();
		}

		return new DatatableResponse(this.tsalesInvoiceRepository.getListWithStockDetails(salesPersonIds));
	}

	@GetMapping(path = "/all/salesorder")
	@ResponseBody
	public DatatableResponse allSalesOrder() {
		// get sales person id and sales person under logged in sales person

		return new DatatableResponse(this.tsalesInvoiceRepository.recentSalesOrder());
	}

	@GetMapping("/tt-allocation")
	public ModelAndView ttAllocation(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales.tt-allocation");
		return modelAndView;

	}

	@GetMapping("tt-allocation-dataSource")
	@ResponseBody
	public DatatableResponse ttAllocationDataSource() {
		return new DatatableResponse(this.tTAllocationRepository.getEntryByStatus());
	}

	@GetMapping("/own-tt-allocation")
	public ModelAndView ownttAllocation(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales.own-tt-allocation");
		return modelAndView;

	}

	@GetMapping("own-tt-allocation-dataSource")
	@ResponseBody
	public DatatableResponse ownttAllocationDataSource() {
		MLoginDto loggedInUser = this.securityService.findLoggedInUser();
		return new DatatableResponse(
				this.tdayBookTransactionRepository.getOwnedTransactionList(loggedInUser.getUserId()));
	}

	@GetMapping("/dayBookTransaction/delete")
	public ResponseEntity<HttpStatus> deleteDayBookTransaction(@RequestParam("dayBookTransId") String dayBookTransId,
			RedirectAttributes redirectAttributes) {
		TDayBookTransaction dayBookTrans = this.tdayBookTransactionRepository.findOneById(dayBookTransId);
		dayBookTrans.setTransactionType(Constants.TRANSACTION_CREDIT);
		this.tdayBookTransactionRepository.save(dayBookTrans);
		redirectAttributes.addFlashAttribute("status", HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@GetMapping("tt-unitAllocation-dataSource-list")
	@ResponseBody
	public DatatableResponse ttUnitAllocationDataSource(@RequestParam String customer) {
		return new DatatableResponse(this.tsalesInvoiceRepository.findAllInvoiceByCustomerAndPaymentStatus(customer));
	}

	@GetMapping("tt-fifoAllocation-dataSource-list")
	@ResponseBody
	public DatatableResponse ttFifoAllocationDataSource(@RequestParam String customer) {
		return new DatatableResponse(
				this.tsalesInvoiceRepository.findAllFifoInvoiceByCustomerAndPaymentStatus(customer));
	}

	@GetMapping("tt-lcAllocation-dataSource-list")
	@ResponseBody
	public DatatableResponse ttLcAllocationDataSource(@RequestParam String daybookId, @RequestParam String customer) {
		if (!AppUtil.isObjectEmpty(daybookId) && !AppUtil.isObjectEmpty(customer)) {
			TDayBook tDayBook = this.tDayBookRepository.findOneByDaybookId(daybookId);
			TLcDetails lcDetails = this.tLcDetailsRepository.findOneByBillOfExchangeNo(tDayBook.getBillOfExchange());
			return new DatatableResponse(
					this.tLcInvoiceRepository.findAllInvoiceByLcInvoiceNo(lcDetails.getLcInvoiceNo(), customer));

		} else {
			return new DatatableResponse(new ArrayList<TTUnitAllocationDto>());
		}
	}

	@PostMapping(path = "/tt-create")
	public ResponseEntity<Response> ttSave(@RequestParam Integer allocationType, @RequestParam String customerId,
			@RequestParam String daybookId, @RequestParam String advanceAmount,
			@RequestParam(value = "exchangeRate1", required = false) String exchangeRate1,
			@RequestParam(value = "exchangeRate2", required = false) String exchangeRate2,
			@RequestBody(required = false) List<Document> data) {

		try {
			if (allocationType == Constants.DAYBOOK_ALLOCATION_FIFO) {
				this.salesOrderService.fifoAllocationByCustomerAndDaybook(advanceAmount, exchangeRate1, exchangeRate2,
						customerId, daybookId);
			} else if ((allocationType == Constants.DAYBOOK_ALLOCATION_UNIT) && !AppUtil.isObjectEmpty(data)) {
				this.salesOrderService.unitAllocationByCustomerAndDaybook(advanceAmount, exchangeRate1, exchangeRate2,
						customerId, daybookId, data);
			} else if (allocationType == Constants.DAYBOOK_ALLOCATION_DEPOSITE) {
				this.salesOrderService.depositOrAdvanceByCustomerAndDaybook(advanceAmount, exchangeRate1, exchangeRate2,
						customerId, daybookId, Constants.DAYBOOK_ALLOCATION_DEPOSITE);
			} else if (allocationType == Constants.DAYBOOK_ALLOCATION_ADVANCE) {
				this.salesOrderService.depositOrAdvanceByCustomerAndDaybook(advanceAmount, exchangeRate1, exchangeRate2,
						customerId, daybookId, Constants.DAYBOOK_ALLOCATION_ADVANCE);
			} else if ((allocationType == Constants.DAYBOOK_ALLOCATION_LC) && !AppUtil.isObjectEmpty(data)) {
				this.salesOrderService.lcAllocationByCustomerAndDaybook(advanceAmount, customerId, daybookId, data);
			} else {
				return new ResponseEntity<>(new Response("failed", "Allocation type not found.", null), HttpStatus.OK);
			}
		} catch (AAJRuntimeException e) {
			return new ResponseEntity<>(new Response("failed", e.getMessage(), null), HttpStatus.OK);
		}

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@PostMapping(path = "/tt-reAllocate")
	public ResponseEntity<Response> ttReAllocate(@RequestParam Integer allocationType, @RequestParam String customerId,
			@RequestParam String daybookId, @RequestBody(required = false) List<Document> data,
			@RequestParam String transId) {

		try {
			if (allocationType == Constants.DAYBOOK_ALLOCATION_FIFO) {
				this.salesOrderService.fifoAllocationByCustomerAndDaybookReAllocate(customerId, daybookId, transId);
			} else if ((allocationType == Constants.DAYBOOK_ALLOCATION_UNIT) && !AppUtil.isObjectEmpty(data)) {
				this.salesOrderService.unitAllocationByCustomerAndDaybookReAllocate(customerId, daybookId, data,
						transId);
			} else {
				return new ResponseEntity<>(new Response("failed", "Allocation type not found.", null), HttpStatus.OK);
			}
		} catch (AAJRuntimeException e) {
			return new ResponseEntity<>(new Response("failed", e.getMessage(), null), HttpStatus.OK);
		}

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@PostMapping(path = "/reserve-details/update")
	@ResponseBody
	public ResponseEntity<Response> reserveDetailsUpdate(@RequestParam("stockNo") String stockNo,
			@RequestBody Map<String, String> data) {
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		try {
			final String custId = AppUtil.isObjectEmpty(data.get("custId").toString()) ? "0.0"
					: data.get("custId").toString();
			final Double reservePrice = Double.parseDouble(data.get("reservePrice"));
			stockService.reserveStock(stockNo, custId, loggedInUser.getUserId(), reservePrice);
		} catch (final AAJRuntimeException e) {
			return new ResponseEntity<>(new Response("failed", e.getMessage(), null), HttpStatus.OK);
		}
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping(path = "/update/userDetails")
	public ResponseEntity<Response> updateUserDetails(@RequestParam("stockNo") String stockNo,
			@RequestBody Map<String, Object> data) {

		final String shippingUser = AppUtil.isObjectEmpty(data.get("shippingUser")) ? ""
				: data.get("shippingUser").toString();
		final String shippingId = AppUtil.isObjectEmpty(data.get("shippingId")) ? ""
				: data.get("shippingId").toString();
		final String shippingTel = AppUtil.isObjectEmpty(data.get("shippingTel")) ? ""
				: data.get("shippingTel").toString();
		final String hsCode = AppUtil.isObjectEmpty(data.get("hsCode")) ? "" : data.get("hsCode").toString();

		TStock stock = this.stockRepository.findOneByStockNo(stockNo);
		stock.setShippingUser(shippingUser);
		stock.setShippingId(shippingId);
		stock.setShippingTel(shippingTel);
		stock.setHsCode(hsCode);
		this.stockRepository.save(stock);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping(value = { "sales-home" })
	public ModelAndView getSalesHome(Model model, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales.home");
		return modelAndView;
	}

	@DeleteMapping("/delete/proformaInvoice")
	public ResponseEntity<Response> deleteDayBookTransaction(@RequestParam("invoiceNo") String invoiceNo) {
		this.proformaInvoiceOrderRepository.deleteByInvoiceNo(invoiceNo);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@PostMapping("/delete/shippingInstruction")
	@Transactional
	public ResponseEntity<String> deleteShippingInstruction(
			@RequestParam("shippingInstructionId") String shippingInstructionId,
			@RequestBody Map<String, Object> data) {

		final String remarks = data.get("remarks").toString();
		TShippingInstruction instruction = shippingInstructionRepository
				.findOneByShippingInstructionId(shippingInstructionId);
		instruction.setRemarks(remarks);
		instruction.setDeleteStatus(Constants.DELETE_FLAG_1);
		shippingInstructionRepository.save(instruction);

		final TStock stock = stockRepository.findOneByStockNo(instruction.getStockNo());
		stock.setShippingStatus(Constants.STOCK_SHIPPING_STATUS_IDLE);
		stock.setShippingInstructionStatus(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_IDLE);
		stockRepository.save(stock);
//		this.shippingInstructionRepository.deleteByShippingInstructionId(shippingInstructionId);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	@GetMapping(value = { "custom/report" })
	public ModelAndView stockTracker(ModelAndView modelAndView) {
		modelAndView.setViewName("sales.custom.report");
		return modelAndView;
	}

	@GetMapping("order/export/excel/new")
	public void exportExcelNew(HttpServletResponse response) throws IOException {
		this.salesReportService.exportAllSalesOrders(response);
	}

	@Transactional
	@PostMapping(path = "change/bidding-sales-person")
	public ResponseEntity<Response> updateUserDetails(@RequestParam("salesPersonId") String salesPersonId,
			@RequestBody List<String> stockNos) {
		this.stockRepository.updateSalesPersonByStockNos(salesPersonId, stockNos);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/addConsignee")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> create(@RequestParam("code") String code,
			@RequestParam("consigneeName") String consigneeName, @RequestParam("consigneeAddr") String consigneeAddr) {

		TCustomer customerToSave = this.customerRepository.findOneByCode(code);
		ConsigneeNotifyparty consignee = new ConsigneeNotifyparty();
		consignee.setId(ObjectId.get().toString());
		consignee.setcFirstName(consigneeName);
		consignee.setcAddress(consigneeAddr);
		consignee.setcLastName("");
		List<ConsigneeNotifyparty> consigneeNotifyParties = customerToSave.getConsigneeNotifyparties();
		consigneeNotifyParties.add(consignee);
		customerToSave.setConsigneeNotifyparties(consigneeNotifyParties);

		this.customerRepository.save(customerToSave);
		return new ResponseEntity<>(new Response("success", customerToSave), HttpStatus.OK);
	}

	@PostMapping("/addNotifyParty")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> createNP(@RequestParam("code") String code,
			@RequestParam("notifyPartyName") String notifyPartyName,
			@RequestParam("notifyPartyAddr") String notifyPartyAddr) {

		TCustomer customerToSave = this.customerRepository.findOneByCode(code);
		ConsigneeNotifyparty consignee = new ConsigneeNotifyparty();
		consignee.setId(ObjectId.get().toString());
		consignee.setNpFirstName(notifyPartyName);
		consignee.setNpAddress(notifyPartyAddr);
		consignee.setNpLastName("");
		List<ConsigneeNotifyparty> consigneeNotifyParties = customerToSave.getConsigneeNotifyparties();
		consigneeNotifyParties.add(consignee);
		customerToSave.setConsigneeNotifyparties(consigneeNotifyParties);

		this.customerRepository.save(customerToSave);
		return new ResponseEntity<>(new Response("success", customerToSave), HttpStatus.OK);
	}

	@GetMapping("/stockInfo")
	public ModelAndView salesStockInfo(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales.stockinfo");
		return modelAndView;
	}

}