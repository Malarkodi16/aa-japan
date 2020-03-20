package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.InventoryDto;

public interface InventoryRepositoryCustom {

	List<InventoryDto> findAllInventoryStockDetails();
}
