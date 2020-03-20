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
import com.nexware.aajapan.models.MAuctionGradesExterior;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.MAuctionGradesExteriorRepository;
import com.nexware.aajapan.services.SequenceService;

@Controller
@RequestMapping("/master/auctionGradeExterior")
public class MasterExteriorGradeController {

	@Autowired
	private MAuctionGradesExteriorRepository exteriorGradeRepository;

	@Autowired
	private SequenceService sequenceService;

	@GetMapping("/list")
	public ModelAndView listAuctionExteriorGradePage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.auctionGradeExterior.list");
		return modelAndView;
	}

	@GetMapping("/list-data")

	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.exteriorGradeRepository.getAllUnDeletedExteriorGrade());
	}

	@PostMapping("save")

	@ResponseBody

	@Transactional
	public ResponseEntity<Response> create(@RequestBody MAuctionGradesExterior auctionGradesExterior) {
		boolean isNewEntry = auctionGradesExterior.getId().trim().isEmpty();
		MAuctionGradesExterior auctionGradesExteriorToSave = null;
		if (isNewEntry) {
			auctionGradesExteriorToSave = auctionGradesExterior;
			auctionGradesExteriorToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
			auctionGradesExteriorToSave.setId(null);
			auctionGradesExteriorToSave.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_AUEXTGRD));
		} else {
			auctionGradesExteriorToSave = this.exteriorGradeRepository.findOneById(auctionGradesExterior.getId());
			auctionGradesExteriorToSave.setGrade(auctionGradesExterior.getGrade());
		}
		this.exteriorGradeRepository.save(auctionGradesExteriorToSave);
		return new ResponseEntity<>(
				new Response("success", this.exteriorGradeRepository.findOneById(auctionGradesExteriorToSave.getId())),
				HttpStatus.OK);
	}

	@GetMapping("/delete/{id}")
	public ResponseEntity<Response> deleteSupplier(@PathVariable("id") String id, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		MAuctionGradesExterior AuctionGradesExterior = this.exteriorGradeRepository.findOneById(id);
		AuctionGradesExterior.setDeleteFlag(Constants.DELETE_FLAG_1);
		this.exteriorGradeRepository.save(AuctionGradesExterior);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

}
