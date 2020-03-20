package com.nexware.aajapan.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nexware.aajapan.dto.BranchSalesInvoiceDto;
import com.nexware.aajapan.dto.ProformaInvoiceDto;
import com.nexware.aajapan.dto.TSalesInvoiceDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MBank;
import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.repositories.MCurrencyRepository;
import com.nexware.aajapan.repositories.MasterBankRepository;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TProformaInvoiceRepository;
import com.nexware.aajapan.repositories.TSalesInvoiceRepository;
import com.nexware.aajapan.utils.AppUtil;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
@RequestMapping("download")
public class DownloadController {
	@Autowired
	StockRepository stockRepository;
	@Autowired
	TProformaInvoiceRepository proformaInvoiceOrderRepository;
	@Autowired
	private TSalesInvoiceRepository salesInvoiceOrderRepository;
	@Autowired
	private MasterBankRepository masterBankRepository;
	@Autowired
	private MCurrencyRepository currencyRepository;

	@GetMapping("/proforma/invoice/{orderNo}.pdf")
	public void proformaInvoice1(@PathVariable String orderNo, HttpServletResponse response)
			throws JRException, IOException {
		ProformaInvoiceDto proformaInvoice = this.proformaInvoiceOrderRepository.findByInvoiceNo(orderNo);
		MCurrency currency = currencyRepository.findOneByCurrencySeq(proformaInvoice.getCurrencyType());
		proformaInvoice.getItems().forEach(item -> item.setCurrencySymbol(currency.getSymbol()));
		// proforma item dataset
		JRBeanCollectionDataSource datasourceJRBean = new JRBeanCollectionDataSource(proformaInvoice.getItems());
		Map<String, Object> params = new HashMap<>();
		InputStream jasperStream = null;
		String country = proformaInvoice.getCountry();
		boolean isSlBlCustomer = !AppUtil.isObjectEmpty(country)
				&& (country.equalsIgnoreCase("SRI LANKA") || country.equalsIgnoreCase("BANGLADESH"));
		if (proformaInvoice.isLcCustomer() && isSlBlCustomer) {
			jasperStream = this.getClass().getResourceAsStream("/templates/jasper/ProformaInvoice_CIF_SL_BL.jasper");
			params.put("proformaInvoice", datasourceJRBean);
			params.put("proformaInvoiceTotal", proformaInvoice);
			params.put("currencySymbol", currency.getSymbol());
		} else if (proformaInvoice.isLcCustomer() && !isSlBlCustomer) {
			jasperStream = this.getClass().getResourceAsStream("/templates/jasper/ProformaInvoice_CIF.jasper");
			params.put("proformaInvoice", datasourceJRBean);
			params.put("proformaInvoiceTotal", proformaInvoice);
			params.put("currencySymbol", currency.getSymbol());
		} else {
			jasperStream = this.getClass().getResourceAsStream("/templates/jasper/ProformaInvoice_CNF_FOB.jasper");
			params.put("detailsDataset", datasourceJRBean);
			params.put("summary", proformaInvoice);
			params.put("currencySymbol", currency.getSymbol());
		}
		try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/jasper/images/logo.png")) {
			params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
		} catch (JRException | IOException e) {
			throw new AAJRuntimeException("Failed to load images", e);
		}
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "inline; filename=" + orderNo + ".pdf");
		final OutputStream outStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	}

	@GetMapping("/proforma/invoice/pdf")
	public void proformaInvoice(@RequestParam("invoiceNo") String invoiceNo,
			@RequestParam(value = "bank1", required = false) String bank1,
			@RequestParam(value = "bank2", required = false) String bank2, HttpServletResponse response)
			throws JRException, IOException {
		ProformaInvoiceDto proformaInvoice = this.proformaInvoiceOrderRepository.findByInvoiceNo(invoiceNo);
		MBank bank = this.masterBankRepository.findOneByBankSeq(bank1);
		MCurrency currency = this.currencyRepository.findOneByCurrencySeq(bank.getCurrencyType());
		// proforma item dataset
		JRBeanCollectionDataSource datasourceJRBean = new JRBeanCollectionDataSource(proformaInvoice.getItems());

		Map<String, Object> params = new HashMap<>();
		InputStream jasperStream = null;
		if (proformaInvoice.isLcCustomer()) {
			jasperStream = this.getClass().getResourceAsStream("/templates/jasper/ProformaInvoice_CIF.jasper");
			params.put("proformaInvoice", datasourceJRBean);
			params.put("proformaInvoiceTotal", proformaInvoice);
		} else {
			jasperStream = this.getClass().getResourceAsStream("/templates/jasper/ProformaInvoice_CNF_FOB.jasper");
			params.put("detailsDataset", datasourceJRBean);
			params.put("summary", proformaInvoice);
			params.put("bankDetails", bank);
			params.put("currencyDetails", currency);
		}

		try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/jasper/images/logo.png")) {
			params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
		} catch (JRException | IOException e) {
			throw new AAJRuntimeException("Failed to load images", e);
		}
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "inline; filename=" + invoiceNo + ".pdf");
		final OutputStream outStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	}

	// download/sales/invoice/order.pdf

	@GetMapping("/sales/invoice/{orderNo}.pdf")
	public void salesInvoice(@PathVariable String orderNo, HttpServletResponse response)
			throws JRException, IOException {
		TSalesInvoiceDto salesInvoice = this.salesInvoiceOrderRepository.getOneByInvoiceNo(orderNo);
		JRBeanCollectionDataSource datasourceJRBean = new JRBeanCollectionDataSource(
				salesInvoice.getSalesInvoiceDetails());
		if (salesInvoice.isLcCustomer()) {
			InputStream jasperStream = this.getClass().getResourceAsStream("/templates/jasper/SalesInvoice_CIF.jasper");

			Map<String, Object> params = new HashMap<>();

			params.put("salesInvoice", datasourceJRBean);
			params.put("salesInvoiceTotal", salesInvoice);
			try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/jasper/images/logo.png")) {
				params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
			} catch (JRException | IOException e) {
				throw new AAJRuntimeException("Failed to load images", e);
			}
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
			response.setContentType("application/x-pdf");
			response.setHeader("Content-disposition", "inline; filename=" + orderNo + ".pdf");

			final OutputStream outStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		} else {
			InputStream jasperStream = this.getClass()
					.getResourceAsStream("/templates/jasper/SalesInvoice_CNF_FOB.jasper");

			Map<String, Object> params = new HashMap<>();

			params.put("detailsDataset", datasourceJRBean);
			params.put("summary", salesInvoice);
			try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/jasper/images/logo.png")) {
				params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
			} catch (JRException | IOException e) {
				throw new AAJRuntimeException("Failed to load images", e);
			}
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
			response.setContentType("application/x-pdf");
			response.setHeader("Content-disposition", "inline; filename=" + orderNo + ".pdf");

			final OutputStream outStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		}

	}

	// Branch Invoice download
	@GetMapping("/branch/sales/invoice/{orderNo}.pdf")
	public void branchSalesInvoice(@PathVariable String orderNo, HttpServletResponse response)
			throws JRException, IOException {
		BranchSalesInvoiceDto salesInvoice = this.salesInvoiceOrderRepository.getOneByBranchInvoiceNo(orderNo);
		JRBeanCollectionDataSource datasourceJRBean = new JRBeanCollectionDataSource(
				salesInvoice.getSalesInvoiceDetails());
		JRBeanCollectionDataSource datasourceJRBean1 = new JRBeanCollectionDataSource(
				salesInvoice.getSalesInvoiceDetails());

		InputStream jasperStream = this.getClass().getResourceAsStream("/templates/jasper/BranchSalesInvoice.jasper");

		Map<String, Object> params = new HashMap<>();

		params.put("salesInvoiceDetailsDataset", datasourceJRBean);
		params.put("salesInvoiceStockDetails", datasourceJRBean1);
		params.put("invoiceDetails", salesInvoice);
		try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/jasper/images/logo.png")) {
			params.put("logo", ImageIO.read(new ByteArrayInputStream(JRLoader.loadBytes(inputStream))));
		} catch (JRException | IOException e) {
			throw new AAJRuntimeException("Failed to load images", e);
		}
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "inline; filename=" + orderNo + ".pdf");

		final OutputStream outStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);

	}
}
