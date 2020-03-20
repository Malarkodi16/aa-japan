package com.nexware.aajapan.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mongodb.bulk.BulkWriteResult;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.services.ClaimService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("documents/recycle")
public class DocumentationRecycleController {
	@Autowired
	private TPurchaseInvoiceRepository tpurchaseInvoiceRepository;
	@Autowired
	private ClaimService claimService;

	@GetMapping("/dashboard")
	public ModelAndView documentTracking(ModelAndView modelAndView) {
		modelAndView.setViewName("document.recycle.dashboard");
		return modelAndView;
	}

	@GetMapping("/claim")
	public ModelAndView documentTrackingNotReceived(ModelAndView modelAndView) {
		modelAndView.setViewName("document.recycle.claim");
		return modelAndView;
	}

	@GetMapping("/status")
	public ModelAndView documentTrackingReceived(ModelAndView modelAndView) {
		modelAndView.setViewName("document.recycle.status");
		return modelAndView;
	}

	@GetMapping("/claim/list/datasource")
	public ResponseEntity<DatatableResponse> recycleNotClaimedList(@RequestParam("status") Integer status) {
		DatatableResponse response = new DatatableResponse(new ArrayList<>());
		if (!AppUtil.isObjectEmpty(status) && status.equals(0)) {
			response = new DatatableResponse(this.tpurchaseInvoiceRepository
					.getRecycleClaimByRecycleClaimStatus(Constants.TPURCHASEINVOICE_RECYCLE_NOT_CLAIMED));
		} else if (!AppUtil.isObjectEmpty(status) && status.equals(1)) {
			response = new DatatableResponse(this.tpurchaseInvoiceRepository
					.getRecycleClaimByRecycleClaimStatus(Constants.TPURCHASEINVOICE_RECYCLE_CLAIM_APPLIED));
		} else if (!AppUtil.isObjectEmpty(status) && status.equals(2)) {
			response = new DatatableResponse(this.tpurchaseInvoiceRepository
					.getRecycleClaimByRecycleClaimStatus(Constants.TPURCHASEINVOICE_RECYCLE_RECEIVED));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(path = "/claim/receive")
	@Transactional
	public ResponseEntity<Response> purchasedSave(@RequestBody List<Document> data) throws IOException {
		BulkWriteResult blkReslt = claimService.receiveRecycleClaim(data);
		return blkReslt.getModifiedCount() > 0 ? new ResponseEntity<>(new Response("success"), HttpStatus.OK)
				: null;
		
	}

	@PostMapping("/claim/apply/uploadExcelFile")
	public ModelAndView claimApply(MultipartFile file, ModelAndView modelAndView, RedirectAttributes redirectAttributes)
			throws IOException {
		BulkWriteResult bulkWriteResult = claimService.applyRecycleClaimByExcel(file);
		int modifiedCount = 0;
		if (!AppUtil.isObjectEmpty(bulkWriteResult)) {
			modifiedCount = bulkWriteResult.getModifiedCount();
		}
		redirectAttributes.addFlashAttribute("message", String.format("Success! Modified count %s", modifiedCount));
		modelAndView.setViewName("redirect:/documents/recycle/claim");
		return modelAndView;
	}

	@PostMapping("/claim/receive/uploadExcelFile")
	public ModelAndView claimReceive(MultipartFile file, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) throws IOException {
		BulkWriteResult bulkWriteResult = claimService.receiveRecycleClaimByExcel(file);
		int modifiedCount = 0;
		if (!AppUtil.isObjectEmpty(bulkWriteResult)) {
			modifiedCount = bulkWriteResult.getModifiedCount();
		}
		redirectAttributes.addFlashAttribute("message", String.format("Success! Modified count %s", modifiedCount));
		modelAndView.setViewName("redirect:/accounts/claim/recycle");
		return modelAndView;
	}

	@GetMapping("/claim/insurance")
	public ModelAndView insurance(ModelAndView modelAndView) {
		modelAndView.setViewName("documents.insurance.claim");
		return modelAndView;
	}
}
