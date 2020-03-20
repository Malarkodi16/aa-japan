package com.nexware.aajapan.controllers.master;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.MCOA;
import com.nexware.aajapan.models.MForeignBank;
import com.nexware.aajapan.models.MForwarder;
import com.nexware.aajapan.models.MVechicleMaker;
import com.nexware.aajapan.models.Model;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MCOARepository;
import com.nexware.aajapan.repositories.MForeignBankRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/master")
public class MasterBankController {

	@Autowired
	private MasterBankRepository masterBankRepository;

	@Autowired
	private MForeignBankRepository masterFBankRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private MCOARepository mCoaRepo;

	@GetMapping("/list-bank")
	public ModelAndView listBank(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.masterbank.list");

		return modelAndView;
	}

	// Added by Yogeshwar
	@GetMapping("/foreign-bank-list")
	public ModelAndView listForeignBank(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.masterforeignbank.list");

		return modelAndView;
	}

	@GetMapping("/create-foreign-bank")
	public ModelAndView createForeignBank(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.masterforeignbank.create");

		return modelAndView;
	}

	@GetMapping("/list-foreignbank-data")
	@ResponseBody
	public DatatableResponse getForeignListBankData() {
		return new DatatableResponse(this.masterFBankRepository.getAllUnDeletedForeignBank());
	}

	// Added by Yogeshwar

	@GetMapping("/create-bank")
	public ModelAndView createBank(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.masterbank.create");

		return modelAndView;
	}

	@GetMapping("/list-bank-data")
	@ResponseBody
	public DatatableResponse getListBankData() {
		return new DatatableResponse(this.masterBankRepository.findAllByAccountType());
	}

	@GetMapping("/list-bank-data-source")
	@ResponseBody
	public DatatableResponse getListBankDataByAccountType() {
		return new DatatableResponse(this.masterBankRepository.findAllByAccountTypeBank());
	}

	@GetMapping("/dash-board/bank.json")
	@ResponseBody
	public List<MBank> findAllBanks() {
		return this.masterBankRepository.findAllByAccountType(Constants.TYPE_FOREIGN_BANK_ACCOUNT);
	}

	// save-bank
	@PostMapping("/save-bank")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> saveBank(@RequestBody MBank mBank, RedirectAttributes redirectAttributes,
			ModelAndView modelAndView) {
		boolean isNewEntry = AppUtil.isObjectEmpty(mBank.getBankSeq());
		MBank mBankToSave = null;
		if (isNewEntry) {
			mBankToSave = mBank;
			mBankToSave.setAccountType(Constants.TYPE_FOREIGN_BANK_ACCOUNT);
			mBankToSave.setBankSeq(this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_BNK));
		} else {
			mBankToSave = this.masterBankRepository.findOneByBankSeq(mBank.getBankSeq());
		}
		MCOA subAccount = this.mCoaRepo.findByCode(mBank.getCoaCode());
		subAccount.setStatus(Constants.COA_STATUS_TYPE_OLD);
		this.mCoaRepo.save(subAccount);
		this.masterBankRepository.save(mBankToSave);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	// Added by Yogeshwar
	@PostMapping("/save-foreign-bank")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> saveForeignBank(@RequestBody MForeignBank mFBank) {
		boolean isNewEntry = AppUtil.isObjectEmpty(mFBank.getBankId());
		MForeignBank mBankToSave = null;
		if (isNewEntry) {
			mBankToSave = mFBank;
			mBankToSave.setBankId(this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_FOREIGN_BNK));
			mBankToSave.setBank(mFBank.getBank());
			mBankToSave.setCountry(mFBank.getCountry());
			mBankToSave.setBeneficiaryCertify(mFBank.getBeneficiaryCertify());
			mBankToSave.setLicenseDoc(mFBank.getLicenseDoc());
			mBankToSave.setDeleteFlag(Constants.DELETE_FLAG_0);

		} else {
			mBankToSave = this.masterFBankRepository.findOneByBankId(mFBank.getBankId());
		}

		this.masterFBankRepository.save(mBankToSave);

		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
	// Added by Yogeshwar

	@PutMapping("/edit-bank")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> editBank(@RequestBody Map<String, Object> data) {

		final String bankId = data.get("bankId").toString();

		MForeignBank mBankToEdit = this.masterFBankRepository.findOneByBankId(bankId);
		mBankToEdit.setBank(data.get("bank").toString());
		mBankToEdit.setCountry(data.get("country").toString());
		mBankToEdit.setBeneficiaryCertify(data.get("beneficiaryCertify").toString());
		mBankToEdit.setLicenseDoc(data.get("licenseDoc").toString());
		this.masterFBankRepository.save(mBankToEdit);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
	
	@GetMapping("/bank/delete/{bankId}")
	public ResponseEntity<Response> deleteSupplier(@PathVariable("bankId") String bankId, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		MForeignBank mForeignBank = this.masterFBankRepository.findOneByBankId(bankId);
		mForeignBank.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.masterFBankRepository.save(mForeignBank);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

}
