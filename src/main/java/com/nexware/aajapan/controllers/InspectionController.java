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
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.InspectionApplicationDto;
import com.nexware.aajapan.dto.InspectionDto;
import com.nexware.aajapan.dto.InspectionStockDetailsDto;
import com.nexware.aajapan.dto.StockSearchDto;
import com.nexware.aajapan.dto.TInspectionInstructionDto;
import com.nexware.aajapan.dto.TInspectionRearrangeDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.InspectionCompanyLocation;
import com.nexware.aajapan.models.InspectionDetail;
import com.nexware.aajapan.models.TInspectionInstruction;
import com.nexware.aajapan.models.TInspectionOrderRequest;
import com.nexware.aajapan.models.TInspectionOrderRequestCancelled;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TInspectionInstructionRepository;
import com.nexware.aajapan.repositories.TInspectionOrderRequestCancelledRepository;
import com.nexware.aajapan.repositories.TInspectionOrderRequestRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;
import com.nexware.aajapan.utils.ExcelUtil;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Controller
@RequestMapping("inspection")
public class InspectionController {
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private TInspectionOrderRequestRepository tInspectionOrderRequestRepository;
	@Autowired
	private TInspectionInstructionRepository inspectionInstructionRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TInspectionOrderRequestCancelledRepository inspectionOrderRequestCancelled;

	@GetMapping(value = { "/inspection" })
	public ModelAndView stockInspection(ModelAndView modelAndView) {

		modelAndView.setViewName("shipping.inspection");
		return modelAndView;
	}

	@GetMapping("/inspection-data")
	@ResponseBody
	public DatatableResponse getInspectionData() {
		return new DatatableResponse(stockRepository.findAllByInspectionStatus());
	}

	@GetMapping("/instruction-data")
	@ResponseBody
	public DatatableResponse getInstructionData() {
		return new DatatableResponse(inspectionInstructionRepository.getAllGivenInstruction());
	}

	@GetMapping("/inspection-data-arranged")
	@ResponseBody
	public DatatableResponse getInspectionDataArranged(@RequestParam("show") int show,
			@RequestParam("showVehicel") int showVehicle) {
		Integer[] status;
		if (showVehicle == 0) {
			status = new Integer[] { Constants.INSPECTION_ORDER_REQUEST_INITIATED,
					Constants.INSPECTION_ORDER_REQUEST_PASSED };
		} else {
			status = new Integer[] { Constants.INSPECTION_ORDER_REQUEST_PASSED };
		}
		if (show == 0) {
			return new DatatableResponse(
					tInspectionOrderRequestRepository.findAllInspectionOrderRequestByStatus(status));
		} else {
			return new DatatableResponse(
					tInspectionOrderRequestRepository.findAllInspectionOrderRequestByTransportNotComplete(status));
		}

	}

	@GetMapping("/inspection-data-completed")
	@ResponseBody
	public DatatableResponse getInspectionDataCompleted() {
		return new DatatableResponse(tInspectionOrderRequestRepository
				.findAllInspectionCompletedByStatus(Constants.INSPECTION_ORDER_REQUEST_COMPLETE));
	}

	@GetMapping("/cancelled/order")
	@ResponseBody
	public DatatableResponse getInspectionDataCancelled() {
		return new DatatableResponse(
				tInspectionOrderRequestRepository.getCancelledOrderByStatus(Constants.INSPECTION_ORDER_REQUEST_FAILED));
	}

	@PutMapping("order/re-arrange")
	@Transactional
	public ResponseEntity<Response> reArrangeOrder(@RequestParam("code") String code,
			@RequestBody TInspectionRearrangeDto reArrOrderRequest) throws IOException, ParseException {
		TInspectionOrderRequest order = tInspectionOrderRequestRepository.findOneByCode(code);
		TInspectionOrderRequest reArrange = new TInspectionOrderRequest();
		reArrange.setStockNo(order.getStockNo());
		reArrange.setInstructionId(order.getInstructionId());
		reArrange.setCountry(reArrOrderRequest.getCountry());
		reArrange.setInspectionDate(reArrOrderRequest.getInspectionDate());
		reArrange.setForwarder(
				!AppUtil.isObjectEmpty(reArrOrderRequest.getForwarder()) ? reArrOrderRequest.getForwarder() : "");
		reArrange.setInspectionCompanyFlag(reArrOrderRequest.getInspectionCompanyFlag());
		reArrange.setInspectionCompany(!AppUtil.isObjectEmpty(reArrOrderRequest.getInspectionCompany())
				? reArrOrderRequest.getInspectionCompany()
				: "");
		reArrange.setLocation(!AppUtil.isObjectEmpty(reArrOrderRequest.getLocation()) ? reArrOrderRequest.getLocation()
				: new InspectionCompanyLocation());
		reArrange.setStatus(Constants.INSPECTION_ORDER_REQUEST_INITIATED);
		reArrange.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_TINSPECTION_ORDER_REQUEST));
		tInspectionOrderRequestRepository.save(reArrange);

		TStock stock = this.stockRepository.findOneByStockNo(order.getStockNo());
		// TODO : code = to create sequence in m_sqnc and generate sequence key
		inspectionOrderRequestCancelled.save(new TInspectionOrderRequestCancelled(
				this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_INSPECTION_FAILED), order.getStockNo(),
				reArrange.getCode(), reArrOrderRequest.getCountry(),
				reArrOrderRequest.getInspectionCompanyFlag() == 0 ? reArrOrderRequest.getInspectionCompanyValue()
						: reArrOrderRequest.getForwarderValue(),
				reArrOrderRequest.getLocationValue(), stock.getDestinationCountry(),
				reArrOrderRequest.getInspectionDate(), new Date(), order.getCancelRemark(),
				reArrOrderRequest.getInspectionCompanyFlag() == 0 ? "AAJ" : "FORWARDER"));
		tInspectionOrderRequestRepository.deleteByCode(code);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/failed/history/data-source")
	@ResponseBody
	public DatatableResponse invoicePaymentsDataList(@RequestParam("stockNo") String stockNo) {
		return new DatatableResponse(inspectionOrderRequestCancelled.findAllByStockNo(stockNo));
	}

	@PostMapping("instruction/save")
	@Transactional
	public ResponseEntity<Response> saveInstruction(@RequestBody List<TInspectionInstruction> data)
			throws IOException, ParseException {
		data.forEach(req -> {
			req.setStatus(Constants.INSPECTION_INSTRUCTION_GIVEN);
			req.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_TINSPECTION_INSTRUCTION));
		});
		final List<String> stockNos = data.stream().map(TInspectionInstruction::getStockNo)
				.collect(Collectors.toList());
		inspectionInstructionRepository.saveAll(data);
		stockRepository.updateInspectionStatusByStockNo(Constants.STOCK_NOT_AVAILABLE_FOR_INSPECTION, stockNos);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("instruction/moveToRequested")
	public ResponseEntity<Response> moveToRequested(@RequestParam("instructionId") String instructionId,
			@RequestParam("code") String code, @RequestBody TInspectionInstruction data)
			throws IOException, ParseException {
		if (AppUtil.isObjectEmpty(instructionId)) {
			String seqCode = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_TINSPECTION_INSTRUCTION);
			data.setStatus(Constants.INSPECTION_INSTRUCTION_GIVEN);
			data.setCode(seqCode);
			inspectionInstructionRepository.save(data);
			TInspectionOrderRequest order = tInspectionOrderRequestRepository.findOneByCode(code);
			order.setInstructionId(seqCode);
			tInspectionOrderRequestRepository.save(order);
		} else {
			TInspectionInstruction tInspectionInstruction = inspectionInstructionRepository
					.findOneByCode(instructionId);
			tInspectionInstruction.setStatus(Constants.INSPECTION_INSTRUCTION_GIVEN);
			inspectionInstructionRepository.save(tInspectionInstruction);
		}

		// tInspectionOrderRequestRepository.deleteByStockNo(stockNo);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping(path = "request/cancel")
	@Transactional
	public ResponseEntity<Response> cancelRequest(@RequestParam("id") String id) {
		TInspectionInstruction inspectionInstruction = inspectionInstructionRepository.findOneByCode(id);
		inspectionInstruction.setStatus(Constants.INSPECTION_INSTRUCTION_CANCELLED);
		inspectionInstructionRepository.save(inspectionInstruction);
		TStock stock = stockRepository.findOneByStockNo(inspectionInstruction.getStockNo());
		stock.setInspectionStatus(Constants.STOCK_AVAILABLE_FOR_INSPECTION);
		stockRepository.save(stock);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping(path = "request/cancelled/update/remark")
	@Transactional
	public ResponseEntity<Response> updateRemark(@RequestParam("id") String id, @RequestBody Document request) {
		String remark = request.getString("remark");
		TInspectionOrderRequest inspectionOrderRequest = tInspectionOrderRequestRepository.findOneByCode(id);
		inspectionOrderRequest.setRemark(remark);
		tInspectionOrderRequestRepository.save(inspectionOrderRequest);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/save")
	@Transactional
	public ResponseEntity<Response> saveInspection(@RequestBody List<TInspectionOrderRequest> data)
			throws IOException, ParseException {
		data.forEach(req -> {
			req.setStatus(Constants.INSPECTION_ORDER_REQUEST_INITIATED);
			req.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_TINSPECTION_ORDER_REQUEST));
		});
		tInspectionOrderRequestRepository.saveAll(data);
		data.forEach(req -> {
			final TInspectionInstruction instruction = inspectionInstructionRepository
					.findOneByCode(req.getInstructionId());
			instruction.setStatus(Constants.INSPECTION_INSTRUCTION_INITIATED);
			inspectionInstructionRepository.save(instruction);
		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("stock/update/photoReceived")
	@Transactional
	public ResponseEntity<Response> updatePhotoReceivedStatus(@RequestBody List<StockSearchDto> data) {
		data.forEach(req -> {
			TStock stock = stockRepository.findOneByStockNo(req.getStockNo());
			stock.setIsPhotoUploaded(Constants.STOCK_PHOTOS_UPLOADED);
			stockRepository.save(stock);
		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("stock/update/photoNotReceived")
	@Transactional
	public ResponseEntity<Response> updatePhotoNotReceivedStatus(@RequestBody List<StockSearchDto> data) {
		data.forEach(req -> {
			TStock stock = stockRepository.findOneByStockNo(req.getStockNo());
			stock.setIsPhotoUploaded(Constants.STOCK_PHOTOS_NOT_UPLOADED);
			stockRepository.save(stock);
		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("stock/update/photoNotReceived/single")
	@Transactional
	public ResponseEntity<Response> updateSinglePhotoNotReceivedStatus(@RequestParam("stockNo") String stockNo) {
		TStock stock = stockRepository.findOneByStockNo(stockNo);
		stock.setIsPhotoUploaded(Constants.STOCK_PHOTOS_NOT_UPLOADED);
		stockRepository.save(stock);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/cancel")
	@Transactional
	public ResponseEntity<Response> cancel(@RequestParam("code") String code, @RequestBody Map<String, String> data) {
		final String reason = data.get("reason");
		// final String stockNo = data.get("stockNo");
		final TInspectionOrderRequest orderRequest = tInspectionOrderRequestRepository.findOneByCode(code);
		orderRequest.setCancelRemark(reason);
		orderRequest.setStatus(Constants.INSPECTION_ORDER_REQUEST_FAILED);
		tInspectionOrderRequestRepository.save(orderRequest);
		// update stock info
//		final TStock stock = stockRepository.findOneByStockNo(stockNo);
//		stock.setInspectionStatus(Constants.STOCK_AVAILABLE_FOR_INSPECTION);
//		stockRepository.save(stock);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/update-document")
	public ResponseEntity<Response> updateInspection(@RequestBody Map<String, String> data) throws ParseException {
		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		final Integer status = Integer.parseInt(data.get("status"));
		final Date date = formatter.parse(data.get("date"));
		final String id = data.get("id");
		tInspectionOrderRequestRepository.updateInspectionDocumentSentStatus(id, status, date);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/update-document-multiple")
	public ResponseEntity<Response> updateInspectionMultiple(@RequestBody List<Map<String, String>> data)
			throws ParseException {
		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		final Date date = formatter.parse(data.get(0).get("date"));
		final Integer status = Integer.parseInt(data.get(0).get("status"));
		data.forEach(inspectionId -> {
			final String id = inspectionId.get("id");
			tInspectionOrderRequestRepository.updateInspectionDocumentSentStatus(id, status, date);
		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/update-inspection-info")
	@Transactional
	public ResponseEntity<Response> updateInspectionArrange(@RequestBody Map<String, Object> data)
			throws ParseException {
		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		final Date dateOfIssue = formatter.parse((String) data.get("dateOfIssue"));
		final String certificateNo = data.get("certificateNo").toString();
		final String inspectionRequestId = (String) data.get("ids");

		final String engineNo = data.get("engineNo").toString();
		final String chassisNo = data.get("chassisNo").toString();
		final String color = data.get("color").toString();
		final Double cc = Double.valueOf(AppUtil.isObjectEmpty(data.get("cc")) ? "0.0" : data.get("cc").toString());
		final String maker = data.get("maker").toString();
		final String model = data.get("model").toString();
		final String sFirstRegDate = data.get("sFirstRegDate").toString();
		final String remarks = data.get("remarks").toString();
		final String hsCode = data.get("hsCode").toString();
		final String country = data.get("country").toString();
		final String stockNo = data.get("stockNo").toString();
		@SuppressWarnings("unchecked")
		final List<String> equipment = (List<String>) data.get("equipment");

		// update stock table
		final TStock stock = stockRepository.findOneByStockNo(stockNo);
		stock.setChassisNo(chassisNo);
		stock.setCc(cc);
		stock.setMaker(maker);
		stock.setModel(model);
		stock.setsFirstRegDate(sFirstRegDate);
		stock.setColor(color);
		stock.setHsCode(hsCode);
		stock.setRemarks(remarks);
		stock.setEquipment(equipment);
		stock.setEngineNo(engineNo);
		// stock.setInspectionStatus(Constants.STOCK_AVAILABLE_FOR_INSPECTION);
		List<InspectionDetail> inspectionDetails = stock.getInspectionDetails();
		// check inspection is exist for country
		if (!AppUtil.isObjectEmpty(inspectionDetails) && !inspectionDetails.isEmpty()) {
			inspectionDetails.removeIf(inspection -> inspection.getCountry().equalsIgnoreCase(country));
		} else {
			inspectionDetails = new ArrayList<>();
		}

		final TInspectionOrderRequest orderRequest = tInspectionOrderRequestRepository.findOneByid(inspectionRequestId);
		orderRequest.setCertificateNo(certificateNo);
		orderRequest.setDateOfIssue(dateOfIssue);
		orderRequest.setStatus(Constants.INSPECTION_ORDER_REQUEST_COMPLETE);
		tInspectionOrderRequestRepository.save(orderRequest);

		inspectionDetails.add(new InspectionDetail(country, orderRequest.getCode()));
		stock.setInspectionDetails(inspectionDetails);
		stockRepository.save(stock);
		// update inspection request

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/pass")
	@Transactional
	public ResponseEntity<Response> updateInspectionArrange(@RequestParam("inspectionId") String inspectionId)
			throws ParseException {
		final TInspectionOrderRequest orderRequest = tInspectionOrderRequestRepository.findOneByid(inspectionId);
		orderRequest.setStatus(Constants.INSPECTION_ORDER_REQUEST_PASSED);
		tInspectionOrderRequestRepository.save(orderRequest);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("/pass-multiple")
	@Transactional
	public ResponseEntity<Response> updateInspectionArrangeMultiple(@RequestBody List<String> inspectionIds)
			throws ParseException {

		inspectionIds.forEach(id -> {
			final TInspectionOrderRequest orderRequest = tInspectionOrderRequestRepository.findOneByid(id);
			orderRequest.setStatus(Constants.INSPECTION_ORDER_REQUEST_PASSED);
			tInspectionOrderRequestRepository.save(orderRequest);
		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	@Transactional
	public ResponseEntity<Response> deleteInspectionArrange(@RequestParam("inspectionId") String inspectionId)
			throws ParseException {

		final TInspectionOrderRequest orderRequest = tInspectionOrderRequestRepository.findOneByid(inspectionId);
		orderRequest.setStatus(Constants.INSPECTION_ORDER_REQUEST_DELETED);
		tInspectionOrderRequestRepository.save(orderRequest);

		TStock stock = stockRepository.findOneByStockNo(orderRequest.getStockNo());
		stock.setInspectionStatus(Constants.STOCK_AVAILABLE_FOR_INSPECTION);
		stockRepository.save(stock);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	// .pdf
	@GetMapping("/application")
	public void pdfForInspectionCompany(
			@RequestParam(value = "inspection_company", required = false) String inspectionCompanyId,
			@RequestParam(value = "location", required = false) String locationId,
			@RequestParam(value = "date", required = true) @DateTimeFormat(pattern = "dd-MM-yyyy") Date inspectionDate,
			@RequestParam(value = "country", required = true) String country,
			@RequestParam(value = "forwarder", required = false) String forwarderId,
			@RequestParam("format") String format, HttpServletResponse response) throws JRException, IOException {

		final InspectionApplicationDto stockItems = tInspectionOrderRequestRepository
				.findAllInspectionOrderRequestByParams(inspectionDate, country, inspectionCompanyId, forwarderId,
						locationId);
		JRBeanCollectionDataSource datasourceJRBean = new JRBeanCollectionDataSource(stockItems.getItems());
		final List<InspectionStockDetailsDto> items;
		final Map<String, Object> params = new HashMap<>();
		InputStream jasperStream = null;
		String logoPath = "";

		if ((stockItems.getInspectionCompanyFlag() == 0) && (stockItems.getCountry().contentEquals("SRI LANKA")
				&& stockItems.getInspectionCompanyId().contentEquals("2"))) {
			if (stockItems.getItems().size() <= 5) {
				items = AppUtil.addEmptyObject(stockItems.getItems(), 5 - (stockItems.getItems().size() % 5));
			} else {
				items = AppUtil.addEmptyObject(stockItems.getItems(), 20 - (stockItems.getItems().size() % 20));
			}
			datasourceJRBean = new JRBeanCollectionDataSource(items);
			logoPath = "/templates/jasper/images/jevicLogo.png";
			jasperStream = this.getClass()
					.getResourceAsStream("/templates/jasper/JevicSrilankaInspectionTemplate.jasper");

		} else if ((stockItems.getInspectionCompanyFlag() == 0) && (stockItems.getCountry().contentEquals("BANGLADESH")
				&& stockItems.getInspectionCompanyId().contentEquals("1"))) {
			items = AppUtil.addEmptyObject(stockItems.getItems(), 20 - (stockItems.getItems().size() % 20));
			datasourceJRBean = new JRBeanCollectionDataSource(items);
			logoPath = "/templates/jasper/images/logo.png";
			jasperStream = this.getClass()
					.getResourceAsStream("/templates/jasper/JaaiBangladeshInspectionTemplate.jasper");

		} else if ((stockItems.getInspectionCompanyFlag() == 0) && (stockItems.getCountry().contentEquals("SRI LANKA")
				&& stockItems.getInspectionCompanyId().contentEquals("1"))) {
			items = AppUtil.addEmptyObject(stockItems.getItems(), 10 - (stockItems.getItems().size() % 10));
			datasourceJRBean = new JRBeanCollectionDataSource(items);
			jasperStream = this.getClass().getResourceAsStream("/templates/jasper/Application_JAAI_Kanagawa.jasper");
			logoPath = "/templates/jasper/images/logo.png";
		} else if ((stockItems.getInspectionCompanyFlag() == 0) && (stockItems.getCountry().contentEquals("JAMAICA")
				&& stockItems.getInspectionCompanyId().contentEquals("4"))) {
			items = AppUtil.addEmptyObject(stockItems.getItems(), 10 - (stockItems.getItems().size() % 10));
			datasourceJRBean = new JRBeanCollectionDataSource(items);
			jasperStream = this.getClass().getResourceAsStream("/templates/jasper/AutoTerminal_Jamaica.jasper");
			logoPath = "/templates/jasper/images/logo.png";
		} else if ((stockItems.getInspectionCompanyFlag() == 0) && (stockItems.getCountry().contentEquals("SRI LANKA")
				&& stockItems.getInspectionCompanyId().contentEquals("3"))) {
			items = AppUtil.addEmptyObject(stockItems.getItems(), 10 - (stockItems.getItems().size() % 10));
			datasourceJRBean = new JRBeanCollectionDataSource(items);
			final JRBeanCollectionDataSource datasourceJRBean1 = new JRBeanCollectionDataSource(items);
			jasperStream = this.getClass().getResourceAsStream("/templates/jasper/RFI_1.jasper");
			logoPath = "/templates/jasper/images/logo.png";
			params.put("stockDetails", datasourceJRBean1);
		} else {
			items = AppUtil.addEmptyObject(stockItems.getItems(), 20 - (stockItems.getItems().size() % 20));
			datasourceJRBean = new JRBeanCollectionDataSource(items);
			jasperStream = this.getClass().getResourceAsStream("/templates/jasper/ForwarderTemplate.jasper");
			logoPath = "/templates/jasper/images/logo.png";
		}
		params.put("stockItems", datasourceJRBean);
		params.put("inspectionDetails", stockItems);

		try (InputStream inputStream = this.getClass().getResourceAsStream(logoPath)) {
			params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
		} catch (JRException | IOException e) {
			throw new AAJRuntimeException("Failed to load images", e);
		}
		final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

		final String filename = "InspectionApplication";

		final OutputStream outStream = response.getOutputStream();
		if (format.equalsIgnoreCase("pdf")) {
			response.setContentType("application/x-pdf");
			response.setHeader("Content-disposition", "inline; filename=" + filename + ".pdf");
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		} else {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
			final JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
			exporter.exportReport();
			outStream.flush();
		}

		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	}

	@PutMapping("stock/update/mashoCopyReceived")
	@Transactional
	public ResponseEntity<Response> updateMashoCopyReceivedDate(
			@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date,
			@RequestBody List<StockSearchDto> data) throws IOException, ParseException {

		data.forEach(req -> {
			TStock stock = stockRepository.findOneByStockNo(req.getStockNo());
			stock.setMashoCopyReceivedDate(date);
			stockRepository.save(stock);
		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("stock/excel/report")
	public void inspectionStockExcelReport(@RequestParam(name = "stockNos[]", required = false) List<String> stockNos,
			HttpServletResponse response) {
		StringBuilder filename = new StringBuilder("inspection_available_report").append(".xlsx");
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet("Inspection Available");
			Integer rowNo = 0;

			String[] columns = { "Chassis No", "Model", "Dest. Country ", "Staff", "Year", "Color", "Final Port",
					"Supplier", "Shuppin", "Photo", "Masho Copy", "Estimated Depature", "ETD", "Vessal Name",
					"Transporter", "Delivery Date", "Inspection Status", "Shipping Status" };

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			Row headerRow = sheet.createRow(rowNo++);
			for (int col = 0; col < columns.length; col++) {
				ExcelUtil.autoSizeColumnNumber(sheet, col);
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(columns[col]);
				cell.setCellStyle(headerCellStyle);
			}
			List<InspectionDto> list;
			if (!AppUtil.isObjectEmpty(stockNos)) {
				list = stockRepository.findAllByInspectionStatusAndStockNos(stockNos);
			} else {
				list = stockRepository.findAllByInspectionStatus();
			}

			Row row;
			for (InspectionDto data : list) {
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, columns.length);
				ExcelUtil.setCellValue(row.getCell(0), data.getChassisNo());
				ExcelUtil.setCellValue(row.getCell(1), data.getModel());
				ExcelUtil.setCellValue(row.getCell(2), data.getDestinationCountry());
				ExcelUtil.setCellValue(row.getCell(3), data.getBookingDetails());
				ExcelUtil.setCellValue(row.getCell(4), data.getFirstRegDate());
				ExcelUtil.setCellValue(row.getCell(5), data.getColor());
				if (!AppUtil.isObjectEmpty(data.getLastTransportLocation())) {
					if (data.getLastTransportLocation().toLowerCase().equalsIgnoreCase("others")) {
						ExcelUtil.setCellValue(row.getCell(6), data.getLastTransportLocationCustom());
					} else {
						ExcelUtil.setCellValue(row.getCell(6), data.getsLastTransportLocation());
					}
				}

				ExcelUtil.setCellValue(row.getCell(7), data.getSupplierName());
				ExcelUtil.setCellValue(row.getCell(8), data.getLotNo());
				if (!AppUtil.isObjectEmpty(data.getIsPhotoUploaded())) {
					if (data.getIsPhotoUploaded().equals(Constants.STOCK_PHOTOS_UPLOADED)) {
						ExcelUtil.setCellValue(row.getCell(9), "PHOTO OK");
					} else if (data.getIsPhotoUploaded().equals(Constants.STOCK_PHOTOS_NOT_UPLOADED)) {
						ExcelUtil.setCellValue(row.getCell(9), "NO PHOTO");
					}

				}
				ExcelUtil.setCellValue(row.getCell(10), data.getDocumentReceivedDate());
				if (!AppUtil.isObjectEmpty(data.getShippingInstructionStatus())) {
					if (data.getShippingInstructionStatus().equals(Constants.SHIPPMENT_SCHEDULE_TYPE_IMMEDIATE)) {
						ExcelUtil.setCellValue(row.getCell(11), "Immediate");
					} else if (data.getShippingInstructionStatus()
							.equals(Constants.SHIPPMENT_SCHEDULE_TYPE_NEXT_AVAILABLE_SHIP)) {
						ExcelUtil.setCellValue(row.getCell(11), "Next Available");
					} else if (data.getShippingInstructionStatus()
							.equals(Constants.SHIPPMENT_SCHEDULE_TYPE_PREFERRED_MONTH)) {
						ExcelUtil.setCellValue(row.getCell(11), data.getEstimatedDeparture());
					}
				}
				ExcelUtil.setCellValue(row.getCell(12), data.getShippingDate());
				ExcelUtil.setCellValue(row.getCell(13), data.getVessalName());
				ExcelUtil.setCellValue(row.getCell(14), data.getTransporterName());
				ExcelUtil.setCellValue(row.getCell(15), data.getTransportDeliveryDate());
				if (!AppUtil.isObjectEmpty(data.getInspectionStatus())) {
					if (data.getInspectionStatus().equals(Constants.INSPECTION_NOT_ARRANGED)) {
						ExcelUtil.setCellValue(row.getCell(16), "Yet to Arrange");
					} else if (data.getInspectionStatus().equals(Constants.INSPECTION_DONE)) {
						ExcelUtil.setCellValue(row.getCell(16), "Inspection Done");
					}
				}
				ExcelUtil.setCellValue(row.getCell(17), "Not Arranged");
				if (!AppUtil.isObjectEmpty(data.getShippingStatus())) {
					if (data.getShippingStatus().equals(0)) {
						ExcelUtil.setCellValue(row.getCell(17), "Shipping Arranged");
					} else if (data.getShippingStatus().equals(1)) {
						ExcelUtil.setCellValue(row.getCell(17), "Shipping Confirmed");
					}
				}

			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			workbook.write(response.getOutputStream());
		} catch (final Exception e) {
			throw new AAJRuntimeException("Exception while inspection available report.", e);
		}
	}

	@PostMapping("requested/stock/excel/report")
	public void inspectionRequestedStockExcelReport(
			@RequestParam(name = "stockNos[]", required = false) List<String> stockNos, HttpServletResponse response) {
		StringBuilder filename = new StringBuilder("inspection_requested_report").append(".xlsx");
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet("Inspection Requested");
			Integer rowNo = 0;

			String[] columns = { "Chassis No", "Model", "Year", "Color", "Final Port", "Supplier", "Shuppin", "Photo",
					"Masho Copy", "Staff", "ETD", "Vessal Name", "Transporter", "Inspection Status",
					"Shipping Status" };

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			Row headerRow = sheet.createRow(rowNo++);
			for (int col = 0; col < columns.length; col++) {
				ExcelUtil.autoSizeColumnNumber(sheet, col);
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(columns[col]);
				cell.setCellStyle(headerCellStyle);
			}
			List<TInspectionInstructionDto> list = null;
			if (!AppUtil.isObjectEmpty(stockNos)) {
				list = inspectionInstructionRepository.getAllGivenInstructionByStockNo(stockNos);
			} else {
				list = inspectionInstructionRepository.getAllGivenInstruction();
			}

			Row row;
			for (TInspectionInstructionDto data : list) {
				row = ExcelUtil.createRow(workbook, sheet, rowNo++, columns.length);
				ExcelUtil.setCellValue(row.getCell(0), data.getChassisNo());
				ExcelUtil.setCellValue(row.getCell(1), data.getModel());
				ExcelUtil.setCellValue(row.getCell(2), data.getFirstRegDate());
				ExcelUtil.setCellValue(row.getCell(3), data.getColor());
				ExcelUtil.setCellValue(row.getCell(4), data.getDestinationPort());
				ExcelUtil.setCellValue(row.getCell(5), data.getSupplierName());
				ExcelUtil.setCellValue(row.getCell(6), data.getLotNo());
				if (!AppUtil.isObjectEmpty(data.getIsPhotoUploaded())) {
					if (data.getIsPhotoUploaded().equals(Constants.STOCK_PHOTOS_UPLOADED)) {
						ExcelUtil.setCellValue(row.getCell(7), "PHOTO OK");
					} else if (data.getIsPhotoUploaded().equals(Constants.STOCK_PHOTOS_NOT_UPLOADED)) {
						ExcelUtil.setCellValue(row.getCell(7), "NO PHOTO");
					}

				}
				ExcelUtil.setCellValue(row.getCell(8), data.getDocumentReceivedDate());
				ExcelUtil.setCellValue(row.getCell(9), data.getBookingDetails());
				ExcelUtil.setCellValue(row.getCell(10), data.getShippingDate());
				ExcelUtil.setCellValue(row.getCell(11), data.getVessalName());
				ExcelUtil.setCellValue(row.getCell(12), data.getTransporterName());
				if (!AppUtil.isObjectEmpty(data.getInspectionStatus())) {
					if (data.getInspectionStatus().equals(Constants.INSPECTION_NOT_ARRANGED)) {
						ExcelUtil.setCellValue(row.getCell(13), "Yet to Arrange");
					} else if (data.getInspectionStatus().equals(Constants.INSPECTION_DONE)) {
						ExcelUtil.setCellValue(row.getCell(13), "Inspection Done");
					}
				}
				ExcelUtil.setCellValue(row.getCell(14), "Not Arranged");
				if (!AppUtil.isObjectEmpty(data.getShippingStatus())) {
					if (data.getShippingStatus().equals(0)) {
						ExcelUtil.setCellValue(row.getCell(14), "Shipping Arranged");
					} else if (data.getShippingStatus().equals(1)) {
						ExcelUtil.setCellValue(row.getCell(14), "Shipping Confirmed");
					}
				}
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			workbook.write(response.getOutputStream());
		} catch (final Exception e) {
			throw new AAJRuntimeException("Exception while inspection requested report.", e);
		}
	}

}
