package com.nexware.aajapan.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.models.TFreightShippingInvoice;
import com.nexware.aajapan.models.TFwdrInvoice;
import com.nexware.aajapan.models.TInvoice;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TTransportInvoice;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.TFreightShippingInvoiceRepository;
import com.nexware.aajapan.repositories.TFwdrInvoiceRepository;
import com.nexware.aajapan.repositories.TInvoiceRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.repositories.TTransportInvoiceRepository;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("accounts/dash-board")
public class AccountsDashboardController {

	@Autowired
	private TPurchaseInvoiceRepository tPurchaseInvoiceRepository;
	@Autowired
	TTransportInvoiceRepository tTransportInvoiceRepository;
	@Autowired
	private TInvoiceRepository tInvoiceRepository;
	@Autowired
	TFwdrInvoiceRepository tFwdrInvoiceRepository;
	@Autowired
	TFreightShippingInvoiceRepository tFreightShippingInvoiceRepository;

	@GetMapping("/view")
	public ModelAndView listBank(HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.dashboard.view");

		return modelAndView;
	}

//	accounts/dash-board/total-amount
	@GetMapping("/total-amount")
	public ResponseEntity<Response> getAccountsDashboardPaymentTotal() {
		final Map<String, Double> info = new HashMap<>();
		final Date date = new Date();
		final List<TPurchaseInvoice> tPurchaseInvoice = tPurchaseInvoiceRepository
				.findAllByDueDate(AppUtil.atStartOfDay(date), AppUtil.atEndOfDay(date));
		final Double auctionTotal = tPurchaseInvoice.stream().mapToDouble(TPurchaseInvoice::getTotalTaxIncluded).sum();
		final List<TTransportInvoice> tTransportInvoice = tTransportInvoiceRepository
				.findAllByDueDate(AppUtil.atStartOfDay(date), AppUtil.atEndOfDay(date));
		final Double transportTotal = tTransportInvoice.stream().mapToDouble(TTransportInvoice::getInvoiceTotal).sum();
		final List<TInvoice> tInvoice = tInvoiceRepository.findAllByDueDate(AppUtil.atStartOfDay(date),
				AppUtil.atEndOfDay(date));
		final Double tInvoiceTotal = tInvoice.stream().mapToDouble(TInvoice::getAmountInYen).sum();
		final List<TFwdrInvoice> tFwdrInvoice = tFwdrInvoiceRepository.findAllByDueDate(AppUtil.atStartOfDay(date),
				AppUtil.atEndOfDay(date));
		final Double tFwdrInvoiceTotal = tFwdrInvoice.stream().mapToDouble(TFwdrInvoice::getTotalAmountInclusiveTax)
				.sum();
		final List<TFreightShippingInvoice> tFreightShippingInvoice = tFreightShippingInvoiceRepository
				.findAllByDueDate(AppUtil.atStartOfDay(date), AppUtil.atEndOfDay(date));
		final Double tFrghtInvoiceTotal = tFreightShippingInvoice.stream()
				.mapToDouble(TFreightShippingInvoice::getTotalAmount).sum();
		info.put("auction", auctionTotal);
		info.put("transport", transportTotal);
		info.put("freight", tFrghtInvoiceTotal);
		info.put("generalExpense", tInvoiceTotal);
		info.put("storage", tFwdrInvoiceTotal);

		return new ResponseEntity<>(new Response("success", info), HttpStatus.OK);
	}

}
