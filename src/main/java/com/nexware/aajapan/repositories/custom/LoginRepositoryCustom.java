package com.nexware.aajapan.repositories.custom;

import java.util.List;

import org.bson.Document;

import com.nexware.aajapan.dto.MLoginDto;

public interface LoginRepositoryCustom {

	List<MLoginDto> findAllByRole();
	
	List<MLoginDto> findAllSalesPerson();

	MLoginDto findOneByUsername(String username);

	Document findUserIdByDepartmentAndHierarchyLevelIsLessThanOrEqual(String userId);

	List<Document> findAllByDepartmentAndRole(String department, String role);
}
