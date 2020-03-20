package com.nexware.aajapan.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.CustomerEmailDto;
import com.nexware.aajapan.dto.DayBookListDto;
import com.nexware.aajapan.dto.DocumentStatusCountDto;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.dto.MUserInfoDto;
import com.nexware.aajapan.dto.MVechicleMakerDto;
import com.nexware.aajapan.dto.PurchasedSaveDto;
import com.nexware.aajapan.dto.SpecialUserDto;
import com.nexware.aajapan.dto.StockSearchDto;
import com.nexware.aajapan.dto.StockStatusCountDto;
import com.nexware.aajapan.dto.TInquiryDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.AuctionPaymentType;
import com.nexware.aajapan.models.InquiryVehicle;
import com.nexware.aajapan.models.MAuctionGradesExterior;
import com.nexware.aajapan.models.MAuctionGradesInterior;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.MCOA;
import com.nexware.aajapan.models.MColor;
import com.nexware.aajapan.models.MContinent;
import com.nexware.aajapan.models.MCountryPort;
import com.nexware.aajapan.models.MCraneCut;
import com.nexware.aajapan.models.MCraneType;
import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.models.MCustomListFields;
import com.nexware.aajapan.models.MDepartment;
import com.nexware.aajapan.models.MExel;
import com.nexware.aajapan.models.MExtraEquipments;
import com.nexware.aajapan.models.MForeignBank;
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.models.MForwarderDetail;
import com.nexware.aajapan.models.MFuelTankKiloLitre;
import com.nexware.aajapan.models.MFuelType;
import com.nexware.aajapan.models.MGeneralLedger;
import com.nexware.aajapan.models.MGeneralSupplier;
import com.nexware.aajapan.models.MInspectionCompany;
import com.nexware.aajapan.models.MInvoiceType;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.models.MOfficeLocation;
import com.nexware.aajapan.models.MPaymentCategory;
import com.nexware.aajapan.models.MPlateNoRegistration;
import com.nexware.aajapan.models.MPort;
import com.nexware.aajapan.models.MRemitType;
import com.nexware.aajapan.models.MReportingCategoryType;
import com.nexware.aajapan.models.MRole;
import com.nexware.aajapan.models.MShip;
import com.nexware.aajapan.models.MShippingCharge;
import com.nexware.aajapan.models.MShippingCompany;
import com.nexware.aajapan.models.MShippingMarks;
import com.nexware.aajapan.models.MShippingTerms;
import com.nexware.aajapan.models.MSpecialExchangeRate;
import com.nexware.aajapan.models.MSubAccountType;
import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.MTransmissionType;
import com.nexware.aajapan.models.MTransportCategory;
import com.nexware.aajapan.models.MTransporter;
import com.nexware.aajapan.models.MTyreSize;
import com.nexware.aajapan.models.MUser;
import com.nexware.aajapan.models.MVechicleCategory;
import com.nexware.aajapan.models.MVechicleMaker;
import com.nexware.aajapan.models.OtherDirectExpense;
import com.nexware.aajapan.models.Schedule;
import com.nexware.aajapan.models.TBillOfExchange;
import com.nexware.aajapan.models.TInquiry;
import com.nexware.aajapan.models.TNotification;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TReAuction;
import com.nexware.aajapan.models.TShipmentSchedule;
import com.nexware.aajapan.models.TShippingRequest;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TTransportInvoice;
import com.nexware.aajapan.models.TransportPaymentCategory;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.AuctionPaymentTypeRepository;
import com.nexware.aajapan.repositories.BillOfExchangeRepo;
import com.nexware.aajapan.repositories.CountryPortRepository;
import com.nexware.aajapan.repositories.CustomListFieldsRepo;
import com.nexware.aajapan.repositories.InquiryRepository;
import com.nexware.aajapan.repositories.LocationRepository;
import com.nexware.aajapan.repositories.LoginRepository;
import com.nexware.aajapan.repositories.MAuctionGradesExteriorRepository;
import com.nexware.aajapan.repositories.MAuctionGradesInteriorRepository;
import com.nexware.aajapan.repositories.MCOARepository;
import com.nexware.aajapan.repositories.MCraneCutRepository;
import com.nexware.aajapan.repositories.MCraneTypeRepository;
import com.nexware.aajapan.repositories.MCurrencyRepository;
import com.nexware.aajapan.repositories.MDepartmentRepository;
import com.nexware.aajapan.repositories.MExelRepository;
import com.nexware.aajapan.repositories.MExtraEquipmentsRepository;
import com.nexware.aajapan.repositories.MForeignBankRepository;
import com.nexware.aajapan.repositories.MForwarderDetailRepository;
import com.nexware.aajapan.repositories.MForwarderRepository;
import com.nexware.aajapan.repositories.MFuelTankKiloLitreRepository;
import com.nexware.aajapan.repositories.MGeneralLedgerRepository;
import com.nexware.aajapan.repositories.MGeneralSupplierRepository;
import com.nexware.aajapan.repositories.MInspectionCompanyRepository;
import com.nexware.aajapan.repositories.MInvoiceTypeRepository;
import com.nexware.aajapan.repositories.MOfficeLocationRepository;
import com.nexware.aajapan.repositories.MPlateNoRegistrationRepository;
import com.nexware.aajapan.repositories.MPortRepository;
import com.nexware.aajapan.repositories.MRemitTypeRep;
import com.nexware.aajapan.repositories.MReportingCategoryTypeRep;
import com.nexware.aajapan.repositories.MRoleRepository;
import com.nexware.aajapan.repositories.MShipChargeRepository;
import com.nexware.aajapan.repositories.MShippingMarksRepository;
import com.nexware.aajapan.repositories.MShippingTermsRepository;
import com.nexware.aajapan.repositories.MSubAccountTypeRep;
import com.nexware.aajapan.repositories.MTransportCategoryRepository;
import com.nexware.aajapan.repositories.MTyreSizeRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.MasterColorsRepository;
import com.nexware.aajapan.repositories.MasterContinentRepository;
import com.nexware.aajapan.repositories.MasterCountryPortRepository;
import com.nexware.aajapan.repositories.MasterFuelTypeRepository;
import com.nexware.aajapan.repositories.MasterPaymentRepository;
import com.nexware.aajapan.repositories.MasterShipRepository;
import com.nexware.aajapan.repositories.MasterShippingCompanyRepository;
import com.nexware.aajapan.repositories.MasterSpecialExchangeRateRepository;
import com.nexware.aajapan.repositories.MasterSupplierRepository;
import com.nexware.aajapan.repositories.MasterTransmissionTypesRepository;
import com.nexware.aajapan.repositories.MasterVechicleCategoryRepository;
import com.nexware.aajapan.repositories.MasterVechicleMakerRepository;
import com.nexware.aajapan.repositories.OtherDirectExpenseRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TAdvancePaymentRepository;
import com.nexware.aajapan.repositories.TBankTransactionRepository;
import com.nexware.aajapan.repositories.TCustomReportRepository;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.repositories.TDayBookTransactionRepository;
import com.nexware.aajapan.repositories.TDocumentConversionRepository;
import com.nexware.aajapan.repositories.TDocumentReceivedRepository;
import com.nexware.aajapan.repositories.TFreightShippingInvoiceRepository;
import com.nexware.aajapan.repositories.TFwdrInvoiceRepository;
import com.nexware.aajapan.repositories.TInspectionOrderRequestCancelledRepository;
import com.nexware.aajapan.repositories.TInspectionOrderRequestRepository;
import com.nexware.aajapan.repositories.TInsuranceRepository;
import com.nexware.aajapan.repositories.TInvoiceRepository;
import com.nexware.aajapan.repositories.TProformaInvoiceRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.repositories.TReAuctionRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.repositories.TShipmentScheduleRepository;
import com.nexware.aajapan.repositories.TShippingInstructionRepository;
import com.nexware.aajapan.repositories.TShippingRequestRepository;
import com.nexware.aajapan.repositories.TSupplierTransactionRepository;
import com.nexware.aajapan.repositories.TTransportInvoiceRepository;
import com.nexware.aajapan.repositories.TTransporterFeeRepository;
import com.nexware.aajapan.repositories.TransportOrderItemRepository;
import com.nexware.aajapan.repositories.TransportPaymentRepository;
import com.nexware.aajapan.repositories.TransportersRepository;
import com.nexware.aajapan.repositories.UserRepository;
import com.nexware.aajapan.services.CustomerService;
import com.nexware.aajapan.services.MLoginService;
import com.nexware.aajapan.services.MSupplierService;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TNotificationService;
import com.nexware.aajapan.services.TPurchaseInvoiceService;
import com.nexware.aajapan.utils.AppUtil;

@RestController
public class RESTController {
	@Autowired
	private TCustomerRepository customerRepository;
	@Autowired
	private MRemitTypeRep mRemitTypeRep;
	@Autowired
	private TShipmentScheduleRepository addShipmentScheduleRepository;
	@Autowired
	private OtherDirectExpenseRepository otherDirectExpenseRepository;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CountryPortRepository countryPortRepository;
	@Autowired
	private TDocumentConversionRepository tDocumentConversionRepository;
	@Autowired
	private MasterContinentRepository masterContinentRepository;
	@Autowired
	private TransportPaymentRepository transportPaymentRepository;
	@Autowired
	private AuctionPaymentTypeRepository auctionPaymentTypeRepository;
	@Autowired
	private TTransportInvoiceRepository tTransportInvoiceRepository;
	@Autowired
	private TInvoiceRepository tInvoiceRepository;
	@Autowired
	private InquiryRepository inquiryRepository;
	@Autowired
	private MasterShippingCompanyRepository masterShippingCompanyRepository;
	@Autowired
	private TPurchaseInvoiceRepository purchaseInvoiceRepository;
	@Autowired
	private MDepartmentRepository departmentRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private TInsuranceRepository insuranceRepository;

	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoginRepository loginRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private MForwarderRepository forwarderRepository;
	@Autowired
	private MOfficeLocationRepository mOfficeLocationRepository;
	@Autowired
	private MasterVechicleCategoryRepository masterVechicleCategoryRepository;
	@Autowired
	private TFreightShippingInvoiceRepository tFreightShippingInvoiceRepository;
	@Autowired
	private MasterCountryPortRepository masterCountryPortRepository;
	@Autowired
	private MTransportCategoryRepository mTransportCategoryRepository;
	@Autowired
	private MForeignBankRepository mForeignBankRepository;
	@Autowired
	private MasterVechicleMakerRepository masterVechicleMakerRepository;
	@Autowired
	private TransportersRepository transportersRepository;
	@Autowired
	private MSubAccountTypeRep mSubAccountTypeRep;
	@Autowired
	private MReportingCategoryTypeRep mReportingCategoryTypeRep;
	@Autowired
	private MCOARepository mCoaRepo;
	@Autowired
	private MasterShipRepository masterShipRepository;
	@Autowired
	private MasterTransmissionTypesRepository masterTransmissionTypesRepository;
	@Autowired
	private MasterFuelTypeRepository masterFuelTypeRepository;
	@Autowired
	private MasterColorsRepository masterColorsRepository;
	@Autowired
	private MasterSupplierRepository masterSupplierRepository;
	@Autowired
	private TReAuctionRepository tReAuctionRepository;
	@Autowired
	private BillOfExchangeRepo billOfExchange;

	@Autowired
	private MInvoiceTypeRepository mInvoiceTypeRepository;
	@Autowired
	private TFwdrInvoiceRepository tFwdrInvoiceRepository;
	@Autowired
	private MasterPaymentRepository masterPaymentRepository;
	@Autowired
	private MasterBankRepository masterBankRepository;
	@Autowired
	private TBankTransactionRepository tBankTransactionRepository;
	@Autowired
	private MCurrencyRepository currencyRepository;
	@Autowired
	private TProformaInvoiceRepository tproformaInvoiceRepository;
	@Autowired
	private TSalesInvoiceRepository tsalesInvoiceRepository;
	@Autowired
	private TShippingInstructionRepository shippingInstructionRepository;
	@Autowired
	private TInspectionOrderRequestRepository inspectionOrderRequestRepository;
	@Autowired
	private TShippingRequestRepository shippingRequestRepository;
	@Autowired
	private TNotificationService notificationService;
	@Autowired
	private TransportOrderItemRepository transportOrderItemRepository;
	@Autowired
	private TTransporterFeeRepository ttransporterFeeRepository;
	@Autowired
	private MAuctionGradesExteriorRepository auctionGradesExteriorRepository;
	@Autowired
	private MAuctionGradesInteriorRepository auctionGradesInteriorRepository;
	@Autowired
	private TSupplierTransactionRepository supplierTransactionRepository;
	@Autowired
	private MInspectionCompanyRepository inspectionCompanyRepository;
	@Autowired
	private TDocumentReceivedRepository documentReceivedRepository;
	@Autowired
	private MPlateNoRegistrationRepository plateNoRegistrationRepository;
	@Autowired
	private MForwarderDetailRepository mForwarderDetailRepository;
	@Autowired
	private TAdvancePaymentRepository advancePaymentRepository;
	@Autowired
	private MSupplierService supplierService;
	@Autowired
	private MRoleRepository roleRepository;
	@Autowired
	private MLoginService loginService;
	@Autowired
	private MGeneralLedgerRepository generalLedgerRepository;
	@Autowired
	private TPurchaseInvoiceService purchaseInvoiceService;
	@Autowired
	private MPortRepository portRepository;
	@Autowired
	private MGeneralSupplierRepository masterGeneralSupplierRepository;
	@Autowired
	private MShipChargeRepository mShipChargeRepo;
	@Autowired
	private CustomListFieldsRepo customFields;
	@Autowired
	TCustomReportRepository customReportRepository;
	@Autowired
	TDayBookTransactionRepository tDayBookTransactionRepository;
	@Autowired
	private MExtraEquipmentsRepository extraEquipmentsRepository;
	@Autowired
	private MCraneCutRepository craneCutRepository;
	@Autowired
	private MCraneTypeRepository craneTypeRepository;
	@Autowired
	private MExelRepository exelRepository;
	@Autowired
	private MFuelTankKiloLitreRepository fuelTankKiloLitreRepository;
	@Autowired
	private MTyreSizeRepository tyreSizeRepository;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private MShippingTermsRepository shippingTerms;
	@Autowired
	private MShippingMarksRepository shippingMarks;
	@Autowired
	private TInspectionOrderRequestCancelledRepository inspectionOrderRequestCancelledRepository;
	@Autowired
	private MasterSpecialExchangeRateRepository specialExchangeRateRepository;

	@GetMapping("/data/shippingMarks.json")
	public List<MShippingMarks> findAllShippingMarks() {
		return shippingMarks.findAllByDeleteFlag(Constants.DELETE_FLAG_0);
	}

	@GetMapping("/data/shippingTerms.json")
	public List<MShippingTerms> findAllShippingTerms() {
		return shippingTerms.findAllByDeleteFlag(Constants.DELETE_FLAG_0);
	}

	@GetMapping("/data/locations.json")
	public List<MLocation> findAllLocations() {

		return locationRepository.findAllByDeleteFlag(Constants.DELETE_FLAG_0);
	}

	@GetMapping("/data/ports.json")
	public List<MPort> findAllPort() {

		return portRepository.findAll();
	}

	@GetMapping("/data/mOfficeLocation.json")
	public List<MOfficeLocation> findAllOfficeLocation() {
		return mOfficeLocationRepository.findAll();
	}

	@GetMapping("/data/mRemitType.json")
	public List<MRemitType> findAllRemitTypes() {
		return mRemitTypeRep.findAll();
	}

	@GetMapping("/data/departments.json")
	public List<MDepartment> findAllDepartment() {
		return departmentRepository.findAll();
	}

	@GetMapping("/data/roles")
	public List<MRole> findAllRoles(@RequestParam("department") String department) {
		return roleRepository.findAllByDepartment(department);
	}

	@GetMapping("/data/salesPerson")
	public List<MLoginDto> findAllSalesPerson() {
		return loginRepository.findAllSalesPerson();
	}
	
	@GetMapping("/data/reportingPerson")
	public List<Document> findAllReportingPerson(@RequestParam("department") String department,
			@RequestParam("role") String role) {
		return loginRepository.findAllByDepartmentAndRole(department, role);
	}

	@GetMapping("/data/forwarders.json")
	public List<MForwarder> findAllForwarders() {
		return forwarderRepository.findAll();
	}

	@GetMapping("/data/inspectioncompanies.json")
	public List<MInspectionCompany> findAllInspectionCompanies() {
		return inspectionCompanyRepository.findAll();
	}

	@GetMapping("/data/currency.json")
	public List<MCurrency> findAllCurrency() {
		return currencyRepository.findAll();
	}

	@GetMapping("/data/subaccounttype.json")
	public List<MSubAccountType> findAllSubAccountType() {
		return mSubAccountTypeRep.findAll();
	}

	@GetMapping("/data/shippingCompany.json")
	public List<MShippingCompany> findAllShippingCompany() {
		return masterShippingCompanyRepository.findAllByDeleteFlag(Constants.DELETE_FLAG_0);
	}

	@GetMapping("/data/shipname.json")
	public List<MShip> findAllShipName() {
		return masterShipRepository.findAllByDeleteFlag(Constants.DELETE_FLAG_0);
	}

	@GetMapping("/data/reportingcategorytype.json")
	public List<MReportingCategoryType> findAllReportingCategoryType() {
		return mReportingCategoryTypeRep.findAll();
	}

	@GetMapping("/data/accname.json")
	public List<MCOA> findAllCoaDesc() {
		return mCoaRepo.findAll();// ByStatus(Constants.COA_STATUS_TYPE_NEW);
	}

	@GetMapping("/data/accnameFilter.json")
	public List<MCOA> findAllAccnameFilter() {
		return mCoaRepo.findAll();
	}

	@GetMapping("/data/stockno.json")
	public List<TStock> findAllStockNo() {
		return stockRepository.findAll();
	}

	@GetMapping("/data/categories.json")
	public List<MVechicleCategory> findAllCategories() {
		return masterVechicleCategoryRepository.findAll();
	}

	@GetMapping("/data/makers.json")
	public List<MVechicleMakerDto> findAllMakers() {
		return masterVechicleMakerRepository.findAllModel();
	}

	@GetMapping("/data/makerModel.json")
	public List<MVechicleMaker> findAllMakerModel() {
		return masterVechicleMakerRepository.getListWithoutDelete();
	}

	@GetMapping("/data/transporters.json")
	public List<MTransporter> findAllTransporters() {
		return transportersRepository.findAll();
	}

	@GetMapping("/data/transporterselect.json")
	public List<MTransporter> findAllTransporterSelect() {
		return transportersRepository.findAll();
	}

	@GetMapping("/data/transmissionTypes.json")
	public List<MTransmissionType> findAllTransmissionTypes() {
		return masterTransmissionTypesRepository.findAll();
	}

	@GetMapping("/data/countries.json")
	public List<MCountryPort> findAllCountries() {
		return masterCountryPortRepository.findAll();
	}

	@GetMapping("/data/fuelTypes.json")
	public List<MFuelType> findAllFuelTypes() {
		return masterFuelTypeRepository.findAll();
	}

	@GetMapping("/data/colors.json")
	public List<MColor> findAllColors() {
		return masterColorsRepository.findAll();
	}

	@GetMapping("/data/suppliers.json")
	public List<MSupplier> findAllSupplier() {
		return masterSupplierRepository.findAll();
	}

	@GetMapping("/data/active/suppliers.json")
	public List<MSupplier> findAllActiveSupplier() {
		return masterSupplierRepository.getListWithoutDeletedSuppliers();
	}

	@GetMapping("customer/email")
	public List<CustomerEmailDto> getCustomerEmails(@RequestParam("search") String search, HttpServletRequest request) {
		return customerService.getCustomerEmailListBySearchTerm(search);
	}

	@GetMapping("/data/foreignBanks.json")
	public List<MForeignBank> findAllForeignBanks() {
		return mForeignBankRepository.getAllUnDeletedForeignBank();
	}

	@GetMapping("/data/paymentCategory.json")
	public List<MPaymentCategory> findAllPaymentCategory() {
		return masterPaymentRepository.findAll();
	}
	
	@GetMapping("/data/otherDirectExpense.json")
	public List<OtherDirectExpense> findAllOtherDirectExpense() {
		return otherDirectExpenseRepository.findAll();
	}

	@GetMapping("/data/transportPaymentCategory.json")
	public List<TransportPaymentCategory> findAllTransportPaymentCategory() {
		return transportPaymentRepository.findAll();
	}

	@GetMapping("/data/auctionPayment.json")
	public List<AuctionPaymentType> findAllAuctionPaymentCategory() {
		return auctionPaymentTypeRepository.findAll();
	}

	// masterPaymentRepository

	@GetMapping("supplier/getSupplier")
	public List<MSupplier> getSupplier(@RequestParam("search") String search, @RequestParam("criteria") String criteria,
			HttpServletRequest request) {

		return masterSupplierRepository.getSupplierListBySearchTerm(search, criteria);

	}

	@GetMapping("supplier/get-supplier/{supplier}")
	public MSupplier getSupplierByName(@PathVariable("supplier") String supplier, HttpServletRequest request) {
		return masterSupplierRepository.findOneBySupplierCode(supplier);

	}

	@GetMapping("/supplier/getPosNo")
	public List<String> findPosBySupplierCode(@RequestParam("supplier") String supplier,
			@RequestParam("auctionHouse") String auctionHouse) {
		return masterSupplierRepository.getPOSNos(supplier, auctionHouse);
	}

	@GetMapping("/data/countryWithContinentName")
	@ResponseBody
	public DatatableResponse countryRequestedContinent() {
		return new DatatableResponse(masterContinentRepository.getCountryWithContinent());
	}

	@GetMapping("/data/continent.json")
	public List<MContinent> findAllContinent() {
		return masterContinentRepository.findAll();
	}

	@GetMapping("/country/find-port/{country}")
	public MCountryPort findPortByCountry(@PathVariable("country") String country) {
		return countryPortRepository.findOneByCountry(country);
	}

	@GetMapping("/shipping-company/find-ship/{shippingCompanyNo}")
	public MShippingCompany findshipByshippingCompanyNo(@PathVariable("shippingCompanyNo") String shippingCompanyNo) {
		return masterShippingCompanyRepository.findOneByshippingCompanyNo(shippingCompanyNo);
	}

	@PostMapping("/inquiry/update")
	public ResponseEntity<Response> inquiryeditsave(@RequestBody Map<String, Object> data) throws IOException {

		final InquiryVehicle inquiryVehicles = mapper.readValue(mapper.writeValueAsString(data.get("inquiryData")),
				new TypeReference<InquiryVehicle>() {
				});
		final String inquiryId = (String) data.get("inquiryId");
		final String inquiryItemId = (String) data.get("inquiryItemId");
		final Query query = new Query(new Criteria().andOperator(Criteria.where("id").is(inquiryId),
				Criteria.where("inquiryVehicles").elemMatch(Criteria.where("id").is(inquiryItemId))));
		final Update update = new Update().set("inquiryVehicles.$.category", inquiryVehicles.getCategory())
				.set("inquiryVehicles.$.subCategory", inquiryVehicles.getSubCategory())
				.set("inquiryVehicles.$.maker", inquiryVehicles.getMaker())
				.set("inquiryVehicles.$.model", inquiryVehicles.getModel())
				.set("inquiryVehicles.$.subModel", inquiryVehicles.getSubModel())
				.set("inquiryVehicles.$.country", inquiryVehicles.getCountry())
				.set("inquiryVehicles.$.port", inquiryVehicles.getPort());
		mongoTemplate.updateMulti(query, update, TInquiry.class);
		final TInquiryDto inquiry = inquiryRepository.findOneInquiryById(inquiryId, inquiryItemId);
		return new ResponseEntity<>(new Response("success", inquiry), HttpStatus.OK);
	}

	@PostMapping("/sales/inquiry/delete")
	public ResponseEntity<Response> inquiryDelete(@RequestBody Map<String, Object> data) {
		final String id = (String) data.get("id1");
		final String inquiryId = data.get("inquiryId").toString();

		final Update update = new Update().pull("inquiryVehicles", Query.query(Criteria.where("_id").is(inquiryId)));
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(id)), update, TInquiry.class);
		final TInquiry inquiry = inquiryRepository.findOneByid(id);
		if (AppUtil.isObjectEmpty(inquiry.getInquiryVehicles())) {
			inquiryRepository.delete(inquiry);
		}
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@PostMapping("/supplier/save")
	public ResponseEntity<Response> supplier(MSupplier supplier) {
		supplier.setSupplierCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SUPPLIER));
		masterSupplierRepository.save(supplier);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/stock/purchased/confirm")
	public ResponseEntity<Response> purchasedSave(
			@RequestParam("dueDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date dueDate,
			@RequestParam(value = "auctionRefNo", required = false) String auctionRefNo,
			@RequestBody List<PurchasedSaveDto> purchasedSaveDtos) {
		String invoiceNo = "";
		TStock stock = null;
		double totalSupplierCreditAmount = 0.0;
		for (final PurchasedSaveDto p : purchasedSaveDtos) {
			final TPurchaseInvoice auctionInvoice = purchaseInvoiceRepository.findOneByCode(p.getInvoiceNo());
			if (auctionInvoice != null) {
				if (AppUtil.isObjectEmpty(invoiceNo)) {
					invoiceNo = purchaseInvoiceService.generateInvoiceNo(auctionInvoice.getCode());
				}
				auctionInvoice.setInvoiceNo(invoiceNo);
				auctionInvoice.setPurchaseCost(AppUtil.ifNullOrEmpty(p.getPurchaseCost(), 0.0));
				auctionInvoice.setCommision(AppUtil.ifNullOrEmpty(p.getCommision(), 0.0));
				auctionInvoice.setPurchaseCostTaxAmount(AppUtil.ifNullOrEmpty(p.getPurchaseCostTaxAmount(), 0.0));
				auctionInvoice.setCommisionTaxAmount(AppUtil.ifNullOrEmpty(p.getCommisionTaxAmount(), 0.0));
				auctionInvoice.setOtherCharges(AppUtil.ifNullOrEmpty(p.getOtherCharges(), 0.0));
				auctionInvoice.setOthersCostTaxAmount(AppUtil.ifNullOrEmpty(p.getOthersCostTaxAmount(), 0.0));
				auctionInvoice.setRecycle(AppUtil.ifNullOrEmpty(p.getRecycle(), 0.0));
				auctionInvoice.setRoadTax(AppUtil.ifNullOrEmpty(p.getRoadTax(), 0.0));

				auctionInvoice.setStatus(Constants.INV_STATUS_VERIFIED);

				auctionInvoice.setPaymentStatus(Constants.PAYMENT_STATUS_INITIATED);
				auctionInvoice.setRemarks(p.getRemarks());
				auctionInvoice.setInvoiceName(p.getInvoiceName());
				auctionInvoice.setInvoiceType(p.getInvoiceType());
				auctionInvoice.setPurchaseCostTax(AppUtil.ifNullOrEmpty(p.getPurchaseCostTax(), 0.0));
				auctionInvoice.setCommisionTax(AppUtil.ifNullOrEmpty(p.getCommisionTax(), 0.0));
				auctionInvoice.setOtherChargesTax(AppUtil.ifNullOrEmpty(p.getOtherChargesTax(), 0.0));
				auctionInvoice.setDueDate(dueDate);
				auctionInvoice.setAuctionRefNo(auctionRefNo);
				auctionInvoice.setInvoiceUpload(Constants.INVOICE_NOT_UPLOADED);

				totalSupplierCreditAmount += auctionInvoice.getTotalTaxIncluded();

//				if (auctionInvoice.getType().equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)) {
				// supplier transaction entry

				// update stock

				if (auctionInvoice.getType().equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_REAUCTION)) {
					final TReAuction reAuction = tReAuctionRepository.findOneByStockNo(p.getStockNo());
					reAuction.setStatus(Constants.REAUCTION_STATUS_INVOICE_CONFIRMED);
					tReAuctionRepository.save(reAuction);
				}
				if (auctionInvoice.getType().equals(Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE)) {
					stock = stockRepository.findOneByStockNo(p.getStockNo());
					stock.setStatus(Constants.STOCK_STATUS_PURCHASED_CONFIRMED);
					stock.setDocumentFob(auctionInvoice.getTotalTaxIncluded() + 100000);
					// for save status
					stock.setInventoryStatus(Constants.STOCK_INVENTORY_STATUS_GENERAL);
					stockRepository.save(stock);

					auctionInvoice.setPurchaseCostFlag(Constants.FLAG_YES);
					auctionInvoice.setCommissionFlag(Constants.FLAG_YES);
					auctionInvoice.setRoadTaxFlag(Constants.FLAG_YES);
					auctionInvoice.setRecycleFlag(Constants.FLAG_YES);
				}
				// update purchanse invoice
				purchaseInvoiceRepository.save(auctionInvoice);

			} else {
				throw new AAJRuntimeException("Purcahse invoice not found for stock ::" + p.getStockNo());
			}

		}

		// notification
		final List<TNotification> notifications = notificationService
				.purchaseSaveNotificationToSales(purchasedSaveDtos);

		notificationService.notify(notifications, "sales@aaj.com");

		return new ResponseEntity<>(new Response("success", invoiceNo), HttpStatus.OK);
	}

	@PostMapping("/specialuser/stock/save")
	@Transactional
	public ResponseEntity<Response> sepecialUserStockSave(@RequestBody List<SpecialUserDto> objectArr) {

		final BulkOperations ops = mongoTemplate.bulkOps(BulkMode.UNORDERED, TStock.class);
		objectArr.forEach(stock -> {
			stock.getThresholdRange();
			stock.getFob();
			final Update update = new Update();
			update.set("thresholdRange", stock.getThresholdRange()).set("fob", stock.getFob()).set("showForSales",
					Constants.SHOW_FOR_SALES);
			ops.updateOne(Query.query(Criteria.where("stockNo").is(stock.getStockNo())), update);
		});
		ops.execute();
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/update/sellingPrice")
	@Transactional
	public ResponseEntity<Response> updateSellingPrice(@RequestParam("stockNo") String stockNo,
			@RequestParam("showStock") boolean showStock, @RequestBody final Map<String, String> data) {
		final TStock stock = stockRepository.findOneByStockNo(stockNo);
		stock.setFob(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("sellingPriceInYen"), "0.0")));
		stock.setMinSellingPriceInDollar(
				Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("sellingPriceInDollar"), "0.0")));
		stock.setExchangeRate(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("exchangeRate"), "0.0")));
		stock.setShippingCharge(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("shippingCharge"), "0.0")));
		stock.setInspectionCharge(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("inspectionCharge"), "0.0")));
		stock.setRadiationCharge(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("radiationCharge"), "0.0")));
		stock.setFreightPerM3(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("freightPerM3"), "0.0")));
		stock.setFreightCharge(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("freightCharge"), "0.0")));
		stock.setCourierCharge(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("courierCharge"), "0.0")));
		stock.setProfit(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("profit"), "0.0")));
		stock.setExcludingProfit(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("excludingProfit"), "0.0")));
		stock.setOtherCharge(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("otherCharges"), "0.0")));
		stock.setTransportCharge(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("transportCharge"), "0.0")));
		stock.setShowForSales(Constants.SHOW_FOR_SALES);
		stock.setOfferPrice(Double.parseDouble(AppUtil.ifNullOrEmpty(data.get("offerPrice"), "0.0")));
		if (showStock) {
			stock.setShowStock(Constants.SHOW_STOCK_FOR_FEED);
		} else {
			stock.setShowStock(Constants.NOT_SHOW_STOCK_FOR_FEED);
		}

		stockRepository.save(stock);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/update/sellingPrice/onMultiSelect")
	@Transactional
	public ResponseEntity<Response> updateSellingPriceOnMultiSelect(@RequestBody List<String> data,
			@RequestParam("sellingPrice") Double sellingPrice, @RequestParam("offerPrice") Double offerPrice,
			@RequestParam("showStock") boolean showStock) {
		final MCurrency currency = currencyRepository.findOneByCurrencySeq(Constants.CURRENCY_USD);
		data.forEach(stockNo -> {
			final TStock stock = stockRepository.findOneByStockNo(stockNo);
			stock.setFob(sellingPrice);
			stock.setExchangeRate(currency.getSalesExchangeRate());
			stock.setMinSellingPriceInDollar(sellingPrice / (currency.getSalesExchangeRate()));
			stock.setShowForSales(Constants.SHOW_FOR_SALES);
			stock.setOfferPrice(offerPrice);
			if (showStock) {
				stock.setShowStock(Constants.SHOW_STOCK_FOR_FEED);
			} else {
				stock.setShowStock(Constants.NOT_SHOW_STOCK_FOR_FEED);
			}

			stockRepository.save(stock);
		});

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PostMapping("/shipment/edit/save")
	public ResponseEntity<Response> shipmentSave(@RequestBody Map<String, Object> data) throws IOException {
		final Schedule schedule = mapper.readValue(mapper.writeValueAsString(data.get("shipmentList")),
				new TypeReference<Schedule>() {
				});
		final String shipId = (String) data.get("id1");
		final String schId = (String) data.get("shippingId");
		final Query query = new Query(new Criteria().andOperator(Criteria.where("_id").is(schId),
				Criteria.where("schedule").elemMatch(Criteria.where("_id").is(shipId))));

		final Update update = new Update().set("schedule.$", schedule);
		mongoTemplate.updateMulti(query, update, TShipmentSchedule.class);

		return new ResponseEntity<>(new Response("success", update), HttpStatus.OK);
	}

	@PostMapping("/shipment/add/save")
	public ResponseEntity<Response> shipmentAddSave(@RequestBody Map<String, Object> data) throws IOException {

		final List<Schedule> schedule = mapper.readValue(mapper.writeValueAsString(data.get("shipmentList2")),
				new TypeReference<List<Schedule>>() {
				});

		final String shipId = (String) data.get("addid");

		final TShipmentSchedule newshipment = addShipmentScheduleRepository.findOneByid(shipId);
		newshipment.getSchedule().addAll(schedule);

		addShipmentScheduleRepository.save(newshipment);
		return new ResponseEntity<>(new Response("success", schedule), HttpStatus.OK);

	}

	@GetMapping("/data/boe.json")
	public List<TBillOfExchange> findAllBOE() {
		return billOfExchange.findAll();
	}

	@PostMapping("/ship/schedule/delete")
	public ResponseEntity<Response> scheduleDelete(@RequestBody Map<String, Object> data) {

		final String id = (String) data.get("id1");
		final String scheduleId = data.get("id2").toString();

		final Update update = new Update().pull("schedule", Query.query(Criteria.where("_id").is(scheduleId)));
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(id)), update, TShipmentSchedule.class);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);

	}

	@GetMapping("/get/exchange.json")
	public List<MCurrency> getExchageRate() {
		return currencyRepository.findAll();
	}
	
	@GetMapping("/get/special/exchange.json")
	public MSpecialExchangeRate getSpecialExchageRate() {
		return specialExchangeRateRepository.findOneByOrderByCreatedDateDesc();
	}
	
	@GetMapping("/check/exchange.json")
	public boolean checkExchageRate() {
		return currencyRepository.existsByLastModifiedDateBetween(AppUtil.atStartOfDay(new Date()),
				AppUtil.atEndOfDay(new Date()));
	}

	@GetMapping("/data/bank.json")
	public List<MBank> findAllBanks() {
		return masterBankRepository.findAllByAccountType(Constants.ACCOUNT_TYPE_BANK);
	}
	
	@GetMapping("/data/savingBank.json")
	public List<MBank> findAllSavingBank() {
		return masterBankRepository.findAllByAccountType(Constants.TYPE_SAVING_ACCOUNT);
	}

	@GetMapping("/data/shipping-dashboard/status-count")
	public ResponseEntity<Response> getShippingDashboardCount() {
		final StockStatusCountDto statusCountDto = new StockStatusCountDto();
		statusCountDto.setPurchased(purchaseInvoiceRepository.getAllPurchasedDataCountShipping());
		statusCountDto.setPurchasedConfirm(stockRepository.countByStatusPurchaseConfirmedAndSold());
		
		Integer transportRequestedCount = transportOrderItemRepository.findCountOfTransportRequested();
		Integer transportConfirmedCount = transportOrderItemRepository.findCountOfTransportConfirmed();
		
		statusCountDto.setInTransit(transportRequestedCount + " / " + transportConfirmedCount);
		
		Integer inspectionAvailableCount = inspectionOrderRequestRepository.findCountOfInspectionAvailable();
		Integer inspectionRequestedCount = inspectionOrderRequestRepository.findCountOfInspectionRequested();

		statusCountDto.setInspection(inspectionAvailableCount + " / " + inspectionRequestedCount);
		statusCountDto.setShippingRoro(
				shippingRequestRepository.getCountByShipmentType(Constants.SHIPPING_INVOICE_TYPE_RORO));
		statusCountDto.setShippingContainer(
				shippingRequestRepository.getCountByShipmentType(Constants.SHIPPING_INVOICE_TYPE_CONTAINER));
		statusCountDto.setShippingStatus(shippingRequestRepository.findAllShippingAcceptedStockCount());
		return new ResponseEntity<>(new Response("success", statusCountDto), HttpStatus.OK);
	}

	@GetMapping("/data/accounts/payment-booking-count")
	public ResponseEntity<Response> getPaymentBookingDashboardCount() {
		final Map<String, Long> info = new HashMap<>();
		// booking data
		info.put("auction", purchaseInvoiceRepository.getAllPurchasedDataCountAccounts());
		info.put("transport", tTransportInvoiceRepository.getCountTransportData(Constants.PAYMENT_NOT_APPROVED));

		// common for this
		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.PAYMENT_NOT_APPROVED);
		info.put("storage", tFwdrInvoiceRepository.getCountStorageData(paymentApprovedStatus));
		info.put("freight", tFreightShippingInvoiceRepository.getCountStorageData(paymentApprovedStatus));

		final List<Integer> invoiceStatus = Arrays.asList(Constants.INV_STATUS_VERIFIED);

		info.put("auctionApproval",
				purchaseInvoiceRepository.getApprovalCountAuctionData(invoiceStatus, paymentApprovedStatus));
		info.put("transportApproval", tTransportInvoiceRepository.getApprovalCountTransportData());
		info.put("others", tInvoiceRepository.getCountOthersData(paymentApprovedStatus));

		info.put("auctionFreeze", purchaseInvoiceRepository.getCountAuctionData(Constants.PAYMENT_FREEZE));
		info.put("transportFreeze", tTransportInvoiceRepository.getCountTransportData(Constants.PAYMENT_FREEZE));
		// info.put("othersFreeze",
		// tInvoiceRepository.getCountOthersData(Constants.PAYMENT_FREEZE));
		// info.put("storageFreeze",
		// tFwdrInvoiceRepository.getCountStorageData(Constants.PAYMENT_FREEZE));
		// info.put("freightFreeze",
		// tFreightShippingInvoiceRepository.getCountStorageData(Constants.PAYMENT_FREEZE));
		info.put("paymentAdvance", advancePaymentRepository.getCountOnAdvanceBooking(Constants.PAYMENT_NOT_APPROVED));

		return new ResponseEntity<>(new Response("success", info), HttpStatus.OK);
	}

	@GetMapping("/data/accounts/payment/approval/payment-booking-count")
	public ResponseEntity<Response> getPaymentApprovalBookingDashboardCount() {
		final Map<String, Long> info = new HashMap<>();

		info.put("auctionApproval", purchaseInvoiceRepository.getPaymentApprovalCountAuctionData());
		info.put("transportApproval", tTransportInvoiceRepository.getPaymentApprovalCountTransportData());
		info.put("othersApproval", purchaseInvoiceRepository.getPaymentApprovalCountOthersData());

		final List<Integer> paymentApprovedStatusStorage = Arrays.asList(Constants.PAYMENT_COMPLETED,
				Constants.PAYMENT_PARTIAL, Constants.PAYMENT_CANCELED);

		info.put("storageApproval", tFwdrInvoiceRepository.getCountStorageData(paymentApprovedStatusStorage));

		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.PAYMENT_COMPLETED,
				Constants.PAYMENT_CANCELED);

		info.put("freightApproval", tFreightShippingInvoiceRepository.getCountStorageData(paymentApprovedStatus));

		return new ResponseEntity<>(new Response("success", info), HttpStatus.OK);
	}

	@GetMapping("/data/accounts/freeze/payment-booking-count")
	public ResponseEntity<Response> getFreezedBookingDashboardCount() {
		final Map<String, Long> info = new HashMap<>();

		info.put("auctionFreezed", purchaseInvoiceRepository.getFreezedCountAuctionData());

		final List<Integer> paymentApprovedStatus = Arrays.asList(Constants.PAYMENT_FREEZE);
		info.put("transportFreezed", tTransportInvoiceRepository.getFreezedCountTransportData(paymentApprovedStatus));
		info.put("freightFreezed", tFreightShippingInvoiceRepository.getCountStorageData(paymentApprovedStatus));
		info.put("othersFreezed", tInvoiceRepository.getCountOthersData(paymentApprovedStatus));
		info.put("storageFreezed", tFwdrInvoiceRepository.getCountStorageData(paymentApprovedStatus));

		return new ResponseEntity<>(new Response("success", info), HttpStatus.OK);
	}

	@GetMapping("/data/accounts/claim-count")
	public ResponseEntity<Response> getClaimDashboardCount() {
		final Map<String, Long> info = new HashMap<>();
		info.put("recycle",
				purchaseInvoiceRepository.countByRecycleClaimStatus(Constants.TPURCHASEINVOICE_RECYCLE_NOT_CLAIMED));
		info.put("purchaseTax", purchaseInvoiceRepository
				.countByPurchaseTaxClaimStatus(Constants.TPURCHASEINVOICE_PURCHASETAX_NOT_CLAIMED));
		info.put("commissionTax", purchaseInvoiceRepository
				.countByCommisionTaxClaimStatus(Constants.TPURCHASEINVOICE_COMMISSIONTAX_NOT_CLAIMED));
		info.put("carTax",
				purchaseInvoiceRepository.countByCarTaxClaimStatus(Constants.TPURCHASEINVOICE_CARTAX_NOT_CLAIMED));
		info.put("insurance", insuranceRepository.count());

		return new ResponseEntity<>(new Response("success", info), HttpStatus.OK);
	}

	@GetMapping("/data/reauction-cancel-dashboard/status-count")
	public ResponseEntity<Response> getReauctionCancelDashboardCount() {
		final DocumentStatusCountDto documentStatusCountDto = new DocumentStatusCountDto();
		documentStatusCountDto.setCancelStock(purchaseInvoiceRepository.countByStatus(Constants.INV_STATUS_CANCEL));
		documentStatusCountDto.setReAuction(tReAuctionRepository.count());
		return new ResponseEntity<>(new Response("success", documentStatusCountDto), HttpStatus.OK);
	}

	@GetMapping("/data/sales-dashboard/status-count")
	public ResponseEntity<Response> getSalesDashboardCount() {
		// get sales person id and sales person under logged in sales person
		MLoginDto loginDto = this.securityService.findLoggedInUser();
		List<String> salesPersonIds = new ArrayList<>();
		salesPersonIds.add(loginDto.getUserId());

		final Map<String, Long> info = new HashMap<>();
		info.put("inquiry", inquiryRepository.getCountBySalesPersonId(salesPersonIds));
		info.put("porforma", tproformaInvoiceRepository.getCountBySalesPersonId(salesPersonIds));
		info.put("reserved", stockRepository.findByReserveStockCount(salesPersonIds));
		info.put("shipping", shippingInstructionRepository.findByShippingInstructionCount(salesPersonIds));
		info.put("salesorder", tsalesInvoiceRepository.getListWithCustomerDetailsCount(salesPersonIds));
		info.put("status", shippingInstructionRepository.findByShippingInstructionStatusCount(salesPersonIds));
		return new ResponseEntity<>(new Response("success", info), HttpStatus.OK);
	}

	@GetMapping("/data/invoice-management/status-count")
	public ResponseEntity<Response> getInvoiceMangementCount() {
		// get sales person id and sales person under logged in sales person
		final List<String> salesPersonIds = loginService.getSalesPersonIdsByHierarchyLevel();

		final Map<String, Long> info = new HashMap<>();

		info.put("porforma", tproformaInvoiceRepository.getCountBySalesPersonId(salesPersonIds));

		info.put("salesorder", tsalesInvoiceRepository.getListWithCustomerDetailsCount(salesPersonIds));

		return new ResponseEntity<>(new Response("success", info), HttpStatus.OK);
	}

	@GetMapping("/data/documents-dashboard/data-count")
	public ResponseEntity<Response> getDocumentsDashboardCount() {
		final Map<String, Long> info = new HashMap<>();
		info.put("notReceived", stockRepository.findAllByDocumentStatusCount());
		info.put("received", documentReceivedRepository.findAllByDocumentStatusReceivedCount());
		info.put("receivedRikuji", documentReceivedRepository.findAllByDocumentStatusReceivedRikujiCount());
		info.put("exportCertificate", tDocumentConversionRepository.findAllByExportCertificateCount());
		info.put("nameTransfer", tDocumentConversionRepository
				.countByDocConvertTo(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_NAME_TRANSFER));
		info.put("domestic",
				tDocumentConversionRepository.countByDocConvertTo(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_DOMESTIC));
		info.put("parts",
				tDocumentConversionRepository.countByDocConvertTo(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_PARTS));
		info.put("shuppin",
				tDocumentConversionRepository.countByDocConvertTo(Constants.STOCK_DOCUMENT_TYPE_CONVERT_TO_SHUPPIN));
		return new ResponseEntity<>(new Response("success", info), HttpStatus.OK);
	}

	@GetMapping("/data/documents-dashboard/recycle-data-count")
	public ResponseEntity<Response> getDocumentsRecycleDashboardCount() {
		final Map<String, Long> info = new HashMap<>();
		info.put("recycleClaim",
				purchaseInvoiceRepository.countByRecycleClaimStatus(Constants.TPURCHASEINVOICE_RECYCLE_CLAIMED));
		info.put("recycleReceived",
				purchaseInvoiceRepository.countByRecycleClaimStatus(Constants.TPURCHASEINVOICE_RECYCLE_RECEIVED));
		return new ResponseEntity<>(new Response("success", info), HttpStatus.OK);
	}

	@GetMapping("/stock/stockNo-search")
	public ResponseEntity<Response> searchCustomer(@RequestParam("search") String search) {
		final List<StockSearchDto> stock = stockRepository.findBySearchDto(search);
		return new ResponseEntity<>(new Response("success", stock), HttpStatus.OK);
	}

	@GetMapping("/stock/shipping/available/search")
	public ResponseEntity<Response> stockShippingAvailableSearch(@RequestParam("search") String search) {
		final List<StockSearchDto> stock = stockRepository.findShippingAvailableStock(search);
		return new ResponseEntity<>(new Response("success", stock), HttpStatus.OK);
	}

	@GetMapping("/stock/cancelled/search")
	public ResponseEntity<Response> searchCustomer(@RequestParam("search") String search,
			@RequestParam("invoiceDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date purchaseDate) {
		if (AppUtil.isObjectEmpty(purchaseDate)) {
			return new ResponseEntity<>(new Response("success", new ArrayList<>()), HttpStatus.OK);
		}
		final List<StockSearchDto> stock = stockRepository.findCancelledStock(search, purchaseDate);
		return new ResponseEntity<>(new Response("success", stock), HttpStatus.OK);
	}

	@GetMapping("/stock/cancelled/search/account-inspection")
	public ResponseEntity<Response> searchStockForReInspection(@RequestParam("search") String search) {
		return new ResponseEntity<>(
				new Response("success", inspectionOrderRequestCancelledRepository.searchStockForReInspection(search)),
				HttpStatus.OK);
	}

	@GetMapping("/stock/reauction/search")
	public ResponseEntity<Response> searchReauctionStock(@RequestParam("search") String search,
			@RequestParam("invoiceDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date purchaseDate) {
		if (AppUtil.isObjectEmpty(purchaseDate)) {
			return new ResponseEntity<>(new Response("success", new ArrayList<>()), HttpStatus.OK);
		}
		final List<StockSearchDto> stock = stockRepository.findReauctionStock(search, purchaseDate);
		return new ResponseEntity<>(new Response("success", stock), HttpStatus.OK);
	}

	@GetMapping("/stock/rikuso/search")
	public ResponseEntity<Response> searchRikusoStock(@RequestParam("search") String search) {
//		if (AppUtil.isObjectEmpty(auctionCompany) || AppUtil.isObjectEmpty(auctionHouse)) {
//			return new ResponseEntity<>(new Response("success", new ArrayList<>()), HttpStatus.OK);
//		}
		final List<StockSearchDto> stock = stockRepository.searchRikusoStock(search);
		return new ResponseEntity<>(new Response("success", stock), HttpStatus.OK);
	}

	@GetMapping("/stock/take-out/search")
	public ResponseEntity<Response> searchTakeOutStock(@RequestParam("search") String search,
			@RequestParam("auctionCompany") String auctionCompany, @RequestParam("auctionHouse") String auctionHouse) {
		if (AppUtil.isObjectEmpty(auctionCompany) || AppUtil.isObjectEmpty(auctionHouse)) {
			return new ResponseEntity<>(new Response("success", new ArrayList<>()), HttpStatus.OK);
		}
		final List<StockSearchDto> stock = stockRepository.searchTakeOutStock(search, auctionCompany, auctionHouse);
		return new ResponseEntity<>(new Response("success", stock), HttpStatus.OK);
	}

	@GetMapping("/purchased/stock/stockNo-search")
	public ResponseEntity<Response> searchPurchasedStock(@RequestParam("search") String search) {
		final List<Document> stock = purchaseInvoiceRepository.findBySearchDto(search);
		return new ResponseEntity<>(new Response("success", stock), HttpStatus.OK);
	}

	@GetMapping("/data/invoiceTypes.json")
	public List<MInvoiceType> findAllInvoiceTypes() {
		return mInvoiceTypeRepository.findAll();
	}

	@GetMapping("/data/byUserRole")
	public List<MUser> findUserByRole() {
		return userRepository.findAllByRole();
	}

	@GetMapping("/data/findAllUsers")
	public List<MUser> findAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/check/existing/transporter")
	public boolean findExistingTransporterFeeList(@RequestParam(value = "id", required = false) String id,
			@RequestParam String transporter, @RequestParam String from, @RequestParam String to,
			@RequestParam List<String> categories) {
		boolean isValid;

		if (id.isEmpty()) {
			isValid = ttransporterFeeRepository.isExist(transporter, from, to, categories, Constants.DELETE_FLAG_0);
		} else {
			isValid = ttransporterFeeRepository.isExist(id, transporter, from, to, categories, Constants.DELETE_FLAG_0);
			if (!isValid) {
				isValid = ttransporterFeeRepository.isExist(transporter, from, to, categories, Constants.DELETE_FLAG_0);
			} else {
				isValid = false;
			}

		}
		return isValid;

	}

	@GetMapping("/check/existing/customer")
	public boolean findExistingCustomerList(@RequestParam(value = "id", required = false) String id, String email) {
		boolean isValid;

		if (id.isEmpty()) {
			isValid = customerRepository.existsByEmail(email);
		} else {
			isValid = customerRepository.existsByIdAndEmail(id, email);
			if (!isValid) {
				isValid = customerRepository.existsByEmail(email);
			} else {
				isValid = false;
			}
		}
		return isValid;
	}

	@GetMapping("/data/auctionExteriorGrades.json")
	public List<MAuctionGradesExterior> findAllAuctionGrades() {
		return auctionGradesExteriorRepository.findAll();
	}

	@GetMapping("/data/auctionInteriorGrades.json")
	public List<MAuctionGradesInterior> findAllInteriorAuctionGrades() {
		return auctionGradesInteriorRepository.findAll();
	}

	@GetMapping("/check/existing/chassisNo")
	public boolean findExistingChassisNo(@RequestParam(value = "stockNo", required = false) String stockNo,
			@RequestParam String chassisNo) {

		boolean isValid;
		if (AppUtil.isObjectEmpty(stockNo)) {
			isValid = stockRepository.isExistsByChassisNo(chassisNo);
		} else {
			isValid = stockRepository.isExistsByStockNoAndChassisNo(stockNo, chassisNo);
			if (!isValid) {
				isValid = stockRepository.isExistsByChassisNo(chassisNo);
			} else {
				isValid = false;
			}

		}
		return isValid;
	}

	@GetMapping("/check/existing/stock")
	public boolean findExistingStock(@RequestParam(value = "stockNo", required = false) String stockNo,
			@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date purchaseDate, @RequestParam Long lotNo,
			@RequestParam String supplier, @RequestParam String auctionHouse) {
		boolean exists = false;
		TStock stock = null;
		if (AppUtil.isObjectEmpty(purchaseDate) || AppUtil.isObjectEmpty(lotNo) || AppUtil.isObjectEmpty(supplier)
				|| AppUtil.isObjectEmpty(auctionHouse)) {
			return false;
		}

		if (AppUtil.isObjectEmpty(stockNo)) {
			stock = stockRepository.findByLotNoAndPurchaseInfoDate(lotNo, AppUtil.atStartOfDay(purchaseDate),
					AppUtil.atEndOfDay(purchaseDate), supplier, new ObjectId(auctionHouse));
			if (!AppUtil.isObjectEmpty(stock)) {
				exists = true;
			}
		} else {
			stock = stockRepository.findByStockNoLotNoAndPurchaseInfoDate(stockNo, lotNo,
					AppUtil.atStartOfDay(purchaseDate), AppUtil.atEndOfDay(purchaseDate), supplier,
					new ObjectId(auctionHouse));
			if (AppUtil.isObjectEmpty(stock)) {
				stock = stockRepository.findByLotNoAndPurchaseInfoDate(lotNo, AppUtil.atStartOfDay(purchaseDate),
						AppUtil.atEndOfDay(purchaseDate), supplier, new ObjectId(auctionHouse));
				if (!AppUtil.isObjectEmpty(stock)) {
					exists = true;
				}
			}

		}
		return exists;
	}

	@GetMapping("/check/existing/invoiceNo")
	public boolean findExistingInvoiceNo(@RequestParam String invoiceNo) {

		boolean isValid;
		if (!(AppUtil.isObjectEmpty(invoiceNo))) {
			isValid = purchaseInvoiceRepository.isExistsByInvoiceNo(invoiceNo);
		} else {
			isValid = false;
		}
		return isValid;
	}

	@GetMapping("/check/existing/company")
	public boolean findExistingCompany(@RequestParam(value = "supplierCode", required = false) String supplierCode,
			@RequestParam String company) {
		// Constants.DELETE_FLAG_0
		boolean isValid;
		if (AppUtil.isObjectEmpty(supplierCode)) {
			isValid = masterSupplierRepository.isExistsByCompany(company, Constants.DELETE_FLAG_0);
		} else {
			isValid = masterSupplierRepository.isExistsBySupplierCodeAndCompany(supplierCode, company,
					Constants.DELETE_FLAG_0);
			if (!isValid) {
				isValid = masterSupplierRepository.isExistsByCompany(company, Constants.DELETE_FLAG_0);
			} else {
				isValid = false;
			}
		}
		return isValid;
	}

	@GetMapping("/supplier/credit/balance/{supplierCode}")
	public ResponseEntity<Response> creditBalance(@PathVariable("supplierCode") String supplierCode) {
		final Double transactionAmount = supplierTransactionRepository.getCreditBalanceAmount(supplierCode);
		final MSupplier supplier = masterSupplierRepository.findOneBySupplierCode(supplierCode);
		final Double creditLimit = supplier.getMaxCreditAmount();
		return new ResponseEntity<>(new Response("success", (creditLimit - transactionAmount)), HttpStatus.OK);
	}

	@GetMapping("/japan/find-port")
	public MCountryPort findPortByJapanCountry() {
		final String country = "JAPAN";
		return countryPortRepository.findOneByCountry(country);
	}

	@GetMapping("/data/user-details/by/{department}.json")
	public List<MUserInfoDto> findListByDepartment(@PathVariable("department") String department) {
		return userRepository.getListByDepartment(department);
	}

	@GetMapping("/data/plate-no-registrations.json")
	public List<MPlateNoRegistration> findAllPlateNos() {
		return plateNoRegistrationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	@GetMapping("/bank/currentBalance")
	public ResponseEntity<Response> bankCurrentBalanceByBankId(@RequestParam("bankId") String bankId,
			@RequestParam("currency") int currency) {
		int currencyType = -1;
		Double balance = 0.0;
		final MBank bank = masterBankRepository.findOneByBankSeq(bankId);
		if (currency == 1) {
			currencyType = Constants.CURRENCY_YEN;
			balance = bank.getYenBalance();
		}
		if (currencyType == -1) {
			return new ResponseEntity<>(new Response("success", -1), HttpStatus.OK);
		}

		return new ResponseEntity<>(new Response("success", balance), HttpStatus.OK);
	}

	@GetMapping("/bank/currentBankBalance")
	public ResponseEntity<Response> bankCurrentBankBalanceByBankId(@RequestParam("bankId") String bankId,
			@RequestParam("currency") int currency) {
		int currencyType = -1;
		Double openingBalance = 0.0;
		final MBank bank = masterBankRepository.findOneByBankSeq(bankId);
		if (currency == 1) {
			currencyType = Constants.CURRENCY_YEN;
			openingBalance = bank.getYenBalance();
		}
		if (currencyType == -1) {
			return new ResponseEntity<>(new Response("success", -1), HttpStatus.OK);
		}
		final Double transactionAmount = tBankTransactionRepository.bankCurrentBankBalanceByBankIdAndCurrency(bankId,
				currencyType);

		return new ResponseEntity<>(new Response("success", (openingBalance - transactionAmount)), HttpStatus.OK);
	}

	@GetMapping("/data/forwarderDetail.json")
	public MForwarderDetail findOneForwarderDetail(@RequestParam("frwdrId") String frwdrId,
			@RequestParam("orginPortFilter") String orginPortFilter,
			@RequestParam("destPortFilter") String destPortFilter) {
		return mForwarderDetailRepository.findOneByForwarderIdAndOrginPortAndDestPort(frwdrId, orginPortFilter,
				destPortFilter);
	}

	@GetMapping("/stock/price/details")
	public ResponseEntity<Response> getPrice(@RequestParam("stockNo") String stockNo) {
		final Map<String, Object> response = new HashMap<>();
		final List<TPurchaseInvoice> purchaseInvoice = purchaseInvoiceRepository.findByStockNoAndType(stockNo,
				Constants.PURCHASE_INVOICE_ITEM_TYPE_PURCHASE);
		final TStock stock = stockRepository.findOneByStockNo(stockNo);
		final List<TTransportInvoice> transportInvoice = tTransportInvoiceRepository.findAllByStockNo(stockNo);
		final MCurrency currency = currencyRepository.findOneByCurrencySeq(Constants.CURRENCY_USD);
		final Double transportationCharges = transportInvoice.stream().mapToDouble(TTransportInvoice::getInvoiceTotal)
				.sum();
		response.put("purchaseCost", purchaseInvoice.stream().mapToDouble(TPurchaseInvoice::getPurchaseCost).sum());
		response.put("purchaseCostTaxAmount", AppUtil
				.ifNull(purchaseInvoice.stream().mapToDouble(TPurchaseInvoice::getPurchaseCostTaxAmount).sum(), 0.0));
		response.put("commision",
				AppUtil.ifNull(purchaseInvoice.stream().mapToDouble(TPurchaseInvoice::getCommision).sum(), 0.0));
		response.put("commisionTaxAmount", AppUtil
				.ifNull(purchaseInvoice.stream().mapToDouble(TPurchaseInvoice::getCommisionTaxAmount).sum(), 0.0));
		response.put("roadTax",
				AppUtil.ifNull(purchaseInvoice.stream().mapToDouble(TPurchaseInvoice::getRoadTax).sum(), 0.0));
		response.put("recycle",
				AppUtil.ifNull(purchaseInvoice.stream().mapToDouble(TPurchaseInvoice::getRecycle).sum(), 0.0));
		response.put("otherCharges",
				AppUtil.ifNull(purchaseInvoice.stream().mapToDouble(TPurchaseInvoice::getOtherCharges).sum(), 0.0));
		response.put("transportationCharges", AppUtil.ifNull(transportationCharges, 0.0));
		response.put("offerPrice", stock.getOfferPrice());
		response.put("minSellingPriceInYen", stock.getFob());
		response.put("minSellingPriceInDollar", stock.getMinSellingPriceInDollar());
		response.put("shippingCharge", stock.getShippingCharge());
		response.put("inspectionCharge", stock.getInspectionCharge());
		response.put("radiationCharge", stock.getRadiationCharge());
		response.put("freightPerM3", stock.getFreightPerM3());
		response.put("freightCharge", stock.getFreightCharge());
		response.put("courierCharge", stock.getCourierCharge());
		response.put("profit", stock.getProfit());
		response.put("excludingProfit", stock.getExcludingProfit());
		response.put("stockOtherCharge", stock.getOtherCharge());
		response.put("stockTransportCharge", stock.getTransportCharge());
		response.put("usdExchangeRate", currency.getSalesExchangeRate());
		return new ResponseEntity<>(new Response("success", response), HttpStatus.OK);
	}

	@GetMapping("/currency/exchangeRate")
	public ResponseEntity<Response> getExchangeRate(@RequestParam("currency") Integer currency) {
		final MCurrency mCurrency = currencyRepository.findOneByCurrencySeq(currency);
		return new ResponseEntity<>(new Response("success", mCurrency.getExchangeRate()), HttpStatus.OK);
	}

	@GetMapping("/data/bankOnCurrencyType.json")
	public List<MBank> findAllByCurrencyType(@RequestParam Integer currencyType) {
		return masterBankRepository.findAllByCurrencyType(currencyType);
	}

	@GetMapping("/data/general-ledger.json")
	public List<MGeneralLedger> findAllGeneralLedger() {

		return generalLedgerRepository.findAll();
	}

	@GetMapping("/data/loanBank.json")
	public List<MBank> findAllLoanBanks() {
		return masterBankRepository.findAllByAccountType(Constants.TYPE_LOAN_ACCOUNT);
	}

	@GetMapping("/data/general/suppliers.json")
	public List<MGeneralSupplier> findAllGGeneralSuppliers() {
		return masterGeneralSupplierRepository.findAll();
	}

	@GetMapping("/data/sales-home/monthwise-data")
	public ResponseEntity<Response> getMonthWiseSalesData() {
		final Map<String, Integer> monthwiseData = stockRepository.getMonthWiseSalesData();
		return new ResponseEntity<>(new Response("success", monthwiseData), HttpStatus.OK);
	}

	@GetMapping("/data/sales-home/yearly-total")
	public ResponseEntity<Response> getYearlySalesData() {
		final Integer yearlyData = stockRepository.getYearlySalesTotal();
		return new ResponseEntity<>(new Response("success", yearlyData), HttpStatus.OK);
	}

	@GetMapping("/data/sales-home/top-customer-data")
	public ResponseEntity<Response> getTopCustomerData() {
		// get sales person id and sales person under logged in sales person
		final Map<String, Integer> customerData = stockRepository.getTopCustomerData();
		return new ResponseEntity<>(new Response("success", customerData), HttpStatus.OK);
	}

	@GetMapping("/data/sales-home/top-model-data")
	public ResponseEntity<Response> getTopModelData() {
		// get sales person id and sales person under logged in sales person
		final Map<String, Integer> modelData = stockRepository.getTopModelData();
		return new ResponseEntity<>(new Response("success", modelData), HttpStatus.OK);
	}

	@GetMapping("/data/ship/charge.json")
	public DatatableResponse findAllShipCharge() {
		final List<MShippingCharge> data = mShipChargeRepo.findAll();
		return new DatatableResponse(data);
	}

	@GetMapping("/data/customfields.json")
	public List<MCustomListFields> findAllCustomFields() {
		MLoginDto loggedInUser = securityService.findLoggedInUser();
		Integer access = AppUtil.getDeptIdByDept(loggedInUser.getDepartment());
		return customFields.findAllByAccess(access);
	}

	@GetMapping("/data/cartaxclaimed")
	public List<DayBookListDto> findAllCarTaxClaimedVahicles() {
		return tDayBookTransactionRepository.findCarTaxClaimedList();
	}

	@GetMapping("/data/extra-equipments.json")
	public List<MExtraEquipments> findAllExtraEquipments() {
		return extraEquipmentsRepository.findAll();
	}

	@GetMapping("/data/crane-cut.json")
	public List<MCraneCut> findAllCraneCut() {
		return craneCutRepository.findAll();
	}

	@GetMapping("/data/crane-type.json")
	public List<MCraneType> findAllCraneType() {
		return craneTypeRepository.findAll();
	}

	@GetMapping("/data/exel.json")
	public List<MExel> findAllExel() {
		return exelRepository.findAll();
	}

	@GetMapping("/data/tankKiloLitre.json")
	public List<MFuelTankKiloLitre> findAllFuelTankKiloLitre() {
		return fuelTankKiloLitreRepository.findAll();
	}

	@GetMapping("/data/tyre-size.json")
	public List<MTyreSize> findAllTyreSize() {
		return tyreSizeRepository.findAll();
	}

	@GetMapping("/data/transport-category.json")
	public List<MTransportCategory> findAllTransportCategory() {
		return mTransportCategoryRepository.findAll();
	}

	@GetMapping("/data/blData.json")
	public List<TShippingRequest> findAllBlNo() {
		return shippingRequestRepository.findAllBlNo();
	}

	@GetMapping("/transport/invoice/status")
	public ResponseEntity<Integer> getTransportInvoiceStatus(@RequestParam("orderId") String orderId,
			@RequestParam("stockNo") String stockNo) {
		TTransportInvoice invoice = tTransportInvoiceRepository.findOneByOrderIdAndStockNo(orderId, stockNo);
		return new ResponseEntity<>(invoice.getStatus(), HttpStatus.OK);
	}
}