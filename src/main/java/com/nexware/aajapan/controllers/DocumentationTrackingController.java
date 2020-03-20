package com.nexware.aajapan.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MForeignBank;
import com.nexware.aajapan.models.TDocumentConversion;
import com.nexware.aajapan.models.TDocumentReceived;
import com.nexware.aajapan.models.TExportCertificate;
import com.nexware.aajapan.models.TLcDetails;
import com.nexware.aajapan.models.TLcInvoice;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MForeignBankRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TDocumentConversionRepository;
import com.nexware.aajapan.repositories.TDocumentReceivedRepository;
import com.nexware.aajapan.repositories.TExportCertificateRepository;
import com.nexware.aajapan.repositories.TLcDetailsRepository;
import com.nexware.aajapan.repositories.TLcInvoiceRepository;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.TExportCertificateService;
import com.nexware.aajapan.utils.AppUtil;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
@RequestMapping("documents/tracking")
public class DocumentationTrackingController {
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private TDocumentConversionRepository tDocumentConversionRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private TExportCertificateRepository exportCertificateRepository;
	@Autowired
	private TDocumentReceivedRepository documentReceivedRepository;
	@Autowired
	private TExportCertificateService exportCertificateService;
	@Autowired
	private TLcDetailsRepository lcDetailsRepository;
	@Autowired
	private TLcInvoiceRepository lcInvoiceRepository;
	@Autowired
	private MForeignBankRepository foreignBankRepository;

	@GetMapping("/dashboard")
	public ModelAndView documentTracking(ModelAndView modelAndView) {
		modelAndView.setViewName("document.dashboard");
		return modelAndView;
	}

	@GetMapping("/not-received")
	public ModelAndView documentTrackingNotReceived(ModelAndView modelAndView) {
		modelAndView.setViewName("document.dashboard.notreceived");
		return modelAndView;
	}

	@GetMapping("/received")
	public ModelAndView documentTrackingReceived(ModelAndView modelAndView) {
		modelAndView.setViewName("document.dashboard.received");
		return modelAndView;
	}

	@GetMapping("/export-certificates")
	public ModelAndView documentTrackingExportCerficates(ModelAndView modelAndView) {
		modelAndView.setViewName("document.dashboard.exportcerficates");
		return modelAndView;
	}

	@GetMapping("/name-transfer")
	public ModelAndView documentTrackingNameTransfer(ModelAndView modelAndView) {
		modelAndView.setViewName("document.dashboard.nametransfer");
		return modelAndView;
	}

	@GetMapping("/cancelled-list")
	public ModelAndView documentTrackingCancelledStock(ModelAndView modelAndView) {
		modelAndView.setViewName("documents.stock.cancelled");
		return modelAndView;
	}

	@GetMapping("/domestic")
	public ModelAndView documentTrackingDomestic(ModelAndView modelAndView) {
		modelAndView.setViewName("document.dashboard.domestic");
		return modelAndView;
	}

	@GetMapping("/notReceived-list")
	@ResponseBody
	public DatatableResponse getNotReceivedList() {
		return new DatatableResponse(this.stockRepository.findAllDocumentNotReceivedList());

	}

	@GetMapping("/received-list")
	@ResponseBody
	public DatatableResponse getReceivedList(@RequestParam("show") int show) {
		if (show == 0) {
			return new DatatableResponse(
					this.documentReceivedRepository.findAllDocumentReceivedList(Constants.STOCK_RIKUJI_STATUS_0));
		} else if (show == 1) {
			return new DatatableResponse(
					this.documentReceivedRepository.findAllDocumentReceivedList(Constants.STOCK_RIKUJI_STATUS_1));
		}
		return new DatatableResponse(new ArrayList<>());
	}

	@GetMapping("/export-certificate-list")
	@ResponseBody
	public DatatableResponse getExportCertificateList(
			@RequestParam("exportCertificateStatus") Integer exportCertificateStatus,
			@RequestParam(value = "docSentStatus", required = false) Integer docSentStatus) {
		return new DatatableResponse(
				this.tDocumentConversionRepository.findAllExportCertificatesByExportCertificateStatusAndDocSentStatus(
						exportCertificateStatus, docSentStatus));
	}

	@GetMapping("/name-transfer-list")
	@ResponseBody
	public DatatableResponse getNameTransferList(@RequestParam("docConvertTo") int docConvertTo) {
		return new DatatableResponse(this.tDocumentConversionRepository.findAllByStatus(docConvertTo));
	}

	@Transactional
	@PutMapping(path = "/updatePlateNo")
	public ResponseEntity<Response> documentsTrackingUpdatePlateNo(@RequestBody Map<String, Object> data)
			throws ParseException {
		MLoginDto loggedInUser = this.securityService.findLoggedInUser();
		String mileage1 = AppUtil.isObjectEmpty(data.get("mileage")) ? "" : data.get("mileage").toString();
		Long mileage = Long.parseLong(mileage1);
		String chassisNo = AppUtil.isObjectEmpty(data.get("chassisNo")) ? "" : data.get("chassisNo").toString();
		String stockNo = AppUtil.isObjectEmpty(data.get("stockNo")) ? "" : data.get("stockNo").toString();
		TStock stock = this.stockRepository.findOneByStockNo(stockNo);
		stock.setOldNumberPlate(
				AppUtil.isObjectEmpty(data.get("oldNumberPlate")) ? "" : data.get("oldNumberPlate").toString());

		Date plateNoReceivedDate = null;

		String plateNoDateString = AppUtil.isObjectEmpty(data.get("plateNoReceivedDate")) ? ""
				: data.get("plateNoReceivedDate").toString();

		if (!AppUtil.isObjectEmpty(plateNoDateString)) {
			plateNoReceivedDate = new SimpleDateFormat("dd-MM-yyyy").parse(plateNoDateString);
			plateNoReceivedDate = AppUtil.appendTime(plateNoReceivedDate);
		}

		stock.setPlateNoReceivedDate(plateNoReceivedDate);
		stock.setsFirstRegDate(
				AppUtil.isObjectEmpty(data.get("sFirstRegDate")) ? "" : data.get("sFirstRegDate").toString());
		stock.setDocumentRemarks(
				AppUtil.isObjectEmpty(data.get("documentRemarks")) ? "" : data.get("documentRemarks").toString());
		if (AppUtil.isObjectEmpty(stock.getOldChassisNo())) {
			stock.setOldChassisNo(stock.getChassisNo());
		}
		stock.setChassisNo(chassisNo);
		stock.setMileage(mileage);
		stock.setStockNo(stockNo);

		this.stockRepository.save(stock);
		TStock recvdStock = this.stockRepository.findOneByStockNo(stockNo);
		if (!AppUtil.isObjectEmpty(stock.getDocumentReceivedDate()) && recvdStock.getDocumentConvertTo()
				.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE)) {
			TDocumentReceived docReceived = new TDocumentReceived();
			docReceived.setStockNo(recvdStock.getStockNo());
			docReceived.setDocumentType(recvdStock.getDocumentType());
			docReceived.setDocumentReceivedDate(recvdStock.getDocumentReceivedDate());
			docReceived.setDocumentStatus(Constants.STOCK_DOCUMENT_RECEIVED);
			docReceived.setDocumentConvertTo(recvdStock.getDocumentConvertTo());
			docReceived.setRikujiStatus(Constants.STOCK_RIKUJI_STATUS_0);
			this.documentReceivedRepository.save(docReceived);

			TStock stockUpdate = this.stockRepository.findOneByStockNo(recvdStock.getStockNo());
			stockUpdate.setDocumentStatus(Constants.STOCK_DOCUMENT_CONVERT);
			stockUpdate.setLastModifiedDate(new Date());
			stockUpdate.setLastModifiedBy(loggedInUser.getUsername());
			this.stockRepository.save(stockUpdate);

		}
		return new ResponseEntity<>(
				new Response("success", this.stockRepository.findOneDocumentNotReceivedStockDetails(stockNo)),
				HttpStatus.OK);
	}

	@PutMapping("/updateChassisNo")
	public ResponseEntity<Response> updateChassisNo(@RequestBody final Map<String, Object> data) {

		final String stockNo = (String) data.get("stockNo");
		TStock stockUpdate = this.stockRepository.findOneByStockNo(stockNo);
		if (AppUtil.isObjectEmpty(stockUpdate.getOldChassisNo())) {
			stockUpdate.setOldChassisNo(stockUpdate.getChassisNo());
		}
		stockUpdate.setChassisNo(data.get("chassisNo").toString());
		this.stockRepository.save(stockUpdate);

		return new ResponseEntity<>(
				new Response("success", this.stockRepository.findOneDocumentNotReceivedStockDetails(stockNo)),
				HttpStatus.OK);

	}

	@PutMapping(path = "/updateDocType")
	@Transactional
	public ResponseEntity<Response> documentsTrackingUpdateDocType(@RequestParam("docType") Integer docType,
			@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date,
			@RequestParam("documentConvertTo") Integer documentConvertTo, @RequestBody List<String> data)
			throws ParseException {
		// append current time
		Date receivedDate = AppUtil.appendTime(date);
//		TDocumentConversion docConversion = new TDocumentConversion(stock.getStockNo(),
//				stock.getDocumentConvertTo());
		data.forEach(stockNo -> {
			TDocumentReceived received = this.documentReceivedRepository.findOneByStockNo(stockNo);
			TStock stock = this.stockRepository.findOneByStockNo(stockNo);
			boolean updateTracking = true;
			if (AppUtil.isObjectEmpty(received)
					|| documentConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_DOMESTIC)) {
				if (stock.getNumberPlate().equalsIgnoreCase("yes")
						&& AppUtil.isObjectEmpty(stock.getPlateNoReceivedDate())
						&& documentConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE)) {
					stock.setDocumentStatus(Constants.STOCK_DOCUMENT_NOT_RECEIVED);
					stock.setDocumentConvertTo(documentConvertTo);
					stock.setDocumentType(docType);
					stock.setDocumentReceivedDate(receivedDate);
					updateTracking = false;
				} else {
					TDocumentReceived docReceived = new TDocumentReceived();
					docReceived.setStockNo(stockNo);
					docReceived.setDocumentType(docType);
					docReceived.setDocumentReceivedDate(receivedDate);

					if (documentConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_DOMESTIC)) {
						docReceived.setDocumentStatus(Constants.STOCK_DOCUMENT_CONVERT);
						// update document status
						stock.setDocumentStatus(Constants.STOCK_DOCUMENT_CONVERT);

						TDocumentConversion docConversion = new TDocumentConversion(stockNo,
								Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_DOMESTIC);
						tDocumentConversionRepository.insert(docConversion);

					} else if (documentConvertTo.equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_SHUPPIN)) {
						docReceived.setDocumentStatus(Constants.STOCK_DOCUMENT_CONVERT);
						// update document status
						stock.setDocumentStatus(Constants.STOCK_DOCUMENT_CONVERT);

						TDocumentConversion docConversion = new TDocumentConversion(stockNo,
								Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_SHUPPIN);
						tDocumentConversionRepository.insert(docConversion);
					} else {
						docReceived.setDocumentStatus(Constants.STOCK_DOCUMENT_RECEIVED);
						stock.setDocumentStatus(Constants.STOCK_DOCUMENT_RECEIVED);
					}

					docReceived.setDocumentConvertTo(documentConvertTo);
					docReceived.setRikujiStatus(Constants.STOCK_RIKUJI_STATUS_0);
					if (!AppUtil.isObjectEmpty(received)) {
						docReceived.setId(received.getId());
					}
					this.documentReceivedRepository.save(docReceived);

				}

			} else {
				TDocumentReceived documentReceived = documentReceivedRepository.findOneByStockNo(stockNo);
				documentReceived.setDocumentType(docType);
				documentReceived.setDocumentReceivedDate(date);
				documentReceived.setDocumentConvertTo(documentConvertTo);
				documentReceived.setDocumentStatus(Constants.STOCK_DOCUMENT_RECEIVED);
				documentReceived.setRikujiStatus(Constants.STOCK_RIKUJI_STATUS_0);
				documentReceivedRepository.save(documentReceived);
			}

			if (updateTracking) {
				stock.setHandoverTo(Constants.DOCUMENTS_HANDOVER_TO_DOCUMENTS_TEAM);
				stock.setDocumentStatus(Constants.STOCK_DOCUMENT_RECEIVED);
				stock.setRikujiStatus(Constants.STOCK_RIKUJI_STATUS_0);
			}
			stockRepository.save(stock);
		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping(path = "/updateConvertedType")
	@Transactional
	public ResponseEntity<Response> documentsTrackingupdateConvertedType(
			@RequestParam("documentConvertTo") Integer documentConvertTo, @RequestBody List<String> data)
			throws ParseException {
		data.forEach(stockNo -> {
			if (documentConvertTo == 7) {
				TStock stockUpdate = this.stockRepository.findOneByStockNo(stockNo);
				stockUpdate.setDocumentStatus(Constants.STOCK_DOCUMENT_NOT_RECEIVED);
				stockUpdate.setDocumentConvertTo(0);
				this.stockRepository.save(stockUpdate);

				TDocumentConversion tDocumentConversion = this.tDocumentConversionRepository.findOneByStockNo(stockNo);
				tDocumentConversion.setDocConvertTo(0);
				this.tDocumentConversionRepository.save(tDocumentConversion);

			} else if (documentConvertTo == 8) {
				TStock stockUpdate = this.stockRepository.findOneByStockNo(stockNo);
				stockUpdate.setDocumentConvertTo(0);
				this.stockRepository.save(stockUpdate);

				TDocumentReceived tDocumentReceived = this.documentReceivedRepository.findOneByStockNo(stockNo);
				tDocumentReceived.setDocumentStatus(Constants.STOCK_DOCUMENT_RECEIVED);
				this.documentReceivedRepository.save(tDocumentReceived);

				TDocumentConversion tDocumentConversion = this.tDocumentConversionRepository.findOneByStockNo(stockNo);
				tDocumentConversion.setDocConvertTo(0);
				this.tDocumentConversionRepository.save(tDocumentConversion);
			} else {
				TStock stockUpdate = this.stockRepository.findOneByStockNo(stockNo);
				stockUpdate.setDocumentConvertTo(documentConvertTo);
				this.stockRepository.save(stockUpdate);

				TDocumentConversion tDocumentConversion = this.tDocumentConversionRepository.findOneByStockNo(stockNo);
				tDocumentConversion.setDocConvertTo(documentConvertTo);
				this.tDocumentConversionRepository.save(tDocumentConversion);

			}

		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping(path = "/updateDocumentType")
	@Transactional
	public ResponseEntity<Response> documentsTrackingupdateDocumentType(
			@RequestParam("documentConvertTo") Integer documentConvertTo, @RequestBody List<String> data)
			throws ParseException {
		data.forEach(stockNo -> {
			if (documentConvertTo == 0) {
				TStock stockUpdate = this.stockRepository.findOneByStockNo(stockNo);
				stockUpdate.setDocumentStatus(documentConvertTo);
				this.stockRepository.save(stockUpdate);

				TDocumentConversion tDocumentConversion = this.tDocumentConversionRepository.findOneByStockNo(stockNo);
				tDocumentConversion.setDocConvertTo(documentConvertTo);
				this.tDocumentConversionRepository.save(tDocumentConversion);

			} else if (documentConvertTo == 1) {
				TDocumentReceived tDocumentReceived = this.documentReceivedRepository.findOneByStockNo(stockNo);
				tDocumentReceived.setDocumentStatus(documentConvertTo);
				tDocumentReceived.setRikujiStatus(0);
				this.documentReceivedRepository.save(tDocumentReceived);

				TDocumentConversion tDocumentConversion = this.tDocumentConversionRepository.findOneByStockNo(stockNo);
				tDocumentConversion.setDocConvertTo(documentConvertTo);
				this.tDocumentConversionRepository.save(tDocumentConversion);

			} else if (documentConvertTo == 2) {
				TDocumentConversion tDocumentConversion = this.tDocumentConversionRepository.findOneByStockNo(stockNo);
				tDocumentConversion.setDocConvertTo(documentConvertTo);
				this.tDocumentConversionRepository.save(tDocumentConversion);

			}

		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/update/received")
	@Transactional
	public ResponseEntity<Response> recycleReceived(@RequestBody Map<String, Object> data) throws ParseException {
		String oldNumberPlate = AppUtil.isObjectEmpty(data.get("oldNumberPlate")) ? ""
				: data.get("oldNumberPlate").toString();
		String documentConverted = AppUtil.isObjectEmpty(data.get("documentConvertTo")) ? ""
				: data.get("documentConvertTo").toString();
		MLoginDto loggedInUser = this.securityService.findLoggedInUser();
		Date documentConvertedDate = null;
		Date plateNoReceivedDate = null;

		String documentConvertedString = AppUtil.isObjectEmpty(data.get("documentConvertedDate")) ? ""
				: data.get("documentConvertedDate").toString();
		String plateNoDateString = AppUtil.isObjectEmpty(data.get("plateNoReceivedDate")) ? ""
				: data.get("plateNoReceivedDate").toString();
		if (!AppUtil.isObjectEmpty(documentConvertedString)) {
			documentConvertedDate = new SimpleDateFormat("dd-MM-yyyy").parse(documentConvertedString);
			documentConvertedDate = AppUtil.appendTime(documentConvertedDate);
		}

		if (!AppUtil.isObjectEmpty(plateNoDateString)) {
			plateNoReceivedDate = new SimpleDateFormat("dd-MM-yyyy").parse(plateNoDateString);
			plateNoReceivedDate = AppUtil.appendTime(plateNoReceivedDate);
		}

		String stockNo = data.get("stockNo").toString();
		String showString = data.get("show").toString();
		Integer show = Integer.parseInt(showString);
		Integer documentConvertTo = Integer.parseInt(documentConverted);
		TDocumentReceived tDocumentReceived = this.documentReceivedRepository.findOneByStockNo(stockNo);
		tDocumentReceived.setDocumentConvertedDate(documentConvertedDate);
		tDocumentReceived.setDocumentConvertTo(documentConvertTo);
		tDocumentReceived.setLastModifiedDate(new Date());
		tDocumentReceived.setLastModifiedBy(loggedInUser.getUsername());
		this.documentReceivedRepository.save(tDocumentReceived);

		TStock stockUpdate = this.stockRepository.findOneByStockNo(stockNo);
		stockUpdate.setOldNumberPlate(oldNumberPlate);
		stockUpdate.setPlateNoReceivedDate(plateNoReceivedDate);
		this.stockRepository.save(stockUpdate);

		return new ResponseEntity<>(
				new Response("success",
						this.documentReceivedRepository.findOneDocumentReceivedStockDetails(show, stockNo)),
				HttpStatus.OK);
	}

	@PostMapping("/update/reconvert")
	public ResponseEntity<Response> reconvert(@RequestBody Map<String, Object> data) throws ParseException {
		String documentConverted = data.get("documentConvertTo").toString();
		String documentConvertedString = data.get("documentConvertedDate").toString();
		Date documentConvertedDate = new SimpleDateFormat("dd-MM-yyyy").parse(documentConvertedString);
		String stockNo = data.get("stockNo").toString();

		Integer documentConvertTo = null;
		if (documentConverted.equalsIgnoreCase("Export Certificate")) {
			documentConvertTo = Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE;
		} else if (documentConverted.equalsIgnoreCase("Name Transfer")) {
			documentConvertTo = Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_NAME_TRANSFER;
		} else if (documentConverted.equalsIgnoreCase("Domestic")) {
			documentConvertTo = Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_DOMESTIC;
		}
		TStock stockUpdate = this.stockRepository.findOneByStockNo(stockNo);
		stockUpdate.setDocumentConvertedDate(documentConvertedDate);
		stockUpdate.setDocumentConvertTo(documentConvertTo);
		this.stockRepository.save(stockUpdate);

		return new ResponseEntity<>(new Response("success", this.stockRepository.findOneByStockNo(stockNo)),
				HttpStatus.OK);
	}

	@PostMapping(path = "/exportCerficate/create")
	public ResponseEntity<Response> save(@RequestBody TExportCertificate exportCertificate) {
		this.exportCertificateService.saveExportCertificate(exportCertificate);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	// .pdf

	@GetMapping("/export-certificate/{stockNo}.pdf")
	public void exportCertificate(@PathVariable String stockNo, HttpServletResponse response)
			throws JRException, IOException {
		TExportCertificate tExportCertificate = this.exportCertificateRepository.findOneByStockNo(stockNo);
		if (!AppUtil.isObjectEmpty(tExportCertificate)) {
			TLcInvoice invoice = this.lcInvoiceRepository.findOneByStockNo(tExportCertificate.getStockNo());
			if (!AppUtil.isObjectEmpty(invoice)) {
				TLcDetails details = this.lcDetailsRepository.findOneByLcInvoiceNo(invoice.getLcDtlId());
				MForeignBank bank = this.foreignBankRepository.findOneByBankId(details.getBankId());

				tExportCertificate.setLcNo(details.getLcNo());
				tExportCertificate.setLcBank(bank.getBank());
			}
		}
		// proforma item dataset

		Map<String, Object> params = new HashMap<>();
		InputStream jasperStream = null;
		jasperStream = this.getClass().getResourceAsStream("/templates/jasper/ExportCertificate.jasper");
		params.put("tExportCertificate", tExportCertificate);

		try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/jasper/images/logo.png")) {
			params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
		} catch (JRException | IOException e) {
			throw new AAJRuntimeException("Failed to load images", e);
		}
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "inline; filename=" + stockNo + ".pdf");
		final OutputStream outStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	}

	@GetMapping(path = "/get/exportCertificate/{stockNo}")
	public ResponseEntity<TExportCertificate> save(@PathVariable("stockNo") String stockNo) {
		TExportCertificate exportCertificate = null;
		exportCertificate = this.exportCertificateRepository.findOneByStockNo(stockNo);
		if (!AppUtil.isObjectEmpty(exportCertificate)) {
			return new ResponseEntity<>(exportCertificate, HttpStatus.OK);
		} else {
			exportCertificate = new TExportCertificate();
			TStock stock = this.stockRepository.findOneByStockNo(stockNo);
			exportCertificate.setFirstRegDate(stock.getFirstRegDate());
			exportCertificate.setsFirstRegDate(stock.getsFirstRegDate());
			exportCertificate.setMakerSerialNo(stock.getChassisNo());
			exportCertificate.setTrademarkVehicle(stock.getMaker());
			exportCertificate.setModelType(stock.getModelType());
			exportCertificate.setFixedNumber(stock.getNoOfSeat());
			exportCertificate.setFuel(stock.getFuel());
			exportCertificate.setConvertedDate(stock.getTakeToRikujiDate());

		}
		return new ResponseEntity<>(exportCertificate, HttpStatus.OK);
	}

	@PostMapping(path = "/receivedDoc/cancel")
	@Transactional
	public ResponseEntity<Response> cancelReceivedDoc(@RequestBody Map<String, Object> data) {
		String documentRemarks = AppUtil.isObjectEmpty(data.get("documentRemarks")) ? ""
				: data.get("documentRemarks").toString();
		String stockNo = AppUtil.isObjectEmpty(data.get("stockNo")) ? "" : data.get("stockNo").toString();
		MLoginDto loggedInUser = this.securityService.findLoggedInUser();
		TStock updateStock = this.stockRepository.findOneByStockNo(stockNo);
		updateStock.setDocumentStatus(Constants.STOCK_DOCUMENT_RECEIVED_AND_CANCELLED);
		updateStock.setDocumentRemarks(documentRemarks);
		updateStock.setLastModifiedDate(new Date());
		updateStock.setLastModifiedBy(loggedInUser.getUsername());
		this.stockRepository.save(updateStock);

		TDocumentReceived tDocumentReceived = this.documentReceivedRepository.findOneByStockNo(stockNo);
		tDocumentReceived.setDocumentStatus(Constants.STOCK_DOCUMENT_RECEIVED_AND_CANCELLED);
		tDocumentReceived.setRemarks(documentRemarks);
		tDocumentReceived.setLastModifiedDate(new Date());
		tDocumentReceived.setLastModifiedBy(loggedInUser.getUsername());
		this.documentReceivedRepository.save(tDocumentReceived);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping(path = "/updateRikujiStatus1")
	@Transactional
	public ResponseEntity<Response> updateRikujiToOne(
			@RequestParam("rikujiUpdateToOneDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date rikujiUpdateToOneDate,
			@RequestParam("rikujiRemarks") String rikujiRemarks, @RequestBody List<String> data,
			@RequestParam("flag") String flag) {
		BulkOperations stockTrack = this.mongoTemplate.bulkOps(BulkMode.UNORDERED, TStock.class);
		Date takeToRikujiDate = new Date();
		data.forEach(stock -> {
			TDocumentConversion updateDoc = this.tDocumentConversionRepository.findOneByStockNo(stock);
			if (AppUtil.ifNull(flag, "").equalsIgnoreCase(Constants.UPDATE_RIKUJI_FROM_EXPORT_CERTIFICATE)) {
				updateDoc.setDocConvertTo(Constants.STOCK_DOCUMENT_TYPE_EXPIRED);
				this.tDocumentConversionRepository.save(updateDoc);
			}

			TDocumentReceived tDocumentReceived = this.documentReceivedRepository.findOneByStockNo(stock);
			if (AppUtil.ifNull(flag, "").equalsIgnoreCase(Constants.UPDATE_RIKUJI_FROM_EXPORT_CERTIFICATE)) {
				tDocumentReceived.setRikujiStatus(Constants.STOCK_RIKUJI_STATUS_1);
				tDocumentReceived.setRikujiUpdateToOneDate(rikujiUpdateToOneDate);
				tDocumentReceived.setRikujiRemarks(rikujiRemarks);
				tDocumentReceived.setDocumentStatus(Constants.STOCK_DOCUMENT_RECEIVED);
				this.documentReceivedRepository.save(tDocumentReceived);
			} else {
				tDocumentReceived.setRikujiUpdateToOneDate(rikujiUpdateToOneDate);
				tDocumentReceived.setRikujiRemarks(rikujiRemarks);
				tDocumentReceived.setRikujiStatus(Constants.STOCK_RIKUJI_STATUS_1);
				this.documentReceivedRepository.save(tDocumentReceived);
			}

			TStock tStock = this.stockRepository.findOneByStockNo(stock);
			tStock.setHandoverTo(Constants.DOCUMENTS_HANDOVER_TO_RIKUJI);
			tStock.setRikujiStatus(Constants.STOCK_RIKUJI_STATUS_0);
			tStock.setTakeToRikujiDate(takeToRikujiDate);
			this.stockRepository.save(tStock);

		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping(path = "/updateDocumentFromRikuji")
	@Transactional
	public ResponseEntity<Response> updateDocumentFromRikuji(@RequestBody List<String> data)
			throws IOException, ParseException {
		List<TDocumentReceived> stocks = this.documentReceivedRepository.findAllByStockNoIn(data);
		MLoginDto loggedInUser = this.securityService.findLoggedInUser();
		List<TDocumentConversion> docConversionList = new ArrayList<>();

		stocks.forEach(stock -> {
			TDocumentConversion updateConversion = this.tDocumentConversionRepository
					.findOneByStockNo(stock.getStockNo());
			if (updateConversion == null) {
				TDocumentConversion docConversion = new TDocumentConversion(stock.getStockNo(),
						stock.getDocumentConvertTo());
				if (stock.getDocumentConvertTo().equals(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE)) {
					docConversion.setExportCertificateStatus(Constants.EXPORT_CERTIFICATE_INALAIN);
				}
				docConversionList.add(docConversion);
			} else {
				updateConversion.setDocConvertTo(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_EXPORT_CERTIFICATE);
				this.tDocumentConversionRepository.save(updateConversion);

			}

			TDocumentReceived docReceived = this.documentReceivedRepository.findOneByStockNo(stock.getStockNo());
			docReceived.setDocumentStatus(Constants.STOCK_DOCUMENT_CONVERT);
			docReceived.setLastModifiedDate(new Date());
			docReceived.setLastModifiedBy(loggedInUser.getUsername());
			this.documentReceivedRepository.save(docReceived);

			TStock stockUpdate = this.stockRepository.findOneByStockNo(stock.getStockNo());
			stockUpdate.setHandoverTo(Constants.DOCUMENTS_HANDOVER_TO_DOCUMENTS_TEAM);
			stockUpdate.setRikujiStatus(Constants.STOCK_RIKUJI_STATUS_0);
			stockUpdate.setLastModifiedDate(new Date());
			stockUpdate.setLastModifiedBy(loggedInUser.getUsername());
			this.stockRepository.save(stockUpdate);

		});

		this.tDocumentConversionRepository.saveAll(docConversionList);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/update/exportcertificate/documentdetails")
	@Transactional
	public ResponseEntity<Response> updateDocDetails(@RequestParam("ids[]") List<String> ids,
			@RequestBody Map<String, Object> data) throws ParseException {
		MLoginDto loggedInUser = this.securityService.findLoggedInUser();
		Date docSendDate = null;
		if (!AppUtil.isObjectEmpty(data.get("docSendDate"))) {
			docSendDate = new SimpleDateFormat("dd-MM-yyyy").parse((String) data.get("docSendDate"));
		}
		Integer docEmailSent = AppUtil.isObjectEmpty(data.get("docEmailSent")) ? null
				: Integer.valueOf(data.get("docEmailSent").toString());
		Integer docOriginalSent = AppUtil.isObjectEmpty(data.get("docOriginalSent")) ? null
				: Integer.valueOf(data.get("docOriginalSent").toString());
		Integer exportCertificateStatus = AppUtil.isObjectEmpty(data.get("exportCertificateStatus")) ? null
				: Integer.valueOf(data.get("exportCertificateStatus").toString());
		String inspectionCompanyId = AppUtil.isObjectEmpty(data.get("inspectionCompanyId")) ? null
				: data.get("inspectionCompanyId").toString();
		String shippingCompanyId = AppUtil.isObjectEmpty(data.get("shippingCompanyId")) ? null
				: data.get("shippingCompanyId").toString();

		// BulkOperations stockTrack = this.mongoTemplate.bulkOps(BulkMode.UNORDERED,
		// TStock.class);
		List<TDocumentConversion> documentConversions = this.tDocumentConversionRepository.findAllByIdIn(ids);
		for (TDocumentConversion document : documentConversions) {
			TStock tStock = this.stockRepository.findOneByStockNo(document.getStockNo());
			// stock update

			if (exportCertificateStatus.equals(Constants.EXPORT_CERTIFICATE_SHIPPING_COMPANY)) {
				document.setShippingCompanyId(shippingCompanyId);
				document.setDocOriginalSent(docOriginalSent);
				document.setDocEmailSent(docEmailSent);
				document.setExportCertificateStatus(Constants.EXPORT_CERTIFICATE_SHIPPING_COMPANY);
				// update document handover status
				tStock.setHandoverTo(Constants.DOCUMENTS_HANDOVER_TO_SHIPPING);
			} else if (exportCertificateStatus == Constants.EXPORT_CERTIFICATE_INSPECTION_COMPANY) {
				document.setInspectionCompanyId(inspectionCompanyId);
				document.setExportCertificateStatus(Constants.EXPORT_CERTIFICATE_INSPECTION_COMPANY);
				// update document handover status
				tStock.setHandoverTo(Constants.DOCUMENTS_HANDOVER_TO_INSPECTION);
			}
			document.setDocSendDate(docSendDate);
			tStock.setLastModifiedDate(new Date());
			tStock.setLastModifiedBy(loggedInUser.getUsername());
			this.stockRepository.save(tStock);
		}
		// stockTrack.execute();
		this.tDocumentConversionRepository.saveAll(documentConversions);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/update/exportcertificate/originalReceived")
	@Transactional
	public ResponseEntity<Response> updateDocDetails(@RequestParam("ids[]") List<String> ids) {
		List<TDocumentConversion> documents = this.tDocumentConversionRepository.findAllByIdIn(ids);
		documents.forEach(document -> {
			document.setDocReceivedStatus(Constants.EXPORT_CERTIFICATE_ORIGINAL_RECEIVED);
			document.setHandoverStatus(Constants.EXPORT_CERTIFICATE_NOT_HANDOVER);
			this.tDocumentConversionRepository.save(document);
			// update handover status
			TStock stock = this.stockRepository.findOneByStockNo(document.getStockNo());
			stock.setHandoverTo(Constants.DOCUMENTS_HANDOVER_TO_DOCUMENTS_TEAM);
			this.stockRepository.save(stock);
		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	/* export-cerficate-tracking */
	@GetMapping("/export-cerficate-tracking")
	public ModelAndView documentExportCertificateTracking(ModelAndView modelAndView) {
		modelAndView.setViewName("document.dashboard.export-certificate-tracking");
		return modelAndView;
	}

	@GetMapping("/export-certificate-tracking-list")
	@ResponseBody
	public DatatableResponse getExportCertificateTrackingList() {
		return new DatatableResponse(this.stockRepository.findAllExportTrackingList());
	}

	@GetMapping("/cr-received")
	public ModelAndView documentTrackingCRReceived(ModelAndView modelAndView) {
		modelAndView.setViewName("document.cr.received");
		return modelAndView;
	}

	@GetMapping("/cr-received/data-list")
	@ResponseBody
	public DatatableResponse documentTrackingCRReceivedList() {
		return new DatatableResponse(this.tDocumentConversionRepository.findAllCrReceivedDocument());
	}

	@PutMapping(path = "/cr-received/update")
	@Transactional
	public ResponseEntity<Response> crReceivedListUpdate(@RequestParam("handoverTo") String handoverToString,
			@RequestParam("handoverToUserId") String handoverToUserId, @RequestBody List<String> data) {
		MLoginDto loggedInUser = this.securityService.findLoggedInUser();

		data.forEach(stock -> {

			Integer handoverTo = null;
			if (handoverToString.equalsIgnoreCase("ACCOUNTS")) {
				handoverTo = Constants.DOCUMENTS_HANDOVER_TO_ACCOUNTS;
			} else if (handoverToString.equalsIgnoreCase("SALES")) {
				handoverTo = Constants.DOCUMENTS_HANDOVER_TO_SALES;
			} else if (handoverToString.equalsIgnoreCase("RECYCLE_PURPOSE")) {
				handoverTo = Constants.DOCUMENTS_HANDOVER_TO_RECYCLE_PURPOSE;
			}

			TStock stockUpdate = this.stockRepository.findOneByStockNo(stock);
			stockUpdate.setHandoverTo(handoverTo);
			stockUpdate.setHandoverToUserId(handoverToUserId);
			stockUpdate.setLastModifiedDate(new Date());
			stockUpdate.setLastModifiedBy(loggedInUser.getUsername());
			this.stockRepository.save(stockUpdate);

			TDocumentConversion tDocumentConversion = this.tDocumentConversionRepository.findOneByStockNo(stock);
			tDocumentConversion.setHandoverStatus(Constants.EXPORT_CERTIFICATE_HANDOVER);
			this.tDocumentConversionRepository.save(tDocumentConversion);

		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/update/fob-price")
	public ResponseEntity<Response> updateDocumentFobPrice(@RequestBody Map<String, Object> data)
			throws IOException, ParseException {
		this.exportCertificateService.updateDocumentFobPrice(data);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/update/exportcertificate/nonExportedCrReceived")
	@Transactional
	public ResponseEntity<Response> nonExportedCrReceived(@RequestParam("ids[]") List<String> ids) {

		List<TDocumentConversion> documents = this.tDocumentConversionRepository.findAllByIdIn(ids);
		documents.forEach(document -> {
			document.setExportCertificateStatus(Constants.EXPORT_CERTIFICATE_INALAIN);
			document.setDocOriginalSent(Constants.EXPORT_CERTIFICATE_DOCUMENT_ORIGINAL_NOT_SENT);
			this.tDocumentConversionRepository.save(document);
			// update handover status
			TStock stock = this.stockRepository.findOneByStockNo(document.getStockNo());
			stock.setHandoverTo(Constants.DOCUMENTS_HANDOVER_TO_DOCUMENTS_TEAM);
			this.stockRepository.save(stock);
		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/updateReauctionDetails")
	public ResponseEntity<Response> updateReauctionDetails(@RequestParam("stockNos[]") List<String> stockNos,
			@RequestBody final Map<String, String> data) throws ParseException {
		String supplier = data.get("supplier");
		String auctionHouse = data.get("auctionHouse");
		String sReauctionDate = data.get("reauctionDate");
		Date reauctionDate = null;
		ObjectId oAuctionHouse = null;
		if (!AppUtil.isObjectEmpty(sReauctionDate)) {
			reauctionDate = new SimpleDateFormat("dd-MM-yyyy").parse(sReauctionDate);
		}
		if (!AppUtil.isObjectEmpty(auctionHouse)) {
			oAuctionHouse = new ObjectId(auctionHouse);
		}
		for (String stockNo : stockNos) {
			TDocumentConversion document = tDocumentConversionRepository.findOneByStockNo(stockNo);
			document.setSupplier(supplier);
			document.setAuctionHouse(oAuctionHouse);
			document.setReauctionDate(reauctionDate);
			tDocumentConversionRepository.save(document);
		}

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping(value = { "custom/report" })
	public ModelAndView stockTracker(ModelAndView modelAndView) {
		modelAndView.setViewName("documents.custom.report");
		return modelAndView;
	}

	@GetMapping("/stockInfo")
	public ModelAndView documentsStockInfo(final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("documents.stockinfo");
		return modelAndView;
	}
}
