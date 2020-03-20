package com.nexware.aajapan.services;

import com.nexware.aajapan.dto.StockInfoDto;
import com.nexware.aajapan.dto.TStockInspectionInfoDto;
import com.nexware.aajapan.dto.TStockPurchaseInfoDto;
import com.nexware.aajapan.dto.TStockShippingInfoDto;
import com.nexware.aajapan.dto.TStockTransportInfoDto;

public interface StockInfoService {
	StockInfoDto fetchStockData(String stockNo);

	TStockPurchaseInfoDto findPurchaseInfoForStockNo(String stockNo);

	TStockTransportInfoDto findTransportInfoForStockNo(String stockNo);

	TStockInspectionInfoDto findInspectionInfoForStockNo(String stockNo);

	TStockShippingInfoDto findShippingInfoForStockNo(String stockNo);

}
