package com.nexware.aajapan.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.FreightVesselDto;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.dto.ShippingInvoiceContainerDto;
import com.nexware.aajapan.dto.ShippingRequestedItemsDto;
import com.nexware.aajapan.models.TFreightShippingInvoice;
import com.nexware.aajapan.models.TShipmentSchedule;
import com.nexware.aajapan.models.TShippingInvoiceItem;
import com.nexware.aajapan.models.TShippingRequest;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MForwarderRepository;
import com.nexware.aajapan.repositories.MasterShipRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TFreightShippingInvoiceRepository;
import com.nexware.aajapan.repositories.TShipmentScheduleRepository;
import com.nexware.aajapan.repositories.TShippingInvoiceItemRepository;
import com.nexware.aajapan.repositories.TShippingRequestRepository;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TFreightInvoiceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/freight")

public class AFreightController {
	@Autowired
	MForwarderRepository forwarderRepository;
	@Autowired
	MasterShipRepository masterShipRepository;
	@Autowired
	TShipmentScheduleRepository addShipmentScheduleRepository;
	@Autowired
	TShippingRequestRepository tShippingRequestRepository;

	@Autowired
	private TFreightShippingInvoiceRepository tFreightShippingInvoiceRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private TFreightInvoiceService tFreightInvoiceService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private TShippingInvoiceItemRepository shippingInvoiceItemRepository;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private StockRepository stockRepository;

	@GetMapping("/shippingRoRo")
	public ModelAndView getShippingRoRo(ModelAndView modelAndView, HttpServletRequest request) {
		modelAndView.setViewName("accounts.freight-shipping");

		return modelAndView;
	}

	@GetMapping("/shippingContainer")
	public ModelAndView getShippingCotainer(ModelAndView modelAndView, HttpServletRequest request) {
		modelAndView.setViewName("accounts.payment.freight-shipping-container");
		return modelAndView;
	}

	@GetMapping("/find-port/{voyageNo}")
	public ResponseEntity<Response> findPortByCountry(@PathVariable String voyageNo) {
		final ModelAndView modelAndView = new ModelAndView();
		final TShipmentSchedule tShipmentScdl = addShipmentScheduleRepository.findByVoyageNo(voyageNo);
		modelAndView.addObject("shipSchedule", addShipmentScheduleRepository.findAll());
		return new ResponseEntity<>(new Response("success", tShipmentScdl), HttpStatus.OK);
	}

	@GetMapping("/find-freight")
	@ResponseBody
	public ResponseEntity<Response> freight(HttpServletRequest request) {
		final String forwarder = request.getParameter("forwarder");
		final String[] vessel = request.getParameterValues("vessel[]");
		final String[] voyageNo = request.getParameterValues("voyageNo[]");
		List<String> vessels = null;
		if (vessel != null) {
			vessels = Arrays.asList(vessel);
		}
		List<String> voyageNos = null;
		if (voyageNo != null) {
			voyageNos = Arrays.asList(voyageNo);
		}

		final List<FreightVesselDto> tShippingRequest = tShippingRequestRepository.findBySearch(forwarder, vessels,
				voyageNos);

		return new ResponseEntity<>(new Response("success", tShippingRequest), HttpStatus.OK);

	}

	@GetMapping("/shippingRequestRoRo")
	public ResponseEntity<Response> findAllContainerRequest(@RequestParam("forwarder") Optional<String> forwarder,
			@RequestParam(value = "scheduleIds[]", required = false) String[] scheduleIds) {
		if (forwarder.isPresent() || !AppUtil.isObjectEmpty(scheduleIds)) {
			final List<FreightVesselDto> tShippingRequest = tShippingRequestRepository
					.findAllContainerShippingRequestBySearch(forwarder, scheduleIds);
			return new ResponseEntity<>(new Response("success", tShippingRequest), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new Response("success", new ArrayList<>()), HttpStatus.OK);
		}

	}

	@GetMapping("/shippingRequestContainer")
	public ResponseEntity<Response> shippingRequestContainer(@RequestParam("forwarder") Optional<String> forwarder,
			@RequestParam("scheduleIds[]") Optional<List<String>> scheduleIds,
			@RequestParam("orginCountry[]") Optional<List<String>> orginCountry,
			@RequestParam("orginPort[]") Optional<List<String>> orginPort,
			@RequestParam("destinationCountry[]") Optional<List<String>> destinationCountry,
			@RequestParam("destinationPort[]") Optional<List<String>> destinationPort,
			@RequestParam("blNo[]") Optional<List<String>> blNo) {
		final List<Criteria> criterias = new ArrayList<>();
		criterias.add(Criteria.where("shippingType").is(Constants.STOCK_SHIPPING_TYPE_CONTAINER));
		criterias.add(Criteria.where("invoiceStatus").is(Constants.SHIPIING_REQUEST_INVOICE_NOT_CREATED));
		if (forwarder.isPresent() && !forwarder.get().isEmpty()) {
			criterias.add(Criteria.where("forwarderId").is(forwarder.get()));
		}
		if (scheduleIds.isPresent()) {
			criterias.add(Criteria.where("scheduleId").in(scheduleIds.get()));
		}
		if (orginCountry.isPresent()) {
			criterias.add(Criteria.where("orginCountry").in(orginCountry.get()));
		}
		if (orginPort.isPresent()) {
			criterias.add(Criteria.where("orginPort").in(orginPort.get()));
		}
		if (destinationCountry.isPresent()) {
			criterias.add(Criteria.where("destCountry").in(destinationCountry.get()));
		}
		if (destinationPort.isPresent()) {
			criterias.add(Criteria.where("destPort").in(destinationPort.get()));
		}
		if (blNo.isPresent()) {
			criterias.add(Criteria.where("blNo").in(blNo.get()));
		}
		if (criterias.size() >= 3) {
			final List<ShippingRequestedItemsDto> tShippingRequest = tShippingRequestRepository
					.findAllContainerShippingRequestedItems(criterias);
			return new ResponseEntity<>(new Response("success", tShippingRequest), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new Response("success", new ArrayList<>()), HttpStatus.OK);
		}

	}

	@PostMapping("/save")
	@Transactional
	public ResponseEntity<Response> saveFreight(@RequestBody List<TFreightShippingInvoice> data)
			throws IOException, ParseException {
		// Constants.FREIGHT_UPDATED
		final List<String> shipmentRequestIds = data.stream().map(TFreightShippingInvoice::getShipmentRequestId)
				.collect(Collectors.toList());

		tShippingRequestRepository.updateFreightStatus(Constants.FREIGHT_UPDATED, shipmentRequestIds);

		tFreightShippingInvoiceRepository.saveAll(data);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/roro/invoice/create")
	@Transactional
	public ResponseEntity<Response> saveContainer(@RequestBody List<TFreightShippingInvoice> data)
			throws IOException, ParseException {

		List<TFreightShippingInvoice> listData = new ArrayList<>();
		final String invoiceNo = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_FRTSHPNGINV);
		data.forEach(invoiceRow -> {
			invoiceRow.setInvoiceNo(invoiceNo);
			invoiceRow.setInvoiceUpload(Constants.INVOICE_NOT_UPLOADED);
			invoiceRow.setFreightPaymentStatus(Constants.SHIPPING_RORO_INVOICE_FREIGHT_PAYMENT_NOT_DONE);
			invoiceRow.setInvoiceType(Constants.SHIPPING_INVOICE_TYPE_RORO);
			final Double totalAmount = AppUtil.ifNull(invoiceRow.getFreightCharge(), 0.0)
					+ AppUtil.ifNull(invoiceRow.getInspectionCharge(), 0.0)
					+ AppUtil.ifNull(invoiceRow.getShippingCharge(), 0.0)
					+ AppUtil.ifNull(invoiceRow.getRadiationCharge(), 0.0)
					+ AppUtil.ifNull(invoiceRow.getOtherCharges(), 0.0);
			invoiceRow.setTotalAmount(totalAmount);

			listData.add(invoiceRow);

		});
		tFreightShippingInvoiceRepository.insert(listData);

		data.forEach(shippingData -> {
			final TShippingRequest tShippingRequest = tShippingRequestRepository
					.findOneByShipmentRequestId(shippingData.getShipmentRequestId());

			if (!AppUtil.isObjectEmpty(shippingData.getFreightCharge())) {
				tShippingRequest.setFreightChargeStatus(Constants.FREIGHT_SHIPPING_CHARGE);
				tShippingRequest.setFreightUsdChargeStatus(Constants.FREIGHT_SHIPPING_CHARGE);
			}
			if (!AppUtil.isObjectEmpty(shippingData.getShippingCharge())) {
				tShippingRequest.setShippingChargeStatus(Constants.FREIGHT_SHIPPING_CHARGE);
			}
			if (!AppUtil.isObjectEmpty(shippingData.getInspectionCharge())) {
				tShippingRequest.setInspectionChargeStatus(Constants.FREIGHT_SHIPPING_CHARGE);
			}
			if (!AppUtil.isObjectEmpty(shippingData.getRadiationCharge())) {
				tShippingRequest.setRadiationChargeStatus(Constants.FREIGHT_SHIPPING_CHARGE);
			}
			// Charges Completion Status
			if ((tShippingRequest.getFreightChargeStatus() == 1) && (tShippingRequest.getShippingChargeStatus() == 1)
					&& (tShippingRequest.getInspectionChargeStatus() == 1)
					&& (tShippingRequest.getRadiationChargeStatus() == 1)) {
				tShippingRequest.setFreightCompletionStatus(Constants.FREIGHT_SHIPPING_CHARGE);
				tShippingRequest.setInvoiceStatus(Constants.SHIPIING_REQUEST_INVOICE_CREATED);
			}			
			tShippingRequest.setLength(shippingData.getLength());
			tShippingRequest.setWidth(shippingData.getWidth());
			tShippingRequest.setHeight(shippingData.getHeight());
			tShippingRequest.calcM3();
			tShippingRequestRepository.save(tShippingRequest);
			
		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/shipping/all/invoice")
	@ResponseBody
	public DatatableResponse invoiceList() {
		return new DatatableResponse(tFreightShippingInvoiceRepository.findAllRoroInvoicesGroup());
	}

	@GetMapping("/shipping/container/invoice")
	@ResponseBody
	public DatatableResponse containerInvoiceList() {
		return new DatatableResponse(tFreightShippingInvoiceRepository.findAllShippingContainerInvoice());
	}

	@GetMapping("/shipping/container/invoice/items/{invoiceNo}")
	public ResponseEntity<List<Document>> getCOntainerInvoiceItems(@PathVariable String invoiceNo) {
		return new ResponseEntity<List<Document>>(tFreightShippingInvoiceRepository.findAllStockByinvoiceNo(invoiceNo),
				HttpStatus.OK);
	}

	@GetMapping("/shiiping/invoice/paymentCompleted")
	@ResponseBody
	public DatatableResponse paymentCompleted() {
		return new DatatableResponse(tFreightShippingInvoiceRepository.findAllRoroPaymentCompleted());
	}

	@PostMapping(value = "/freeze-freight-payment")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> freezeTransportPayment(@RequestBody Map<String, Object> data)
			throws ParseException {
		final String invoiceNo = data.get("invoiceNo").toString();

		final List<TFreightShippingInvoice> invoices = tFreightShippingInvoiceRepository.findAllByinvoiceNo(invoiceNo);

		for (final TFreightShippingInvoice freightInvoice : invoices) {
			freightInvoice.setPaymentApprove(Constants.PAYMENT_FREEZE);
		}
		// Save Freeze
		tFreightShippingInvoiceRepository.saveAll(invoices);
		TFreightShippingInvoice invoice = invoices.get(0);
		tFreightInvoiceService.freightInvoicePaymentCompleteTransactions(invoice);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@PostMapping(value = "/cancel-freight-payment")
	@Transactional
	public ResponseEntity<Response> cancelTransportPayment(@RequestBody Map<String, Object> data)
			throws ParseException {

		final String invoiceNo = data.get("invoiceNo").toString();
		final String cancelledRemarks = (String) data.get("cancelledRemarks");

		final List<TFreightShippingInvoice> invoices = tFreightShippingInvoiceRepository.findAllByinvoiceNo(invoiceNo);

		for (final TFreightShippingInvoice freightInvoice : invoices) {
			freightInvoice.setPaymentApprove(Constants.PAYMENT_CANCELED);
			freightInvoice.setCancelledRemarks(cancelledRemarks);
		}
		// Save Freeze
		tFreightShippingInvoiceRepository.saveAll(invoices);

		return new ResponseEntity<>(
				new Response("success", tFreightShippingInvoiceRepository.findOneRoroPaymentCompleted(invoiceNo)),
				HttpStatus.OK);
	}

	// Freight approve

	@PostMapping("/accounts/approve")
	public ResponseEntity<Response> approveStorageAndPhoto(@RequestParam("id[]") List<String> invoiceNos,
			@RequestBody Map<String, Object> data) throws ParseException {
		Date dueDate = null;
		final MLoginDto loggedInUser = securityService.findLoggedInUser();
		final String dueDateString = AppUtil.isObjectEmpty(data.get("dueDate")) ? "" : data.get("dueDate").toString();
		final String paymentType = AppUtil.isObjectEmpty(data.get("paymentType")) ? null
				: data.get("paymentType").toString();

		if (!AppUtil.isObjectEmpty(dueDateString)) {
			dueDate = new SimpleDateFormat("dd-MM-yyyy").parse(dueDateString);
		}

		final List<TFreightShippingInvoice> tFreightShippingInvoices = tFreightShippingInvoiceRepository
				.findAllByInvoiceNoInAndPaymentApprove(invoiceNos, Constants.PAYMENT_NOT_APPROVED);

		for (final TFreightShippingInvoice tFreightShippingInvoice : tFreightShippingInvoices) {
			tFreightShippingInvoice.setDueDate(dueDate);
			tFreightShippingInvoice.setPaymentApprove(Constants.PAYMENT_APPROVED);
			tFreightShippingInvoice.setApprovedDate(new Date());
			tFreightShippingInvoice.setApprovedBy(loggedInUser.getUserId());
			if (!AppUtil.isObjectEmpty(paymentType)) {
				Integer result = Integer.parseInt(paymentType);
				tFreightShippingInvoice.setPaymentType(result);
			}
		}

		// account transaction entry
		tFreightInvoiceService.freightInvoicePaymentApproveTransactions(tFreightShippingInvoices);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/shipping/containerInvoice/create")
	public ResponseEntity<Response> containerInvoiceCreate(@RequestBody ShippingInvoiceContainerDto data)
			throws ParseException {
		final TypeToken<List<TShippingInvoiceItem>> type = new TypeToken<List<TShippingInvoiceItem>>() {
		};
		final List<TShippingInvoiceItem> invoiceItem = modelMapper.map(data.getInvoiceItems(), type.getType());
		final List<Document> requestData = data.getShipmentRequest();
		// create invoice
		final String invoiceNo = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_FRTSHPNGINV);
		invoiceItem.forEach(item -> {

			item.setInvoiceNo(invoiceNo);
			if (!AppUtil.isObjectEmpty(item.getUsd()) && item.getUsd() > 0) {
				item.setCurrency(Constants.CURRENCY_USD);
			} else {
				item.setCurrency(Constants.CURRENCY_YEN);
			}
		});
		shippingInvoiceItemRepository.insert(invoiceItem);

		requestData.stream().forEach(request -> {
			final TShippingRequest shippingRequest = tShippingRequestRepository
					.findOneByShipmentRequestId(request.getString("shipmentRequestId"));
			shippingRequest.setInvoiceStatus(Constants.SHIPIING_REQUEST_INVOICE_CREATED);
			// create invoice
			final TFreightShippingInvoice invoice = new TFreightShippingInvoice(shippingRequest.getShipmentRequestId(),
					invoiceNo, data.getInvoiceDate(), data.getForwarder(), shippingRequest.getStockNo());
			invoice.setDueDate(data.getDueDate());
			invoice.setExchangeRate(data.getUsdExchangeRate());
			invoice.setZarExchangeRate(data.getZarExchangeRate());
			invoice.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
			final Double amount = Double.valueOf(request.get("amount").toString());
			invoice.setTotalAmount(amount);
			invoice.setInvoiceType(Constants.SHIPPING_INVOICE_TYPE_CONTAINER);
			tFreightShippingInvoiceRepository.insert(invoice);
			tShippingRequestRepository.save(shippingRequest);
		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
}
