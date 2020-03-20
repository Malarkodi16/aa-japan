package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.SalesSumaryResultDto;
import com.nexware.aajapan.models.TInventoryCost;

public interface TInventoryCostRepositoryCustom {

	List<TInventoryCost> getInventoryAmountsForStock(String stockNo);
	
	List<SalesSumaryResultDto> getInventoryByStockAndType(String stockNo);

}
