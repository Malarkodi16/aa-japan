package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.StockDocumentsDto;

public interface TDocumentReceivedRepositoryCustom {

	List<StockDocumentsDto> findAllDocumentReceivedList(Integer status);

	StockDocumentsDto findOneDocumentReceivedStockDetails(Integer status, String stockNo);

	Long findAllByDocumentStatusReceivedCount();

	Long findAllByDocumentStatusReceivedRikujiCount();

}
