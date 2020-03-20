package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TStockMakerModelDto;

public interface TInspectionOrderRequestCancelledRepositoryCustom {

	List<TStockMakerModelDto> searchStockForReInspection(String search);

}
