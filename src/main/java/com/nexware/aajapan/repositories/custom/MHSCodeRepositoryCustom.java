package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.MHSCodeDto;
import com.nexware.aajapan.models.MHSCode;

public interface MHSCodeRepositoryCustom {

	List<MHSCode> getAllUnDeletedhsCode();

	boolean existsByCcAndCategoryAndSubCategoryAndHsCode(String cc, String category, String subCategory, String hsCode);

	List<MHSCodeDto> findAllData();
}
