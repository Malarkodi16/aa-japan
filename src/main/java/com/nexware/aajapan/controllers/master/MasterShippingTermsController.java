package com.nexware.aajapan.controllers.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MShippingTerms;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MShippingTermsRepository;
import com.nexware.aajapan.services.SequenceService;

@Controller
@RequestMapping("/accounts/master/shippingTerms")
public class MasterShippingTermsController {

	@Autowired
	private MShippingTermsRepository shippingTermsRepository;

	@Autowired
	private SequenceService sequenceService;
	
	@GetMapping("/list")
	public ModelAndView listShippingTermsPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.shippingTerms.list");
		return modelAndView;
	}
	
	@GetMapping("/list-data")

	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.shippingTermsRepository.getAllUnDeletedShippingTerms());
	}
	
	@PostMapping("/save")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> create(@RequestBody MShippingTerms shippingTerms) {
		boolean isNewEntry = (shippingTerms.getTermsId() == null
				|| shippingTerms.getTermsId().isEmpty());
		MShippingTerms shippingTermsToSave = null;
		if (isNewEntry) {
			shippingTermsToSave = shippingTerms;
			shippingTermsToSave
					.setTermsId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SHIPPING_TERMS));
			shippingTermsToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
		} else {
			shippingTermsToSave = this.shippingTermsRepository
					.findOneByTermsId(shippingTerms.getTermsId());
			shippingTermsToSave.setName(shippingTerms.getName());
			shippingTermsToSave.setShippingTerms(shippingTerms.getShippingTerms());
			
		}

		this.shippingTermsRepository.save(shippingTermsToSave);
		return new ResponseEntity<>(new Response("success", shippingTermsToSave), HttpStatus.OK);
	}

	@GetMapping("/delete/{termsId}")
	public ResponseEntity<Response> deleteshippingTerms(@PathVariable("termsId") String termsId,
			ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
		MShippingTerms shippingTerms = this.shippingTermsRepository.findOneByTermsId(termsId);
		shippingTerms.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.shippingTermsRepository.save(shippingTerms);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
}
