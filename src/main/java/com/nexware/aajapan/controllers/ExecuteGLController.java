package com.nexware.aajapan.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.dto.GLDto;
import com.nexware.aajapan.models.MCountryWiseReconciliation;
import com.nexware.aajapan.models.TAccountsTransaction;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.repositories.MCountryWiseReconciliationRep;
import com.nexware.aajapan.repositories.StockRepository;

@Controller

@RequestMapping("accounts/executegl")
public class ExecuteGLController {

	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private MCountryWiseReconciliationRep mCountryWiseReconciliationRep;

	@GetMapping(value = { "gl" })
	public ModelAndView executeGl() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accounts.executegl");
		return modelAndView;
	}

	// Country Reconciliation

	@GetMapping("/list")
	@ResponseBody
	public DatatableResponse glList(@RequestParam("flag") Integer flag) {
		List<Map<String, Object>> list = new ArrayList<>();
		if (flag == 0) {
			return new DatatableResponse(new ArrayList<>());
		}
		MCountryWiseReconciliation countryRec = this.mCountryWiseReconciliationRep.findOneByCountry("SRI LANKA");
		List<GLDto> find = this.stockRepository.getExecuteGLList();

		find.stream().forEach(stock -> {

			final Map<String, Object> data = new HashMap<>();
			Map<String, TAccountsTransaction> result = stock.getItems().stream()
					.collect(Collectors.toMap(TAccountsTransaction::getKey, Function.identity()));

			data.put("stockNo", stock.getStockNo());
			countryRec.getCountryReconciliationItems().forEach(f -> {
				String key = f.getKey();
				if (result.containsKey(key)) {
					TAccountsTransaction gl = result.get(key);
					data.put(f.getKey(), gl.getAmount());
				} else {
					data.put(f.getKey(), "invalid");
				}

			});
			list.add(data);
		});

		return new DatatableResponse(list);
	}

}
