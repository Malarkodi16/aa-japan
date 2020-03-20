package com.nexware.aajapan.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TNotification;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TCustomerRepository;
import com.nexware.aajapan.services.MLoginService;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.TNotificationService;
import com.nexware.aajapan.utils.AppUtil;

@Controller

@RequestMapping("customer")
public class CustomerController {

	@Autowired
	private TCustomerRepository customerRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TNotificationService notificationService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private MLoginService loginService;

	@GetMapping(value = { "/create" })
	public ModelAndView createCustomer(HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sales.createcustomer");
		return modelAndView;
	}

	@GetMapping(value = { "/{custId}" })
	public ModelAndView editCustomer(@PathVariable("custId") String custId, ModelAndView modelAndView) {
		modelAndView.addObject("custId", custId);
		modelAndView.setViewName("sales.createcustomer");
		return modelAndView;
	}

	@GetMapping(value = { "/transaction/{custId}" })
	public ModelAndView customerTansaction(@PathVariable("custId") String custId, ModelAndView modelAndView) {
		modelAndView.addObject("custId", custId);
		modelAndView.setViewName("customer.tansaction");
		return modelAndView;
	}

	@PostMapping(value = { "/save-customer" })
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> saveCustomer(@RequestBody TCustomer tCustomer,
			RedirectAttributes redirectAttributes, ModelAndView modelAndView) {
		// notification for duplicate customer entry

		final List<TCustomer> existing = customerRepository.findByMobileNoAndCity(tCustomer.getMobileNo(),
				tCustomer.getCity());
		if (existing != null && !existing.isEmpty()) {
			final List<TNotification> notifications = notificationService.notifyDupliateCustomerToAdmin(tCustomer);
			notificationService.notify(notifications, "admin@aaj.com");
		}

		// Ensure New or existing

		if (AppUtil.isObjectEmpty(tCustomer.getId())) {
			final MLoginDto logged = securityService.findLoggedInUser();
			tCustomer.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_CSTMR));
			tCustomer.setId(null);
			tCustomer.setBalance(0.0);
			tCustomer.setDepositAmount(0.0);
			tCustomer.setAdvanceAmount(0.0);
			tCustomer.setApproveCustomerflag(Constants.CUSTOMER_APPROVED_FLAG);// for initial test its approved, will
																				// be changed when goes to live
			tCustomer.setSalesPerson(logged.getUserId());
			tCustomer.getConsigneeNotifyparties().forEach(consignee -> consignee.setId(ObjectId.get().toString()));

			customerRepository.save(tCustomer);

		} else {
			TCustomer customerUpdate = customerRepository.findOneByid(tCustomer.getId());
			customerUpdate.setFirstName(tCustomer.getFirstName());
			customerUpdate.setLastName(tCustomer.getLastName());
			customerUpdate.setNickName(tCustomer.getNickName());
			customerUpdate.setEmail(tCustomer.getEmail());
			customerUpdate.setSkypeId(tCustomer.getSkypeId());
			customerUpdate.setMobileNo(tCustomer.getMobileNo());
			customerUpdate.setCompanyName(tCustomer.getCompanyName());
			customerUpdate.setAddress(tCustomer.getAddress());
			customerUpdate.setCity(tCustomer.getCity());
			customerUpdate.setCountry(tCustomer.getCountry());
			customerUpdate.setPort(tCustomer.getPort());
			customerUpdate.setComments(tCustomer.getComments());
			customerUpdate.setLcCustomer(tCustomer.isLcCustomer());
			customerUpdate.setBank(tCustomer.getBank());
			customerUpdate.setAccountNo(tCustomer.getAccountNo());
			customerUpdate.setCurrencyType(tCustomer.getCurrencyType());
			customerUpdate.setPaymentType(tCustomer.getPaymentType());
			customerUpdate.setCreditBalance(tCustomer.getCreditBalance());
			customerUpdate.setCheckCreditLimit(tCustomer.getCheckCreditLimit());
			customerUpdate.setFlag(tCustomer.getFlag());
			customerUpdate.setYard(tCustomer.getYard());
			customerRepository.save(customerUpdate);
			// update consignee
			final BulkOperations ops = mongoTemplate.bulkOps(BulkMode.UNORDERED, TCustomer.class);
			tCustomer.getConsigneeNotifyparties().forEach(consignee -> {
				Query query;
				Update updateConsignee;
				if (!AppUtil.isObjectEmpty(consignee.getId())) {
					query = new Query(new Criteria().andOperator(Criteria.where("id").is(tCustomer.getId()), Criteria
							.where("consigneeNotifyparties").elemMatch(Criteria.where("id").is(consignee.getId()))));
					updateConsignee = new Update().set("consigneeNotifyparties.$.cFirstName", consignee.getcFirstName())
							.set("consigneeNotifyparties.$.cLastName", consignee.getcLastName())
							.set("consigneeNotifyparties.$.cAddress", consignee.getcAddress())
							.set("consigneeNotifyparties.$.npFirstName", consignee.getNpFirstName())
							.set("consigneeNotifyparties.$.npLastName", consignee.getNpLastName())
							.set("consigneeNotifyparties.$.npAddress", consignee.getNpAddress())
							.set("consigneeNotifyparties.$.deleteFlag", consignee.getDeleteFlag());

				} else {
					query = new Query(Criteria.where("id").is(tCustomer.getId()));
					consignee.setId(ObjectId.get().toString());
					updateConsignee = new Update().push("consigneeNotifyparties", consignee);
				}
				ops.updateOne(query, updateConsignee);
			});
			ops.execute();
		}

		redirectAttributes.addFlashAttribute("message", "Customer saved successfully!");

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@GetMapping(value = { "/list" })
	public ModelAndView customerList(HttpServletRequest request, @RequestParam("mobileNo") Optional<String> mobileNo,
			@RequestParam("city") Optional<String> city) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("city", city.isPresent() ? city.get() : "");
		modelAndView.addObject("mobileNo", mobileNo.isPresent() ? mobileNo.get() : "");
		modelAndView.setViewName("sales.customerlist");

		return modelAndView;
	}

	@GetMapping("search/all")
	public ResponseEntity<Response> searchCustomer(@RequestParam("search") String search) {

		return new ResponseEntity<>(new Response("success", customerRepository.findBySearchTerms(search)),
				HttpStatus.OK);
	}

	@GetMapping("search")
	public ResponseEntity<Response> searchCustomer(@RequestParam("search") String search,
			@RequestParam("flag") String flag) {
		Integer customerFlag = -1;

		if (flag.equalsIgnoreCase("branch")) {
			customerFlag = Constants.CUSTOMER_FLAG_BRANCH;
		} else if (flag.equalsIgnoreCase("customer")) {
			customerFlag = Constants.CUSTOMER_FLAG_CUSTOMER;
		}
		// get sales person id and sales person under logged in sales person
		final List<String> salesPersonIds = loginService.getSalesPersonIdsByHierarchyLevel();
		final List<TCustomer> customer = customerRepository.findBySearchTermsAndSalesPerson(search, customerFlag,
				salesPersonIds);
		return new ResponseEntity<>(new Response("success", customer), HttpStatus.OK);
	}
	
	@GetMapping("searchWtSalesPerson")
	public ResponseEntity<Response> searchCustomerByFlag(@RequestParam("search") String search,
			@RequestParam("flag") String flag) {
		Integer customerFlag = -1;

		if (flag.equalsIgnoreCase("branch")) {
			customerFlag = Constants.CUSTOMER_FLAG_BRANCH;
		} else if (flag.equalsIgnoreCase("customer")) {
			customerFlag = Constants.CUSTOMER_FLAG_CUSTOMER;
		}

		final List<TCustomer> customer = customerRepository.findBySearchTermsAndFlag(search, customerFlag);
		return new ResponseEntity<>(new Response("success", customer), HttpStatus.OK);
	}

	@GetMapping("admin/search")
	public ResponseEntity<Response> adminSearchCustomer(@RequestParam("search") String search) {

		// get sales person id and sales person under logged in sales person
		final List<String> salesPersonIds = loginService.getSalesPersonIdsByHierarchyLevel();
		final List<TCustomer> customer = customerRepository.findByAdminSearchTermsAndSalesPerson(search,
				salesPersonIds);
		return new ResponseEntity<>(new Response("success", customer), HttpStatus.OK);
	}

	@GetMapping("getCustomer")
	public ResponseEntity<Response> getAllCustomer(@RequestParam("search") String search,
			@RequestParam("flag") String flag) {
		// get sales person id and sales person under logged in sales person
		final List<TCustomer> customer = customerRepository.findBySearchTerms(search);
		return new ResponseEntity<>(new Response("success", customer), HttpStatus.OK);
	}

	@GetMapping("getCustomerNotApproved")
	public ResponseEntity<Response> getAllNotApprovedCustomer(@RequestParam("search") String search) {
		// get sales person id and sales person under logged in sales person
		final List<TCustomer> customer = customerRepository.findBySearchTerms(search);
		return new ResponseEntity<>(new Response("success", customer), HttpStatus.OK);
	}

	@GetMapping("getApprovedCustomer")
	public ResponseEntity<Response> getAllApprovedCustomer(@RequestParam("search") String search) {
		// get sales person id and sales person under logged in sales person
		final List<TCustomer> customer = customerRepository.findBySearchTerms(search);
		return new ResponseEntity<>(new Response("success", customer), HttpStatus.OK);
	}

	@GetMapping("getLcCustomer")
	public ResponseEntity<Response> getLcCustomer(@RequestParam("search") String search,
			@RequestParam("flag") String flag) {
		// get sales person id and sales person under logged in sales person
		final List<TCustomer> customer = customerRepository.findLcCustomerBySearchTerms(search);
		return new ResponseEntity<>(new Response("success", customer), HttpStatus.OK);
	}

	@GetMapping("shipping/search")
	public ResponseEntity<Response> searchShippingCustomer(@RequestParam("search") String search,
			@RequestParam("salesName") String salesName) {
		final List<TCustomer> customer = customerRepository.findBySearchTermsAndSalesPersonName(search, salesName);
		return new ResponseEntity<>(new Response("success", customer), HttpStatus.OK);
	}

	@GetMapping("data/{code}")
	public ResponseEntity<Response> getCustomerById(@PathVariable String code) {
		final TCustomer customer = customerRepository.findOneByCode(code);
		return new ResponseEntity<>(new Response("success", customer), HttpStatus.OK);
	}

}
