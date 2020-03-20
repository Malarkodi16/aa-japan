package com.nexware.aajapan.services;

import java.util.List;

import com.nexware.aajapan.dto.ExchangeRateDto;
import com.nexware.aajapan.models.TExchangeRate;

public interface TExchangeRateService {
	TExchangeRate findOneByLatestDate();

	void updateExchangeRate(List<ExchangeRateDto> exchangeRate);

}
