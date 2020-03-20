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
import com.nexware.aajapan.models.MAuctionGradesInterior;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MAuctionGradesInteriorRepository;
import com.nexware.aajapan.services.SequenceService;

@Controller
@RequestMapping("/master/auctionGradeInterior")
public class MasterInteriorGradeController {

	@Autowired
	private MAuctionGradesInteriorRepository InteriorGradeRepository;

	@Autowired
	private SequenceService sequenceService;

	@GetMapping("/list")
	public ModelAndView listAuctionInteriorGradePage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.auctionGradeInterior.list");
		return modelAndView;
	}

	@GetMapping("/list-data")

	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.InteriorGradeRepository.getAllUnDeletedInteriorGrade());
	}

	@PostMapping("save")

	@ResponseBody

	@Transactional
	public ResponseEntity<Response> create(@RequestBody MAuctionGradesInterior auctionGradesInterior) {
		boolean isNewEntry = auctionGradesInterior.getId().trim().isEmpty();
		MAuctionGradesInterior auctionGradesInteriorToSave = null;
		if (isNewEntry) {
			auctionGradesInteriorToSave = auctionGradesInterior;
			auctionGradesInteriorToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
			auctionGradesInteriorToSave.setId(null);
			auctionGradesInteriorToSave.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_AUINTGRD));
		} else {
			auctionGradesInteriorToSave = this.InteriorGradeRepository.findOneById(auctionGradesInterior.getId());
			auctionGradesInteriorToSave.setGrade(auctionGradesInterior.getGrade());
		}
		this.InteriorGradeRepository.save(auctionGradesInteriorToSave);
		return new ResponseEntity<>(
				new Response("success", this.InteriorGradeRepository.findOneById(auctionGradesInteriorToSave.getId())),
				HttpStatus.OK);
	}

	@GetMapping("/delete/{id}")
	public ResponseEntity<Response> deleteSupplier(@PathVariable("id") String id, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		MAuctionGradesInterior AuctionGradesInterior = this.InteriorGradeRepository.findOneById(id);
		AuctionGradesInterior.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.InteriorGradeRepository.save(AuctionGradesInterior);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

}
