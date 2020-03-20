package com.nexware.aajapan.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.dto.BillOfLandingDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.TShippingRequest;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.repositories.TShippingRequestRepository;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
@RequestMapping("/accounts/billoflanding")
public class BillofLandingController {

	@Autowired
	TShippingRequestRepository shippingRequestRepository;

	@GetMapping("/form-view")
	public ModelAndView stockSales() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.reports.bill-of-landing");
		return modelAndView;
	}

	@PostMapping("/.pdf")
	public void exportCertificate(@ModelAttribute BillOfLandingDto data, HttpServletResponse response)
			throws JRException, IOException {

		Map<String, Object> params = new HashMap<>();
		InputStream jasperStream = null;
		jasperStream = this.getClass().getResourceAsStream("/templates/jasper/BillOfLandingTemplate.jasper");
		params.put("billOfLandingDto", data);

		try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/jasper/images/logo.png")) {
			params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
		} catch (JRException | IOException e) {
			throw new AAJRuntimeException("Failed to load images", e);
		}
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "inline; filename=test.pdf");
		final OutputStream outStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	}

	@GetMapping("/blNo/{blNo}")
	@ResponseBody
	public List<BillOfLandingDto> getBLData(@PathVariable String blNo) {
		 
		return this.shippingRequestRepository.findByBlNo(blNo);
			
	}
}