package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.MAuctionGradesExterior;
import com.nexware.aajapan.models.MAuctionGradesInterior;

public interface MAuctionGradesInteriorRepositoryCustom {

	List<MAuctionGradesInterior> getAllUnDeletedInteriorGrade();

	boolean existsByIdAndGrade(String id, String grade);

	boolean existsByGrade(String grade);
}
