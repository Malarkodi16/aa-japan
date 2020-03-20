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
import com.nexware.aajapan.models.MShippingMarks;
import com.nexware.aajapan.models.MShippingTerms;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MShippingMarksRepository;
import com.nexware.aajapan.services.SequenceService;

@Controller
@RequestMapping("/accounts/master/shippingMarks")
public class MasterShippingMarksController {

	@Autowired
	private MShippingMarksRepository shippingMarksRepository;

	@Autowired
	private SequenceService sequenceService;
	
	@GetMapping("/list")
	public ModelAndView listShippingMarksPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.master.shippingMarks.list");
		return modelAndView;
	}
	
	@GetMapping("/list-data")

	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.shippingMarksRepository.getAllUnDeletedShippingMarks());
	}
	
	@PostMapping("/save")
	@ResponseBody
	@Transactional
	public ResponseEntity<Response> create(@RequestBody MShippingMarks shippingMarks) {
		boolean isNewEntry = (shippingMarks.getMarksId() == null
				|| shippingMarks.getMarksId().isEmpty());
		MShippingMarks shippingMarksToSave = null;
		if (isNewEntry) {
			shippingMarksToSave = shippingMarks;
			shippingMarksToSave
					.setMarksId(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SHIPPING_MARKS));
			shippingMarksToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
		} else {
			shippingMarksToSave = this.shippingMarksRepository
					.findOneByMarksId(shippingMarks.getMarksId());
			shippingMarksToSave.setName(shippingMarks.getName());
			shippingMarksToSave.setShippingMarks(shippingMarks.getShippingMarks());
			
		}

		this.shippingMarksRepository.save(shippingMarksToSave);
		return new ResponseEntity<>(new Response("success", shippingMarksToSave), HttpStatus.OK);
	}

	@GetMapping("/delete/{marksId}")
	public ResponseEntity<Response> deleteshippingMarks(@PathVariable("marksId") String marksId,
			ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
		MShippingMarks shippingMarks = this.shippingMarksRepository.findOneByMarksId(marksId);
		shippingMarks.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.shippingMarksRepository.save(shippingMarks);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

}
