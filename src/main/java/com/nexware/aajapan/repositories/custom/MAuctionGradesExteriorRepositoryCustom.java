package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.MAuctionGradesExterior;

public interface MAuctionGradesExteriorRepositoryCustom {

	List<MAuctionGradesExterior> getAllUnDeletedExteriorGrade();

	boolean existsByIdAndGrade(String id, String grade);

	boolean existsByGrade(String grade);
}
