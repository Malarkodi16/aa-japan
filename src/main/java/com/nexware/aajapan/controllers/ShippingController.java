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
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.datatable.DataTableRequest;
import com.nexware.aajapan.datatable.DataTableResults;
import com.nexware.aajapan.dto.ShippingStockSearchDto;
import com.nexware.aajapan.dto.StockFilter;
import com.nexware.aajapan.dto.TShippingRequestedCreateDto;
import com.nexware.aajapan.dto.TShippingRequestedDto;
import com.nexware.aajapan.dto.TStockShippingListDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MShip;
import com.nexware.aajapan.models.MShippingCompany;
import com.nexware.aajapan.models.Schedule;
import com.nexware.aajapan.models.TNotification;
import com.nexware.aajapan.models.TShipmentSchedule;
import com.nexware.aajapan.models.TShippingInstruction;
import com.nexware.aajapan.models.TShippingRequest;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.LoginRepository;
import com.nexware.aajapan.repositories.MasterCountryPortRepository;
import com.nexware.aajapan.repositories.MasterShipRepository;
import com.nexware.aajapan.repositories.MasterShippingCompanyRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TShipmentScheduleRepository;
import com.nexware.aajapan.repositories.TShippingInstructionRepository;
import com.nexware.aajapan.repositories.TShippingRequestRepository;
import com.nexware.aajapan.repositories.UserRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TNotificationService;
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
@RequestMapping("shipping")
public class ShippingController {

	@Autowired
	LoginRepository loginRepo;

	@Autowired
	MasterShipRepository masterShipRepository;
	@Autowired
	TShipmentScheduleRepository shipmentScheduleRepository;
	@Autowired
	MasterCountryPortRepository masterCountryPortRepository;
	@Autowired
	MasterShippingCompanyRepository masterShippingCompanyRepository;
	@Autowired
	private TShippingInstructionRepository shippingInstructionRepository;
	@Autowired
	private TShippingRequestRepository shippingRequestRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private TNotificationService notificationService;

	@GetMapping(value = { "/dashboard" })
	public ModelAndView login() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.dashboard");
		return modelAndView;
	}

	@GetMapping("/stock-search/data")
	public ResponseEntity<DataTableResults<TStockShippingListDto>> getStockShippingData(HttpServletRequest request)
			throws ParseException {
		final StockFilter filter = new StockFilter(request);

		final DataTableRequest<TStockShippingListDto> dataTableInRQ = new DataTableRequest<>(request);

		final ShippingStockSearchDto result = stockRepository.getStockShippingList(dataTableInRQ, filter);
		final List<TStockShippingListDto> stocks = AppUtil.isObjectEmpty(result) ? new ArrayList<>()
				: result.getListOfDataObjects();
		final int total = AppUtil.isObjectEmpty(result) ? 0 : result.getRecordsTotal();
		final DataTableResults<TStockShippingListDto> dataTableResult = new DataTableResults<>();

		dataTableResult.setDraw(dataTableInRQ.getDraw());
		dataTableResult.setListOfDataObjects(stocks);
		dataTableResult.setRecordsTotal(total);
		if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
			dataTableResult.setRecordsFiltered(total);
		} else {
			dataTableResult.setRecordsFiltered(stocks.size());
		}
		return new ResponseEntity<>(dataTableResult, HttpStatus.OK);

	}

	@GetMapping(value = { "/shipmentschedule" })
	public ModelAndView view(Model model, ModelAndView modelAndView) {
		final TShipmentSchedule shipmentSchedule = new TShipmentSchedule();
		final List<Schedule> schedule = new ArrayList<>();
		schedule.add(new Schedule());
		shipmentSchedule.setSchedule(schedule);

		modelAndView.addObject("masterCountries", masterCountryPortRepository.findAll());
		modelAndView.addObject("masterShippingCompany",
				masterShippingCompanyRepository.findAllByDeleteFlag(Constants.DELETE_FLAG_0));
		modelAndView.addObject("masterShip", masterShipRepository.findAllByDeleteFlag(Constants.DELETE_FLAG_0));
		modelAndView.addObject("shipmentForm", shipmentSchedule);
		modelAndView.setViewName("shipping.shipmentschedule");
		return modelAndView;
	}

	@GetMapping(value = { "/shipmentschedule/edit/{scheduleId}" })
	public ModelAndView view(@PathVariable("scheduleId") String scheduleId, ModelAndView modelAndView) {
		final TShipmentSchedule shipmentSchedule = new TShipmentSchedule();
		final List<Schedule> schedule = new ArrayList<>();
		schedule.add(new Schedule());
		shipmentSchedule.setSchedule(schedule);
		modelAndView.addObject("masterCountries", masterCountryPortRepository.findAll());
		modelAndView.addObject("masterShippingCompany",
				masterShippingCompanyRepository.findAllByDeleteFlag(Constants.DELETE_FLAG_0));
		modelAndView.addObject("masterShip", masterShipRepository.findAllByDeleteFlag(Constants.DELETE_FLAG_0));
		modelAndView.addObject("shipmentForm", shipmentSchedule);
		modelAndView.addObject("scheduleId", scheduleId);
		modelAndView.setViewName("shipping.shipmentschedule");
		return modelAndView;
	}

	@PostMapping("/schedule/save")
	public ModelAndView create(@ModelAttribute("shipmentForm") TShipmentSchedule shipmentschedule,
			HttpServletRequest request, ModelAndView modelAndView, RedirectAttributes redirectAttributes,
			@RequestParam String action) throws ParseException {
		// final String[] countryArr = request.getParameterValues("schedule.country");
		final String[] portNameArr = request.getParameterValues("schedule.portName");
		final String[] subVesselArr = request.getParameterValues("schedule.subVessel");
		final String[] dateArr = request.getParameterValues("schedule.date");
		final String[] portFlagArr = request.getParameterValues("schedule.portFlag");
		final String[] soCutDateArr = request.getParameterValues("schedule.soCutDate");
		List<Schedule> schedules = new ArrayList<>();
		for (int i = 0; i < portNameArr.length; i++) {
			final String dateString = dateArr[i];
			// final String country = countryArr[i];
			final String portName = portNameArr[i];
			final String subVessel = subVesselArr[i];
			final String portFlag = portFlagArr[i];
			final String soCutDateStr = soCutDateArr.length > i ? soCutDateArr[i] : "";
			Date date = null;
			Date soCutDate = null;
			if (!AppUtil.isObjectEmpty(dateString)) {
				date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
			}
			if (!AppUtil.isObjectEmpty(soCutDateStr)) {
				soCutDate = new SimpleDateFormat("dd-MM-yyyy").parse(soCutDateStr);
			}
			final Schedule schedule = new Schedule(date, portName, subVessel, portFlag);
			schedule.setRoute(i);
			schedule.setSoCutDate(soCutDate);
			schedules.add(schedule);
		}
		shipmentschedule.setSchedule(schedules);
		TShipmentSchedule tShipmentschedule = null;
		if (AppUtil.isObjectEmpty(shipmentschedule.getScheduleId())) {
			tShipmentschedule = shipmentschedule;
			tShipmentschedule.setScheduleId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SCHDL));
			tShipmentschedule.setDeleteFlag(Constants.DELETE_FLAG_0);
		} else {
			tShipmentschedule = shipmentScheduleRepository.findOneByScheduleId(shipmentschedule.getScheduleId());
			tShipmentschedule.setVoyageNo(shipmentschedule.getVoyageNo());
			tShipmentschedule.setShippingCompanyNo(shipmentschedule.getShippingCompanyNo());
			tShipmentschedule.setContinents(shipmentschedule.getContinents());
			tShipmentschedule.setShipId(shipmentschedule.getShipId());
			tShipmentschedule.setDeckHeight(shipmentschedule.getDeckHeight());
		}

		schedules = schedules.stream().filter(s -> !AppUtil.isObjectEmpty(s.getPortName()))
				.collect(Collectors.toList());

		tShipmentschedule.setSchedule(schedules);
		shipmentScheduleRepository.save(tShipmentschedule);
		if (!action.equalsIgnoreCase("save")) {
			redirectAttributes.addFlashAttribute("message", "ShipmentSchedule created successfully.");
			modelAndView.setViewName("redirect:/shipping/shipmentschedule");
		} else {
			modelAndView.setViewName("redirect:/shipping/schedule/list");
		}

		return modelAndView;
	}

	@GetMapping("/schedule/list")
	public ModelAndView shipmentScheduleList(ModelAndView modelAndView) {
		modelAndView.addObject("masterCountries", masterCountryPortRepository.findAll());
		modelAndView.setViewName("shipping.viewshipment");
		return modelAndView;
	}

	@GetMapping("/schedule/info/{scheduleId}")
	public ResponseEntity<Response> scheduleInfo(@PathVariable("scheduleId") String scheduleId) {
		return new ResponseEntity<>(new Response("success", shipmentScheduleRepository.findOneByScheduleId(scheduleId)),
				HttpStatus.OK);
	}

	@PostMapping("/schedule/delete")
	public ResponseEntity<Response> deleteSchedule(@RequestParam("scheduleId") String scheduleId) {
		final TShipmentSchedule schedule = shipmentScheduleRepository.findOneByScheduleId(scheduleId);
		schedule.setDeleteFlag(Constants.DELETE_FLAG_1);
		shipmentScheduleRepository.save(schedule);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/schedule/check/isVoyagenoExists")
	public ResponseEntity<Boolean> isVoyagenoExists(@RequestParam("scheduleId") String scheduleId,
			@RequestParam("shippingCompanyNo") String shippingCompanyNo, @RequestParam("voyageNo") String voyageNo,
			@RequestParam("shipId") String shipId) {
		boolean isExists = false;
		if (AppUtil.isObjectEmpty(scheduleId)) {
			isExists = shipmentScheduleRepository.isExistsByVoyageNo(shipId, voyageNo, shippingCompanyNo,
					Constants.DELETE_FLAG_0);
		} else {
			isExists = shipmentScheduleRepository.isExistsByScheduleIdAndVoyageno(scheduleId, shipId, voyageNo,
					Constants.DELETE_FLAG_0);
			if (!isExists) {
				isExists = shipmentScheduleRepository.isExistsByVoyageNo(shippingCompanyNo, shipId, voyageNo,
						Constants.DELETE_FLAG_0);
			} else {
				isExists = false;
			}

		}
		return new ResponseEntity<>(isExists, HttpStatus.OK);
	}

	// Added by Yogeshwar
	/*
	 * @GetMapping("/schedule/check/isFieldsExists") public ResponseEntity<Boolean>
	 * isfieldsExists(@RequestParam("shippingCompanyNo") String scheduleId,
	 * 
	 * @RequestParam("voyageNo") String voyageNo, @RequestParam("shipId") String
	 * shipId) { boolean isExists = false; if (AppUtil.isObjectEmpty(scheduleId)) {
	 * isExists = shipmentScheduleRepository.isExistsByVoyageNo(voyageNo, shipId,
	 * Constants.DELETE_FLAG_0); } else { isExists =
	 * shipmentScheduleRepository.isExistsByScheduleIdAndVoyageno(scheduleId,
	 * shipId, voyageNo, Constants.DELETE_FLAG_0); if (!isExists) { isExists =
	 * shipmentScheduleRepository.isExistsByVoyageNo(shipId, voyageNo,
	 * Constants.DELETE_FLAG_0); } else { isExists = false; }
	 * 
	 * } return new ResponseEntity<>(isExists, HttpStatus.OK);
	 * 
	 * }
	 */

	// Added by Yogeshwar

	@GetMapping("/schedule/list/data")
	@ResponseBody
	public DatatableResponse getShipmentlistData(

			@RequestParam(value = "orginPort", required = false) String orginPort,

			@RequestParam(value = "destPortSelect", required = false) String destPort) {

		return new DatatableResponse(shipmentScheduleRepository.findAllShippingScheduleDetails(orginPort, destPort));
	}

	@GetMapping("/arrangement")
	public ModelAndView shipmentArrangement(@RequestParam("flag") Optional<String> flag,
			@RequestParam("id") Optional<String> id, ModelAndView modelAndView) {
		modelAndView.addObject("flag", flag.isPresent() ? flag.get() : "");
		modelAndView.addObject("shippingRequestId", id.isPresent() ? id.get() : "");
		modelAndView.setViewName("shipping.arrangement");
		return modelAndView;
	}

	@GetMapping("/status")
	public ModelAndView shipmentStatus(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.status");
		modelAndView.addObject("screenNameFlag", "shipping");
		return modelAndView;
	}

	@GetMapping("/instruction/from-sales/datasource")
	@ResponseBody
	public DatatableResponse shipmentInstructionFrom(ModelAndView modelAndView, @RequestParam("draw") Boolean draw) {
		if (!draw) {
			return new DatatableResponse(new ArrayList<>());
		}
		return new DatatableResponse(shippingInstructionRepository.findAllShippingInstructionFromSales());
	}

	@GetMapping("/vessals.json")
	public ResponseEntity<Response> findVessals(

			@RequestParam(name = "destPort", required = true) String destPort) {

		return new ResponseEntity<>(new Response("success", shipmentScheduleRepository.findAllVessal(destPort)),
				HttpStatus.OK);

	}

	@GetMapping("/vessalsAndVoyageNo.json")
	public ResponseEntity<Response> findVessalsAndVoyageNo() {
		return new ResponseEntity<>(new Response("success", shipmentScheduleRepository.findVessalsAndVoyageNo()),
				HttpStatus.OK);

	}

	@GetMapping("/vessalsAndFwdr.json")
	public ResponseEntity<Response> findVessalsAndFwdr(
			@RequestParam(name = "originPort", required = false) String originPort,
			@RequestParam(name = "destPort", required = true) String destPort) {
		final Map<String, Object> response = new HashMap<>();
		response.put("vessals", shipmentScheduleRepository.findAllVessalByOrigin(originPort, destPort));

		return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);

	}

	@GetMapping("/vessalsByScheduleIds.json")
	public ResponseEntity<Response> vessalsByScheduleIds(
			@RequestParam(value = "shipmentRequestIds[]") String[] shipmentRequestIds) {
		final Map<String, Object> response = new HashMap<>();
		response.put("vessals", shipmentScheduleRepository.findAllScheduleById(shipmentRequestIds));
		return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);

	}

	@GetMapping("/vessalsDetails")
	public ResponseEntity<Response> vessalsDetails(
			@RequestParam(value = "shipmentRequestId") String shipmentRequestId) {
		return new ResponseEntity<>(
				new Response("success",
						shipmentScheduleRepository.findOneVesselDetailsByShippmentRequestId(shipmentRequestId)),
				HttpStatus.OK);

	}

	@PostMapping("/request/roro/save") // shipping/request/save
	@Transactional
	public ResponseEntity<Response> shippingRequestSave(@RequestBody TShippingRequestedCreateDto shippingRequests) {
		final String allocationId = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_ALLCTN);
		shippingRequests.getStockShippingInstructionArray().forEach(shippingRequest -> {

			// find shipping instruction and update status
			final TShippingInstruction instruction = shippingInstructionRepository
					.findOneById(shippingRequest.getShippingInstructionId());
			instruction.setStatus(Constants.SHIPPING_INSTRUCTION_INITIATED);
			shippingInstructionRepository.save(instruction);

			// Shipping Request Save
			final TShippingRequest request = new TShippingRequest();

			request.setStockNo(shippingRequest.getStockNo());
			request.setShippingInstructionId(instruction.getShippingInstructionId());
			request.setForwarderId(shippingRequests.getForwarderId());
			request.setOrginCountry(shippingRequests.getOrginCountry());
			request.setOrginPort(shippingRequests.getOrginPort());
			request.setDestCountry(shippingRequests.getDestCountry());
			request.setDestPort(shippingRequests.getDestPort());
			request.setYardId(shippingRequests.getYardId());
			request.setYard(shippingRequests.getYard());
			request.setScheduleId(shippingRequests.getScheduleId());
			request.setShippingType(shippingRequests.getShippingType());
			request.setAllocationId(allocationId);
			request.setShipmentRequestId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SR));
			request.setStatus(Constants.SHIPIING_REQUEST_INITIATED);
			request.setFreightChargeStatus(Constants.FREIGHT_SHIPPING_NO_CHARGES);
			request.setShippingChargeStatus(Constants.FREIGHT_SHIPPING_NO_CHARGES);
			request.setInspectionChargeStatus(Constants.FREIGHT_SHIPPING_NO_CHARGES);
			request.setRadiationChargeStatus(Constants.FREIGHT_SHIPPING_NO_CHARGES);
			request.setFreightCompletionStatus(Constants.FREIGHT_SHIPPING_CHARGE_NOT_COMPLETED);
			request.setBlDraftStatus(Constants.BL_DRAFT_STATUS_NOT_RECEIVED);
			request.setBlOriginalStatus(Constants.BL_ORIGINAL_STATUS_NOT_RECEIVED);
			request.setInvoiceStatus(Constants.SHIPIING_REQUEST_INVOICE_NOT_CREATED);
			request.setRecSurStatus(Constants.SHIPIING_REQUEST_REC_SUR_STATUS_IDLE);
			request.setBlDocumentStatus(Constants.SHIPIING_REQUEST_BL_DOC_STATUS_IDLE);
			shippingRequestRepository.save(request);

			// find stock and update status
			final TStock stock = stockRepository.findOneByStockNo(request.getStockNo());
			stock.setShipmentRequestId(request.getShipmentRequestId());
			stock.setShippingStatus(Constants.STOCK_SHIPPING_STATUS_SHIPPINGARRANGED);
			stockRepository.save(stock);

			final List<TNotification> notifications = notificationService.acceptShippingRequest(shippingRequests);
			notificationService.notify(notifications, "sales@aaj.com");

		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/container/allocation/save")
	@Transactional
	public ResponseEntity<Response> containerAllocationSave(@RequestBody List<TShippingRequest> shippingRequest)
			throws IOException {
		final String allocationId = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_ALLCTN);
		shippingRequest.stream().forEach(request -> {
			final TShippingInstruction shippingInstruction = shippingInstructionRepository
					.findOneById(request.getShippingInstructionId());
			request.setShipmentRequestId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SR));
			request.setStatus(Constants.SHIPIING_REQUEST_INITIATED);
			request.setShippingStatus(Constants.SHIPIING_REQUEST_SHIPPING_NOT_CONFIRMED);
			request.setAllocationId(allocationId);
			request.setShippingInstructionId(shippingInstruction.getShippingInstructionId());
			request.setInvoiceStatus(Constants.SHIPIING_REQUEST_INVOICE_NOT_CREATED);
			request.setShippingInstructionId(shippingInstruction.getShippingInstructionId());

			request.setRecSurStatus(Constants.SHIPIING_REQUEST_REC_SUR_STATUS_IDLE);
			request.setBlDocumentStatus(Constants.SHIPIING_REQUEST_BL_DOC_STATUS_IDLE);

			// update status
			shippingInstruction.setStatus(Constants.SHIPPING_INSTRUCTION_INITIATED);
			shippingInstructionRepository.save(shippingInstruction);
		});
		shippingRequestRepository.insert(shippingRequest);
		// update shipping request id in stock table
		final BulkOperations ops = mongoTemplate.bulkOps(BulkMode.UNORDERED, TStock.class);
		shippingRequest.forEach(request -> {
			final Update update = new Update();
			update.set("shipmentRequestId", request.getShipmentRequestId());
			update.set("shippingStatus", Constants.STOCK_SHIPPING_STATUS_SHIPPINGARRANGED);
			ops.updateOne(Query.query(Criteria.where("stockNo").is(request.getStockNo())), update);
		});
		ops.execute();

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/container/allocation/edit")
	@Transactional
	public ResponseEntity<Response> editContainerAllocationSave(@RequestBody List<TShippingRequest> shippingRequestItem)
			throws IOException {

		shippingRequestItem.forEach(request -> {
			final TShippingRequest shippingRequest = shippingRequestRepository
					.findOneByShipmentRequestId(request.getShipmentRequestId());
			if (request.getStatus() == 0) {
				shippingRequest.setOrginCountry(request.getOrginCountry());
				shippingRequest.setOrginPort(request.getOrginPort());
				shippingRequest.setDestCountry(request.getDestCountry());
				shippingRequest.setDestPort(request.getDestPort());
				shippingRequest.setScheduleId(request.getScheduleId());
				shippingRequest.setForwarderId(request.getForwarderId());
				shippingRequest.setContainerNo(request.getContainerNo());
			} else {
				shippingRequest.setStatus(Constants.SHIPIING_REQUEST_CANCELLED);
				TStock stock = stockRepository.findOneByStockNo(shippingRequest.getStockNo());
				stock.setShipmentRequestId(null);
				stock.setShippingInstructionInfo(null);
				stock.setShippingInstructionStatus(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_IDLE);
				stock.setShippingStatus(Constants.STOCK_SHIPPING_STATUS_IDLE);
				stockRepository.save(stock);
				TShippingInstruction shippingInstruction = shippingInstructionRepository
						.findOneByShippingInstructionId(shippingRequest.getShippingInstructionId());
				shippingInstruction.setStatus(Constants.SHIPPING_INSTRUCTION_CANCELLED);
				shippingInstructionRepository.save(shippingInstruction);

			}
			shippingRequestRepository.save(shippingRequest);
		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/container/allocation/confirmVessel")
	@Transactional
	public ResponseEntity<Response> confirmVessel(@RequestParam("type") String type,
			@RequestBody Map<String, String> requestBody) throws IOException {
		final String allocationId = requestBody.get("allocationId");
		final String containerNo = requestBody.get("containerNo");
		final String scheduleId = requestBody.get("scheduleId");
		List<TShippingRequest> shippingRequests;
		if (type.equalsIgnoreCase("allocation") && !AppUtil.isObjectEmpty(allocationId)) {
			shippingRequests = shippingRequestRepository.findAllByAllocationIdAndStatus(allocationId,
					Constants.SHIPIING_REQUEST_CONTAINER_CONFIRMED);
		} else if (type.equalsIgnoreCase("container") && !AppUtil.isObjectEmpty(allocationId)
				&& !AppUtil.isObjectEmpty(containerNo)) {
			shippingRequests = shippingRequestRepository.findAllByAllocationIdAndContainerNoAndStatus(allocationId,
					containerNo, Constants.SHIPIING_REQUEST_CONTAINER_CONFIRMED);
		} else {
			return new ResponseEntity<>(new Response("failure"), HttpStatus.OK);
		}
		shippingRequests.forEach(request -> {
			request.setStatus(Constants.SHIPIING_REQUEST_VESSEL_CONFIRMED);
			request.setShippingStatus(Constants.SHIPIING_REQUEST_SHIPPING_INITIATED);
			request.setScheduleId(scheduleId);
		});
		shippingRequestRepository.saveAll(shippingRequests);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/container/allocation/replace/stock")
	@Transactional
	public ResponseEntity<Response> replaceStockAllcation(@RequestParam("shipmentRequestId") String shipmentRequestId,
			@RequestParam("stockNo") String stockNo) {
		final TShippingRequest shippingRequest = shippingRequestRepository
				.findOneByShipmentRequestId(shipmentRequestId);
		TStock oldstock = stockRepository.findOneByStockNo(shippingRequest.getStockNo());
		TShippingInstruction shippingInstruction = shippingInstructionRepository
				.findOneByShippingInstructionId(shippingRequest.getShippingInstructionId());
		// update stock
		oldstock.setShipmentRequestId(null);
		oldstock.setShippingInstructionInfo(null);
		oldstock.setShippingInstructionStatus(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_IDLE);
		oldstock.setShippingStatus(Constants.STOCK_SHIPPING_STATUS_IDLE);
		stockRepository.save(oldstock);
		// update shipping instruction
		shippingInstruction.setStatus(Constants.SHIPPING_INSTRUCTION_CANCELLED);
		shippingInstructionRepository.save(shippingInstruction);
//		update shipping request
		shippingRequest.setStockNo(stockNo);
		shippingRequestRepository.save(shippingRequest);
		// update new stock
		TStock newstock = stockRepository.findOneByStockNo(stockNo);
		newstock.setShippingStatus(Constants.STOCK_SHIPPING_STATUS_SHIPPINGARRANGED);
		newstock.setShipmentRequestId(shippingRequest.getShipmentRequestId());
		stockRepository.save(newstock);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/container/allocation/confirm")
	@Transactional
	public ResponseEntity<Response> confirmContainer(@RequestParam("allocationId") String allocationId,
			@RequestParam("containerNo") String containerNo, @RequestBody Map<String, String> requestBody) {
		final String containerName = requestBody.get("containerName");
		final String slaNo = requestBody.get("slaNo");
		final List<TShippingRequest> shippingRequests = shippingRequestRepository
				.findAllByAllocationIdAndContainerNoAndStatus(allocationId, containerNo,
						Constants.SHIPIING_REQUEST_INITIATED);
		shippingRequests.forEach(request -> {
			request.setStatus(Constants.SHIPIING_REQUEST_CONTAINER_CONFIRMED);
			request.setContainerName(containerName);
			request.setSlaNo(slaNo);
		});
		shippingRequestRepository.saveAll(shippingRequests);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/container/allocation/update/vessel/confirmed")
	@Transactional
	public ResponseEntity<Response> confirmContainer(@RequestParam("allocationId") String allocationId,
			@RequestBody Map<String, String> requestBody) {
		final String blNo = requestBody.get("blNo");
		final String statusString = requestBody.get("status");
		Integer status = Integer.valueOf(statusString);
		final List<TShippingRequest> shippingRequests = shippingRequestRepository
				.findAllByAllocationIdAndStatus(allocationId, status);
		shippingRequests.forEach(request -> request.setBlNo(blNo));
		shippingRequestRepository.saveAll(shippingRequests);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/container/allocation/update/shipping/status")
	@Transactional
	public ResponseEntity<Response> updateShippingStatus(@RequestParam("allocationId") String allocationId,
			@RequestParam("action") int action) {
		Integer status = -1;
		if (action == 2) {
			status = Constants.SHIPIING_REQUEST_SHIPPING_INTRANSIT;
		} else if (action == 3) {
			status = Constants.SHIPIING_REQUEST_SHIPPING_COMPLETED;
		}
		if (status != -1) {
			final List<TShippingRequest> shippingRequests = shippingRequestRepository
					.findAllByAllocationIdAndStatus(allocationId, Constants.SHIPIING_REQUEST_VESSEL_CONFIRMED);
			for (final TShippingRequest tShippingRequest : shippingRequests) {
				tShippingRequest.setShippingStatus(status);
			}
			shippingRequestRepository.saveAll(shippingRequests);
		}

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping(value = { "/last-lap-vehicles" })
	public ModelAndView lastLapVehicles() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("last-lap-vehicles");
		return modelAndView;
	}

	@GetMapping("/lastLap-vehicles/list/datasource")
	@ResponseBody
	public DatatableResponse getLastLapVehicleslistData() {
		return new DatatableResponse(stockRepository.getAllLastLapVehiclesList());
	}

	@GetMapping("/requested/datasource")
	@ResponseBody
	public DatatableResponse shippingRequestedDatasource(@RequestParam("show") int show) {

		if (show == 0) {
			return new DatatableResponse(shippingRequestRepository
					.findAllShippingArrangedRequestedStock(Constants.SHIPIING_REQUEST_INITIATED));
		} else if (show == 1) {
			return new DatatableResponse(
					shippingRequestRepository.findAllShippingRequestedStock(Constants.SHIPIING_REQUEST_ACCEPTED));
		} else if (show == 2) {
			return new DatatableResponse(shippingRequestRepository
					.findAllShippingRequestedStock(Constants.SHIPIING_REQUEST_RORO_UPDATED_BL_AND_SHIPPED));
		} else if (show == 3) {
			return new DatatableResponse(
					shippingRequestRepository.findAllShippingRequestedStock(Constants.SHIPIING_REQUEST_CANCELLED));
		}
		return new DatatableResponse(new ArrayList<>());

	}

	@PostMapping("/request/cancel/arranged")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> cancelShippingRequestArranged(
			@RequestParam("shipmentRequestId") String shipmentRequestId,
			@RequestParam("allocationId") String allocationId, @RequestParam("remarks") String remarks) {

		// set shipping request status to cancelled
		final TShippingRequest request = shippingRequestRepository.findOneByShipmentRequestId(shipmentRequestId);
		request.setStatus(Constants.SHIPIING_REQUEST_CANCELLED);
		request.setRemarks(remarks);
		shippingRequestRepository.save(request);

		// set stock shipping req and instruction to idle
		final TStock stock = stockRepository.findOneByStockNo(request.getStockNo());
		stock.setShippingStatus(Constants.STOCK_SHIPPING_STATUS_IN_REQUESTED);
		stock.setShippingInstructionStatus(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_GIVEN);
		stockRepository.save(stock);

		// set shipping instruction to cancelled
		final TShippingInstruction instruction = shippingInstructionRepository
				.findOneByShippingInstructionId(request.getShippingInstructionId());
		instruction.setStatus(Constants.SHIPPING_INSTRUCTION_GIVEN);
		shippingInstructionRepository.save(instruction);

		return new ResponseEntity<>(new Response("success", shippingRequestRepository
				.findShippingRequestedStockByScheduleId(allocationId, Constants.SHIPIING_REQUEST_INITIATED)),
				HttpStatus.OK);
	}

	@PostMapping("/container/request/cancel/arranged")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> cancelContainerShippingRequestArranged(
			@RequestParam("shipmentRequestId") String shipmentRequestId,
			@RequestParam("allocationId") String allocationId, @RequestParam("remarks") String remarks) {

		// set shipping request status to cancelled
		final TShippingRequest request = shippingRequestRepository.findOneByShipmentRequestId(shipmentRequestId);
		request.setStatus(Constants.SHIPIING_REQUEST_CANCELLED);
		request.setRemarks(remarks);
		shippingRequestRepository.save(request);

		// set stock shipping req and instruction to idle
		final TStock stock = stockRepository.findOneByStockNo(request.getStockNo());
		stock.setShippingStatus(Constants.STOCK_SHIPPING_STATUS_IN_REQUESTED);
		stock.setShippingInstructionStatus(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_GIVEN);
		stockRepository.save(stock);

		// set shipping instruction to cancelled
		final TShippingInstruction instruction = shippingInstructionRepository
				.findOneByShippingInstructionId(request.getShippingInstructionId());
		instruction.setStatus(Constants.SHIPPING_INSTRUCTION_GIVEN);
		shippingInstructionRepository.save(instruction);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/request/cancel/confirmed")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> cancelShippingRequestConfirmed(
			@RequestParam("shipmentRequestId") String shipmentRequestId,
			@RequestParam("allocationId") String allocationId, @RequestParam("remarks") String remarks) {

		// set shipping request status to cancelled
		final TShippingRequest cancellingRequest = shippingRequestRepository
				.findOneByShipmentRequestId(shipmentRequestId);
		cancellingRequest.setStatus(Constants.SHIPIING_REQUEST_INITIATED);
		cancellingRequest.setRemarks(remarks);
		shippingRequestRepository.save(cancellingRequest);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/container/request/cancel/confirmed")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> cancelContainerShippingRequestConfirmed(
			@RequestParam("shipmentRequestId") String shipmentRequestId,
			@RequestParam("allocationId") String allocationId, @RequestParam("remarks") String remarks) {

		// set shipping request status to cancelled
		final TShippingRequest cancellingRequest = shippingRequestRepository
				.findOneByShipmentRequestId(shipmentRequestId);
		cancellingRequest.setStatus(Constants.SHIPIING_REQUEST_INITIATED);
		cancellingRequest.setRemarks(remarks);
		shippingRequestRepository.save(cancellingRequest);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/request/edit")
	public ResponseEntity<Response> shippingRequestEdit(@RequestBody Map<String, Object> data,
			@RequestParam(value = "allocationId", required = false) String allocationId,
			@RequestParam("show") int show) {
		final String scheduleId = (String) data.get("scheduleId");
		final String forwarderId = (String) data.get("forwarderId");
		final String orginPort = (String) data.get("orginPort");
		final List<String> shipmentRequestIds = (List<String>) data.get("shipmentRequestId");

		shipmentRequestIds.forEach(shipmentRequest -> {
			TShippingRequest shippingRequest = null;
			if (show == 0) {
				shippingRequest = shippingRequestRepository.findOneByShipmentRequestIdAndStatus(shipmentRequest,
						Constants.SHIPIING_REQUEST_INITIATED);
			}
			if (show == 1) {
				shippingRequest = shippingRequestRepository.findOneByShipmentRequestIdAndStatus(shipmentRequest,
						Constants.SHIPIING_REQUEST_ACCEPTED);
			}
			shippingRequest.setOrginPort(orginPort);
			shippingRequest.setScheduleId(scheduleId);
			shippingRequest.setForwarderId(forwarderId);
			shippingRequestRepository.save(shippingRequest);
		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/request/accept")
	public ResponseEntity<Response> inquiryeditsave(@RequestBody Map<String, String> data,
			@RequestParam("shipmentRequestId") List<String> shipmentRequestId,
			@RequestParam("allocationId") String allocationId) {
		final String scheduleId = data.get("scheduleId");
		final String forwarderId = data.get("forwarderId");
		final List<TShippingRequest> shippingRequests = shippingRequestRepository
				.findAllByShipmentRequestIdIn(shipmentRequestId);
		shippingRequests.forEach(shippingRequest -> {
			shippingRequest.setStatus(Constants.SHIPIING_REQUEST_ACCEPTED);
			shippingRequest.setScheduleId(scheduleId);
			shippingRequest.setForwarderId(forwarderId);
		});
		shippingRequestRepository.saveAll(shippingRequests);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping("/requested/status/datasource")
	@ResponseBody
	public DatatableResponse shippingRequestedStatusDatasource(@RequestParam("forwarderFilter") String forwarderFilter,
			@RequestParam("countryFilter") String countryFilter, @RequestParam("portFilter") String portFilter,
			@RequestParam("shipmentTypeFilter") String shipmentTypeFilter) {
		if (forwarderFilter.isEmpty()) {
			return new DatatableResponse(shippingRequestRepository
					.findAllShippingRequestedStockByStatus(Constants.SHIPIING_REQUEST_ACCEPTED));
		}
		final Optional<List<TShippingRequestedDto>> list = shippingRequestRepository
				.findAllShippingRequestedStatusStock(forwarderFilter, countryFilter, portFilter, shipmentTypeFilter);
		return new DatatableResponse(list.isPresent() ? list : new ArrayList<>());
	}

	@GetMapping("/data/shipsWithCompanyName")
	@ResponseBody
	public DatatableResponse shippingRequestedCompany() {
		return new DatatableResponse(masterShipRepository.getShipsWithCompanyName());
	}

	@GetMapping("/order/request")
	public void pdfDownload(@RequestParam("format") String format, @RequestParam("shipId") String shipId,
			HttpServletResponse response) throws JRException, IOException {
		final TShippingRequestedDto shippingRequestedDto = shippingRequestRepository.findByShipId(shipId);
		final JRBeanCollectionDataSource datasourceJRBean = new JRBeanCollectionDataSource(
				shippingRequestedDto.getItems());
		final InputStream jasperStream = this.getClass()
				.getResourceAsStream("/templates/jasper/ShippingOrderRequest.jasper");

		final Map<String, Object> params = new HashMap<>();
		params.put("itemDatasource", datasourceJRBean);
		params.put("orderDetails", shippingRequestedDto);
		try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/jasper/images/logo.png")) {
			params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
		} catch (JRException | IOException e) {
			throw new AAJRuntimeException("Failed to load images", e);
		}
		final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

		final String filename = "Shipping Request";

		final OutputStream outStream = response.getOutputStream();
		if (format.equalsIgnoreCase("pdf")) {
			response.setContentType("application/x-pdf");
			response.setHeader("Content-disposition", "inline; filename=" + filename + ".pdf");
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		}
	}

	@PostMapping("/requestFromSales/remarks")
	public ResponseEntity<Response> recycleReceived(@RequestParam("id") String id,
			@RequestBody Map<String, Object> data) {
		final String remarks = data.get("remarks").toString();
		final Update update = new Update();
		update.set("remarks", remarks);
		shippingInstructionRepository.updateById(id, update);
		return new ResponseEntity<>(
				new Response("success", shippingInstructionRepository.findOneShippingInstructionFromSales(id)),
				HttpStatus.OK);
	}

	@PostMapping("/requestFromSales/cancelStock")
	@Transactional
	public ResponseEntity<Response> cancelStock(@RequestParam("id") String id, @RequestBody Map<String, Object> data) {
		final String remarks = data.get("remarks").toString();
		final String stockNo = data.get("stockNo").toString();
		TShippingInstruction instruction = shippingInstructionRepository.findOneById(id);
		instruction.setRemarks(remarks);
		instruction.setDeleteStatus(Constants.DELETE_FLAG_1);
		shippingInstructionRepository.save(instruction);

		final TStock stock = stockRepository.findOneByStockNo(stockNo);
		stock.setShippingStatus(Constants.STOCK_SHIPPING_STATUS_IDLE);
		stock.setShippingInstructionStatus(Constants.STOCK_SHIPPING_INSTRUCTION_STATUS_IDLE);
		stockRepository.save(stock);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/shipment-container")
	@ResponseBody
	public DatatableResponse shippingContainerDatasource(@RequestParam("draw") Boolean draw,
			@RequestParam("show") int show) {
		if (!draw) {
			return new DatatableResponse(new ArrayList<>());
		}
		if (show == 0) {
			return new DatatableResponse(
					shippingRequestRepository.findAllShippingContainerStock(Constants.SHIPIING_REQUEST_INITIATED));
		} else if (show == 1) {
			return new DatatableResponse(shippingRequestRepository
					.findAllShippingContainerStock(Constants.SHIPIING_REQUEST_CONTAINER_CONFIRMED));
		} else if (show == 2) {
			return new DatatableResponse(shippingRequestRepository
					.findAllShippingContainerStock(Constants.SHIPIING_REQUEST_VESSEL_CONFIRMED));

		}
		return new DatatableResponse(new ArrayList<>());
	}

	@PostMapping("/container/allocation/update/roro/confirmed")
	@Transactional
	public ResponseEntity<Response> confirmRoro(@RequestParam("shipmentRequestId") String shipmentRequestId,
			@RequestBody Map<String, String> requestBody) {
		final String blNo = requestBody.get("blNo");
		final TShippingRequest shippingRequest = shippingRequestRepository
				.findOneByShipmentRequestId(shipmentRequestId);
		shippingRequest.setBlNo(blNo);
		// shippingRequest.setStatus(Constants.SHIPIING_REQUEST_RORO_UPDATED_BL_AND_SHIPPED);
		shippingRequestRepository.save(shippingRequest);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping(path = "/update/ShipName")
	public ResponseEntity<Response> createScheduleShipNameUpdate(@RequestBody Map<String, Object> data) {

		final String shipName = AppUtil.isObjectEmpty(data.get("shipName")) ? "" : data.get("shipName").toString();

		final MShip ship = new MShip(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_MSHIP), shipName);
		ship.setDeleteFlag(Constants.DELETE_FLAG_0);
		masterShipRepository.save(ship);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping(path = "/create/ShipComp")
	public ResponseEntity<Response> createScheduleShipComp(@RequestBody Map<String, Object> data) {

		final String shipComp = AppUtil.isObjectEmpty(data.get("name")) ? "" : data.get("name").toString();
		final String shipAddr = AppUtil.isObjectEmpty(data.get("shipCompAddr")) ? ""
				: data.get("shipCompAddr").toString();
		final String shipEmail = AppUtil.isObjectEmpty(data.get("shipCompMail")) ? ""
				: data.get("shipCompMail").toString();
		final String shipMobile = AppUtil.isObjectEmpty(data.get("mobileNo")) ? "" : data.get("mobileNo").toString();

		final MShippingCompany ship = new MShippingCompany(
				sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SHIPPING_COMP), shipComp, shipAddr, shipEmail,
				shipMobile);
		ship.setDeleteFlag(Constants.DELETE_FLAG_0);
		masterShippingCompanyRepository.save(ship);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping(value = { "custom/report" })
	public ModelAndView stockTracker(ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.custom.report");
		return modelAndView;
	}

	@GetMapping("/transport/approval")
	public ModelAndView approveTransport() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.transport.approval");
		return modelAndView;
	}

	@GetMapping("/stockInfo")
	public ModelAndView shippingStockInfo(@RequestParam(value = "stockNo", required = false) String stockNo,
			final HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("stockNo", stockNo);
		modelAndView.setViewName("shipping.stockinfo");
		return modelAndView;
	}
}
