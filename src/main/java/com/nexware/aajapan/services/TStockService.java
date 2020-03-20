package com.nexware.aajapan.services;

import java.io.IOException;
import java.util.List;

import com.nexware.aajapan.dto.TStockEditHistoryDto;
import com.nexware.aajapan.form.StockForm;
import com.nexware.aajapan.models.TReAuction;

public interface TStockService {
	void createStock(StockForm stockForm, String attachmentTempDirectory) throws IOException;

	void editStock(StockForm stockForm) throws IOException;

	Integer getStockInventoryStatus(String stockNo);

	void saveReAuctionAndChangeStockStatus(List<TReAuction> reauction);

	void reserveStock(String stockNo, String custId, String userId, Double price);

	List<TStockEditHistoryDto> findAllByStockNo(String stockNo);

}
