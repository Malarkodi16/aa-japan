package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.MShippingMarks;

public interface MShippingMarksRepositoryCustom {
	List<MShippingMarks> getAllUnDeletedShippingMarks();

}
