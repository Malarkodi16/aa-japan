package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.dto.TExchangeRateDto;
import com.nexware.aajapan.models.TExchangeRate;

public interface TExchageRateRepositoryCustom {
	
	List<TExchangeRateDto> getAllList();
	
	List<TExchangeRate> findTop3ByOrderByCreatedDate(Date remitDate);
	
	TExchangeRate findTopOneByOrderByCreatedDate(Date remitDate,Integer currency);

}
