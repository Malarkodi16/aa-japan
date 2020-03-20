package com.nexware.aajapan.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.AddTransportPaymentDto;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.dto.TTransportOrderInvoiceDto;
import com.nexware.aajapan.dto.TTransportOrderInvoiceItemDto;
import com.nexware.aajapan.dto.TTransporterChargeDto;
import com.nexware.aajapan.dto.TransportDeliveryConfirmDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MCountryPort;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.models.MTransporter;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TTransportInvoice;
import com.nexware.aajapan.models.TTransportOrderItem;
import com.nexware.aajapan.models.TTransporterFee;
import com.nexware.aajapan.models.TransportInfo;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.LocationRepository;
import com.nexware.aajapan.repositories.MasterCountryPortRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.repositories.TTransportInvoiceRepository;
import com.nexware.aajapan.repositories.TTransporterFeeRepository;
import com.nexware.aajapan.repositories.TransportOrderItemRepository;
import com.nexware.aajapan.repositories.TransportersRepository;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

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
@RequestMapping("transport")
public class TransportController {

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	LocationRepository locationRepository;

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TransportOrderItemRepository transportOrderItemRepository;
	@Autowired
	private TTransportInvoiceRepository tTransportInvoiceRepository;
	@Autowired
	private TTransporterFeeRepository ttransporterFeeRepository;
	@Autowired
	private TPurchaseInvoiceRepository tPurchaseInvoiceRepository;
	@Autowired
	private MasterCountryPortRepository countryPortRepository;
	@Autowired
	private TransportersRepository transportersRepository;

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public ModelAndView list(ModelAndView modelAndView) {

		modelAndView.setViewName("shipping.dashboard.transport");
		return modelAndView;
	}

	@GetMapping(path = "/list/datasource")
	@ResponseBody
	public DatatableResponse listDataSource() {
		return new DatatableResponse(transportOrderItemRepository.findAllTransportOrders());
	}

	@GetMapping(path = "/confirm/list/datasource")
	@ResponseBody
	public DatatableResponse confirmListDataSource() {
		return new DatatableResponse(transportOrderItemRepository.findAllConfirmTransportOrders());
	}

	@GetMapping(path = "/completed/list/datasource")
	@ResponseBody
	public DatatableResponse completedListDataSource(@RequestParam("show") int show) {
		if (show == 3) {
			return new DatatableResponse(transportOrderItemRepository.findAllCompletedTransportOrders());
		} else if (show == 4) {
			return new DatatableResponse(transportOrderItemRepository.findAllDeliveredTransportOrders());
		}
		return new DatatableResponse(new ArrayList<>());

	}

	@GetMapping(path = "/deliveryConfirmed/list/datasource")
	@ResponseBody
	public DatatableResponse deliveryConfirmed(@RequestParam("show") int show) {
		if (show == 5) {
			return new DatatableResponse(transportOrderItemRepository.findAllDeliveryConfirmedTransportOrders());
		}
		return new DatatableResponse(new ArrayList<>());
	}

	@GetMapping(path = "/rearrange/list/datasource")
	@ResponseBody
	public DatatableResponse rearrangeListDataSource() {
		return new DatatableResponse(stockRepository.findAllRearrangeItems());
	}

	@PostMapping(path = "/order/save")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> transportOrderSave(@RequestBody List<TTransportOrderItem> transportOrderDto) {
		final MLoginDto loginDto = securityService.findLoggedInUser();
		final BulkOperations ops = mongoTemplate.bulkOps(BulkMode.UNORDERED, TStock.class);
		// get order item in first index for getting common details
		final TTransportOrderItem orderItem = transportOrderDto.get(0);

		transportOrderDto.forEach(item -> {
			final TransportInfo transportInfo = new TransportInfo();
			transportInfo.setDeliveryDate(item.getDeliveryDate());
			transportInfo.setDeliveryTime(item.getDeliveryTime());
			transportInfo.setPickupDate(item.getPickupDate());
			transportInfo.setPickupTime(item.getPickupTime());
			transportInfo.setTransporter(item.getTransporter());
			transportInfo.setPickupLocation(item.getPickupLocation());
			transportInfo.setPickupLocationCustom(item.getPickupLocationCustom());
			transportInfo.setRemarks(item.getRemarks());
			transportInfo.setDropLocation(item.getDropLocation());
			transportInfo.setDropLocationCustom(item.getDropLocationCustom());
			transportInfo.setStatus(Constants.TRANSPORT_ITEM_INITIATED);
			final Update update = new Update();
			update.set("transportInfo", transportInfo);
			update.set("lastTransportLocation", item.getDropLocation());
			update.set("lastTransportLocationCustom", item.getDropLocationCustom());
			update.set("transportationStatus", Constants.TRANSPORT_INTRANSIT);
			update.set("destinationCountry", item.getDestinationCountry());
			update.set("destinationPort", item.getDestinationPort());
			update.set("posNo", item.getPosNo());
			update.set("lastModifiedDate", new Date());
			update.set("lastModifiedBy", loginDto.getUsername());
			update.set("forwarder", item.getForwarder());
			if (!AppUtil.isObjectEmpty(item.getDestinationCountry())) {
				MCountryPort countryPort = this.countryPortRepository.findOneByCountry(item.getDestinationCountry());
				// stock.setInspectionFlag(countryPort.getInspectionFlag());
				update.set("inspectionFlag", countryPort.getInspectionFlag());
			}

			// increment transport count
			ops.updateOne(Query.query(Criteria.where("stockNo").is(item.getStockNo())), update);
		});
		ops.execute();
		// transportationCount
		final List<String> stockNos = transportOrderDto.stream().map(TTransportOrderItem::getStockNo)
				.collect(Collectors.toList());

		if (!AppUtil.isObjectEmpty(orderItem.getSelectedDate())
				&& (orderItem.getSelectedDate() == Constants.TRANSPORT_PICKUP_AFTER_PAYMENT)) {
			final List<TPurchaseInvoice> purchaseInvoices = tPurchaseInvoiceRepository.findAllByStockNoIn(stockNos);
			purchaseInvoices.forEach(invoice -> invoice.setDueDate(orderItem.getDeliveryDate()));
			tPurchaseInvoiceRepository.saveAll(purchaseInvoices);
		}
		final String sequenceNo = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_TO);
		transportOrderDto.forEach(item -> {
			item.setInvoiceNo(sequenceNo);
			item.setScheduleType(item.getScheduleType());
			item.setStatus(Constants.TRANSPORT_ITEM_INITIATED);
			item.setId(null);
		});

		transportOrderItemRepository.saveAll(transportOrderDto);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/addpayment")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> transportAddPayment(@RequestBody AddTransportPaymentDto transportPaymentDto) {

		final TTransportInvoice transportOrderItem = new TTransportInvoice();
		transportOrderItem.setInvoiceNo("");
		transportOrderItem.setStockNo(transportPaymentDto.getStockNo());
		transportOrderItem.setTransporterId(transportPaymentDto.getTransporter());
		transportOrderItem.setPickupLocation(Constants.TYPE_OTHERS);
		transportOrderItem.setPickupLocationCustom("Other Charges");
		transportOrderItem.setDropLocation(Constants.TYPE_OTHERS);
		transportOrderItem.setDropLocationCustom(transportPaymentDto.getCategory());
		transportOrderItem.setCreatedDate(transportPaymentDto.getInvoiceDate());
		transportOrderItem.setAmount(transportPaymentDto.getAmount());
		transportOrderItem.setTax(transportPaymentDto.getTax());
		transportOrderItem.setTaxAmount(transportPaymentDto.getTaxAmount());
		transportOrderItem.setTotalTaxIncluded(transportPaymentDto.getTotalTaxIncluded());
		transportOrderItem.setStatus(Constants.TRANSPORT_INVOICE_NOT_BOOKED);
		transportOrderItem.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
		tTransportInvoiceRepository.save(transportOrderItem);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/order/item/cancel")
	@Transactional
	public ResponseEntity<Response> updateTransportStatus(@RequestParam("invoiceId") String invoiceId,
			@RequestBody Map<String, Object> data) {
		final String id = AppUtil.ifNull(data.get("id"), "").toString();
		final String stockNo = AppUtil.ifNull(data.get("stockNo"), "").toString();

		final String reason = AppUtil.ifNull(data.get("reason"), "").toString();
		final boolean rearrange = (boolean) data.get("rearrange");
		Integer transportStatus = 0;
		if (rearrange) {
			transportStatus = Constants.TRANSPORT_ITEM_REARRANGE;

		} else {
			transportStatus = Constants.TRANSPORT_ITEM_NEW;
		}
		final Map<String, Object> response = new HashMap<>();
		transportOrderItemRepository.cancelItemById(id, reason);
		stockRepository.updateTransportCancelInfo(reason, transportStatus, stockNo);
		response.put("updatedData", transportOrderItemRepository.findOneTransportOrdersById(invoiceId));
		return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);

	}

	@PostMapping(path = "/order/item/completed")
	@Transactional
	public ResponseEntity<Response> transportCompleted(@RequestParam("invoiceId") String invoiceId,
			@RequestBody Map<String, Object> data) {
		final String id = AppUtil.ifNull(data.get("id"), "").toString();
		final String stockNo = AppUtil.ifNull(data.get("stockNo"), "").toString();
		final Map<String, Object> response = new HashMap<>();
		transportOrderItemRepository.updateStatusById(id, Constants.TRANSPORT_ITEM_DELIVERY_CONFIRMED);
		final TransportInfo transportInfo = new TransportInfo();

		final TStock stockDetails = stockRepository.findOneByStockNo(stockNo);
//		stockDetails.setTransportationStatus(Constants.TRANSPORT_IDLE);
		stockDetails.setTransportInfo(transportInfo);

		stockRepository.save(stockDetails);

		response.put("updatedData", transportOrderItemRepository.findOneTransportOrdersById(invoiceId));
		return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);

	}

	@PostMapping("/order/request/confirm")
	@Transactional
	public ResponseEntity<Response> confirmAllTransport(@RequestBody List<String> data, HttpServletResponse response) {
		final List<TTransportOrderItem> transportOrderItems = transportOrderItemRepository.findAllByIdIn(data);
		transportOrderItems.forEach(order -> {
			order.setStatus(Constants.TRANSPORT_ITEM_CONFIRMED);
			TStock stock = stockRepository.findOneByStockNo(order.getStockNo());
			final TransportInfo transportInfo = stock.getTransportInfo();
			transportInfo.setStatus(Constants.TRANSPORT_ITEM_CONFIRMED);
			stock.setTransportInfo(transportInfo);

			transportOrderItemRepository.save(order);
			stockRepository.save(stock);
		});
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping(path = "/create/invoice")
	@Transactional
	public ResponseEntity<Response> createTransportInvoice(@RequestParam("invoiceId") String invoiceId,
			@RequestBody List<TTransportInvoice> data) {
		final Map<String, Object> response = new HashMap<>();
		final String invoiceNo = sequenceService.getNextSequence(Constants.SEQUENCE_KEY_TI);
		data.forEach(invoice -> {
			invoice.setInvoiceNo(invoiceNo);
			invoice.setStatus(Constants.TRANSPORT_INVOICE_NOT_BOOKED);
			invoice.setPaymentApprove(Constants.PAYMENT_NOT_APPROVED);
		});

		tTransportInvoiceRepository.insert(data);
		data.forEach(order -> {
			// update order request
			TTransportOrderItem orderRequest = transportOrderItemRepository
					.findOneByInvoiceNoAndStockNo(order.getOrderId(), order.getStockNo());
			orderRequest.setStatus(Constants.TRANSPORT_ITEM_DELIVERY_CONFIRMED);
			orderRequest.setEtd(order.getEtd());
			orderRequest.setCharge(order.getAmount());
			transportOrderItemRepository.save(orderRequest);

			// find location and update shipment type
			MLocation location = null;
			if (!order.getDropLocation().equalsIgnoreCase("others")) {
				location = locationRepository.findOneByCode(order.getDropLocation());
			}

			// update stock
			final TStock stock = stockRepository.findOneByStockNo(order.getStockNo());
			final TransportInfo transportInfo = stock.getTransportInfo();
			transportInfo.setStatus(Constants.TRANSPORT_ITEM_DELIVERY_CONFIRMED);
			stock.setTransportInfo(transportInfo);
			stock.setTransportInvoiceNo(invoiceNo);
			if (!AppUtil.isObjectEmpty(location) && !AppUtil.isObjectEmpty(location.getShipmentType())) {
				stock.setShipmentType(location.getShipmentType());
			}

			stock.setTransportationStatus(Constants.TRANSPORT_INTRANSIT);
			stockRepository.save(stock);

		});

		response.put("updatedData", transportOrderItemRepository.findOneTransportOrdersById(invoiceId));

		return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);
	}

	@PostMapping(path = "/confirm/delivered")
	@Transactional
	public ResponseEntity<Response> confirmDelivered(@RequestBody List<TransportDeliveryConfirmDto> data) {
		for (TransportDeliveryConfirmDto item : data) {
			TTransportOrderItem order = transportOrderItemRepository.findOneByInvoiceNoAndStockNo(item.getOrderId(),
					item.getStockNo());
			order.setCharge(item.getAmount());
			order.setStatus(Constants.TRANSPORT_ITEM_DELIVERED);
			order.setEtd(item.getEtd());
			transportOrderItemRepository.save(order);
			// update invoice
			TTransportInvoice invoice = tTransportInvoiceRepository.findOneByOrderIdAndStockNo(item.getOrderId(),
					item.getStockNo());
			if (invoice.getStatus().equals(Constants.TRANSPORT_INVOICE_NOT_BOOKED)) {
				invoice.setAmount(item.getAmount());
				tTransportInvoiceRepository.save(invoice);
			}
			// update stock
			final TStock stock = stockRepository.findOneByStockNo(item.getStockNo());
			final TransportInfo transportInfo = stock.getTransportInfo();
			transportInfo.setStatus(Constants.TRANSPORT_ITEM_DELIVERED);
			stock.setTransportInfo(transportInfo);
			stock.setTransportationCount(stock.getTransportationCount() + 1);
			stock.setTransportationStatus(Constants.TRANSPORT_COMPLETED);
			stockRepository.save(stock);
		}
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping("/order/request")
	public void pdfDownload(@RequestParam("format") String format, @RequestParam("invoiceNo") String invoiceNo,
			@RequestParam("purchaseDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date purchaseDate,
			@RequestParam("transporter") String transporter, @RequestParam("flag") String flag,
			HttpServletResponse response) throws JRException, IOException {

		List<Integer> matchTransStatus = null;
		if (flag.equalsIgnoreCase("requested")) {
			matchTransStatus = Arrays.asList(Constants.TRANSPORT_ITEM_INITIATED, Constants.TRANSPORT_ITEM_CONFIRMED);
		} else if (flag.equalsIgnoreCase("confirmed")) {
			matchTransStatus = Arrays.asList(Constants.TRANSPORT_ITEM_CONFIRMED);
		} else if (flag.equalsIgnoreCase("delivered")) {
			matchTransStatus = Arrays.asList(Constants.TRANSPORT_ITEM_DELIVERY_CONFIRMED);
		}

		final TTransportOrderInvoiceDto transportOrder = transportOrderItemRepository
				.findOneTransportOrdersInvoiceByInvoiceID(invoiceNo, purchaseDate, transporter, matchTransStatus);

		// get all locations except others
		final ArrayList<String> locations = new ArrayList<>();
		for (final TTransportOrderInvoiceItemDto orderItem : transportOrder.getOrderItem()) {

			if (!orderItem.getDropLocation().equalsIgnoreCase("others")
					&& !locations.contains(orderItem.getDropLocation())) {
				locations.add(orderItem.getDropLocation());
			}
		}

		// location dataset
		final List<MLocation> locationsList = locationRepository.findAllByCodeIn(locations);
		// order item dataset
		final JRBeanCollectionDataSource datasourceLocations = new JRBeanCollectionDataSource(locationsList);
		final JRBeanCollectionDataSource datasourceJRBean = new JRBeanCollectionDataSource(
				transportOrder.getOrderItem());
		final InputStream jasperStream = this.getClass()
				.getResourceAsStream("/templates/jasper/TransportOrderRequest.jasper");

		final Map<String, Object> params = new HashMap<>();
		params.put("itemDatasource", datasourceJRBean);
		params.put("locationList", datasourceLocations);
		params.put("orderDetails", transportOrder);
		try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/jasper/images/logo.png")) {
			params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
		} catch (JRException | IOException e) {
			throw new AAJRuntimeException("Failed to load images", e);
		}
		final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

		final String filename = "Transport Request";

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
	}

	@PostMapping(value = "multiple/order/request")
	public void multiplepdfDownload(@RequestParam("invoiceIds[]") String[] invoiceIds,
			@RequestParam("format") String format, HttpServletResponse response) throws JRException, IOException {
//		List<String> invoiceIds = new ArrayList<String>();
		final TTransportOrderInvoiceDto transportOrder = transportOrderItemRepository
				.findOneTransportOrdersInvoiceByInvoiceID(invoiceIds);

		transportOrder.setCommentAppended(transportOrder.getComment().stream().collect(Collectors.joining("\n")));

		// get all locations except others
		final ArrayList<String> locations = new ArrayList<>();
		for (final TTransportOrderInvoiceItemDto orderItem : transportOrder.getOrderItem()) {

			if (!orderItem.getDropLocation().equalsIgnoreCase("others")
					&& !locations.contains(orderItem.getDropLocation())) {
				locations.add(orderItem.getDropLocation());
			}
		}

		// location dataset
		final List<MLocation> locationsList = locationRepository.findAllByCodeIn(locations);
		// order item dataset
		final JRBeanCollectionDataSource datasourceLocations = new JRBeanCollectionDataSource(locationsList);
		final JRBeanCollectionDataSource datasourceJRBean = new JRBeanCollectionDataSource(
				transportOrder.getOrderItem());
		final InputStream jasperStream = this.getClass()
				.getResourceAsStream("/templates/jasper/TransportOrderRequest.jasper");

		final Map<String, Object> params = new HashMap<>();
		params.put("itemDatasource", datasourceJRBean);
		params.put("locationList", datasourceLocations);
		params.put("orderDetails", transportOrder);
		try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/jasper/images/logo.png")) {
			params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
		} catch (JRException | IOException e) {
			throw new AAJRuntimeException("Failed to load images", e);
		}
		final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

		final String filename = "Transport Request";

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

	}

	@GetMapping("/transporter/fee/create")
	public ModelAndView transporterFee(ModelAndView modelAndView) {
		modelAndView.setViewName("transporter.fee.create");
		return modelAndView;
	}

	@PostMapping(path = "/transporter/fee/save")
	public ModelAndView transporterFeeSave(@ModelAttribute TTransporterFee ttransporterFee,
			RedirectAttributes redirectAttributes, ModelAndView modelAndView) {

		TTransporterFee ttransporterFeeToSave = null;
		if (AppUtil.isObjectEmpty(ttransporterFee.getId())) {
			ttransporterFeeToSave = ttransporterFee;
			ttransporterFee.setId(null);
			ttransporterFeeToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
		} else {
			ttransporterFeeToSave = ttransporterFeeRepository.findOneById(ttransporterFee.getId());
			ttransporterFeeToSave.setAmount(ttransporterFee.getAmount());
//			ttransporterFeeToSave.setCategories(ttransporterFee.getCategories());
			ttransporterFeeToSave.setTransportCategory(ttransporterFee.getTransportCategory());
			ttransporterFeeToSave.setFrom(ttransporterFee.getFrom());
			ttransporterFeeToSave.setTo(ttransporterFee.getTo());
			ttransporterFeeToSave.setTransporter(ttransporterFee.getTransporter());
		}
		ttransporterFeeRepository.save(ttransporterFeeToSave);
		redirectAttributes.addFlashAttribute("message", "Transporter Fee Added successfully.");
		modelAndView.setViewName("redirect:/transport/transporter/fee/list");
		return modelAndView;
	}

	@GetMapping("/transporter/fee/list")
	public ModelAndView transporterFeeList() {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("transporter.fee.list");
		return modelAndView;
	}

	@GetMapping("/transporter/fee/list/datasource")
	@ResponseBody
	public DatatableResponse transporterFeeListDataSource() {
		return new DatatableResponse(ttransporterFeeRepository.getListBeforeDeleteAndWithoutCode());
	}

	@GetMapping("/transporter/fee/edit/{id}")
	public ModelAndView supplierCreate(@PathVariable("id") String id, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("id", id);
		modelAndView.setViewName("redirect:/transport/transporter/fee/create");
		return modelAndView;
	}

	@GetMapping("/transporter/fee/info/{id}.json")
	public ResponseEntity<Response> infoSupplier(@PathVariable("id") String id) {
		return new ResponseEntity<>(new Response("success", ttransporterFeeRepository.findOneById(id)), HttpStatus.OK);
	}

	@GetMapping("/transporter/fee/delete/{id}")
	public ModelAndView deleteSupplier(@PathVariable("id") String id, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		final TTransporterFee tTransporterFee = ttransporterFeeRepository.findOneById(id);
		tTransporterFee.setDeleteFlag(Constants.DELETE_FLAG_1);
		ttransporterFeeRepository.save(tTransporterFee);
		redirectAttributes.addFlashAttribute("message", "deleted Successfully");
		modelAndView.setViewName("redirect:/transport/transporter/fee/list");
		return modelAndView;
	}

	@GetMapping(path = "/charge")
	public ResponseEntity<Response> getCharge(@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to,
			@RequestParam(value = "transportCategory", required = false) String transportCategory) {
		if (AppUtil.isObjectEmpty(from) || AppUtil.isObjectEmpty(to)) {
			return new ResponseEntity<>(new Response("success", new ArrayList<>()), HttpStatus.OK);

		}
		List<TTransporterChargeDto> tFee = ttransporterFeeRepository.findByLocation(from, transportCategory, to);
		boolean autopopulate = false;
		if (!AppUtil.isObjectEmpty(tFee)) {
			autopopulate = true;
			tFee = new ArrayList<>(tFee);
			List<String> transporters = tFee.stream().map(TTransporterChargeDto::getCode).collect(Collectors.toList());
			List<MTransporter> list = transportersRepository.findAllByCodeNotIn(transporters);
			for (MTransporter transporter : list) {
				TTransporterChargeDto dto = new TTransporterChargeDto();
				dto.setCode(transporter.getCode());
				dto.setName(transporter.getName());
				tFee.add(dto);
			}
		} else {
			List<MTransporter> list = transportersRepository.findAll();
			tFee = list.stream().map(t -> {
				TTransporterChargeDto dto = new TTransporterChargeDto();
				dto.setCode(t.getCode());
				dto.setName(t.getName());
				return dto;
			}).collect(Collectors.toList());
		}
		Document result = new Document();
		result.put("list", tFee);
		result.put("autopopulate", autopopulate);
		return new ResponseEntity<>(new Response("success", result), HttpStatus.OK);
	}

	@PostMapping(path = "/order/update")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> transportOrderUpdate(@RequestBody List<TTransportOrderItem> transportOrderDto) {
		final MLoginDto loginDto = securityService.findLoggedInUser();

		// get order item in first index for getting common details
		final TTransportOrderItem orderItem = transportOrderDto.get(0);

		transportOrderDto.forEach(item -> {
			if (!AppUtil.isObjectEmpty(item.getId())) {
				transportOrderItemRepository.findById(item.getId()).ifPresent(i -> {
					i.setStockNo(AppUtil.ifNull(item.getStockNo(), ""));
					i.setPickupDate(item.getPickupDate());
					i.setDeliveryDate(item.getDeliveryDate());
					i.setPickupTime(AppUtil.ifNull(item.getPickupTime(), ""));
					i.setDeliveryTime(AppUtil.ifNull(item.getDeliveryTime(), ""));
					i.setTransporter(AppUtil.ifNull(item.getTransporter(), ""));
					i.setDestinationCountry(AppUtil.ifNull(item.getDestinationCountry(), ""));
					i.setDestinationPort(AppUtil.ifNull(item.getDestinationPort(), ""));
					i.setPickupLocation(AppUtil.ifNull(item.getPickupLocation(), ""));
					i.setDropLocation(AppUtil.ifNull(item.getDropLocation(), ""));
					i.setPickupLocationCustom(AppUtil.ifNull(item.getPickupLocationCustom(), ""));
					i.setDropLocationCustom(AppUtil.ifNull(item.getDropLocationCustom(), ""));
					i.setCharge(AppUtil.ifNull(item.getCharge(), 0.0));
					i.setPosNo(AppUtil.ifNull(item.getPosNo(), ""));
					i.setRemarks(AppUtil.ifNull(item.getRemarks(), ""));
					i.setScheduleType(AppUtil.ifNull(item.getScheduleType(), null));
					i.setSelectedDate(AppUtil.ifNull(item.getSelectedDate(), null));
					i.setComment(AppUtil.ifNull(item.getComment(), ""));
					i.setEtd(item.getEtd());
					transportOrderItemRepository.save(i);
					// update stock
					TStock stock = stockRepository.findOneByStockNo(item.getStockNo());

					final TransportInfo transportInfo = new TransportInfo();
					transportInfo.setDeliveryDate(item.getDeliveryDate());
					transportInfo.setDeliveryTime(item.getDeliveryTime());
					transportInfo.setPickupDate(item.getPickupDate());
					transportInfo.setPickupTime(item.getPickupTime());
					transportInfo.setTransporter(item.getTransporter());
					transportInfo.setPickupLocation(item.getPickupLocation());
					transportInfo.setPickupLocationCustom(item.getPickupLocationCustom());
					transportInfo.setRemarks(item.getRemarks());
					transportInfo.setDropLocation(item.getDropLocation());
					transportInfo.setDropLocationCustom(item.getDropLocationCustom());
					transportInfo.setStatus(i.getStatus());

					stock.setTransportInfo(transportInfo);
					stock.getPurchaseInfo().getAuctionInfo().setPosNo(item.getPosNo());
					stock.setTransportationStatus(Constants.TRANSPORT_INTRANSIT);
					stock.setLastTransportLocation(item.getDropLocation());
					stock.setLastTransportLocationCustom(item.getDropLocationCustom());
					stock.setDestinationCountry(item.getDestinationCountry());
					stock.setDestinationPort(item.getDestinationPort());
					stock.setLastModifiedDate(new Date());
					stock.setLastModifiedBy(loginDto.getUsername());
					stock.setForwarder(item.getForwarder());
					stockRepository.save(stock);
				});

			}
		});

		// transportationCount
		final List<String> stockNos = transportOrderDto.stream().map(TTransportOrderItem::getStockNo)
				.collect(Collectors.toList());

		if (!AppUtil.isObjectEmpty(orderItem.getSelectedDate())
				&& (orderItem.getSelectedDate() == Constants.TRANSPORT_PICKUP_AFTER_PAYMENT)) {
			final List<TPurchaseInvoice> purchaseInvoices = tPurchaseInvoiceRepository.findAllByStockNoIn(stockNos);
			purchaseInvoices.forEach(invoice -> invoice.setDueDate(orderItem.getDeliveryDate()));
			tPurchaseInvoiceRepository.saveAll(purchaseInvoices);
		}

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
}
