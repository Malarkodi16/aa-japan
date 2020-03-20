package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.MInspectionCompany;
import com.nexware.aajapan.models.MSupplier;

public interface MInspectionCompanyRepositoryCustom {
	
	List<MInspectionCompany> getAllUnDeletedInspectionCompany();
	
	MInspectionCompany getActiveInspectionCompany(String code);
	
	boolean existsByCodeAndName(String code, String name);

	boolean existsByName(String name);

}
