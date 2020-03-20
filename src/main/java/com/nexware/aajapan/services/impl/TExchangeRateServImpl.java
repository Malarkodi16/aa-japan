package com.nexware.aajapan.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.dto.ExchangeRateDto;
import com.nexware.aajapan.models.MCurrency;
import com.nexware.aajapan.models.TExchangeRate;
import com.nexware.aajapan.repositories.MCurrencyRepository;
import com.nexware.aajapan.repositories.TExchageRateRepository;
import com.nexware.aajapan.services.TExchangeRateService;

@Service
@Transactional
public class TExchangeRateServImpl implements TExchangeRateService {

	@Autowired
	private MongoOperations mongoOperation;
	@Autowired
	private TExchageRateRepository exchageRateRepository;
	@Autowired
	private MCurrencyRepository currencyRepository;

	@Override
	public TExchangeRate findOneByLatestDate() {
		Query query = new Query();
		query.limit(1);
		query.with(new Sort(Sort.Direction.DESC, "createdDate"));
		return this.mongoOperation.findOne(query, TExchangeRate.class);
	}

	@Override
	public void updateExchangeRate(List<ExchangeRateDto> exchangeRate) {
		exchangeRate.forEach(rate -> {
			this.exchageRateRepository.insert(new TExchangeRate(rate.getCurrency(), rate.getExchangeRate(),
					rate.getSalesExchangeRate(), rate.getSpecialExchangeRate()));
			MCurrency currency = this.currencyRepository.findOneByCurrencySeq(rate.getCurrency());
			currency.setExchangeRate(rate.getExchangeRate());
			currency.setSalesExchangeRate(rate.getSalesExchangeRate());
			currency.setSpecialExchangeRate(rate.getSpecialExchangeRate());
			this.currencyRepository.save(currency);
		});
	}

}
