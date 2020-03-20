package com.nexware.aajapan.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.ProformaInvoiceDto;
import com.nexware.aajapan.dto.TLcInvoiceDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.ProformaInvoiceItem;
import com.nexware.aajapan.models.TLcDetails;
import com.nexware.aajapan.models.TLcInvoice;
import com.nexware.aajapan.models.TProformaInvoice;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TLcDetailsRepository;
import com.nexware.aajapan.repositories.TLcInvoiceRepository;
import com.nexware.aajapan.repositories.TProformaInvoiceRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller

@RequestMapping("accounts/lc")
public class LCController {
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TLcDetailsRepository tLcDetailsRepository;
	@Autowired
	private TLcInvoiceRepository tLcInvoiceRepository;

	@Autowired
	private TProformaInvoiceRepository proformaInvoiceRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private StockRepository stockRepository;

	@GetMapping("create")
	public ModelAndView create(ModelAndView modelAndView) {

		modelAndView.setViewName("accounts.lc.create");
		return modelAndView;
	}

	@GetMapping("createOld")
	public ModelAndView createOld(ModelAndView modelAndView) {
		modelAndView.setViewName("accounts.lc.createOld");
		return modelAndView;
	}

	@GetMapping("list")
	public ModelAndView list(@RequestParam(value = "lcNo", required = false) String lcNo, ModelAndView modelAndView) {
		modelAndView.addObject("lcNo", lcNo);
		modelAndView.setViewName("accounts.lc.list");
		return modelAndView;
	}

	@PostMapping(path = "/create")
	@Transactional
	public ResponseEntity<Response> updateLCDetails(@RequestBody Map<String, Object> data, ModelAndView modelAndView)
			throws IOException {
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		final TLcDetails lcDetails = this.objectMapper.readValue(
				this.objectMapper.writeValueAsString(data.get("lcDetails")), new TypeReference<TLcDetails>() {
				});
		List<Map<String, String>> stockMap = this.objectMapper.readValue(
				this.objectMapper.writeValueAsString(data.get("stockDataArr")),
				new TypeReference<List<LinkedHashMap<String, String>>>() {
				});
		String proformaInvoiceId = AppUtil.ifNull(data.get("proformaInvoiceId"), "").toString();
		lcDetails.setProformaInvoiceNo(proformaInvoiceId);
		lcDetails.setProformaInvoiceId(proformaInvoiceId);
		lcDetails.setBillOfExchangeStatus(Constants.BILL_OF_EXCHANGE_NOT_CREATED);
		lcDetails.setLcInvoiceNo(this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_LCINV));
		TProformaInvoice proformaInvoice = proformaInvoiceRepository.findOneByInvoiceNo(proformaInvoiceId);
		lcDetails.setSalesPerson(proformaInvoice.getSalesPerson());
		this.tLcDetailsRepository.insert(lcDetails);
		stockMap.forEach(stock -> {
			String schedule = AppUtil.ifNull(stock.get("scheduleText"), "");
			String stockNo = AppUtil.ifNull(stock.get("stockNo"), "");
			String maker = AppUtil.ifNull(stock.get("maker"), "");
			String model = AppUtil.ifNull(stock.get("model"), "");
			String hsCode = AppUtil.ifNull(stock.get("hsCode"), "");
			String chassisNo = AppUtil.ifNull(stock.get("chassisNo"), "");
			String customerId = AppUtil.ifNull(stock.get("customerId"), "");
			String vessel = AppUtil.ifNull(stock.get("vessel"), "");
			String from = AppUtil.ifNull(stock.get("from"), "");
			String to = AppUtil.ifNull(stock.get("to"), "");
			String shippingMarksId = AppUtil.ifNull(stock.get("shippingMarksId"), "");
			String shippingMarks = AppUtil.ifNull(stock.get("shippingMarks"), "");
			String sailingDateStr = AppUtil.ifNull(stock.get("sailingDate"), "");
			String bankSentDateStr = AppUtil.ifNull(stock.get("bankSentDate"), "");
			String stockProformaInvoiceId = AppUtil.ifNull(stock.get("proformaInvoiceId"), "");

			Double fob = Double.parseDouble(AppUtil.ifNull(stock.get("fob"), "0.0"));
			Double insurance = Double.parseDouble(AppUtil.ifNull(stock.get("insurance"), "0.0"));
			Double freight = Double.parseDouble(AppUtil.ifNull(stock.get("freight"), "0.0"));
			Double amount = Double.parseDouble(AppUtil.ifNull(stock.get("amount"), "0.0"));
			TLcInvoice invoice = new TLcInvoice(lcDetails.getLcInvoiceNo(), stockNo, chassisNo, maker, model, hsCode,
					customerId, amount, stockProformaInvoiceId, lcDetails.getLcInvoiceNo(), schedule,
					Constants.BILL_OF_EXCHANGE_NOT_UPDATED);
			invoice.setFob(fob);
			invoice.setInsurance(insurance);
			invoice.setFreight(freight);
			invoice.setPerVessel(vessel);
			invoice.setFrom(from);
			invoice.setStatus(Constants.LC_INVOICE_INIT);
			invoice.setTo(to);
			invoice.setShippingMarksId(shippingMarksId);
			invoice.setShippingMarks(shippingMarks);
			invoice.setProformaInvoiceNo(stockProformaInvoiceId);
			invoice.setAmountAllocatted(0.0);
			if (!AppUtil.isObjectEmpty(sailingDateStr)) {
				invoice.setSailingDate(AppUtil.parseDate(sailingDateStr, "dd-MM-yyyy"));
			}
			if (!AppUtil.isObjectEmpty(bankSentDateStr)) {
				invoice.setBankSentDate(AppUtil.parseDate(bankSentDateStr, "dd-MM-yyyy"));
			}
			tLcInvoiceRepository.insert(invoice);
			TStock stockToUpdate = stockRepository.findOneByStockNo(stockNo);
			stockToUpdate.setLcStatus(Constants.STOCK_LC_APPLIED);
			stockRepository.save(stockToUpdate);
		});

		return new ResponseEntity<>(new Response("success", lcDetails.getLcNo()), HttpStatus.OK);

	}

	@PostMapping(path = "/edit")
	@Transactional
	public ResponseEntity<Response> editLCDetails(@RequestBody Map<String, Object> requestMap,
			ModelAndView modelAndView) throws IOException, ParseException {
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		final Document data = this.objectMapper.readValue(
				this.objectMapper.writeValueAsString(requestMap.get("lcDetails")), new TypeReference<Document>() {
				});
		String consignee = data.getString("consignee");
		String notifyParty = data.getString("notifyParty");
		String npAddress = data.getString("npAddress");
		String cAddress = data.getString("cAddress");
		String lcNo = data.getString("lcNo");
		String bankId = data.getString("bankId");
		String issueDateString = data.getString("issueDate");
		String expiryDateString = data.getString("expiryDate");
		Double amount = Double.valueOf(data.getString("amount"));
		String shippingTermsName = data.getString("shippingTermsName");
		String shippingTerms = data.getString("shippingTerms");
		String beneficiaryCertify = data.getString("beneficiaryCertify");
		String licenseDoc = data.getString("licenseDoc");
		String lcInvoiceNo = data.getString("lcInvoiceNo");
		Date issueDate = null;
		Date expiryDate = null;
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
		if (!AppUtil.isObjectEmpty(issueDateString)) {
			issueDate = dateFormat.parse(issueDateString);
		}
		if (!AppUtil.isObjectEmpty(expiryDateString)) {
			expiryDate = dateFormat.parse(expiryDateString);
		}
		// update lc details
		TLcDetails lcDetails = tLcDetailsRepository.findOneByLcInvoiceNo(lcInvoiceNo);
		lcDetails.setConsigneeName(consignee);
		lcDetails.setNotifyPartyName(notifyParty);
		lcDetails.setNpAddress(npAddress);
		lcDetails.setcAddress(cAddress);
		lcDetails.setLcNo(lcNo);
		lcDetails.setBankId(bankId);
		lcDetails.setIssueDate(issueDate);
		lcDetails.setExpiryDate(expiryDate);
		lcDetails.setAmount(amount);
		lcDetails.setShippingTerms(shippingTermsName);
		lcDetails.setShippingTerms(shippingTerms);
		lcDetails.setBeneficiaryCertify(beneficiaryCertify);
		lcDetails.setLicenseDoc(licenseDoc);
		tLcDetailsRepository.save(lcDetails);
		List<Document> stockMap = this.objectMapper.readValue(
				this.objectMapper.writeValueAsString(requestMap.get("stockDataArr")),
				new TypeReference<List<Document>>() {
				});

		stockMap.forEach(stock -> {
			String schedule = AppUtil.ifNull(stock.getString("scheduleText"), "");
			String stockNo = AppUtil.ifNull(stock.getString("stockNo"), "");
			String maker = AppUtil.ifNull(stock.getString("maker"), "");
			String model = AppUtil.ifNull(stock.getString("model"), "");
			String hsCode = AppUtil.ifNull(stock.getString("hsCode"), "");
			String chassisNo = AppUtil.ifNull(stock.getString("chassisNo"), "");
			String invCustomerId = AppUtil.ifNull(stock.getString("customerId"), "");
			String vessel = AppUtil.ifNull(stock.getString("vessel"), "");
			String from = AppUtil.ifNull(stock.getString("from"), "");
			String to = AppUtil.ifNull(stock.getString("to"), "");
			String shippingMarksId = AppUtil.ifNull(stock.getString("shippingMarksId"), "");
			String shippingMarks = AppUtil.ifNull(stock.getString("shippingMarks"), "");
			String sailingDateStr = AppUtil.ifNull(stock.getString("sailingDate"), "");
			String bankSentDateStr = AppUtil.ifNull(stock.getString("bankSentDate"), "");
			String proformaInvoiceId = AppUtil.ifNull(stock.getString("proformaInvoiceId"), "");
			String proformaInvoiceNo = AppUtil.ifNull(stock.getString("proformaInvoiceNo"), "");
			Double fob = Double.parseDouble(AppUtil.ifNull(stock.getString("fob"), "0.0"));
			Double insurance = Double.parseDouble(AppUtil.ifNull(stock.getString("insurance"), "0.0"));
			Double freight = Double.parseDouble(AppUtil.ifNull(stock.getString("freight"), "0.0"));
			Double invAmount = Double.parseDouble(AppUtil.ifNull(stock.getString("amount"), "0.0"));
			boolean isNew = Boolean.parseBoolean(stock.getString("isNew"));
			boolean isCancelled = Boolean.parseBoolean(stock.getString("isCancelled"));
			TLcInvoice lcInvoice;
			if (!isNew) {
				lcInvoice = tLcInvoiceRepository.findOneByLcDtlIdAndStockNoAndStatusNot(lcDetails.getLcInvoiceNo(), stockNo,Constants.LC_INVOICE_CANCELLED);
			} else {
				lcInvoice = new TLcInvoice();
			}
			TStock stockToUpdate = stockRepository.findOneByStockNo(stockNo);
			if (!isCancelled) {
				lcInvoice.setLcDtlId(lcDetails.getLcInvoiceNo());
				lcInvoice.setStockNo(stockNo);
				lcInvoice.setChassisNo(chassisNo);
				lcInvoice.setMaker(maker);
				lcInvoice.setModel(model);
				lcInvoice.setHsCode(hsCode);
				lcInvoice.setCustomerId(invCustomerId);
				lcInvoice.setFob(fob);
				lcInvoice.setFreight(freight);
				lcInvoice.setInsurance(insurance);
				
				lcInvoice.setAmount(invAmount);
				lcInvoice.setProformaInvoiceId(proformaInvoiceId);
				lcInvoice.setProformaInvoiceNo(proformaInvoiceNo);				
				lcInvoice.setLcInvoiceNo(lcDetails.getLcInvoiceNo());
				lcInvoice.setSchedule(schedule);
				lcInvoice.setIsBolUpdated(Constants.BILL_OF_EXCHANGE_NOT_UPDATED);
				lcInvoice.setPerVessel(vessel);
				lcInvoice.setFrom(from);
				lcInvoice.setTo(to);
				lcInvoice.setShippingMarksId(shippingMarksId);
				lcInvoice.setShippingMarks(shippingMarks);

				if (!AppUtil.isObjectEmpty(sailingDateStr)) {
					lcInvoice.setSailingDate(AppUtil.parseDate(sailingDateStr, "dd-MM-yyyy"));
				}
				if (!AppUtil.isObjectEmpty(bankSentDateStr)) {
					lcInvoice.setBankSentDate(AppUtil.parseDate(bankSentDateStr, "dd-MM-yyyy"));
				}
				lcInvoice.setStatus(Constants.LC_INVOICE_INIT);
				stockToUpdate.setLcStatus(Constants.STOCK_LC_APPLIED);
			} else {
				lcInvoice.setStatus(Constants.LC_INVOICE_CANCELLED);
				stockToUpdate.setLcStatus(Constants.STOCK_LC_NOT_APPLIED);
			}
			tLcInvoiceRepository.save(lcInvoice);
			// update stock
			stockRepository.save(stockToUpdate);
		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping("/proformaInvoice/list")
	@ResponseBody
	public DatatableResponse dashBoard(@RequestParam("custId") String custId) {
		if (custId.isEmpty()) {
			return new DatatableResponse(new ArrayList<>());
		}
		Optional<List<ProformaInvoiceDto>> list = this.proformaInvoiceRepository
				.findAllProformaInvoiceDetailsByCustId(custId);

		return new DatatableResponse(list.isPresent() ? list : new ArrayList<>());

	}

	@GetMapping("/search/proformaInvoice")
	@ResponseBody
	public DatatableResponse searchProformaInvoice(@RequestParam("custId") String custId) {
		if (custId.isEmpty()) {
			return new DatatableResponse(new ArrayList<>());
		}
		List<ProformaInvoiceItem> list = this.proformaInvoiceRepository.findAllProformaInvoiceByCustId(custId);

		return new DatatableResponse(!AppUtil.isObjectEmpty(list) ? list : new ArrayList<>());

	}

	@GetMapping("/search/customer/proformaInvoice")
	public ResponseEntity<List<ProformaInvoiceItem>> searchProformaInvoiceByCustomer(
			@RequestParam("custId") String custId) {
		if (custId.isEmpty()) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
		}
		List<ProformaInvoiceItem> list = this.proformaInvoiceRepository.findAllProformaInvoiceByCustId(custId);
		return new ResponseEntity<>(!AppUtil.isObjectEmpty(list) ? list : new ArrayList<>(), HttpStatus.OK);
	}

	@GetMapping("/invoice/list")
	@ResponseBody
	public DatatableResponse invoiceList(HttpServletRequest request) {
		String lcNo = request.getParameter("lcNo");
		String flag = request.getParameter("flag");
		if (flag.equals("0")) {
			if (AppUtil.isObjectEmpty(lcNo)) {
				return new DatatableResponse(this.tLcDetailsRepository.findAllLcInvoice());
			} else {
				return new DatatableResponse(this.tLcDetailsRepository.findOneLcInvoiceByLcNO(lcNo));
			}
		} else {
			return new DatatableResponse(this.tLcDetailsRepository.findAllBillOfExchangeUpdated());
		}
	}

	@PutMapping("/invoice/update")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> updateInvoiceById(@RequestParam("id") String id,
			@RequestBody Map<String, String> data) throws ParseException {

		TLcDetails lcDetails = this.tLcDetailsRepository.findOneById(id);

		lcDetails.setConsigneeName(AppUtil.ifNull(data.get("consignee"), ""));
		lcDetails.setNotifyPartyName(AppUtil.ifNull(data.get("notifyparty"), ""));
		lcDetails.setcAddress(AppUtil.ifNull(data.get("cAddress"), ""));
		lcDetails.setNpAddress(AppUtil.ifNull(data.get("npAddress"), ""));
		lcDetails.setShippingTerms(AppUtil.ifNull(data.get("shippingTerms"), ""));
		lcDetails.setBeneficiaryCertify(AppUtil.ifNull(data.get("beneficiaryCertify"), ""));
		lcDetails.setLicenseDoc(AppUtil.ifNull(data.get("licenseDoc"), ""));
		this.tLcDetailsRepository.save(lcDetails);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/invoiceItem/update")
	@ResponseBody
	public ResponseEntity<Response> updateInvoiceItemById(@RequestParam("id") String id,
			@RequestBody Map<String, String> data) throws ParseException {
		Date bankSentDate = null;
		String sBankSentDate = AppUtil.ifNull(data.get("bankSentDate"), "");
		if (!AppUtil.isObjectEmpty(sBankSentDate)) {
			bankSentDate = new SimpleDateFormat("dd-MM-yyyy").parse(sBankSentDate);
		}
		TLcInvoice invoice = this.tLcInvoiceRepository.findOneById(id);
		invoice.setMaker(AppUtil.ifNull(data.get("maker"), ""));
		invoice.setModel(AppUtil.ifNull(data.get("model"), ""));
		invoice.setHsCode(AppUtil.ifNull(data.get("hsCode"), ""));
		invoice.setPerVessel(AppUtil.ifNull(data.get("perVessel"), ""));
		invoice.setFrom(AppUtil.ifNull(data.get("from"), ""));
		invoice.setTo(AppUtil.ifNull(data.get("to"), ""));
		invoice.setShippingMarks(AppUtil.ifNull(data.get("shippingMarks"), ""));
		invoice.setBankSentDate(bankSentDate);
		this.tLcInvoiceRepository.save(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/invoiceDetail/{lcInvoiceNo}.pdf")
	public void exportCertificate(@PathVariable String lcInvoiceNo, HttpServletResponse response)
			throws JRException, IOException {
		List<TLcInvoiceDto> tLcDetailsList = this.tLcDetailsRepository.findOneLcDetailsId(lcInvoiceNo);
		// proforma item dataset
		JRBeanCollectionDataSource datasourceLocations = new JRBeanCollectionDataSource(tLcDetailsList);
		Map<String, Object> params = new HashMap<>();
		params.put("itemList", datasourceLocations);
		InputStream jasperStream = null;
		jasperStream = this.getClass().getResourceAsStream("/templates/jasper/LcInvoice.jasper");

		try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/jasper/images/logo.png")) {
			params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
		} catch (JRException | IOException e) {
			throw new AAJRuntimeException("Failed to load images", e);
		}
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "inline; filename=" + lcInvoiceNo + ".pdf");
		final OutputStream outStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	}

	@PutMapping("/update/billOfExchange")
	@Transactional
	public ResponseEntity<Response> updateBillOfExchange(@RequestParam("lcInvoiceNo") String lcInvoiceNo,
			@RequestParam("proformaInvoiceId") String proformaInvoiceId, @RequestBody Document data) {
		@SuppressWarnings("unchecked")
		ArrayList<String> stockNos =(ArrayList<String>) data.get("stockNos",List.class);
		Integer methodOfProcess = Integer.parseInt(data.get("methodOfProcess",String.class));
		String yenBank = data.get("yenBank",String.class);
		TLcDetails lcDetails = this.tLcDetailsRepository.findOneByLcInvoiceNo(lcInvoiceNo);
		List<TLcInvoice> lcInvoice = this.tLcInvoiceRepository.findAllByLcInvoiceNo(lcInvoiceNo);

		String billOfExchangeNo = this.sequenceService.generateSalesCustomerSeqProformaInvioceId(proformaInvoiceId,
				stockNos.size());
		lcDetails.setBillOfExchangeNo(billOfExchangeNo);
		lcDetails.setYenBankId(yenBank);
		lcDetails.setMethodOfProcess(methodOfProcess);
		
		if (stockNos.size() == lcInvoice.size()) {
			lcDetails.setBillOfExchangeStatus(Constants.BILL_OF_EXCHANGE_CREATED);
		} else {
			lcDetails.setBillOfExchangeStatus(Constants.BILL_OF_EXCHANGE_PARTIALLY_CREATED);
		}

		lcInvoice.forEach(invoice -> {
			for (String stockNo : stockNos) {
				if (stockNo.equals(invoice.getStockNo())) {
					invoice.setIsBolUpdated(Constants.BILL_OF_EXCHANGE_UPDATED);
				}
			}

		});
		this.tLcDetailsRepository.save(lcDetails);
		this.tLcInvoiceRepository.saveAll(lcInvoice);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
	@PutMapping("/update/dhlNo")
	@Transactional
	public ResponseEntity<Response> updateBillOfExchange(@RequestParam("lcInvoiceNo") String lcInvoiceNo,@RequestBody Document request) {
		String dhlNo=request.getString("dhlNo");		
		TLcDetails lcDetails = this.tLcDetailsRepository.findOneByLcInvoiceNo(lcInvoiceNo);
		lcDetails.setDhlNo(dhlNo);
		this.tLcDetailsRepository.save(lcDetails);
		

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
}