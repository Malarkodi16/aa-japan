package com.nexware.aajapan.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mongodb.bulk.BulkWriteResult;
import com.nexware.aajapan.services.OpeningStockService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("opening/stock")
public class OpeningStockUploadController {

	@Autowired
	private OpeningStockService stkUpload;

	@GetMapping("/upload")
	public ModelAndView claimApply(ModelAndView modelAndView) throws IOException {
		modelAndView.setViewName("shipping.stock.uplaod");
		return modelAndView;
	}

	@PostMapping("/uploadStock")
	public ModelAndView claimApply(MultipartFile file, ModelAndView modelAndView, RedirectAttributes redirectAttributes,
			HttpServletResponse response) throws IOException {
		int modifiedCount = stkUpload.openingStockUpload(file);
		modelAndView.addObject("message", modifiedCount + " Records Uploaded Successfully");
		modelAndView.setViewName("shipping.stock.uplaod");
		return modelAndView;
	}
}
