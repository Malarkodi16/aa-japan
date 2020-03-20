package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.MVechicleMakerDto;
import com.nexware.aajapan.dto.MVechicleMakerExportExcelDto;
import com.nexware.aajapan.models.MVechicleMaker;
import com.nexware.aajapan.models.Model;

public interface MVechicleMakerRepositoryCustom {

	List<MVechicleMakerDto> findAllModel();

	List<MVechicleMaker> getListWithoutDelete();

	MVechicleMaker getListByCodeWithoutDelete(String code);

	List<MVechicleMakerExportExcelDto> exportExcelFormatting();

	List<MVechicleMakerExportExcelDto> exportExcelFormattingRow(String code);

	Model getModelData(String maker, String modelId);

	boolean existsByCodeAndName(String code, String name);

	boolean existsByName(String name);

	Model getModelDataName(String maker, String modelName, String category);

}
