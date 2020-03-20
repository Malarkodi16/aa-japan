package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TStockEditHistoryDto;

public interface TStockEditHistoryRepositoryCustom {

	List<TStockEditHistoryDto> findAllByStockNo(String stockNo);

}
