package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TInspectionInstructionDto;

public interface TInspectionInstructionRepositoryCustom {

	List<TInspectionInstructionDto> getAllGivenInstruction();

	List<TInspectionInstructionDto> getAllGivenInstructionByStockNo(List<String> stockNo);

}
